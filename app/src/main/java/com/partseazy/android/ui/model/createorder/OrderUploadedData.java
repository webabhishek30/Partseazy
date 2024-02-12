package com.partseazy.android.ui.model.createorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderUploadedData {
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
