package com.partseazy.android.ui.model.home.usershop;

import java.util.List;

/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class L2CategoryData {
    public String name;
    public String icon;
    public int id;
    public String imageUrl;
    public int firstChildId;
    public List<L3CategoryData> l3CategoryDataList = null;


    public L2CategoryData() {
    }

    public L2CategoryData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public L2CategoryData(int id, String name, String icon,int firstChildId) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.firstChildId = firstChildId;
    }
}
