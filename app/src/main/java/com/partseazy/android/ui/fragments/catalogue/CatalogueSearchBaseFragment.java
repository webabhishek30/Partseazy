package com.partseazy.android.ui.fragments.catalogue;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.model.catalogue.Bucket;
import com.partseazy.android.ui.model.catalogue.Facet;
import com.partseazy.android.ui.model.home.category.Param;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Naveen Kumar on 6/2/17.
 */

public abstract class CatalogueSearchBaseFragment extends BaseFragment {


    protected String getSelectedFacetsList(Map<String, Set<Bucket>> selectedFacetBucketMap, List<Facet> facetList) {
        List<Facet> newfacetDataList = new ArrayList<Facet>();
        for (Facet facet : facetList) {
            if (facet.buckets != null) {
                if (selectedFacetBucketMap.containsKey(facet.code)) {
                    Set<Bucket> bucketSet = new HashSet<>();
                    bucketSet = selectedFacetBucketMap.get(facet.code);
                    if (bucketSet != null && bucketSet.size() > 0) {
                        for (Bucket bucketItem : bucketSet) {
                            for (int i = 0; i < facet.buckets.size(); i++) {
                                if (bucketItem.value.equals(facet.buckets.get(i).value)) {
                                    facet.buckets.get(i).isSelected = true;
                                }
                            }
                        }

                    }
                }
            }
            newfacetDataList.add(facet);
        }
        String facetDataJson = new Gson().toJson(newfacetDataList);
        return facetDataJson;
    }

    protected Map<String, Object> getRequestParams(Map<String, Set<Bucket>> selectedFacetBucketMap, List<Param> requestParams) {

        Map<String, Object> paramMap = new HashMap<>();
        if (selectedFacetBucketMap != null) {
            for (Map.Entry<String, Set<Bucket>> entry : selectedFacetBucketMap.entrySet()) {
                String key = entry.getKey();
                Set<Bucket> bucketList = entry.getValue();
                List<String> selectedList = new ArrayList<>();
                if (bucketList != null && bucketList.size() > 0) {
                    for (Bucket bucket : bucketList) {
                        selectedList.add(bucket.value);
                    }
                }
                paramMap.put(key, selectedList);
                CustomLogger.d("key::" + key + "value :: " + paramMap.get(key));
            }
        }
        if (requestParams != null && requestParams.size() > 0) {
            for (Param param : requestParams) {
                paramMap.put(param.name, param.value);
            }
        }

        CustomLogger.d("paramMAp Size ::" + paramMap.size());
        return paramMap;
    }
}
