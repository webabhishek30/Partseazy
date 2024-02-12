package com.partseazy.android.ui.model.financialapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Naveen Kumar on 2/2/17.
 */

public class FinanceStatus {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("url")
    @Expose
    public String url;

}
