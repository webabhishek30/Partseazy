package com.partseazy.android.ui.fragments.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.registration.RegisterL2SegmentAdapter;
import com.partseazy.android.ui.model.categorylevelone.CategoryRoot;
import com.partseazy.android.ui.model.categorylevelone.Child;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.StoreModel;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by naveen on 8/12/16.
 */

public class RegisterL2SegmentFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.continueBT)
    protected Button continueBTN;
    @BindView(R.id.continueRL)
    protected RelativeLayout continueRL;
    @BindView(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;
    private RegisterL2SegmentAdapter registerCategoryAdapter;
    private ArrayList<Child> categoryList;
    private Set<Integer> selectedSubCategoryList;
    public static final String STORE_ID = "STORE_ID";
    private int storeId;
    private LinearLayoutManager lm;


    public static RegisterL2SegmentFragment newInstance(int storeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(STORE_ID, storeId);
        RegisterL2SegmentFragment fragment = new RegisterL2SegmentFragment();
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
            setL2CategoryAdapter();
        } else {
            loadCategoryTreeList();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_l2_l3;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.item_you_sell);
    }

    public static String getTagName() {
        return RegisterL2SegmentFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        toolbar.setTitle(R.string.item_you_sell);
        selectedSubCategoryList = new HashSet<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initCollapsingToolbar(view, getString(R.string.what_product_sell), getString(R.string.tell_us));
        continueBTN.setOnClickListener(this);
        return view;
    }


    private void setL2CategoryAdapter() {
        if (registerCategoryAdapter == null)
            registerCategoryAdapter = new RegisterL2SegmentAdapter(RegisterL2SegmentFragment.this, categoryList);
        lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(registerCategoryAdapter);
    }

    private void loadCategoryTreeList() {
        showProgressDialog(getString(R.string.loading), false);
        getNetworkManager().GetRequest(RequestIdentifier.CATEGORY_NTREE_1_2_REQUEST_ID.ordinal(),
                WebServiceConstants.CATEGORY_NTREE_1_2_LIST, null, null, this, this, false);
    }

    private void postL2CategoriesCall() {
        if (selectedSubCategoryList != null && selectedSubCategoryList.size() > 0) {
            showProgressDialog(getString(R.string.please_wait), false);
            String subCatList = selectedSubCategoryList.toString();
            Map params = WebServicePostParams.sendL2CategoryListParams(subCatList);
            String l2StoreUrl = WebServiceConstants.POST_L2_STORE_LIST + storeId;
            getNetworkManager().PutRequest(RequestIdentifier.POST_L2_STORE_REQUEST_ID.ordinal(),
                    l2StoreUrl, params, null, this, this, false);

        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.please_select_product));
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.CATEGORY_NTREE_1_2_REQUEST_ID.ordinal()) {
            showError();
        }
        if (request.getIdentifier() == RequestIdentifier.POST_L2_STORE_REQUEST_ID.ordinal()) {
            showError();
        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        hideProgressDialog();
        CustomLogger.d("Response ::"+responseObject.toString());
        if (request.getIdentifier() == RequestIdentifier.CATEGORY_NTREE_1_2_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), CategoryRoot.class, new OnGsonParseCompleteListener<CategoryRoot>() {
                        @Override
                        public void onParseComplete(CategoryRoot data) {
                            if (data.children != null && data.children.size() > 0) {
                                categoryList = new ArrayList<Child>();
                                categoryList.addAll(data.children);
                                setL2CategoryAdapter();
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
        if (request.getIdentifier() == RequestIdentifier.POST_L2_STORE_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), StoreModel.class, new OnGsonParseCompleteListener<StoreModel>() {
                        @Override
                        public void onParseComplete(StoreModel data) {
                            hideProgressDialog();
                            BaseFragment fragment = RegisterL3SegmentFragment.newInstance(data.id);
                            BaseFragment.removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, fragment.getTag());
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }

            );

        }
        return true;
    }

    @Override
    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.SEND_SUBCATEGORY_ID) {
            Integer subCategoryId = (Integer) eventObject.objects[0];
            if (selectedSubCategoryList != null && selectedSubCategoryList.size() > 0) {
                if (selectedSubCategoryList.contains(subCategoryId)) {
                    selectedSubCategoryList.remove(subCategoryId);
                    if (selectedSubCategoryList.size() == 0) {
                        CommonUtility.animateLayoutTopToBottom(continueRL);
                        continueRL.setVisibility(View.GONE);
//                        if(mRecyclerView.is)
//                        lm.setStackFromEnd(false);

                    }
                } else {
                    selectedSubCategoryList.add(subCategoryId);
                }
            } else {
                selectedSubCategoryList.add(subCategoryId);
                CommonUtility.animateLayoutBottomToTop(continueRL);
                continueRL.setVisibility(View.VISIBLE);
//                lm.setStackFromEnd(true);


            }
            CustomLogger.d("SSubCategory selected  list size ::  " + selectedSubCategoryList.size());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == continueBTN.getId()) {
            if (continueRL.getVisibility() == View.VISIBLE)
                postL2CategoriesCall();
        }
    }

    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }
}
