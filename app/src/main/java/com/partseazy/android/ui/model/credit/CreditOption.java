package com.partseazy.android.ui.model.credit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Adeptical Solutions on 1/2/2017.
 */

public class CreditOption {

    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("options")
    @Expose
    public List<CreditOptionList> options = null;
}
