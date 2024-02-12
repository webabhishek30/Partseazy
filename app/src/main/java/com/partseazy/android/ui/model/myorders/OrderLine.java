
package com.partseazy.android.ui.model.myorders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderLine {

    @SerializedName("amount")
    @Expose
    public Amount_ amount;
    @SerializedName("correction")
    @Expose
    public Object correction;
    @SerializedName("cost")
    @Expose
    public Integer cost;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("deleted")
    @Expose
    public Integer deleted;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("invoice_num")
    @Expose
    public Object invoiceNum;
    @SerializedName("is_sample")
    @Expose
    public Integer isSample;
    @SerializedName("line_no")
    @Expose
    public Integer lineNo;
    @SerializedName("machine_state")
    @Expose
    public String machineState;
    @SerializedName("master_odin")
    @Expose
    public String masterOdin;
    @SerializedName("odin")
    @Expose
    public String odin;
    @SerializedName("order_shipment_id")
    @Expose
    public Object orderShipmentId;
    @SerializedName("payable")
    @Expose
    public Integer payable;
    @SerializedName("product_id")
    @Expose
    public Integer productId;
    @SerializedName("product_info")
    @Expose
    public ProductInfo productInfo;
    @SerializedName("product_item_id")
    @Expose
    public Integer productItemId;
    @SerializedName("product_master_id")
    @Expose
    public Integer productMasterId;
    @SerializedName("product_sku_id")
    @Expose
    public Integer productSkuId;
    @SerializedName("quantity")
    @Expose
    public Integer quantity;
    @SerializedName("state_comment")
    @Expose
    public String stateComment;
    @SerializedName("state_tag")
    @Expose
    public String stateTag;
    @SerializedName("stated_at")
    @Expose
    public String statedAt;
    @SerializedName("sub_odin")
    @Expose
    public String subOdin;
    @SerializedName("supplier_id")
    @Expose
    public Integer supplierId;
    @SerializedName("tags")
    @Expose
    public Object tags;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

}
