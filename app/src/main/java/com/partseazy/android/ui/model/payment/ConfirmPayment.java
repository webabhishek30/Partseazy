
package com.partseazy.android.ui.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmPayment {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("ptin")
    @Expose
    public String ptin;
    @SerializedName("order_master_id")
    @Expose
    public Integer orderMasterId;
    @SerializedName("method")
    @Expose
    public String method;
    @SerializedName("due")
    @Expose
    public Integer due;
    @SerializedName("payer_user_id")
    @Expose
    public Object payerUserId;
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
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
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
