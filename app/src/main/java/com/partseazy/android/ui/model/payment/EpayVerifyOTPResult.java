package com.partseazy.android.ui.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 22/4/17.
 */

public class EpayVerifyOTPResult {
    @SerializedName("success")
    @Expose
    public Integer success;
}
