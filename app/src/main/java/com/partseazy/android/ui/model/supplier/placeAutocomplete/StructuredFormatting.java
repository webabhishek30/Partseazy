package com.partseazy.android.ui.model.supplier.placeAutocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 6/9/17.
 */

public class StructuredFormatting {
    @SerializedName("main_text")
    @Expose
    public String mainText;
    //    @SerializedName("main_text_matched_substrings")
//    @Expose
//    public List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
    @SerializedName("secondary_text")
    @Expose
    public String secondaryText;
}
