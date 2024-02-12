package com.partseazy.android.ui.fragments.deals.buy_deal;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.deals.filters.DealChildFilterAdapter;
import com.partseazy.android.ui.adapters.deals.filters.DealParentFilterAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.filters.FilterMaster;
import com.partseazy.android.ui.model.deal.filters.FilterValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 30/8/17.
 */

public class DealFilterFragment extends BaseFragment implements View.OnClickListener,
        DealParentFilterAdapter.OnParentItemSelectedListener, DealChildFilterAdapter.OnChildItemSelectedListener {

    @BindView(R.id.parentRV)
    protected RecyclerView parentRV;

    @BindView(R.id.childRV)
    protected RecyclerView childRV;

    @BindView(R.id.dealSwitchLL)
    protected LinearLayout dealSwitchLL;

    @BindView(R.id.childRVLyt)
    protected LinearLayout childRVLyt;

    @BindView(R.id.dealSwitch)
    protected Switch dealSwitch;

    @BindView(R.id.searchET)
    protected EditText searchET;

    @BindView(R.id.editCrossIcon)
    protected ImageView editCrossIcon;

    @BindView(R.id.applyBT)
    protected Button applyBT;

    @BindView(R.id.clearAllBT)
    protected TextView clearAllBT;


    public static final String FILTER_LIST = "FILTER_LIST";
    public static final String SELECTED_MAP = "SELECTED_MAP";
    public static final String IS_PUBLIC_DEAL = "IS_PUBLIC_DEAL";


    private OnFilterAppliedListener onFilterAppliedListener;
    private DealParentFilterAdapter dealParentFilterAdapter;
    private DealChildFilterAdapter dealChildFilterAdapter;


    protected Map<String, Set<FilterValue>> selectedFilterMap;

    private List<FilterMaster> filterMasterList = new ArrayList<>();
    private List<FilterValue> filterValueList = new ArrayList<>();
    private int isPublicDeal = 1;
    private String selectedFilterMasterKey;

    public static DealFilterFragment newInstance() {
        DealFilterFragment fragment = new DealFilterFragment();
        return fragment;
    }

    public static DealFilterFragment newInstance(String filterListJson, String selectedMapJson, Integer isPublicDeal, OnFilterAppliedListener listener) {
        DealFilterFragment fragment = new DealFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILTER_LIST, filterListJson);
        bundle.putString(SELECTED_MAP, selectedMapJson);
        bundle.putInt(IS_PUBLIC_DEAL, isPublicDeal);
        fragment.setArguments(bundle);
        fragment.setOnFilterChangeNotifier(listener);
        return fragment;
    }


    public interface OnFilterAppliedListener {


        public void onApplyFilter(Integer isPublicDeal, Map<String, Set<FilterValue>> selectedFilterMap);

        public void onClearFilterApplied();
    }

    public void setOnFilterChangeNotifier(OnFilterAppliedListener onFilterAppliedListener) {
        this.onFilterAppliedListener = onFilterAppliedListener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_deal_filters;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.filter);
    }

    public static String getTagName() {
        return DealFilterFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (filterMasterList.size() > 0) {
                    setParentAdapter();
                    setChildAdapter();
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filterListJson = getArguments().getString(FILTER_LIST);
        Type listType = new TypeToken<List<FilterMaster>>() {
        }.getType();
        filterMasterList = new Gson().fromJson(filterListJson, listType);


        Type selectedBucketMap = new TypeToken<Map<String, Set<FilterValue>>>() {
        }.getType();

        String selectedFilterMapJson = getArguments().getString(SELECTED_MAP);
        selectedFilterMap = new Gson().fromJson(selectedFilterMapJson, selectedBucketMap);

        isPublicDeal = getArguments().getInt(IS_PUBLIC_DEAL, isPublicDeal);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCrossNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        applyBT.setOnClickListener(this);
        clearAllBT.setOnClickListener(this);
        editCrossIcon.setOnClickListener(this);
        searchET.addTextChangedListener(new SearchTextWatcher());
        setSwitchHandling();
        return view;

    }


    private void setParentAdapter() {
        if (dealParentFilterAdapter == null)
            dealParentFilterAdapter = new DealParentFilterAdapter(context, filterMasterList, this);
        parentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        parentRV.setAdapter(dealParentFilterAdapter);
    }


    private void setChildAdapter() {
        if (dealChildFilterAdapter == null)
            dealChildFilterAdapter = new DealChildFilterAdapter(context, filterValueList, this);
        childRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        childRV.setAdapter(dealChildFilterAdapter);
    }


    private void setSwitchHandling() {
        if (isPublicDeal == 1) {
            dealSwitch.setChecked(false);
        } else {
            dealSwitch.setChecked(true);
        }

        dealSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isPublicDeal = 0;
                } else {
                    isPublicDeal = 1;
                }

            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == applyBT.getId()) {
            ((BaseActivity) getActivity()).onPopBackStack(true);
            onFilterAppliedListener.onApplyFilter(isPublicDeal, selectedFilterMap);
            // onFilterAppliedListener.onFilterApplied(dealChildFilterAdapter.getSelectedCategoryIdList(),isPublicDeal);
        }

        if (view.getId() == clearAllBT.getId()) {
            clearAllSelectedValue();
            isPublicDeal = 1;
            setSwitchHandling();
            onFilterAppliedListener.onClearFilterApplied();
        }

        if (view.getId() == editCrossIcon.getId()) {
            searchET.setText("");
        }

    }

    @Override
    public void onParentSelected(FilterMaster model) {

        selectedFilterMasterKey = model.code;

        if (model.code.equals(DealConstants.FILTER_SWITCH_CODE)) {
            childRVLyt.setVisibility(View.GONE);
            dealSwitchLL.setVisibility(View.VISIBLE);
        } else {

            filterValueList.clear();
//            dealChildFilterAdapter.setFilterMasterKey(model.type);
//            filterValueList.addAll(model.values);
//            dealChildFilterAdapter.notifyDataSetChanged();
            dealChildFilterAdapter.updateFilterData(model.code, model.values);
            childRVLyt.setVisibility(View.VISIBLE);
            dealSwitchLL.setVisibility(View.GONE);
        }

    }


    private void addSelectedFilterValueToMap(String filterMasterKey, FilterValue filterValue) {

        if (selectedFilterMap.containsKey(filterMasterKey)) {
            Set<FilterValue> valueSet = new HashSet<>();
            valueSet = selectedFilterMap.get(filterMasterKey);
            valueSet.add(filterValue);
            selectedFilterMap.put(filterMasterKey, valueSet);

        } else {
            Set<FilterValue> valueSet = new HashSet<>();
            valueSet.add(filterValue);
            selectedFilterMap.put(filterMasterKey, valueSet);

        }
        CustomLogger.d("Add::  MapKey::" + filterValue.name + "  List::" + selectedFilterMap.get(filterMasterKey).size());
    }


    private void removeSelectedFilteValueFromMap(String filterMasterKey, FilterValue filterValue) {

        if (selectedFilterMap != null) {
            if (selectedFilterMap.containsKey(filterMasterKey)) {
                Set<FilterValue> valueSet = new HashSet<>();
                valueSet = selectedFilterMap.get(filterMasterKey);
                for (FilterValue selectedValueItem : valueSet) {
                    CustomLogger.d("oldValue " + selectedValueItem.name + "newValue ::" + filterValue.name);
                    if (selectedValueItem.name.equals(filterValue.name)) {
                        valueSet.remove(selectedValueItem);
                        if (valueSet.size() == 0) {
                            selectedFilterMap.remove(filterMasterKey);
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onSelectChild(String filterMasterKey, FilterValue filterValue) {
        addSelectedFilterValueToMap(filterMasterKey, filterValue);
    }

    @Override
    public void onUnselectChild(String filterMasterKey, FilterValue filterValue) {
        removeSelectedFilteValueFromMap(filterMasterKey, filterValue);
    }

    private class SearchTextWatcher implements TextWatcher, DealChildFilterAdapter.OnChildItemSelectedListener {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            query = query.toString().toLowerCase();

            final List<FilterValue> filteredList = new ArrayList<>();

            for (int i = 0; i < filterValueList.size(); i++) {

                final String text = filterValueList.get(i).name.toLowerCase();

                if (text.contains(query)) {

                    filteredList.add(filterValueList.get(i));
                }
            }
            //  if (filteredList != null && filteredList.size() > 0) {
            dealChildFilterAdapter = new DealChildFilterAdapter(getContext(), selectedFilterMasterKey, filteredList, this);
            childRV.setAdapter(dealChildFilterAdapter);
            dealChildFilterAdapter.notifyDataSetChanged();
            //  }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onSelectChild(String key, FilterValue filterValue) {
            addSelectedFilterValueToMap(key, filterValue);
        }

        @Override
        public void onUnselectChild(String key, FilterValue filterValue) {
            removeSelectedFilteValueFromMap(key, filterValue);
        }
    }


    private void clearAllSelectedValue() {
        if (selectedFilterMap != null)
            selectedFilterMap.clear();
        if (dealChildFilterAdapter != null)
            dealChildFilterAdapter.clearList();
    }
}
