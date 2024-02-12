package com.partseazy.android.ui.model.deal.deal_detail;

import com.partseazy.android.ui.model.deal.selldeallist.Info;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 10/5/17.
 */

public class Deal {

    @SerializedName("address")
    @Expose
    public Address address;

    @SerializedName("info")
    @Expose
    public Info info;

    @SerializedName("opens")
    @Expose
    public Integer opens;
    @SerializedName("skus")
    @Expose
    public List<Sku> skus = null;
    @SerializedName("supplier")
    @Expose
    public String supplier;
    @SerializedName("supplier_rating")
    @Expose
    public Double supplierRating;
    @SerializedName("trade")
    @Expose
    public Trade trade;
    @SerializedName("views")
    @Expose
    public Integer views;

    @SerializedName("non_guest_users")
    @Expose
    public boolean nonGuestUsers;

    @SerializedName("l2_category")
    @Expose
    public String l2Category;
    @SerializedName("l3_category")
    @Expose
    public String l3Category;
    @SerializedName("user")
    @Expose
    public User user;

    @SerializedName("bookings")
    @Expose
    public List<Booking> bookings = null;

    @SerializedName("demo_requests")
    @Expose
    public List<DemoRequest> demoRequests = null;

}
