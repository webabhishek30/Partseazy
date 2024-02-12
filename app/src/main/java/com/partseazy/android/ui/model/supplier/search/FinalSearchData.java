package com.partseazy.android.ui.model.supplier.search;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by naveen on 6/9/17.
 */

public class FinalSearchData {
    public Set<String> footfallList;
    public Set<String> turnOverList;
    public Set<String> shopSizeList;
    public Set<String> categoryList;
    public String locationName;
    public String geoLocation;
    public String geoBox;
    public String sortCode;
    public int page;
    public boolean isMap;

    public FinalSearchData() {

        this.footfallList = new HashSet<>();
        this.turnOverList = new HashSet<>();
        this.shopSizeList = new HashSet<>();
        this.categoryList = new HashSet<>();
    }
}
