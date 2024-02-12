package com.partseazy.android.ui.fragments.catalogue;

import com.partseazy.android.ui.model.catalogue.Bucket;

import java.util.Comparator;

/**
 * Created by taushif on 19/1/17.
 */

public class FilterBucketComparator implements Comparator<Bucket> {

    @Override
    public int compare(Bucket bucket1, Bucket bucket2) {

        if(bucket1.isSelected && bucket2.isSelected)
            return 0;
        else if(bucket1.isSelected && !bucket2.isSelected)
            return -1;
        else
            return 1;
    }
}
