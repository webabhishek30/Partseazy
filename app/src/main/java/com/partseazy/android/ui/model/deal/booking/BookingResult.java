
package com.partseazy.android.ui.model.deal.booking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingResult {

    @SerializedName("booking")
    @Expose
    public Booking booking;
    @SerializedName("booking_sku")
    @Expose
    public List<BookingSku> bookingSku = null;

}
