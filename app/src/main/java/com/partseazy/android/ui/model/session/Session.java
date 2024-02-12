
package com.partseazy.android.ui.model.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Session {

    @SerializedName("auth")
    @Expose
    public String auth;
    @SerializedName("session_id")
    @Expose
    public String sessionId;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("user_role")
    @Expose
    public List<String> userRole = null;

}
