package com.partseazy.android.ui.fragments.supplier.search;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.adapters.suppliers.search.CheckboxAdapter;
import com.partseazy.android.ui.adapters.suppliers.search.HorizontalSearchShopAdapter;
import com.partseazy.android.ui.adapters.suppliers.search.OnPlaceClickListener;
import com.partseazy.android.ui.fragments.supplier.shop.ShopHomeFragment;
import com.partseazy.android.ui.fragments.supplier.shop.ShopsDetailBaseFragment;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.supplier.geolocation.GeoLocationResult;
import com.partseazy.android.ui.model.supplier.geolocation.GeoPlaceData;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;
import com.partseazy.android.ui.model.supplier.search.FinalSearchData;
import com.partseazy.android.ui.model.supplier.search.TagModel;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.partseazy.android.ui.widget.chipView.Chip;
import com.partseazy.android.ui.widget.chipView.ChipView;
import com.partseazy.android.ui.widget.chipView.ChipViewAdapter;
import com.partseazy.android.ui.widget.chipView.MainChipViewAdapter;
import com.partseazy.android.ui.widget.chipView.OnChipClickListener;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 6/9/17.
 */

public class ShopsSearchFragment extends ShopsDetailBaseFragment implements View.OnClickListener, OnChipClickListener, OnPlaceClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.shopSizeRecyclerView)
    protected RecyclerView shopSizeRecyclerView;

    @BindView(R.id.footfallRecyclerView)
    protected RecyclerView footfallRecyclerView;

    @BindView(R.id.turnOverRecyclerView)
    protected RecyclerView turnOverRecyclerView;

    @BindView(R.id.creditRV)
    protected RecyclerView creditRV;

    @BindView(R.id.rentRV)
    protected RecyclerView rentRV;

    @BindView(R.id.chipView)
    protected ChipView categoryChipView;

    @BindView(R.id.locationChipView)
    protected ChipView locationChipView;

    @BindView(R.id.catBTN)
    protected TextView catBTN;

    @BindView(R.id.addLocationBTN)
    protected TextView addLocationBTN;

    @BindView(R.id.applyBT)
    protected Button applyBT;

    @BindView(R.id.categoryLL)
    protected LinearLayout categoryLL;

    private HorizontalSearchShopAdapter footfallAdapter;
    private HorizontalSearchShopAdapter shopSizeAdapter;
    private HorizontalSearchShopAdapter turnOverAdapter;
    private HorizontalSearchShopAdapter creditAdapter;
    private HorizontalSearchShopAdapter rentAdapter;

    private List<CheckboxModel> categoryList;
    private List<Chip> categoryTagList;
    private List<Chip> locationTagList;
    private Map<String, GeoPlaceData> geoPlaceDataMap;

    public enum CHECKBOXTYPE {
        FOOTFALL, SHOP_SIZE, TURNOVER, CREDIT, RENT

    }

    public static ShopsSearchFragment newInstance() {
        Bundle bundle = new Bundle();
        ShopsSearchFragment fragment = new ShopsSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackNavigation();
        categoryTagList = new ArrayList<>();
        locationTagList = new ArrayList<>();
        geoPlaceDataMap = new HashMap<>();
        categoryList = new ArrayList<>();
        categoryList.addAll(getCategoryList());

    }


    @Override
    public FinalSearchData getFinalSearchData() {
        //Everytime new Final  attribute on starting
        return new FinalSearchData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shop_search;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.search_for_shop);
    }

    public static String getTagName() {
        return ShopsSearchFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                setFootfallAdapter();
                if (StaticMap.shopSizeMap != null) {
                    setShopSizeAdapter();
                }
                if (StaticMap.turnOverMap != null) {
                    setTurnOverAdapter();
                }
                setCategoryChipView();
                setLocationChipView();
                setCreditAdapter();
                setRentAdapter();
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();

        catBTN.setOnClickListener(this);
        applyBT.setOnClickListener(this);
        addLocationBTN.setOnClickListener(this);
        categoryLL.setVisibility(View.VISIBLE);
        return view;
    }

    private void setFootfallAdapter() {
        List<CheckboxModel> footfallList = new ArrayList<>();
        for (Map.Entry<String, String> footfallObj : StaticMap.footfallMap.entrySet()) {
            footfallList.add(new CheckboxModel(footfallObj.getKey(), footfallObj.getValue()));
        }

        if (footfallAdapter == null)
            footfallAdapter = new HorizontalSearchShopAdapter(context, footfallList, CHECKBOXTYPE.FOOTFALL, true);
        footfallRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        footfallRecyclerView.setAdapter(footfallAdapter);

    }

    private void setShopSizeAdapter() {
        List<CheckboxModel> shopSizeList = new ArrayList<>();
        for (Map.Entry<String, String> footfallObj : StaticMap.shopSizeMap.entrySet()) {
            shopSizeList.add(new CheckboxModel(footfallObj.getKey(), footfallObj.getValue()));
        }
        if (shopSizeAdapter == null)
            shopSizeAdapter = new HorizontalSearchShopAdapter(context, shopSizeList, CHECKBOXTYPE.SHOP_SIZE, true);
        shopSizeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        shopSizeRecyclerView.setAdapter(shopSizeAdapter);
    }

    private void setTurnOverAdapter() {
        List<CheckboxModel> turnOverList = new ArrayList<>();
        for (Map.Entry<String, String> footfallObj : StaticMap.turnOverMap.entrySet()) {
            turnOverList.add(new CheckboxModel(footfallObj.getKey(), footfallObj.getValue()));
        }

        if (turnOverAdapter == null)
            turnOverAdapter = new HorizontalSearchShopAdapter(context, turnOverList, CHECKBOXTYPE.TURNOVER, true);
        turnOverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        turnOverRecyclerView.setAdapter(turnOverAdapter);
    }

    private void setCreditAdapter() {
        List<CheckboxModel> creditList = new ArrayList<>();
        creditList.add(new CheckboxModel("Yes", "Yes"));
        creditList.add(new CheckboxModel("No", "No"));

        if (creditAdapter == null)
            creditAdapter = new HorizontalSearchShopAdapter(context, creditList, CHECKBOXTYPE.CREDIT, false);
        creditRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        creditRV.setAdapter(creditAdapter);
    }

    private void setRentAdapter() {
        List<CheckboxModel> rentList = new ArrayList<>();
        rentList.add(new CheckboxModel("Yes", "Yes"));
        rentList.add(new CheckboxModel("No", "No"));

        if (rentAdapter == null)
            rentAdapter = new HorizontalSearchShopAdapter(context, rentList, CHECKBOXTYPE.RENT, false);
        rentRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rentRV.setAdapter(rentAdapter);
    }

    private void showCategoryDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dailog_search_category_list, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView headingTV = (TextView) view.findViewById(R.id.headingTV);
        TextView submitBTN = (TextView) view.findViewById(R.id.submitBTN);

        final Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();


        headingTV.setText(getString(R.string.select_category));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CheckboxAdapter checkboxAdapter = new CheckboxAdapter(context, categoryList);
        recyclerView.setAdapter(checkboxAdapter);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    private List<CheckboxModel> getCategoryList() {
        List<CheckboxModel> checkBoxModelList = new ArrayList<>();
        checkBoxModelList.add(new CheckboxModel("Featured Phone", "Featured Phone"));
        checkBoxModelList.add(new CheckboxModel("Cases & Covers", "Cases & Covers"));
        checkBoxModelList.add(new CheckboxModel("Power Banks", "Power Banks"));
        checkBoxModelList.add(new CheckboxModel("Mobile Cables", "Mobile Cables"));
        checkBoxModelList.add(new CheckboxModel("Mobile Batteries", "Mobile Batteries"));
        checkBoxModelList.add(new CheckboxModel("Mobile Chargers", "Mobile Chargers"));
        checkBoxModelList.add(new CheckboxModel("Headphones & Headsets", "Headphones & Headsets"));
        checkBoxModelList.add(new CheckboxModel("Memory Card & Card Readers", "Memory Card & Card Readers"));
        checkBoxModelList.add(new CheckboxModel("Selfie Sticks", "Selfie Sticks"));
        checkBoxModelList.add(new CheckboxModel("Screen Guards", "Screen Guards"));
        checkBoxModelList.add(new CheckboxModel("Speakers", "Speakers"));
        return checkBoxModelList;
    }


    public void setCategoryChipView() {

        if (isAdded() && getActivity() != null) {
            ChipViewAdapter adapterLayout = new MainChipViewAdapter(getContext());
            categoryChipView.setAdapter(adapterLayout);
            categoryChipView.setChipLayoutRes(R.layout.chip_close);
            categoryChipView.setChipBackgroundColor(getResources().getColor(R.color.light_pink));
            categoryChipView.setChipBackgroundColorSelected(getResources().getColor(R.color.light_pink));
            categoryChipView.setChipList(categoryTagList);
            categoryChipView.setOnChipClickListener(this);
        }
    }

    public void setLocationChipView() {

        if (isAdded() && getActivity() != null) {
            ChipViewAdapter adapterLayout = new MainChipViewAdapter(getContext());
            locationChipView.setAdapter(adapterLayout);
            locationChipView.setChipLayoutRes(R.layout.chip_close);
            locationChipView.setChipBackgroundColor(getResources().getColor(R.color.light_pink));
            locationChipView.setChipBackgroundColorSelected(getResources().getColor(R.color.light_pink));
            locationChipView.setChipList(locationTagList);
            locationChipView.setOnChipClickListener(this);
        }
    }

    private void updateCategoryList(CheckboxModel checkBoxModel) {
        if (categoryList != null && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).key.equals(checkBoxModel.key)) {
                    CheckboxModel model = categoryList.remove(i);
                    if (model.isSelected) {
                        model.isSelected = false;
                    } else {
                        model.isSelected = true;
                    }
                    categoryList.add(i, model);
                    break;
                }
            }
        }
    }

    private void unSelectCategory(String modelId) {
        if (categoryList != null && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).key.equals(modelId)) {
                    CheckboxModel model = categoryList.remove(i);
                    model.isSelected = false;
                    categoryList.add(i, model);
                    break;
                }
            }
        }
    }

    private void updateCategoryTagList(TagModel tagModel) {
        boolean isAbsent = true;
        if (categoryTagList != null && categoryTagList.size() > 0) {
            for (int i = 0; i < categoryTagList.size(); i++) {
                TagModel catTag = (TagModel) categoryTagList.get(i);
                if (tagModel.tagKey.equals(catTag.tagKey)) {
                    categoryTagList.remove(i);
                    isAbsent = false;
                    break;
                }
            }
        }
        if (isAbsent) {
            categoryTagList.add(tagModel);
        }
    }

    private void updateLocationTagList(TagModel tagModel) {
        boolean isPresent = false;
        if (locationTagList != null && locationTagList.size() > 0) {
            for (int i = 0; i < locationTagList.size(); i++) {
                TagModel catTag = (TagModel) locationTagList.get(i);
                if (tagModel.tagKey.equals(catTag.tagKey)) {
                    isPresent = true;
                    break;
                }
            }
        }
        if (!isPresent) {
            locationTagList.add(tagModel);
        }
    }


    private void loadGeoLocationDetail(String addressName) {
        String callingUrl;
        try {
            callingUrl = WebServiceConstants.GEO_LOCATION + URLEncoder.encode(addressName, "utf8");
            getNetworkManager().GetRequest(RequestIdentifier.GEO_CODE_LOCATION_ID.ordinal(),
                    callingUrl, null, null, this, this, false);
        } catch (UnsupportedEncodingException e) {
            CustomLogger.e("Exception ", e);
        }
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        if (request.getIdentifier() == RequestIdentifier.GEO_CODE_LOCATION_ID.ordinal()) {
            showError();
        }


        return true;
    }


    @Override
    protected boolean handleBrowseShopsDataError(Request request, APIError error) {
        if (error != null) {
            showError(error.issue, MESSAGETYPE.SNACK_BAR);
        } else {
            showError();
        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.GEO_CODE_LOCATION_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), GeoLocationResult.class, new OnGsonParseCompleteListener<GeoLocationResult>() {
                        @Override
                        public void onParseComplete(GeoLocationResult data) {

                            if (data.results != null && data.results.size() > 0) {
                                if (!geoPlaceDataMap.containsKey(data.results.get(0).placeId)) {
                                    geoPlaceDataMap.put(data.results.get(0).placeId, data.results.get(0));
                                }
                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }


        return super.handleResponse(request, responseObject, response);
    }

    @Override
    protected boolean handleBrowseShopsDataResponse(ShopResultData data) {
        hideProgressBar();
        hideProgressDialog();
        launchRetailerDetailPager(data, 0);
        return true;
    }

    private Set<String> getCheckSet(Set<String> set, String item) {
        if (set != null) {
            if (set.contains(item)) {
                set.remove(item);
            } else {
                set.add(item);
            }
        }
        return set;
    }


    private Set<String> removeCheckSet(Set<String> set, String item) {
        if (set != null) {
            if (set.contains(item)) {
                set.remove(item);
            }
        }
        return set;
    }

    private void removeFromLocationMap(String placeId) {
        if (geoPlaceDataMap != null) {
            if (geoPlaceDataMap.containsKey(placeId)) {
                geoPlaceDataMap.remove(placeId);
            }
        }
    }


    private void launchRetailerDetailPager(ShopResultData shopResultData, int tabPosition) {
        String retailerDataJson = new Gson().toJson(shopResultData);
        ShopHomeFragment fragment = ShopHomeFragment.newInstance(retailerDataJson, tabPosition);
        BaseFragment.addToBackStack((BaseActivity) context, fragment, fragment.getTag());

    }

    @Override
    public void onChipClick(Chip chip) {
        CustomLogger.d("Chip :: " + chip.getKey());
        CustomLogger.d("placeId  step3  ::" + chip.getKey());
        unSelectCategory(chip.getKey());
        finalSearchData.categoryList = removeCheckSet(finalSearchData.categoryList, chip.getValue());
        removeFromLocationMap(chip.getKey());
        categoryChipView.remove(chip);
        locationChipView.remove(chip);
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);


        if (eventObject.id == EventConstant.SELECT_SEARCH_CATEGORY) {
            CheckboxModel categoryCheckBoxModel = (CheckboxModel) eventObject.objects[0];
            finalSearchData.categoryList = getCheckSet(finalSearchData.categoryList, categoryCheckBoxModel.value);
            updateCategoryList(categoryCheckBoxModel);
            TagModel tagModel = new TagModel(categoryCheckBoxModel.key, categoryCheckBoxModel.value);
            updateCategoryTagList(tagModel);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    categoryChipView.setChipList(categoryTagList);
                }
            });
        }

        if (eventObject.id == EventConstant.SELECT_SEARCH_TURNOVER) {
            CheckboxModel turnOverCB = (CheckboxModel) eventObject.objects[0];
            CustomLogger.d("selected TurnOver ::" + turnOverCB);
            finalSearchData.turnOverList = getCheckSet(finalSearchData.turnOverList, turnOverCB.key);
        }

        if (eventObject.id == EventConstant.SELECT_SEARCH_SIZE) {
            CheckboxModel shopSizeCB = (CheckboxModel) eventObject.objects[0];
            CustomLogger.d("selected shopSizeCB ::" + shopSizeCB);
            finalSearchData.shopSizeList = getCheckSet(finalSearchData.shopSizeList, shopSizeCB.key);
        }

        if (eventObject.id == EventConstant.SELECT_SEARCH_FOOTFALL) {
            CheckboxModel footfallCB = (CheckboxModel) eventObject.objects[0];
            CustomLogger.d("selected footfallCB ::" + footfallCB);
            finalSearchData.footfallList = getCheckSet(finalSearchData.footfallList, footfallCB.key);
        }
    }

    @Override
    public void onSelectPlaceClick(Prediction prediction) {
        TagModel tagModel = new TagModel(prediction.placeId, prediction.structuredFormatting.mainText);
        loadGeoLocationDetail(prediction.structuredFormatting.mainText);
        updateLocationTagList(tagModel);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                locationChipView.setChipList(locationTagList);
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == catBTN.getId()) {
            showCategoryDialog();
        }

        if (view.getId() == applyBT.getId()) {
            setGeoLocation(geoPlaceDataMap);
            finalSearchData.locationName = new Gson().toJson(locationTagList);
            finalSearchData.page = 1;
            callShopListData();

        }

        if (view.getId() == addLocationBTN.getId()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    AutoCompletePlaceDailog autoCompletePlaceDailog = AutoCompletePlaceDailog.newInstance(ShopsSearchFragment.this);
                    autoCompletePlaceDailog.show(getActivity().getFragmentManager(), "dialog");

                }
            });
        }

    }
}
