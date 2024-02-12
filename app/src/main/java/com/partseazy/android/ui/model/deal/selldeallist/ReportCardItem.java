package com.partseazy.android.ui.model.deal.selldeallist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 23/11/17.
 */

public class ReportCardItem {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("active")
    @Expose
    public String active;

    @SerializedName("ending_at")
    @Expose
    public String endingAt;

    @SerializedName("l2")
    @Expose
    public String l2;

    @SerializedName("l2_category")
    @Expose
    public String l2Cat;

    @SerializedName("l3")
    @Expose
    public String l3;

    @SerializedName("l3_category")
    @Expose
    public String l3Cat;

    @SerializedName("machine_state")
    @Expose
    public String machineState;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("public")
    @Expose
    public String isPublic;

    @SerializedName("starting_at")
    @Expose
    public String startingAt;

    @SerializedName("total_booking")
    @Expose
    public String totalBooking;

    @SerializedName("total_booking_amount")
    @Expose
    public String totalBookingAmnt;

    @SerializedName("total_demo")
    @Expose
    public String totalDemo;

    @SerializedName("total_opens")
    @Expose
    public String totalOpens;

    @SerializedName("total_views")
    @Expose
    public String totalViews;

    @SerializedName("trin")
    @Expose
    public String trin;

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("user_mobile")
    @Expose
    public String userMobile;

    @SerializedName("user_name")
    @Expose
    public String userName;
}
