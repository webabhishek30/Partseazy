package com.partseazy.android.ui.fragments.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moe.pushlibrary.MoEHelper;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.registration.UserRoleResult;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.error.ErrorHandlerUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shubhang on 06/06/17.
 */

public class RegisterUserTypeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.retailerRL)
    protected RelativeLayout retailerRL;
    @BindView(R.id.distributorRL)
    protected RelativeLayout distributorRL;
    @BindView(R.id.wholesellerRL)
    protected RelativeLayout wholesellerRL;
    @BindView(R.id.firmRL)
    protected RelativeLayout firmRL;

    @BindView(R.id.retailerCheckBox)
    protected CheckBox retailerCheckBox;
    @BindView(R.id.distributorCheckBox)
    protected CheckBox distributorCheckBox;
    @BindView(R.id.wholesellerCheckBox)
    protected CheckBox wholesellerCheckBox;
    @BindView(R.id.firmCheckBox)
    protected CheckBox firmCheckBox;

    @BindView(R.id.continueBT)
    protected Button continueBT;

    private ArrayList<String> roles;
    protected MoEHelper helper = null;

    public static RegisterUserTypeFragment newInstance() {
        Bundle bundle = new Bundle();
        RegisterUserTypeFragment fragment = new RegisterUserTypeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roles = new ArrayList<>();

        //initializing moengage UserType user attributes
        helper = MoEHelper.getInstance(getActivity());
    }

    private void setUserAttribute(){
        // Helper method to set User uniqueId. Can be String,int,long,float,double
        helper.setUniqueId(DataStore.getUserPhoneNumber(getContext()));
        helper.setUserAttribute(MoengageConstant.User.USERTYPE,roles.toString());


    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_user_type;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.business_type);
    }

    public static String getTagName() {
        return RegisterUserTypeFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        retailerRL.setOnClickListener(this);
        distributorRL.setOnClickListener(this);
        wholesellerRL.setOnClickListener(this);
        firmRL.setOnClickListener(this);
        continueBT.setOnClickListener(this);

        return view;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.USER_ROLES.ordinal()) {
            if (ErrorHandlerUtility.getAPIErrorData(error).message != null) {
                showError(ErrorHandlerUtility.getAPIErrorData(error).message, MESSAGETYPE.SNACK_BAR);
            } else {
                showError();
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.USER_ROLES.ordinal()) {
            CustomLogger.d("User set roles api success");
            getGsonHelper().parse(responseObject.toString(), UserRoleResult.class, new OnGsonParseCompleteListener<UserRoleResult>() {

                @Override
                public void onParseComplete(UserRoleResult data) {
                    hideProgressDialog();
                    if (data.success == 1) {
                        RegisterBasicDetailsFragment fragment = RegisterBasicDetailsFragment.newInstance(roles);
                        removeTopAndAddToBackStack((BaseActivity) getActivity(), fragment, RegisterBasicDetailsFragment.getTagName());
                    } else {
                        showError();
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    hideProgressDialog();
                    showError();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }
        return true;
    }

    private void setUserRoles() {
        showProgressDialog(getString(R.string.please_wait), false);
        Map params = WebServicePostParams.userTypeParams(roles);
        getNetworkManager().PutRequest(RequestIdentifier.USER_ROLES.ordinal(),
                WebServiceConstants.USER_ROLES, params, null, this, this, false);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == retailerRL.getId()) {
            if (retailerCheckBox.isChecked()) {
                roles.remove("retailer");
                if (roles.size() == 0){
                    continueBT.setEnabled(false);
                }
                retailerCheckBox.setChecked(false);
            } else {
                roles.add("retailer");
                retailerCheckBox.setChecked(true);
                continueBT.setEnabled(true);
            }
        } else if (view.getId() == distributorRL.getId()) {
            if (distributorCheckBox.isChecked()) {
                roles.remove("distributor");
                if (roles.size() == 0){
                    continueBT.setEnabled(false);
                }
                distributorCheckBox.setChecked(false);
            } else {
                roles.add("distributor");
                distributorCheckBox.setChecked(true);
                continueBT.setEnabled(true);
            }
        } else if (view.getId() == wholesellerRL.getId()) {
            if (wholesellerCheckBox.isChecked()) {
                roles.remove("wholesaler");
                if (roles.size() == 0){
                    continueBT.setEnabled(false);
                }
                wholesellerCheckBox.setChecked(false);
            } else {
                roles.add("wholesaler");
                wholesellerCheckBox.setChecked(true);
                continueBT.setEnabled(true);
            }
        }else if (view.getId() == firmRL.getId()) {
            if (firmCheckBox.isChecked()) {
                roles.remove("firm");
                if (roles.size() == 0){
                    continueBT.setEnabled(false);
                }
                firmCheckBox.setChecked(false);
            } else {
                roles.add("firm");
                firmCheckBox.setChecked(true);
                continueBT.setEnabled(true);
            }
        } else if (view.getId() == continueBT.getId()) {
            setUserRoles();
        }

    }

    @Override
    public boolean onBackPressed() {
        CommonUtility.exitApp(getActivity());
        return false;
    }
}
