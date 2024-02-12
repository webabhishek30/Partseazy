package com.partseazy.android.ui.model.home.autosuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class AutoSuggesstionKeyword {
    @SerializedName("result")
    @Expose
    public List<AutoSuggesstionKeywordData> result = null;


}
