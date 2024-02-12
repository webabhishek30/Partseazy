package com.partseazy.android.ui.model.registration.banner;



import com.partseazy.android.ui.model.registration.StoreModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class StoreData {

    @SerializedName("retailer")
    @Expose
    public Retailer retailer;
    @SerializedName("shop")
    @Expose
    public StoreModel shop;
    @SerializedName("user")
    @Expose
    public AppUser user;
}
