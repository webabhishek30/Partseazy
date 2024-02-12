
package com.partseazy.android.ui.model.payment;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CODPayment {

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
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("email_verified")
    @Expose
    public Integer emailVerified;
    @SerializedName("roles")
    @Expose
    public List<String> roles = null;
    @SerializedName("address")
    @Expose
    public List<Integer> address = null;
    @SerializedName("cur")
    @Expose
    public String cur;
    @SerializedName("isActive")
    @Expose
    public Integer active;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public Object tags;
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
