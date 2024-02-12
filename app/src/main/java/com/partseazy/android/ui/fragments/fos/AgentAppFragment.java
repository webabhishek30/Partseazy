package com.partseazy.android.ui.fragments.fos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.model.agentapp.AssociateResponse;
import com.partseazy.android.ui.model.agentapp.Retailer;
import com.partseazy.android.ui.model.agentapp.RetailerResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 3/2/17.
 */

public class AgentAppFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.disassociateBTN)
    protected Button disassociateBTN;
    @BindView(R.id.associateBTN)
    protected Button associateBTN;
    @BindView(R.id.searchET)
    protected EditText searchET;
    @BindView(R.id.searchTV)
    protected TextView searchTV;
    @BindView(R.id.userDetailLL)
    protected LinearLayout userDetailLL;
    @BindView(R.id.nameTV)
    protected TextView nameTV;
    @BindView(R.id.shopNameTV)
    protected TextView shopNameTV;
    @BindView(R.id.pincodeLockTV)
    protected TextView pincodeLockTV;
    @BindView(R.id.mobileTV)
    protected TextView mobileTV;
    @BindView(R.id.searchLL)
    protected LinearLayout searchLL;

    private String mobile;
    private Retailer retailer = null;

    public static AgentAppFragment newInstance() {

        Bundle bundle = new Bundle();
        AgentAppFragment fragment = new AgentAppFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.agent_ordering_app);
    }

    public static String getTagName() {
        return AgentAppFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_agent_app;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();

        showAgentStatusView();
        searchTV.setOnClickListener(this);
        associateBTN.setOnClickListener(this);
        disassociateBTN.setOnClickListener(this);
        //handleActionDoneEditTV();
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void loadRetailerData(String mobileNumber) {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        String searchUrl = WebServiceConstants.GET_AGENT_SEARCH_CHAT + mobileNumber;
        getNetworkManager().GetRequest(RequestIdentifier.AGENT_SEARCH_RETAILER_ID.ordinal(), searchUrl, null, params, this, this, false);
    }

    private void associateAgentCall(String mobileNumber) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        String searchUrl = WebServiceConstants.ASSOCIATE_RETAILER + mobileNumber;
        getNetworkManager().PutRequest(RequestIdentifier.ASSOCIATE_RETAILER_ID.ordinal(), searchUrl, null, params, this, this, false);
    }

    private void disAssociateAgentCall() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        String searchUrl = WebServiceConstants.DISASSOCIATE_RETAILER;
        getNetworkManager().PutRequest(RequestIdentifier.DISASSOCIATE_RETAILER_ID.ordinal(), searchUrl, null, params, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        try {
            hideProgressBar();
            hideProgressDialog();
            SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
        } catch (Exception e) {
            CustomLogger.e("Exception ", e);
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.AGENT_SEARCH_RETAILER_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), RetailerResult.class, new OnGsonParseCompleteListener<RetailerResult>() {
                        @Override
                        public void onParseComplete(RetailerResult data) {
                            try {
                                hideProgressBar();
                                if (data != null) {
                                    retailer = new Retailer();
                                    retailer.name = data.name;
                                    retailer.shopName = data.shopName;
                                    retailer.pincodeLock = data.pincodeLock;
                                    retailer.mobile = data.mobile;
                                    setRetailerInfoView(retailer);
                                    associateBTN.setVisibility(View.VISIBLE);

                                }
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        if (request.getIdentifier() == RequestIdentifier.ASSOCIATE_RETAILER_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), AssociateResponse.class, new OnGsonParseCompleteListener<AssociateResponse>() {
                        @Override
                        public void onParseComplete(AssociateResponse data) {
                            try {
                                hideProgressDialog();
                                if (data.success == 1) {
                                    if (getActivity() != null && isAdded()) {
                                        DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.successfully_connected), getString(R.string.OK)
                                                , null, null, new DialogListener() {
                                                    @Override
                                                    public void onPositiveButton(DialogInterface dialog) {
                                                        AgentSingleton agentSingleton = AgentSingleton.getInstance();
                                                        if (agentSingleton != null) {
                                                            agentSingleton.setRetailerData(retailer);
                                                        }
                                                        KeyPadUtility.hideSoftKeypad(getActivity());
                                                        popToMall((BaseActivity) getActivity());
//                                                    ((BaseActivity) getActivity()).onPopBackStack(true);
                                                    }
                                                });
                                    }
                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        if (request.getIdentifier() == RequestIdentifier.DISASSOCIATE_RETAILER_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), AssociateResponse.class, new OnGsonParseCompleteListener<AssociateResponse>() {
                        @Override
                        public void onParseComplete(AssociateResponse data) {
                            try {
                                hideProgressDialog();
                                if (data.success == 1) {
                                    if (getActivity() != null && isAdded()) {
                                        DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.successfully_logout), getString(R.string.OK)
                                                , null, null, new DialogListener() {
                                                    @Override
                                                    public void onPositiveButton(DialogInterface dialog) {
                                                        AgentSingleton agentSingleton = AgentSingleton.getInstance();
                                                        if (agentSingleton != null) {
                                                            agentSingleton.setRetailerData(retailer);
                                                        }
                                                        KeyPadUtility.hideSoftKeypad(getActivity());
                                                        popToMall((BaseActivity) getActivity());
                                                        // ((BaseActivity) getActivity()).onPopBackStack(true);
                                                    }
                                                });
                                    }
                                }
                            }catch (Exception e){
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }


        return true;
    }

    private void showUserLayout() {
        userDetailLL.setVisibility(View.VISIBLE);
        associateBTN.setVisibility(View.VISIBLE);
    }

    private void showAgentStatusView() {
        AgentSingleton agentSingleton = AgentSingleton.getInstance();
        if (agentSingleton != null && agentSingleton.getRetailerData() != null) {
            setRetailerInfoView(agentSingleton.getRetailerData());
            searchLL.setVisibility(View.GONE);
            disassociateBTN.setVisibility(View.VISIBLE);
            agentSingleton.setRetailerData(null);
        } else {
            searchLL.setVisibility(View.VISIBLE);
        }

    }

    private void setRetailerInfoView(Retailer retailerResult) {
        nameTV.setText(retailerResult.name);
        shopNameTV.setText(retailerResult.shopName);
        pincodeLockTV.setText(retailerResult.pincodeLock);
        mobileTV.setText(retailerResult.mobile);
        userDetailLL.setVisibility(View.VISIBLE);
    }

    private void handleActionDoneEditTV() {
        searchET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String mobileNumber = searchET.getText().toString();
                    CustomLogger.d("mobile Number ::" + mobileNumber);
                    if (mobileNumber != null && CommonUtility.isValidMobileNumber(mobileNumber.trim(), false, 10, 10)) {
                        CustomLogger.d("Mobile is Valid");
                    } else {
                        SnackbarFactory.showSnackbar(getActivity(), getString(R.string.invalid_number));
                    }

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == disassociateBTN.getId()) {
            disAssociateAgentCall();
        }

        if (view.getId() == associateBTN.getId()) {
            associateAgentCall(mobile);

        }

        if (view.getId() == searchTV.getId()) {
            String mobileNumber = searchET.getText().toString().trim();
            KeyPadUtility.hideSoftKeypad(getActivity());
            if (CommonUtility.isValidMobileNumber(mobileNumber.trim(), false, 10, 10)) {
                mobile = mobileNumber;
                loadRetailerData(mobileNumber);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.invalid_number));
            }
        }
    }
}
