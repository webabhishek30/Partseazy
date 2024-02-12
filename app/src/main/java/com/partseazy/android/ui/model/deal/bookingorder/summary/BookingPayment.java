package com.partseazy.android.ui.model.deal.bookingorder.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 24/7/17.
 */

public class BookingPayment {

    @SerializedName("ptin")
    @Expose
    public String ptin;
    @SerializedName("bkin")
    @Expose
    public String bkin;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("method")
    @Expose
    public String method;
    @SerializedName("method_paid")
    @Expose
    public String methodPaid;
    @SerializedName("due")
    @Expose
    public Integer due;
    @SerializedName("paid")
    @Expose
    public Integer paid;
    @SerializedName("pg_code")
    @Expose
    public Object pgCode;
    @SerializedName("pg_request")
    @Expose
    public Object pgRequest;
    @SerializedName("pg_response")
    @Expose
    public Object pgResponse;
    @SerializedName("comment")
    @Expose
    public Object comment;
    @SerializedName("machine_state")
    @Expose
    public Object machineState;
    @SerializedName("stated_at")
    @Expose
    public Object statedAt;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}

