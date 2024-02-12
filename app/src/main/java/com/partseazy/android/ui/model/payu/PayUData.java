
package com.partseazy.android.ui.model.payu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayUData {

    @SerializedName("environment")
    @Expose
    public String environment;
    @SerializedName("pg")
    @Expose
    public String pg;
    @SerializedName("pg_params")
    @Expose
    public PgParams pgParams;

}
