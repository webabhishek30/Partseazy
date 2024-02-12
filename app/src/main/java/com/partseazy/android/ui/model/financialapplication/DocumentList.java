package com.partseazy.android.ui.model.financialapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveen on 4/1/17.
 */

public class DocumentList {

    @SerializedName("result")
    @Expose
    public List<Document> result = null;

}