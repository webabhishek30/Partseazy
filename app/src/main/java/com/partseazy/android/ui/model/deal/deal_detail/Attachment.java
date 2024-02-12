
package com.partseazy.android.ui.model.deal.deal_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("file_id")
    @Expose
    public Integer fileId;
    @SerializedName("src")
    @Expose
    public String src;

}
