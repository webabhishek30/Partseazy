package com.partseazy.android.ui.model.deal.promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 1/9/17.
 */

public class PromotionPostData {

    public List<String> clusterList;
    public double creditRequired;
    public int tradeId;

    public PromotionPostData() {
        clusterList = new ArrayList<>();
        creditRequired = 0;
    }

}

