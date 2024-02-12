package com.partseazy.android.ui.model.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 20/12/16.
 */


public class StoreModel {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("address_id")
    @Expose
    public Object addressId;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("format")
    @Expose
    public Object format;
    @SerializedName("try_rooms")
    @Expose
    public Integer tryRooms;
    @SerializedName("l2_ids")
    @Expose
    public Object l2Ids;
    @SerializedName("l3_segments")
    @Expose
    public Object l3Segments;
    @SerializedName("open_time")
    @Expose
    public Object openTime;
    @SerializedName("close_time")
    @Expose
    public Object closeTime;
    @SerializedName("weekly_off")
    @Expose
    public Object weeklyOff;
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
    @SerializedName("state_comment")
    @Expose
    public Object stateComment;
    @SerializedName("state_tag")
    @Expose
    public Object stateTag;
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