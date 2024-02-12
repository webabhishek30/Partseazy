package com.partseazy.android.ui.model.shippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by can on 24/12/16.
 */

public class ShippingAddressDetail implements Serializable{


    @SerializedName("billing_name")
    @Expose
    public String billingName;
    @SerializedName("gstn")
    @Expose
    public String gstn;

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
    public String deletedAt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("landmark")
    @Expose
    public String landmark;
    @SerializedName("latitude")
    @Expose
    public double latitude;
    @SerializedName("longitude")
    @Expose
    public double longitude;
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
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @Expose
    public boolean selectedItem;


}
