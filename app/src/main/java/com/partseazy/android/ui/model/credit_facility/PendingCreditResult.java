package com.partseazy.android.ui.model.credit_facility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 2/8/17.
 */

public class PendingCreditResult {

    @SerializedName("result")
    @Expose
    public CreditResult creditResult;
}
