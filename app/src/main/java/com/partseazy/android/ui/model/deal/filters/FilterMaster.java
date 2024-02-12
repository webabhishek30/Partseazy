package com.partseazy.android.ui.model.deal.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 1/9/17.
 */

public class FilterMaster {

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("values")
    @Expose
    public List<FilterValue> values = null;

    public boolean isSelected;

    public FilterMaster(String type)
    {
        this.type = type;
    }

    public FilterMaster(String code,String type)
    {
        this.type = type;
        this.code = code;
        this.values= new ArrayList<>();
    }


    public FilterMaster(String code,String type,boolean isSelected)
    {
        this.code = code;
        this.type = type;
        this.isSelected = isSelected;
        this.values= new ArrayList<>();
    }

}
