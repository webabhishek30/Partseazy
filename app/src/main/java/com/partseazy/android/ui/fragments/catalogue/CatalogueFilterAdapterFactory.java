package com.partseazy.android.ui.fragments.catalogue;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.partseazy.android.R;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.catalogue.CatalogueSortAdapter;
import com.partseazy.android.ui.model.catalogue.SortAttibute;
import com.partseazy.android.ui.model.registration.StoreSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by naveen on 11/1/17.
 */

public class CatalogueFilterAdapterFactory {

    private Context mContext;
    private boolean isBySearch;
    private String sortCode;

    private LinearLayout  sortLL;
    private RecyclerView  sortRV;

    private CatalogueSortAdapter sortAdapter;


    protected List<SortAttibute> sortList;
    protected List<StoreSize> priceList;

    // TODO Remove the price adapter if not requires

    public CatalogueFilterAdapterFactory(Context context,
                                         boolean isBySearch,
                                         String sortCode,
                                         View attribDialogView) {
        mContext = context;
        this.isBySearch = isBySearch;
        this.sortCode = sortCode;
        initiliseAllViews(attribDialogView);
        if (sortList != null) {
            setAdapter();
        } else {
            try {
                loadSortItemList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initiliseAllViews(View view) {
        sortRV = (RecyclerView) view.findViewById(R.id.scrollable);
        sortLL = (LinearLayout) view.findViewById(R.id.sortLL);
    }

    private void setAdapter() {
            setSortAdapter();
    }

    private void setSortAdapter() {
        sortAdapter = new CatalogueSortAdapter(mContext, sortList);
        sortRV.setLayoutManager(new LinearLayoutManager(mContext));
        sortRV.setAdapter(sortAdapter);
        sortLL.setVisibility(View.VISIBLE);

    }


    private void loadSortItemList() throws JSONException {
        sortList = new ArrayList<>();

        JSONArray catalogueSortList = StaticMap.catalogueSortList;
        if(isBySearch)
        {
            catalogueSortList = StaticMap.searchSortList;
        }
        if(catalogueSortList!=null && catalogueSortList.length()>0) {
            for (int i = 0; i < catalogueSortList.length(); i++) {
                JSONObject sortObject = (JSONObject) catalogueSortList.get(i);
                SortAttibute sortAttibute = new SortAttibute(sortObject.getString("code"), sortObject.getString("name"));
                if (sortCode != null && sortCode.equals(sortAttibute.sortCode)) {
                    sortAttibute.isSelected = true;
                }
                sortList.add(sortAttibute);
            }

        }
        if(sortCode==null && sortList!=null && sortList.size()>0)
        {
            SortAttibute sortAttibute = sortList.get(0);
            sortAttibute.isSelected = true;
            sortList.remove(0);
            sortList.add(0,sortAttibute);
        }
        priceList = new ArrayList<>();
        setAdapter();
    }


}
