
package com.partseazy.android.ui.model.supplier.placeAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {

    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("value")
    @Expose
    public String value;

}
