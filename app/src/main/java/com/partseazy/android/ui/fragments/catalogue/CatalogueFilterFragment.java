package com.partseazy.android.ui.fragments.catalogue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebParamsConstants;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.catalogue.BucketFilterAdapter;
import com.partseazy.android.ui.adapters.catalogue.FacetsFilterAdapter;
import com.partseazy.android.ui.model.catalogue.Bucket;
import com.partseazy.android.ui.model.catalogue.CatalogueResult;
import com.partseazy.android.ui.model.catalogue.Facet;
import com.partseazy.android.ui.model.catalogue.PriceRangeFilter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.widget.rangeSeekBar.OnRangeSeekbarChangeListener;
import com.partseazy.android.ui.widget.rangeSeekBar.OnRangeSeekbarFinalValueListener;
import com.partseazy.android.ui.widget.rangeSeekBar.RangeSeekbar;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 10/1/17.
 */

public class CatalogueFilterFragment extends BaseFragment implements View.OnClickListener,
        FacetsFilterAdapter.OnFacetItemSelectedListener,
        BucketFilterAdapter.onBucketSelectedListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.priceLyt)
    protected LinearLayout priceLyt;
    @BindView(R.id.childTabLyt)
    protected LinearLayout facetTabLyt;
    @BindView(R.id.parentRecylerView)
    protected RecyclerView facetRecylerView;
    @BindView(R.id.childRecylerView)
    protected RecyclerView bucketRecylerView;
    @BindView(R.id.searchET)
    protected EditText searchET;
    @BindView(R.id.editCrossIcon)
    protected ImageView editCrossIcon;
    @BindView(R.id.radioGroup)
    protected RadioGroup radioGroup;
    @BindView(R.id.priceRangeSeekBar)
    protected RangeSeekbar rangeSeekbar;
    @BindView(R.id.maxPriceTV)
    protected TextView maxPriceTV;
    @BindView(R.id.minPriceTV)
    protected TextView minPriceTV;
    @BindView(R.id.applyBT)
    protected Button applyBT;
    @BindView(R.id.clearAllBT)
    protected TextView clearAllBT;

    public static final String SEARCH_QUERY = "search_query";
    public static final String CATEGORY_ID = "category_id";
    public static final String FILTER_DATA_KEY = "filter_data_key";
    public static final String SELECTED_BUCKET_MAP = "selected_bucket_map";
    public static final String NO_PRODUCT_RESULT_PARAM = "result";
    public static final String PRICE = "Price";
    public static final String FACET_PRICE_MIN_KEY = "_f.price_min";
    public static final String FACET_PRICE_MAX_KEY = "_f.price_max";

    private FacetsFilterAdapter facetFilterAdapter;
    private BucketFilterAdapter bucketfilterAdapter;

    private List<Facet> facetList;
    private List<Bucket> facetBucketList;
    private List<PriceRangeFilter> priceBucketList;
    private List<Facet> selectedFacetListFromJson;
    protected Map<String, Set<Bucket>> selectedFacetBucketMap;
    private OnFilterAppliedListener mOnfilOnFilterAppliedListener;

    private Facet selectedFacet;
    private int categoryId;
    private String searchQuery;
    private boolean showProgressOnFilterUpdate;
    private boolean isSearchFilter;
    int minPriceFacetValue = 20;
    int maxPriceFacetValue = 4000;


    public static CatalogueFilterFragment newInstance(String catalogueJson, String selectedBucketMap, int categoryId, OnFilterAppliedListener onFilterAppliedListener) {
        CatalogueFilterFragment fragment = new CatalogueFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILTER_DATA_KEY, catalogueJson);
        bundle.putString(SELECTED_BUCKET_MAP, selectedBucketMap);
        bundle.putInt(CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        fragment.setOnFilterChangeNotifier(onFilterAppliedListener);
        return fragment;
    }

    public static CatalogueFilterFragment newInstance(String catalogueJson, String selectedBucketMap, String searchQuery, OnFilterAppliedListener onFilterAppliedListener) {
        CatalogueFilterFragment fragment = new CatalogueFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FILTER_DATA_KEY, catalogueJson);
        bundle.putString(SELECTED_BUCKET_MAP, selectedBucketMap);
        bundle.putString(SEARCH_QUERY, searchQuery);
        fragment.setArguments(bundle);
        fragment.setOnFilterChangeNotifier(onFilterAppliedListener);

        return fragment;
    }

    public void setOnFilterChangeNotifier(OnFilterAppliedListener onFilterAppliedListener) {
        mOnfilOnFilterAppliedListener = onFilterAppliedListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String productData = getArguments().getString(FILTER_DATA_KEY);
        String selectedBucketMapJson = getArguments().getString(SELECTED_BUCKET_MAP);

        categoryId = getArguments().getInt(CATEGORY_ID);
        searchQuery = getArguments().getString(SEARCH_QUERY);

        Type listType = new TypeToken<List<Facet>>() {
        }.getType();
        Type selectedBucketMap = new TypeToken<Map<String, Set<Bucket>>>() {
        }.getType();

        selectedFacetListFromJson = new Gson().fromJson(productData, listType);
        selectedFacetBucketMap = new Gson().fromJson(selectedBucketMapJson, selectedBucketMap);
        if (categoryId == 0) {
            isSearchFilter = true;
        }

        CustomLogger.d("SelectedFacetBucket Map ::" + selectedBucketMapJson);

    }


    public static String getTagName() {
        return CatalogueFilterFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_catalogue_filter;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.filter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCrossNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (facetList != null) {
            setFacetAdapter();
        } else {
            loadFacetItemList();
        }

        if (facetBucketList != null) {
            setBucketAdapter();
        } else {
            loadFacetBucketList();
        }

        handlePriceRangeBarClicks();
        applyBT.setOnClickListener(this);
        clearAllBT.setOnClickListener(this);
        editCrossIcon.setOnClickListener(this);
        radioGroup.setOnClickListener(this);
        searchET.addTextChangedListener(new SearchTextWatcher());
        return view;

    }

    private void loadFilterData(boolean isFromClearData) {
        if (showProgressOnFilterUpdate) {
            showProgressDialog(getString(R.string.loading), false);
        }

        if (isFromClearData) {
            clearAllSelectedValue();
        }
        Map<String, Object> requestParams = getRequestParams();
        requestParams.put(NO_PRODUCT_RESULT_PARAM, 0);
        String filterRequestUrl;
        if (isSearchFilter) {
            filterRequestUrl = WebServiceConstants.POST_SEARCH;
            requestParams.put(WebParamsConstants.QUERY, searchQuery);
        } else {
            filterRequestUrl = WebServiceConstants.POST_BROWSE + categoryId;
        }

        getNetworkManager().PostRequest(RequestIdentifier.FILTER_DATA_REQUEST_ID.ordinal(),
                filterRequestUrl, requestParams, null, this, this, true);

    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, final JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FILTER_DATA_REQUEST_ID.ordinal()) {
            if (showProgressOnFilterUpdate) {
                hideProgressBar();
            }
            getGsonHelper().parse(responseObject.toString(), CatalogueResult.class, new OnGsonParseCompleteListener<CatalogueResult>() {
                        @Override
                        public void onParseComplete(CatalogueResult data) {
                            try {
                                hideProgressDialog();
                                if (data.facets != null && data.facets.size() > 0) {
                                    populateBucketListData(data.facets, selectedFacet);
                                    if (!selectedFacet.name.equals(PRICE)) {
                                        setBucketApadter(selectedFacet);
                                    }
                                    showProgressOnFilterUpdate = false;
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );

        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        return true;
    }

    private void setFacetAdapter() {
        if (facetFilterAdapter == null)
            facetFilterAdapter = new FacetsFilterAdapter(getContext(), selectedFacetListFromJson, this);
        facetRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        facetRecylerView.setAdapter(facetFilterAdapter);
    }


    private void setBucketAdapter() {
        if (bucketfilterAdapter == null)
            bucketfilterAdapter = new BucketFilterAdapter(getContext(), facetBucketList, selectedFacet, this);
        bucketRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bucketRecylerView.setAdapter(bucketfilterAdapter);
    }

    private void setPriceView() {
        int count = 0;
        rangeSeekbar.setMinValue(minPriceFacetValue);
        rangeSeekbar.setMaxValue(maxPriceFacetValue);
        minPriceTV.setText(getString(R.string.rs, minPriceFacetValue));
        maxPriceTV.setText(getString(R.string.rs, maxPriceFacetValue));
        radioGroup.clearCheck();
        for (PriceRangeFilter priceRange : priceBucketList) {
            RadioButton rbn = new RadioButton(getContext());
            rbn.setId(count);
            rbn.setText(priceRange.rangeValue);
            rbn.setTextSize(16);
            rbn.setTag(priceRange);
            radioGroup.addView(rbn);
            count++;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group != null && checkedId > -1) {
                    View radioButton = radioGroup.findViewById(checkedId);

                    // if(!isClearRadioGroup) {
                    PriceRangeFilter priceRangeFilter = (PriceRangeFilter) radioButton.getTag();
                    minPriceTV.setText(getString(R.string.rs, priceRangeFilter.minVal));
                    maxPriceTV.setText(getString(R.string.rs, priceRangeFilter.maxVal));
                    //rangeSeekbar.setGap(5);
                    addPriceToBucketMap(priceRangeFilter);
                    // }
                }

            }
        });

    }

    private void loadFacetItemList() {
        facetList = new ArrayList<>();
        int counter = 0;
        if (selectedFacetListFromJson != null && selectedFacetListFromJson.size() > 0) {
            for (Facet facet : selectedFacetListFromJson) {
                if (counter == 0) {
                    facet.isSelected = true;
                    selectedFacet = facet;
                }
                facetList.add(facet);
                counter++;
            }
        }
        setFacetAdapter();
    }

    private void loadPriceBucketList(List<PriceRangeFilter> priceBucketList, int minFacetPrice, int maxFacetPrice) throws JSONException {
        List<Integer> priceList = new ArrayList<>();
        priceList.add(minFacetPrice);
        JSONArray priceArray = StaticMap.filterPriceList;
        if (priceArray != null && priceArray.length() > 0) {

            for (int i = 0; i < priceArray.length(); i++) {
                int price = (int) priceArray.get(i);
                if (price > minFacetPrice && price < maxFacetPrice) {
                    priceList.add(price);
                }

            }

        }
        priceList.add(maxFacetPrice);
        for (int i = 1; i < priceList.size(); i++) {
            int firstPrice = priceList.get(i - 1);
            int secondPrice = priceList.get(i);
            PriceRangeFilter priceRange = new PriceRangeFilter();
            priceRange.minVal = firstPrice;
            priceRange.maxVal = secondPrice;
            priceRange.rangeValue = context.getString(R.string.rs, firstPrice) + " to " + context.getString(R.string.rs, secondPrice);
            priceBucketList.add(priceRange);

        }

    }

    private void loadPriceItemList(Facet facet) throws JSONException {
        priceBucketList = new ArrayList<>();
        minPriceFacetValue = CommonUtility.convertionPaiseToRupee(facet.min);
        maxPriceFacetValue = CommonUtility.convertionPaiseToRupee(facet.max);

        if (minPriceFacetValue == maxPriceFacetValue) {
            PriceRangeFilter priceRange = new PriceRangeFilter();
            priceRange.minVal = minPriceFacetValue;
            priceRange.maxVal = maxPriceFacetValue;
            priceRange.rangeValue = context.getString(R.string.rs, minPriceFacetValue);
            priceBucketList.add(priceRange);
            return;
        } else {
            loadPriceBucketList(priceBucketList, minPriceFacetValue, maxPriceFacetValue);
        }

    }


    private void loadFacetBucketList() {
        facetBucketList = new ArrayList<>();
        if (selectedFacetListFromJson != null && selectedFacetListFromJson.size() > 0) {
            Facet facet = selectedFacetListFromJson.get(0);
            if (facet.name.equalsIgnoreCase(PRICE)) {
                facetTabLyt.setVisibility(View.GONE);
                priceLyt.setVisibility(View.VISIBLE);
            } else {
                if (facet.buckets != null) {
                    for (Bucket bucket : facet.buckets) {
                        facetBucketList.add(bucket);
                    }
                }
                facetTabLyt.setVisibility(View.VISIBLE);
                priceLyt.setVisibility(View.GONE);
            }
        }
        setBucketAdapter();
    }

    public void addPriceToBucketMap(PriceRangeFilter priceRangeFilter) {
        Bucket priceMinBucket = new Bucket();
        Bucket priceMaxBucket = new Bucket();
        priceMinBucket.value = CommonUtility.convertionRupeeToPaise(priceRangeFilter.minVal) + "";
        priceMaxBucket.value = CommonUtility.convertionRupeeToPaise(priceRangeFilter.maxVal) + "";
        priceMinBucket.isSelected = true;
        priceMaxBucket.isSelected = true;
        Set<Bucket> minPriceBucketSet = new HashSet<Bucket>();
        Set<Bucket> maxPriceBucketSet = new HashSet<Bucket>();
        minPriceBucketSet.add(priceMinBucket);
        maxPriceBucketSet.add(priceMaxBucket);
        selectedFacetBucketMap.put(FACET_PRICE_MIN_KEY, minPriceBucketSet);
        selectedFacetBucketMap.put(FACET_PRICE_MAX_KEY, maxPriceBucketSet);
    }

    private void handlePriceRangeBarClicks() {

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Integer minVal = Integer.valueOf(String.valueOf(minValue));
                Integer maxVal = Integer.valueOf(String.valueOf(maxValue));
                minPriceTV.setText(getString(R.string.rs, minVal));
                maxPriceTV.setText(getString(R.string.rs, maxVal));
                radioGroup.clearCheck();

            }
        });


        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                CustomLogger.d("CRS=>" + String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                Integer minVal = Integer.valueOf(String.valueOf(minValue));
                Integer maxVal = Integer.valueOf(String.valueOf(maxValue));
                PriceRangeFilter priceRangeFilter = new PriceRangeFilter();
                priceRangeFilter.minVal = minVal;
                priceRangeFilter.maxVal = maxVal;
                radioGroup.clearCheck();
                addPriceToBucketMap(priceRangeFilter);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == applyBT.getId()) {

            final Map<String, Object> filterParams = getRequestParams();
            ((BaseActivity) getActivity()).onPopBackStack(true);
            mOnfilOnFilterAppliedListener.onFilterApplied(filterParams, selectedFacetBucketMap);
        }

        if (view.getId() == clearAllBT.getId()) {
            clearAllSelectedValue();
            loadFilterData(true);
            PartsEazyEventBus.getInstance().postEvent(EventConstant.CLEAR_ALL_FILTER, selectedFacetBucketMap);
            mOnfilOnFilterAppliedListener.onClearDataApplied(selectedFacetBucketMap);
        }

        if (view.getId() == editCrossIcon.getId()) {
            searchET.setText("");
        }


    }


    private Map<String, Object> getRequestParams() {
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
        return paramMap;
    }

    private void clearAllSelectedValue() {
        if(selectedFacetBucketMap!=null)
            selectedFacetBucketMap.clear();
        bucketfilterAdapter.clearList(facetBucketList);
        if (radioGroup != null) {
            radioGroup.clearCheck();
        }
        if (rangeSeekbar != null) {
            rangeSeekbar.setMinValue(minPriceFacetValue);
            rangeSeekbar.setMaxValue(maxPriceFacetValue);
        }
    }

    private void populateBucketListData(List<Facet> facetList, Facet facet) {
        if (facetBucketList != null)
            facetBucketList.clear();

        HashMap<String, Bucket> newBucketMap = new HashMap<>();

        if (selectedFacetBucketMap.containsKey(facet.code)) {
            Set<Bucket> bucketSet = new HashSet<>();
            bucketSet = selectedFacetBucketMap.get(facet.code);
            if (bucketSet != null && bucketSet.size() > 0) {
                for (Bucket bucketItem : bucketSet) {
                    if (bucketItem.count > 0) {
                        newBucketMap.put(bucketItem.value, bucketItem);
                    }
                }

            }
        }

        Facet newFacetItem = null;

        for (Facet fac : facetList) {
            if (fac.name.equals(facet.name)) {
                newFacetItem = new Facet();
                newFacetItem = fac;
                break;
            }
        }

        if (newFacetItem != null) {
            if (newFacetItem.buckets != null && newFacetItem.buckets.size() > 0) {
                for (Bucket bucket : newFacetItem.buckets) {
                    if (!newBucketMap.containsKey(bucket.value)) {
                        if (bucket.count > 0) {
                            newBucketMap.put(bucket.value, bucket);
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, Bucket> entry : newBucketMap.entrySet()) {
            Bucket bucket = entry.getValue();
            facetBucketList.add(bucket);
        }

        //  Used for Sortinf checked Mark on TOP and unselected on Bottom
        Collections.sort(facetBucketList, new FilterBucketComparator());
    }

    private void setBucketApadter(Facet facet) {

        if (facetBucketList != null) {
            bucketfilterAdapter = new BucketFilterAdapter(getContext(), facetBucketList, facet, CatalogueFilterFragment.this);
            bucketRecylerView.setAdapter(bucketfilterAdapter);
            facetTabLyt.setVisibility(View.VISIBLE);
            priceLyt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFacetSelected(Facet facet) {
        selectedFacet = facet;

        if (facet.name.equals(PRICE)) {

            if (priceBucketList == null) {
                try {
                    loadPriceItemList(facet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setPriceView();
            }

            facetTabLyt.setVisibility(View.GONE);
            priceLyt.setVisibility(View.VISIBLE);

        } else {
            loadFilterData(false);
        }
    }

    @Override
    public void onSelectBucket(String facetName, Bucket bucket) {
        addSelectedBucketMap(facetName, bucket);
        showProgressOnFilterUpdate = true;
    }

    @Override
    public void unSelectBucket(String facetName, Bucket bucket) {
        removeSelectedBucket(facetName, bucket);
        showProgressOnFilterUpdate = true;
    }

    private void addSelectedBucketMap(String facetCode, Bucket bucket) {

        if (selectedFacetBucketMap.containsKey(facetCode)) {
            Set<Bucket> bucketSet = new HashSet<>();
            bucketSet = selectedFacetBucketMap.get(facetCode);
            bucketSet.add(bucket);
            selectedFacetBucketMap.put(facetCode, bucketSet);

        } else {
            Set<Bucket> bucketSet = new HashSet<>();
            bucketSet.add(bucket);
            selectedFacetBucketMap.put(facetCode, bucketSet);

        }
        PartsEazyEventBus.getInstance().postEvent(EventConstant.ADD_BUCKET_FILTER_ID, selectedFacetBucketMap);
        CustomLogger.d("Add::  MapKey::" + bucket.value + "  List::" + selectedFacetBucketMap.get(facetCode).size());
    }


    private void removeSelectedBucket(String facetCode, Bucket bucket) {
        if (selectedFacetBucketMap != null) {
            if (selectedFacetBucketMap.containsKey(facetCode)) {
                Set<Bucket> bucketSet = new HashSet<>();
                bucketSet = selectedFacetBucketMap.get(facetCode);
                for (Bucket selectedBucketItem : bucketSet) {
                    CustomLogger.d("oldValue " + selectedBucketItem.value + "newValue ::" + bucket.value);
                    if (selectedBucketItem.value.equals(bucket.value)) {
                        bucketSet.remove(selectedBucketItem);
                        if (bucketSet.size() == 0) {
                            CustomLogger.d("step 7 ::" + bucket.value);
                            selectedFacetBucketMap.remove(facetCode);
                        }
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.REMOVE_BUCKET_FILTER_ID, selectedFacetBucketMap);
                        break;
                    }
                }
            }
        }
    }

    private class SearchTextWatcher implements TextWatcher, BucketFilterAdapter.onBucketSelectedListener {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            query = query.toString().toLowerCase();

            final List<Bucket> filteredList = new ArrayList<>();

            for (int i = 0; i < facetBucketList.size(); i++) {

                final String text = facetBucketList.get(i).value.toLowerCase();

                if (text.contains(query)) {

                    filteredList.add(facetBucketList.get(i));
                }
            }
            //  if (filteredList != null && filteredList.size() > 0) {
            bucketfilterAdapter = new BucketFilterAdapter(getContext(), filteredList, selectedFacet, this);
            bucketRecylerView.setAdapter(bucketfilterAdapter);
            bucketfilterAdapter.notifyDataSetChanged();
            //  }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onSelectBucket(String facetCode, Bucket bucket) {
            addSelectedBucketMap(facetCode, bucket);
        }

        @Override
        public void unSelectBucket(String facetCode, Bucket bucket) {
            removeSelectedBucket(facetCode, bucket);
        }
    }


    interface OnFilterAppliedListener {

        public void onFilterApplied(Map<String, Object> filterMap, Map<String, Set<Bucket>> selectedFacetBucketMap);

        public void onClearDataApplied(Map<String,Set<Bucket>> selectedFacetBucketMap);
    }

}