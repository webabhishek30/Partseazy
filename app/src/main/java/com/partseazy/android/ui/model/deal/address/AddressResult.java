package com.partseazy.android.ui.model.deal.address;

import com.partseazy.android.ui.model.deal.deal_detail.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 28/6/17.
 */

public class AddressResult {
    @SerializedName("address")
    @Expose
    public Address address;
}
