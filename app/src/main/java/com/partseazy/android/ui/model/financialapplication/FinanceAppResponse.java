package com.partseazy.android.ui.model.financialapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 5/1/17.
 */

public class FinanceAppResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("identity_doc_id")
    @Expose
    public Integer identityDocId;
    @SerializedName("address_doc_id")
    @Expose
    public Integer addressDocId;
    @SerializedName("business_doc_id")
    @Expose
    public Integer businessDocId;
    @SerializedName("machine_state")
    @Expose
    public Object machineState;
    @SerializedName("stated_at")
    @Expose
    public Object statedAt;
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