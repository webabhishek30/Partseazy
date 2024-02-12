package com.partseazy.android.ui.model.deal.promotion.locations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 31/8/17.
 */

public class City {

    public int count;
    public String name;
    public List<District> districtList;

    public City()
    {
        districtList = new ArrayList<>();
    }

}
