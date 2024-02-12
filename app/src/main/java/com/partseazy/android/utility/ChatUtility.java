package com.partseazy.android.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserClientService;
import com.applozic.mobicomkit.api.account.user.UserDetail;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.json.GsonUtils;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by shubhang on 09/06/17.
 */

public class ChatUtility {

    private final BaseFragment baseF;
    private final int userId;
    private UserLoginTask.TaskListener listener;
    private String userName;
    private String imageSrc;
    private String name;
    private String identifier;

    public ChatUtility(BaseFragment baseF, int userId, String userName, String imageSrc, String name, String identifier) {
        this.baseF = baseF;
        this.userId = userId;
        this.userName = userName;
        this.imageSrc = imageSrc;
        this.name = name;
        this.identifier = identifier;
    }

    public ChatUtility(BaseFragment baseF, int userId, String userName) {
        this.baseF = baseF;
        this.userId = userId;
        this.userName = userName;
    }


    public void startChatting() {

        //First check user details on local
        if (isUserDetailsAvailable()) {
            baseF.showProgressDialog();

            //Check if user logged in
            if (isUserLoggedIn()) {
                //Check if supplier exist
                new CheckSupplierAsynTask().execute();

            } else {
                //execute login service
                launchLoginTaskForApplogic();

            }

        } else {

            String userId = DataStore.getUserID(baseF.getContext());
            if (userId != null)
                PartsEazyEventBus.getInstance().postEvent(EventConstant.GET_USER_DETAILS, userId);
        }

    }

    private void launchLoginTaskForApplogic() {

        if (!isUserDetailsAvailable()) {
            baseF.hideProgressDialog();
            baseF.showError("User details not found", BaseFragment.MESSAGETYPE.TOAST);
        }


        User user = new User();
        user.setUserId(DataStore.getUserID(baseF.getContext())); //userId it can be any unique user identifier
        user.setDisplayName(DataStore.getUserName(baseF.getContext())); //displayName is the name of the user which will be shown in chat messages
        user.setEmail(DataStore.getUserEmail(baseF.getContext())); //optional
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password


        new UserLoginTask(user, listener, baseF.getContext()).execute((Void) null);

        listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                new CheckSupplierAsynTask().execute();
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                baseF.hideProgressDialog();
                baseF.showError();
            }
        };


    }

    private boolean isUserDetailsAvailable() {

        if (DataStore.getUserID(baseF.getContext()) == null
                || DataStore.getUserName(baseF.getContext()) == null) {

            return false;
        }

        return true;
    }

    private boolean isUserLoggedIn() {

        if (MobiComUserPreference.getInstance(baseF.getContext()).isLoggedIn()) {
            return true;
        }
        return false;
    }

    public class CheckSupplierAsynTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean userExists;
            try {
                userExists = isUserExist(userId + "");
            } catch (Exception e) {
                baseF.hideProgressDialog();
                CustomLogger.e("The excecption", e);
                userExists = false;
                return userExists;
            }
            return userExists;
        }

        @Override
        protected void onPostExecute(Boolean userExists) {
            super.onPostExecute(userExists);
            baseF.hideProgressDialog();
            if (userExists) {
                launchChatActivity();
            } else {
                sendOfflineChatMessage(userId, userName);
//                regsiterSellerToApplogic(userId + "", userName);
            }

        }
    }

    public boolean isUserExist(String userId) {

        UserClientService userClientService = new UserClientService(baseF.getContext());

        Set userIds = new HashSet<String>();
        userIds.add(userId);
        String response = userClientService.getUserDetails(userIds);
        if (!TextUtils.isEmpty(response)) {
            UserDetail[] userDetails = (UserDetail[]) GsonUtils.getObjectFromJson(response, UserDetail[].class);
            for (UserDetail userDetail : userDetails) {
                if (userDetail.getUserId().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void launchChatActivity() {
        Intent intent = new Intent(baseF.getContext(), ConversationActivity.class);
        intent.putExtra(ConversationUIService.USER_ID, userId + "");
        intent.putExtra(ConversationUIService.DISPLAY_NAME, userName);
        if (imageSrc != null && !imageSrc.equals("")) {
            String formatedURL = CommonUtility.getFormattedImageUrl(imageSrc);
            CustomLogger.d("The formatted URL is  " + formatedURL);
            String productName = name + " ( " + identifier + " )";
            intent.putExtra(ConversationUIService.PRODUCT_TOPIC_ID, productName);
            intent.putExtra(ConversationUIService.PRODUCT_IMAGE_URL, formatedURL);
        }
        if (baseF.getActivity() != null && baseF.isAdded())
            baseF.startActivity(intent);
    }

    private void regsiterSellerToApplogic(String supplierid, String supplierName) {
        baseF.hideProgressDialog();
        PartsEazyEventBus.getInstance().postEvent(EventConstant.REGISTER_SELLER_TO_CHAT, supplierid, supplierName);
    }

    private void sendOfflineChatMessage(final int userId, String userName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(baseF.getActivity());
        LayoutInflater inflater = baseF.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_chat_message_offline, null);
        dialogBuilder.setView(dialogView);

        final EditText mET = (EditText) dialogView.findViewById(R.id.messageET);
        final TextInputLayout mTIL = (TextInputLayout) dialogView.findViewById(R.id.messageTIL);

        dialogBuilder.setTitle(userName + " is offline");

        dialogBuilder.setPositiveButton("Done", null);
        dialogBuilder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if ("".equals(mET.getText().toString())) {
                            mTIL.setError("Please provide the message");
                            mTIL.setErrorEnabled(true);
                            return;
                        }
                        Map params = WebServicePostParams.offlineChatMessage(mET.getText().toString());
                        baseF.getNetworkManager().PostRequest(RequestIdentifier.POST_CHAT_OFFLINE_MESSAGE.ordinal(),
                                WebServiceConstants.POST_CHAT_OFFLINE + userId, params, null, baseF, baseF, false);
                        alertDialog.dismiss();
                        baseF.showMessage("Message Sent", BaseFragment.MESSAGETYPE.TOAST);
                    }
                });
            }
        });
        alertDialog.show();

        mET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
