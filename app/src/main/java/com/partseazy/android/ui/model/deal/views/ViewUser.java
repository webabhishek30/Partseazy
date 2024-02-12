package com.partseazy.android.ui.model.deal.views;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 06/11/17.
 */

public class ViewUser {

    @SerializedName("user_id")
    @Expose
    public Integer userID;

    @SerializedName("user_name")
    @Expose
    public String userName;

    @SerializedName("shop_id")
    @Expose
    public Integer shopID;

    @SerializedName("shop_name")
    @Expose
    public String shopName;

    @SerializedName("shop_image")
    @Expose
    public String shopImage;

    @SerializedName("profile_available")
    @Expose
    public Integer profileAvailable;

}
