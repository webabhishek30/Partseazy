
package com.partseazy.android.ui.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetails {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("mobile_verified")
    @Expose
    public Integer mobileVerified;
    @SerializedName("whatsapp")
    @Expose
    public Object whatsapp;
    @SerializedName("l1_category")
    @Expose
    public String l1Category;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("email_verified")
    @Expose
    public Integer emailVerified;
    @SerializedName("role")
    @Expose
    public List<String> role = null;
    @SerializedName("address")
    @Expose
    public Object address;
    @SerializedName("cur")
    @Expose
    public String cur;

    @SerializedName("promotion_credits")
    @Expose
    public Long promotionCredits;

    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public List<String> tags = null;
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
