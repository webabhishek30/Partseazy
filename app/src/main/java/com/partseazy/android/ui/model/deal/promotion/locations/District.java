package com.partseazy.android.ui.model.deal.promotion.locations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 31/8/17.
 */

public class District {

    public int count;
    public String name;
    public boolean isSelected;

    public List<Locality> localityList;

    public District()
    {
        localityList = new ArrayList<>();
    }
}
