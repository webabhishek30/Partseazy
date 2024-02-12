package com.partseazy.android.ui.model.deal.bookingorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 21/6/17.
 */

public class BookingResult {

    @SerializedName("result")
    @Expose
    public List<BookingData> bookingDataList = null;
}
