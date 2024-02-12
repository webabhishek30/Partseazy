package com.partseazy.android.ui.model.deal.deal_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 2/6/17.
 */

public class Address {

    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("city_id")
    @Expose
    public Integer cityId;
    @SerializedName("city_other")
    @Expose
    public Object cityOther;
    @SerializedName("country_id")
    @Expose
    public Integer countryId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("landmark")
    @Expose
    public Object landmark;
    @SerializedName("latitude")
    @Expose
    public Object latitude;
    @SerializedName("longitude")
    @Expose
    public Object longitude;
    @SerializedName("mobile")
    @Expose
    public Object mobile;
    @SerializedName("name")
    @Expose
    public Object name;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("state_id")
    @Expose
    public Integer stateId;
    @SerializedName("street")
    @Expose
    public String street;
    @SerializedName("type")
    @Expose
    public Object type;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}
