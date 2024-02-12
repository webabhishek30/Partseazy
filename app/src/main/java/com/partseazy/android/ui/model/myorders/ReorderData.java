package com.partseazy.android.ui.model.myorders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReorderData {

    @SerializedName("error")
    @Expose
    public String error;

   @SerializedName("message")
    @Expose
    public String message;

}
