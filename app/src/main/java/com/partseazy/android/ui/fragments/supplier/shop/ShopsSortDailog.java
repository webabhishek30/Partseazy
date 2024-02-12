package com.partseazy.android.ui.fragments.supplier.shop;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.map.ShopProfileMap;
import com.partseazy.android.ui.adapters.suppliers.OnSortClickListener;
import com.partseazy.android.ui.adapters.suppliers.search.ShopSortAdapter;
import com.partseazy.android.ui.model.catalogue.SortAttibute;

import org.json.JSONException;

import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopsSortDailog extends DialogFragment {

    private static final String SORT_CODE = "SORT_CODE";

    private RecyclerView sortRV;
    private ShopSortAdapter sortAdapter;
    private List<SortAttibute> sortList;
    private OnSortClickListener onSortClickListener;
    private String sortCode;


    public static ShopsSortDailog newInstance() {

        ShopsSortDailog dialogFragment = new ShopsSortDailog();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;

    }

    public static ShopsSortDailog newInstance(OnSortClickListener onSortClickListener, String sortCode) {

        ShopsSortDailog dialogFragment = new ShopsSortDailog();
        Bundle bundle = new Bundle();
        bundle.putString(SORT_CODE, sortCode);
        dialogFragment.setArguments(bundle);
        dialogFragment.setOnSortClickListener(onSortClickListener);
        return dialogFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        if (sortList != null && sortList.size() > 0) {
            setSortAdapter();
        } else {
            try {
                loadSortItemList();
            } catch (Exception e) {
                CustomLogger.e("Exception :-", e);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortCode = getArguments().getString(SORT_CODE);
        CustomLogger.d("sortCode ::" + sortCode);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_shops_sort, new LinearLayout(getActivity()), false);
        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme);
        Window window = builder.getWindow();
        setWindowMargin(window);
        builder.setContentView(view);
        sortRV = (RecyclerView) view.findViewById(R.id.recyclerView);
        return builder;
    }

    public void setOnSortClickListener(OnSortClickListener onSortClickListener) {
        this.onSortClickListener = onSortClickListener;
    }


    private void setSortAdapter() {
        if (sortAdapter == null)
            sortAdapter = new ShopSortAdapter(getActivity(), sortList, getDialog());

        sortAdapter.setOnSortClickListener(onSortClickListener);
        sortRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        sortRV.setAdapter(sortAdapter);
    }


    private void loadSortItemList() throws JSONException {
        sortList = ShopProfileMap.getSortList();

        if (sortCode == null && sortList != null && sortList.size() > 0) {
            SortAttibute sortAttibute = sortList.get(0);
            sortAttibute.isSelected = true;
            sortList.remove(0);
            sortList.add(0, sortAttibute);
        }
        if (sortList != null && sortList.size() > 0) {
            for (int i = 0; i < sortList.size(); i++) {
                if (sortCode != null && sortCode.equals(sortList.get(i).sortCode)) {
                    sortList.get(i).isSelected = true;
                }
            }

        }

        setSortAdapter();
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    void setWindowMargin(Window window) {
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.y = 250;
//        layoutParams.y = (int) B2C2SupplierApplication.getInstance().getResources().getDimension(R.dimen.defaultCatalogFilterHeight); // bottom margin
        window.setAttributes(layoutParams);
    }


}

