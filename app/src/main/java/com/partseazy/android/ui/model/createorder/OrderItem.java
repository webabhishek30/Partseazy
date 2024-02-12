
package com.partseazy.android.ui.model.createorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("order_master_id")
    @Expose
    public Integer orderMasterId;
    @SerializedName("order_sub_id")
    @Expose
    public Integer orderSubId;
    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("product_sku_id")
    @Expose
    public Integer productSkuId;
    @SerializedName("supplier_id")
    @Expose
    public Integer supplierId;
    @SerializedName("product_item_id")
    @Expose
    public Integer productItemId;
    @SerializedName("quantity")
    @Expose
    public Integer quantity;
    @SerializedName("is_sample")
    @Expose
    public Integer isSample;
    @SerializedName("line_no")
    @Expose
    public Integer lineNo;
    @SerializedName("amount")
    @Expose
    public Amount amount;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("correction")
    @Expose
    public Object correction;
    @SerializedName("order_shipment_id")
    @Expose
    public Object orderShipmentId;
    @SerializedName("invoice_num")
    @Expose
    public Object invoiceNum;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}
