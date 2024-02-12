package com.partseazy.android.ui.model.home.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class Result {
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("label")
    @Expose
    public String label;
    @SerializedName("setting")
    @Expose
    public SettingData setting;
    @SerializedName("data")
    @Expose
    public List<DataItem> datalist = null;
    @SerializedName("data_url")
    @Expose
    public String dataUrl;

    public List<ProductData> productDataList;
}
