
package com.partseazy.android.ui.model.registration.banner;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

    @SerializedName("background")
    @Expose
    public Background background;
    @SerializedName("banners")
    @Expose
    public List<Banner_> banners = null;

    public class Banner_ {

        @SerializedName("src")
        @Expose
        public String src;

    }

    protected class Background {

        @SerializedName("src")
        @Expose
        public String src;

    }
}
