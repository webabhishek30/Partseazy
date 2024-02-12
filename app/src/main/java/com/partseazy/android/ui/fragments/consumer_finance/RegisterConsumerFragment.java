package com.partseazy.android.ui.fragments.consumer_finance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.common.SuccessResponse;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class RegisterConsumerFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.mobileET)
    protected EditText mobileET;

    @BindView(R.id.sendLinkBT)
    protected Button sendLinkBT;

    @BindView(R.id.parentLL)
    protected LinearLayout parentLL;

    public static RegisterConsumerFragment newInstance() {
        Bundle bundle = new Bundle();
        RegisterConsumerFragment fragment = new RegisterConsumerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_consumer;
    }

    public static String getTagName() {
        return RegisterConsumerFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.groups);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendLinkBT.setOnClickListener(this);

        mobileET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendLink(mobileET.getText().toString());
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == sendLinkBT.getId()) {
            sendLink(mobileET.getText().toString());
        }
    }

    private void sendLink(String mobile) {
        if (CommonUtility.isValidMobileNumber(mobile, false, 10, 10)) {
            Map<String, Object> paramMap = WebServicePostParams.SendOTPParams(mobile);
            RequestParams params = new RequestParams();
            params.headerMap = new HashMap<>();
            getNetworkManager().PutRequest(RequestIdentifier.PUT_SEND_REGISTRATION_LINK.ordinal(),
                    WebServiceConstants.SEND_REGISTRATION_LINK, paramMap, params, this, this, false);
        } else {
            mobileET.setError("Invalid Mobile Number");
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.PUT_SEND_REGISTRATION_LINK.ordinal()) {
            if (apiError != null)
                showError(apiError.message + ": " + apiError.issue, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.PUT_SEND_REGISTRATION_LINK.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), SuccessResponse.class, new OnGsonParseCompleteListener<SuccessResponse>() {
                @Override
                public void onParseComplete(SuccessResponse dealData) {
                    hideProgressDialog();
                    if (getActivity() != null && isAdded()) {
                        if (dealData != null && dealData.success == 1) {
                            try {
                                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.send_link_ack), getString(R.string.OK)
                                        , null, null, new DialogListener() {
                                            @Override
                                            public void onPositiveButton(DialogInterface dialog) {
                                                mobileET.setText("");
                                                parentLL.requestFocus();
                                                dialog.dismiss();
                                            }
                                        });
                            } catch (Exception exception) {
                                CustomLogger.e("Exception ", exception);
                            }
                        }
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("Exception ", exception);
                }


            });
        }

        return true;
    }
}
