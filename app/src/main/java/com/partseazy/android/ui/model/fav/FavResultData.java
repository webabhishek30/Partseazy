package com.partseazy.android.ui.model.fav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CAN on 1/12/2017.
 */

public class FavResultData {
    @SerializedName("result")
    @Expose
    public List<FavData> result = null;
}
