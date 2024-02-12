package com.partseazy.android.ui.fragments.customer_management;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.applozic.mobicomkit.Applozic;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.common.SuccessResponse;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shubhang on 10/02/18.
 */

public class RegisterCustomerFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.scrollView)
    protected NestedScrollView scrollView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.mobileNumberET)
    protected EditText mobileNumberET;

    @BindView(R.id.nameET)
    protected EditText nameET;

    @BindView(R.id.emailET)
    protected EditText emailET;

    @BindView(R.id.groupET)
    protected EditText groupET;

    @BindView(R.id.dobRL)
    protected RelativeLayout dobRL;

    @BindView(R.id.dobTV)
    protected TextView dobTV;

    @BindView(R.id.anniversaryRL)
    protected RelativeLayout anniversaryRL;

    @BindView(R.id.anniversaryTV)
    protected TextView anniversaryTV;

    @BindView(R.id.continueBT)
    protected Button continueBT;

    private String mobileNumber, email, name, group;

    public static RegisterCustomerFragment newInstance() {
        Bundle bundle = new Bundle();
        RegisterCustomerFragment fragment = new RegisterCustomerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_customer;
    }

    public static String getTagName() {
        return RegisterCustomerFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.register_customer);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();

        mobileNumberET.setHint(Html.fromHtml(getString(R.string.enter_mobile_number)));

        mobileNumberET.addTextChangedListener(new EditTextWatcher(mobileNumberET));
        nameET.addTextChangedListener(new EditTextWatcher(nameET));
        emailET.addTextChangedListener(new EditTextWatcher(emailET));
        groupET.addTextChangedListener(new EditTextWatcher(groupET));

        continueBT.setOnClickListener(this);
        dobRL.setOnClickListener(this);
        anniversaryRL.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    class EditTextWatcher implements TextWatcher {

        private EditText editText;

        private EditTextWatcher(EditText view) {
            this.editText = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (editText.getId()) {

                case R.id.mobileNumberET:
                    mobileNumber = s.toString();
                    break;

                case R.id.nameET:
                    name = s.toString();
                    break;

                case R.id.emailET:
                    email = s.toString();
                    break;

                case R.id.groupET:
                    group = s.toString();
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == continueBT.getId()) {
            boolean error = false;
            if (!CommonUtility.isValidMobileNumber(mobileNumber, false, 10, 10)) {
                mobileNumberET.setError("Invalid Mobile Number");
                error = true;
            }
            if (mobileNumber == null || "".equals(mobileNumber)) {
                mobileNumberET.setError("Please Enter Mobile Number");
            }
            if (email != null && !"".equals(email) && !CommonUtility.isValidEmail(email)) {
                emailET.setError("Invalid Email Id");
                error = true;
            }
            if (!error) {
                // Submit details to back end
                String dob = dobTV.getText().toString();
                if (dob.contains("Date of Birth")) {
                    dob = null;
                }
                String anni = anniversaryTV.getText().toString();
                if (anni.contains("Anniversary")) {
                    anni = null;
                }

                Map<String, Object> paramMap = WebServicePostParams.registerCustomer(mobileNumber, name, email,
                        dob, anni, group);
                getNetworkManager().PostRequest(RequestIdentifier.REGISTER_CUSTOMER.ordinal(),
                        WebServiceConstants.POST_REGISTER_CUSTOMER, paramMap, null, this, this, false);
            }
        } else if (v.getId() == dobRL.getId()) {
            showDateDialog(dobTV);
        } else if (v.getId() == anniversaryRL.getId()) {
            showDateDialog(anniversaryTV);
        }
    }

    protected void showDateDialog(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = CommonUtility.getDateFromDateDailog(dayOfMonth, monthOfYear + 1, year);
                        textView.setText(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.REGISTER_CUSTOMER.ordinal()) {
            if (apiError != null) {
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            } else {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.REGISTER_CUSTOMER.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), SuccessResponse.class, new OnGsonParseCompleteListener<SuccessResponse>() {
                        @Override
                        public void onParseComplete(SuccessResponse data) {
                            hideProgressDialog();
                            if (isAdded() && data != null && data.success == 1) {
                                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.customer_registered), getString(R.string.OK)
                                        , null, null, new DialogListener() {
                                            @Override
                                            public void onPositiveButton(DialogInterface dialog) {
                                                ((BaseActivity) getActivity()).onPopBackStack(false);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        return true;
    }

}
