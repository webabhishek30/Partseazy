package com.partseazy.android.ui.model.deal.create_deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 10/5/17.
 */

public class NewDealResult {
    @SerializedName("result")
    @Expose
    public NewDealData result;
}
