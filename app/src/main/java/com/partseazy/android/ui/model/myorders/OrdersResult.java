
package com.partseazy.android.ui.model.myorders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersResult {

    @SerializedName("result")
    @Expose
    public List<OrderData> orderDataList = null;

}
