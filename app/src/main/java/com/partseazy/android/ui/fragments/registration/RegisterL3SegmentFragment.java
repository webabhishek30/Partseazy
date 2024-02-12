package com.partseazy.android.ui.fragments.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.registration.RegisterL3SegmentAdapter;
import com.partseazy.android.ui.adapters.registration.SubCategoryPriceSelectionAdapter;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.categoryleveltwo.Category;
import com.partseazy.android.ui.model.categoryleveltwo.L3CategoryList;
import com.partseazy.android.ui.model.categoryleveltwo.SubCategory;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 9/12/16.
 */

public class RegisterL3SegmentFragment extends BaseFragment implements View.OnClickListener, SubCategoryPriceSelectionAdapter.OnUnSelectAllSubCatListener {

    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;
    @BindView(R.id.continueRL)
    protected RelativeLayout continueRL;
    @BindView(R.id.continueBT)
    protected Button continueBTN;
    private RegisterL3SegmentAdapter categoryPriceSelectionAdapter;
    private Set<SubCategory> selectedSubCategoryList;
    private List<Category> categoryList;
    public static final String STORE_ID = "STORE_ID";
    private int storeId;
    private LinearLayoutManager lm;


    public static RegisterL3SegmentFragment newInstance(int storeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(STORE_ID, storeId);
        RegisterL3SegmentFragment fragment = new RegisterL3SegmentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getArguments().getInt(STORE_ID);

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (categoryList != null && categoryList.size() > 0) {
            setL3CategoryAdapter();
        } else {
            loadCategoryList();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_l2_l3;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.sku_you_sell);
    }

    public static String getTagName() {
        return RegisterL3SegmentFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        selectedSubCategoryList = new HashSet<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initCollapsingToolbar(view, getString(R.string.what_product_sell), getString(R.string.thanks));
        continueBTN.setOnClickListener(this);
        return view;
    }

    private void setL3CategoryAdapter() {
        if (categoryPriceSelectionAdapter == null)
            categoryPriceSelectionAdapter = new RegisterL3SegmentAdapter(this, categoryList);
        lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(categoryPriceSelectionAdapter);
    }


    private void loadCategoryList() {
        showProgressDialog(getString(R.string.loading), false);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        String storeUrl = WebServiceConstants.CATEGORY_STORE_L3 + "" + storeId+"?tag=commerce";
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.CATEGORY_STORE_L3_LIST_REQUEST_ID.ordinal(),
                storeUrl, null, params, this, this, false);
    }

    private void postL3CategoriesCall() {
        if (selectedSubCategoryList != null && selectedSubCategoryList.size() > 0) {
            showProgressDialog(getString(R.string.please_wait), false);
            JSONArray stringJson = getSelectedCategoryJsonString(selectedSubCategoryList);
            Map params = WebServicePostParams.sendL3CategoryListParams(stringJson);
            String l2StoreUrl = WebServiceConstants.POST_L2_STORE_LIST + storeId;
            getNetworkManager().PutRequest(RequestIdentifier.POST_L3_STORE_REQUEST_ID.ordinal(),
                    l2StoreUrl, params, null, this, this, false);

        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.please_select_product));
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.CATEGORY_STORE_L3_LIST_REQUEST_ID.ordinal()) {
            showError();
        }
        if (request.getIdentifier() == RequestIdentifier.POST_L3_STORE_REQUEST_ID.ordinal()) {
            showError();
        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.CATEGORY_STORE_L3_LIST_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            getGsonHelper().parse(responseObject.toString(), L3CategoryList.class, new OnGsonParseCompleteListener<L3CategoryList>() {
                        @Override
                        public void onParseComplete(L3CategoryList data) {
                            try {
                                if (data.result != null && data.result.size() > 0) {
                                    categoryList = new ArrayList<>();
                                    categoryList.addAll(data.result);
                                    setL3CategoryAdapter();
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            showError();
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }
        if (request.getIdentifier() == RequestIdentifier.POST_L3_STORE_REQUEST_ID.ordinal()) {
            hideProgressDialog();

            BaseFragment fragment;

            if (DataStore.getProductId(context) != null && !DataStore.getProductId(context).equals("")) {
                fragment = ProductDetailFragment.newInstance(Integer.parseInt(DataStore.getProductId(context)), "");
                DataStore.setProductId(context, null);
            } else {
                fragment = HomeFragment.newInstance();
            }

            BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, fragment.getTag());
        }
        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.SEND_SUBCATEGORY_RANGE_ITEM) {
            SubCategory storeSubCatModel = (SubCategory) eventObject.objects[0];
            Boolean isSelected = (Boolean) eventObject.objects[1];
            updateSingleSubCategory(storeSubCatModel, isSelected);
            CustomLogger.d("SubCategory selected  list size ::  " + selectedSubCategoryList.size());
        }
        if (eventObject.id == EventConstant.SEND_SELECT_ALL_SUBCAT_LIST) {
            List<SubCategory> l3CategoryList = (List<SubCategory>) eventObject.objects[0];
            Boolean isSelected = (Boolean) eventObject.objects[1];
            updateSelectAllSubCategory(l3CategoryList, isSelected);
            CustomLogger.d("l3CategoryList selected  list size ::  " + selectedSubCategoryList.size());
        }
    }

    void updateSelectAllSubCategory(List<SubCategory> subCategoryList, Boolean isSelected) {

        if (subCategoryList != null && subCategoryList.size() > 0) {
            for (int i = 0; i < subCategoryList.size(); i++) {
                SubCategory storeSubCatModel = subCategoryList.get(i);
                if (selectedSubCategoryList != null && selectedSubCategoryList.size() > 0) {
                    if (selectedSubCategoryList.contains(storeSubCatModel)) {
                        if (!isSelected) {
                            selectedSubCategoryList.remove(storeSubCatModel);
                            if (selectedSubCategoryList.size() == 0) {
                                CommonUtility.animateLayoutTopToBottom(continueRL);
                                continueRL.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        selectedSubCategoryList.add(storeSubCatModel);
                    }
                } else {
                    selectedSubCategoryList.add(storeSubCatModel);
                    CommonUtility.animateLayoutBottomToTop(continueRL);
                    continueRL.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    void updateSingleSubCategory(SubCategory storeSubCatModel, Boolean isSelected) {
        if (selectedSubCategoryList != null && selectedSubCategoryList.size() > 0) {
            if (selectedSubCategoryList.contains(storeSubCatModel)) {
                if (!isSelected) {
                    selectedSubCategoryList.remove(storeSubCatModel);
                    if (selectedSubCategoryList.size() == 0) {
                        CommonUtility.animateLayoutTopToBottom(continueRL);
                        continueRL.setVisibility(View.GONE);
                    }
                }
            } else {
                selectedSubCategoryList.add(storeSubCatModel);
            }
        } else {
            selectedSubCategoryList.add(storeSubCatModel);
            CommonUtility.animateLayoutBottomToTop(continueRL);
            continueRL.setVisibility(View.VISIBLE);
//                lm.setStackFromEnd(true);

        }

    }

    public JSONArray getSelectedCategoryJsonString(Set<SubCategory> selectedSubCategoryList) {
        ArrayList<HashMap<String, Object>> dataMap = new ArrayList<>();
        for (SubCategory selectCategory : selectedSubCategoryList) {
            HashMap<String, Object> categoryMap = new HashMap<String, Object>();
            ArrayList<String> tagsList = new ArrayList<String>();
            if (selectCategory.isLowSelected) {
                tagsList.add(AppConstants.LOW_TAG);
            }
            if (selectCategory.isMediumSelected) {
                tagsList.add(AppConstants.MEDIUM_TAG);
            }
            if (selectCategory.isHighSelected) {
                tagsList.add(AppConstants.HIGH_TAG);
            }
            if (selectCategory.isLuxurySelected) {
                tagsList.add(AppConstants.LUXURY_TAG);
            }
            if (tagsList.size() > 0) {
                categoryMap.put(AppConstants.TAG, tagsList);
            }
            categoryMap.put(AppConstants.CATEGORY_ID, selectCategory.id);
            dataMap.add(categoryMap);
        }
        List<JSONObject> jsonObj = new ArrayList<JSONObject>();
        for (HashMap<String, Object> data : dataMap) {
            JSONObject obj = new JSONObject(data);
            jsonObj.add(obj);
        }
        JSONArray jsonArray = new JSONArray(jsonObj);
        return jsonArray;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == continueBTN.getId()) {
            postL3CategoriesCall();
        }
    }

    @Override
    public void onUnselectAllSubCat(int l2CatId, int position) {
        CustomLogger.d("hello" + position);
        if (categoryList != null && categoryList.size() > 0) {
            categoryList.get(position).isSelectedAll = false;
            categoryPriceSelectionAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }
}
