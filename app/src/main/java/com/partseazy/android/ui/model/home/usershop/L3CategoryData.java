package com.partseazy.android.ui.model.home.usershop;

import java.util.List;

/**
 * Created by Naveen Kumar on 1/2/17.
 */

public class L3CategoryData {
    public String name;
    public String icon;
    public int id;
    public String imageUrl;

    public L3CategoryData()
    {

    }

    public L3CategoryData(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public L3CategoryData(int id, String name, String icon)
    {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

}
