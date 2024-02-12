package com.partseazy.android.ui.fragments.deals.create_deal;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.model.deal.FileData;
import com.partseazy.android.ui.model.deal.SkuData;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.CityDetail;
import com.partseazy.android.utility.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naveen on 5/5/17.
 */

public abstract class NewDealBaseFragment extends BaseFragment {

    protected static DealData dealData;


    public DealData getdealData() {

        if (dealData == null)
            dealData = new DealData();

        return dealData;

    }


    public void setFinalDealData(DealData dealData) {
        this.dealData = dealData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealData = getdealData();
    }


    void callPincodeAPI(String pincode) {
        showProgressDialog();
        String completeUrl = WebServiceConstants.GET_CITY + pincode;
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_CITY_ID.ordinal(),
                completeUrl, null, null, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        handlePincodeError(request, apiError);
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_CITY_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), CityDetail.class, new OnGsonParseCompleteListener<CityDetail>() {
                @Override
                public void onParseComplete(CityDetail getCity) {

                    handlePincodeResponse(getCity);

                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("Exception ", exception);
                    handlePincodeError(request, er);
                }


            });
        }

        return true;
    }


    protected boolean handlePincodeResponse(CityDetail data) {
        return false;
    }

    protected boolean handlePincodeError(Request request, APIError error) {
        return false;
    }


    class EditTextWatcher implements TextWatcher {

        private EditText editText;
        private TextInputLayout textInputLayout;

        public EditTextWatcher(EditText view, TextInputLayout textInputLayout) {
            this.editText = view;
            this.textInputLayout = textInputLayout;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textInputLayout.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (editText.getId()) {

                case R.id.productNameET:
                    CustomLogger.d("Hello productDescET");
                    break;

                case R.id.pincodeET:
                    if (editable.length() == 6) {
                        String pincode = editable.toString();
                        dealData.pincode = pincode;
                        callPincodeAPI(pincode);
                    }
                    break;
            }

        }
    }


    protected void showTimePickerDailog(final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                try {
                    String time = CommonUtility.getTimeFromTimeDailog(selectedMinute, selectedHour);
                    textView.setText(time);
                    setDealTime(textView, CommonUtility.getTimeHHmmss(selectedHour, selectedMinute));
                } catch (Exception e) {
                    CustomLogger.e(e + "");
                }

            }
        }, hour, minute, DateFormat.is24HourFormat(getActivity()));//Yes 24 hour time
        mTimePicker.show();
    }


    protected void setDealDate(TextView textView, String date) {
        switch (textView.getId()) {
            case R.id.startDateTV:
                dealData.startDate = date;
                break;

            case R.id.endDateTV:
                dealData.endDate = date;
                break;
        }
    }

    protected void setDealTime(TextView textView, String time) {
        switch (textView.getId()) {
            case R.id.startTimeTV:
                dealData.startTime = time;
                break;

            case R.id.endTimeTV:
                dealData.endTime = time;
                break;
        }
    }

    public static class DealData {

        public String productName;
        public String productDescription;
        public String termsCondition;
        public String l2Id;
        public String l3Id;
        public String startDate;
        public String endDate;
        public String startTime;
        public String endTime;
        public String endDateTime;

        public int isPublic;
        public int escrow;
        public int allowDemo;

        public List<SkuData> skuDataList;
        public List<FileData> fileDataList;
        public Set<String> shipMethodList;
        public Set<String> paymentMethodList;

        public int dispatchDays;
        public String pincode;
        public String address;
        public int shippingCharge;


        public DealData() {
            skuDataList = new ArrayList<>();
            fileDataList = new ArrayList<>();
            shipMethodList = new HashSet<>();
            paymentMethodList = new HashSet<>();

        }
    }

}
