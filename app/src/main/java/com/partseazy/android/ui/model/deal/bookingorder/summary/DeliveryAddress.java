
package com.partseazy.android.ui.model.deal.bookingorder.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryAddress {

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
    public String mobile;
    @SerializedName("name")
    @Expose
    public String name;
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
    public String type;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}
