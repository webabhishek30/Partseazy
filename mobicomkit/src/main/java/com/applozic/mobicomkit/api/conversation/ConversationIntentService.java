package com.applozic.mobicomkit.api.conversation;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.applozic.mobicomkit.api.account.user.UserService;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;

import java.util.List;

/**
 * Created by devashish on 15/12/13.
 */
public class ConversationIntentService extends JobIntentService {

    public static final String SYNC = "AL_SYNC";
    private static final String TAG = "ConversationIntent";
    private static final int PRE_FETCH_MESSAGES_FOR = 6;
    private MobiComMessageService mobiComMessageService;


    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static public void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ConversationIntentService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mobiComMessageService = new MobiComMessageService(this, MessageIntentService.class);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent == null) {
            return;
        }
        boolean sync = intent.getBooleanExtra(SYNC, false);
        Utils.printLog(ConversationIntentService.this,TAG, "Syncing messages service started: " + sync);
        if (sync) {
            mobiComMessageService.syncMessages();
        } else {
            Thread thread = new Thread(new ConversationSync());
            thread.start();
        }
    }

    private class ConversationSync implements Runnable {

        public ConversationSync() {
        }

        @Override
        public void run() {
            try {
                MobiComConversationService mobiComConversationService = new MobiComConversationService(ConversationIntentService.this);
                List<Message> messages = mobiComConversationService.getLatestMessagesGroupByPeople();
                UserService.getInstance(ConversationIntentService.this).processSyncUserBlock();
                for (Message message : messages.subList(0, Math.min(PRE_FETCH_MESSAGES_FOR, messages.size()))) {
                    Contact contact = null;
                    Channel channel = null;

                    if (message.getGroupId() != null) {
                        channel = new Channel(message.getGroupId());
                    } else {
                        contact = new Contact(message.getContactIds());
                    }

                    mobiComConversationService.getMessages(1L, null, contact, channel, null, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
