package com.partseazy.android.ui.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhang on 03/07/17.
 */

public class SuccessResponse {
    @SerializedName("success")
    @Expose
    public int success;
}
