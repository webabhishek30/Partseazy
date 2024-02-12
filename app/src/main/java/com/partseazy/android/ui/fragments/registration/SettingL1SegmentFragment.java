package com.partseazy.android.ui.fragments.registration;

import android.content.Intent;
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
import com.google.gson.Gson;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.NetworkManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.registration.RegisterMainCategoryAdapter;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.model.categorylevelone.CategoryL1;
import com.partseazy.android.ui.model.categorylevelone.CategoryMain;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.StoreModel;
import com.partseazy.android.ui.model.user.UserDetails;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 8/12/16.
 */

public class SettingL1SegmentFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.continueBT)
    protected Button continueBTN;
    @BindView(R.id.continueRL)
    protected RelativeLayout continueRL;
    private RegisterMainCategoryAdapter registerCategoryAdapter;
    private ArrayList<CategoryL1> categoryList;
    private Integer categoryId = 0;

    public static SettingL1SegmentFragment newInstance() {
        SettingL1SegmentFragment fragment = new SettingL1SegmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (categoryList != null && categoryList.size() > 0) {
            setL1CategoryAdapter();
        } else {
            loadCategoryTreeList();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_l1_cateogory;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.select_category);
    }

    public static String getTagName() {
        return SettingL1SegmentFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        toolbar.setTitle(R.string.select_category);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        continueBTN.setText(getString(R.string.save));
        continueBTN.setOnClickListener(this);
        return view;
    }


    private void setL1CategoryAdapter() {
        if (categoryList != null && categoryList.size() > 0) {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(horizontalLayoutManager);
            registerCategoryAdapter = new RegisterMainCategoryAdapter(getContext(), categoryList);
            mRecyclerView.setAdapter(registerCategoryAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);

        }

    }

    private void loadCategoryTreeList() {
        showProgressDialog(getString(R.string.loading), false);
        getNetworkManager().GetRequest(RequestIdentifier.CATEGORY_NTREE_1_REQUEST_ID.ordinal(),
                WebServiceConstants.GET_USER_L1_LIST, null, null, this, this, false);
    }

    private void postL1CategoriesCall(String catId) {
        if (catId != null) {
            showProgressDialog(getString(R.string.please_wait), false);
            Map params = WebServicePostParams.sendL1CategoryParams(catId);
            String l1StoreUrl = WebServiceConstants.POST_L1_SET_USER_CATEGORY;
            getNetworkManager().PutRequest(RequestIdentifier.POST_L1_SUBMIT_ID.ordinal(),
                    l1StoreUrl, params, null, this, this, false);

        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.please_select_product));
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.CATEGORY_NTREE_1_REQUEST_ID.ordinal()) {
            showError();
        }
        if (request.getIdentifier() == RequestIdentifier.POST_L1_SUBMIT_ID.ordinal()) {
            showError();
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        hideProgressDialog();
        CustomLogger.d("Response ::" + responseObject.toString());
        if (request.getIdentifier() == RequestIdentifier.CATEGORY_NTREE_1_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), CategoryMain.class, new OnGsonParseCompleteListener<CategoryMain>() {
                        @Override
                        public void onParseComplete(CategoryMain data) {
                            if (data.getChildren() != null && data.getChildren().size() > 0) {
                                categoryList = new ArrayList<CategoryL1>();
                                categoryList.addAll(data.getChildren());
                                setL1CategoryAdapter();
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
        if (request.getIdentifier() == RequestIdentifier.POST_L1_SUBMIT_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), UserDetails.class, new OnGsonParseCompleteListener<UserDetails>() {
                        @Override
                        public void onParseComplete(UserDetails data) {
                            hideProgressDialog();
                            if (data.l1Category != null) {
                                DataStore.setUserCategoryId(getContext(), data.l1Category);
                                showMessage("Submit Successfully", MESSAGETYPE.TOAST);
                                NetworkManager.getRequestInstance().getCache().clear();
                                Intent intent = context.getIntent();
                                context.finish();
                                context.startActivity(intent);
                            }



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

        if (eventObject.id == EventConstant.SUBMIT_L1_CATEGORY) {
            categoryId = (Integer) eventObject.objects[0];
            if (categoryId == 0) {
                continueBTN.setEnabled(false);
            } else {
                continueBTN.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == continueBTN.getId()) {
            if (categoryId == 0)
                showMessage("Please select category!", MESSAGETYPE.SNACK_BAR);
            else postL1CategoriesCall(categoryId.toString());
        }
    }
   /* @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }*/
}
