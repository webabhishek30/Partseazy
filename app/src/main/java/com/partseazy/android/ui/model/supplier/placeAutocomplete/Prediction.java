package com.partseazy.android.ui.model.supplier.placeAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class Prediction {
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("id")
    @Expose
    public String id;
    //    @SerializedName("matched_substrings")
//    @Expose
//    public List<MatchedSubstring> matchedSubstrings = null;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("structured_formatting")
    @Expose
    public StructuredFormatting structuredFormatting;
}
