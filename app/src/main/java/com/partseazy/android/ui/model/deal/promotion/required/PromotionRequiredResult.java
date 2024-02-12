package com.partseazy.android.ui.model.deal.promotion.required;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 1/9/17.
 */

public class PromotionRequiredResult {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("clusters")
    @Expose
    public List<String> clusters = null;
    @SerializedName("credits_required")
    @Expose
    public Integer creditsRequired;
    @SerializedName("credits_spent")
    @Expose
    public Integer creditsSpent;
    @SerializedName("info")
    @Expose
    public Object info;
    @SerializedName("tags")
    @Expose
    public Object tags;
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
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}
