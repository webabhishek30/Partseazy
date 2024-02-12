
package com.partseazy.android.ui.model.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OnBoardingStatus {

    @SerializedName("actual_status")
    @Expose
    public String actual_status;

    @SerializedName("forced_status")
    @Expose
    public String forced_status;


    @SerializedName("shop_id")
    @Expose
    public Integer storeId;

    @SerializedName("invitation_code")
    @Expose
    public String invitationCode;

    @SerializedName("user_role")
    @Expose
    public ArrayList<String> role = null;
}
