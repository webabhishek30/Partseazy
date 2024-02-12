
package com.partseazy.android.ui.model.deal.bookingorder.summary;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingSummaryResult {


    @SerializedName("booking_payment")
    @Expose
    public BookingPayment bookingPayment;


    @SerializedName("delivery_address")
    @Expose
    public DeliveryAddress deliveryAddress;

    @SerializedName("pickup_address")
    @Expose
    public PickupAddress pickupAddress;


    @SerializedName("skus")
    @Expose
    public List<Sku> skus = null;
    @SerializedName("trade")
    @Expose
    public Trade trade;
    @SerializedName("trade_booking")
    @Expose
    public TradeBooking tradeBooking;

    @SerializedName("supplier")
    @Expose
    public String supplier;

    @SerializedName("user")
    @Expose
    public User user;

}
