package com.partseazy.android.analytics;

import android.content.Context;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import java.util.Date;

public class MoengageUtility {


    public static void trackEvent(String searchQuery, Context context){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(MoengageConstant.Events.SEARCH_QUERY, searchQuery)
                .putAttrDate(MoengageConstant.Events.SEARCH_DATE, new Date());
        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.SEARCH_EVENT, builder);
    }


}
