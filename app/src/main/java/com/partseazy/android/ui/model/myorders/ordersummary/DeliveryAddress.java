
package com.partseazy.android.ui.model.myorders.ordersummary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryAddress {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("street")
    @Expose
    public String street;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("city_id")
    @Expose
    public Integer cityId;
    @SerializedName("city_other")
    @Expose
    public Object cityOther;
    @SerializedName("state_id")
    @Expose
    public Integer stateId;
    @SerializedName("landmark")
    @Expose
    public Object landmark;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("country_id")
    @Expose
    public Integer countryId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("latitude")
    @Expose
    public Object latitude;
    @SerializedName("longitude")
    @Expose
    public Object longitude;
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

    @SerializedName("billing_name")
    @Expose
    public String billingName;
    @SerializedName("gstn")
    @Expose
    public String gstn;

}
