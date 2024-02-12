
package com.partseazy.android.ui.model.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttrProduct {
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("default")
    @Expose
    public String _default;
    @SerializedName("ditto")
    @Expose
    public Boolean ditto;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("tabbed")
    @Expose
    public Integer tabbed;

}
