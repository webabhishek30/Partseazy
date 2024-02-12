package com.partseazy.android.ui.model.financialapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 4/1/17.
 */

public class Document {


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("is_id_proof")
    @Expose
    public Integer isIdProof;
    @SerializedName("is_address_proof")
    @Expose
    public Integer isAddressProof;
    @SerializedName("is_business_proof")
    @Expose
    public Integer isBusinessProof;
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