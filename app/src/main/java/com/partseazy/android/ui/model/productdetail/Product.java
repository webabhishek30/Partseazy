
package com.partseazy.android.ui.model.productdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("is_active")
    @Expose
    public Boolean isActive;
    @SerializedName("in_stock")
    @Expose
    public boolean inStock;
    @SerializedName("supplier_regional")
    @Expose
    public boolean isRegionalSupplier;
    @SerializedName("attribute_product")
    @Expose
    public List<AttrProduct> attrProduct = null;
    @SerializedName("brand")
    @Expose
    public Brand brand;
    @SerializedName("category")
    @Expose
    public Category category;
    @SerializedName("category_path")
    @Expose
    public List<CategoryPath> categoryPath = null;
    @SerializedName("dispatch")
    @Expose
    public Integer dispatch;
    @SerializedName("margin")
    @Expose
    public Integer margin;
    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("max")
    @Expose
    public Integer max;
    @SerializedName("min")
    @Expose
    public Integer min;
    @SerializedName("mop")
    @Expose
    public Integer mop;
    @SerializedName("mrp")
    @Expose
    public Integer mrp;

    @SerializedName("product_master")
    @Expose
    public ProductMaster productMaster;
    @SerializedName("product_master_bag")
    @Expose
    public List<ProductMasterBag> productMasterBag = null;
    @SerializedName("product")
    @Expose
    public List<Product_> products = null;
    @SerializedName("rating")
    @Expose
    public Float rating;
    @SerializedName("stock_correction")
    @Expose
    public StockCorrection stockCorrection;
    @SerializedName("supplier")
    @Expose
    public Supplier supplier;
    @SerializedName("supplier_rating")
    @Expose
    public Float supplierRating;
    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("warranty")
    @Expose
    public String warranty;

    public  boolean isFavourite;

}
