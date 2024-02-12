package com.partseazy.android.ui.model.supplier.placeAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class PlacesResult {
    @SerializedName("predictions")
    @Expose
    public List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    public String status;
}
