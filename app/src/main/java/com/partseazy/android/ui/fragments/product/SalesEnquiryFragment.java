package com.partseazy.android.ui.fragments.product;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.android.utility.error.ErrorHandlerUtility;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by naveen on 6/1/17.
 */

public class SalesEnquiryFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.spinner)
    protected Spinner spinner;
    @BindView(R.id.productIconIV)
    protected NetworkImageView productIconIV;
    @BindView(R.id.productNameTV)
    protected TextView productNameTV;
    @BindView(R.id.productPriceTV)
    protected TextView productPriceTV;
    @BindView(R.id.quantityET)
    protected EditText quantityET;
    @BindView(R.id.commentET)
    protected EditText commentET;
    @BindView(R.id.quantityTIL)
    protected TextInputLayout quantityTIL;
    @BindView(R.id.checkbox)
    protected CheckBox checkbox;
    @BindView(R.id.continueBT)
    protected Button continueBT;

    private ArrayAdapter<String> dataAdapter;
    private List<String> timeCategoryList;

    public static final String ONE_WEEK = "Every Week";
    public static final String ONE_MONTH = "Every Month";
    public static final String THREE_MONTH = "Every Quarter";
    public static final String ONE_TIME = "One Time";

    public static final String SELECTED_SUPPLIER_ID = "supplier_id";
    public static final String SELECTED_PRODUCT_ID = "prod_id";
    public static final String SELECTED_PRODUCT_NAME = "prod_name";
    public static final String SELECTED_PRODUCT_IMAGE = "prod_image";
    public static final String SELECTED_PRODUCT_PRICE = "prod_price";

    private String durationValue = null;
    private String quantityValue;
    private String commentValue;
    private boolean isChecked = true;

    private String errorMessage;

    private int supplierId;
    private int productId;
    private String productPrice;
    private String productImage;
    private String productName;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getInt(SELECTED_PRODUCT_ID, productId);
        productPrice = getArguments().getString(SELECTED_PRODUCT_PRICE, "");
        productName = getArguments().getString(SELECTED_PRODUCT_NAME, "");
        productImage = getArguments().getString(SELECTED_PRODUCT_IMAGE, "");
        supplierId = getArguments().getInt(SELECTED_SUPPLIER_ID, supplierId);
        imageLoader = ImageManager.getInstance(getContext()).getImageLoader();

    }


    public static SalesEnquiryFragment newInstance(int supplierId, int productId, String productName, String productPrice, String productImage) {
        Bundle bundle = new Bundle();
        bundle.putInt(SalesEnquiryFragment.SELECTED_PRODUCT_ID, productId);
        bundle.putInt(SalesEnquiryFragment.SELECTED_SUPPLIER_ID, supplierId);
        bundle.putString(SalesEnquiryFragment.SELECTED_PRODUCT_NAME, productName);
        bundle.putString(SalesEnquiryFragment.SELECTED_PRODUCT_PRICE, productPrice);
        bundle.putString(SalesEnquiryFragment.SELECTED_PRODUCT_IMAGE, productImage);
        SalesEnquiryFragment fragment = new SalesEnquiryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String getTagName() {
        return SalesEnquiryFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sale_enquiry;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.sales_enquiry);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        hideKeyBoard(getView());
        timeCategoryList = getTimelineList();
        if (timeCategoryList != null && timeCategoryList.size() > 0) {
            dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timeCategoryList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }

        spinner.setOnItemSelectedListener(this);
        commentET.addTextChangedListener(new MyTextWatcher(commentET));
        quantityET.addTextChangedListener(new MyTextWatcher(quantityET));
        init();
        handleClick();
        return view;

    }

    public void init() {
        productNameTV.setText(productName);
        if (productPrice != null && !productPrice.equals(0 + "")) {
            productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
            productPriceTV.setVisibility(View.VISIBLE);
        } else {
            productPriceTV.setVisibility(View.GONE);
        }

        if (productImage != null && !productImage.equals("")) {
            CustomLogger.d("product Image ::" + productImage);


//            String formatedURL = CommonUtility.getFormattedImageUrl(productImage);
//            productIconIV.setImageUrl(formatedURL, imageLoader);

            String formatedURL = CommonUtility.getFormattedImageUrl(productImage, productIconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, productIconIV, formatedURL);

        }

    }

    public void handleClick() {

        checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CustomLogger.d("is checked selected ");
                    isChecked = true;

                } else {
                    CustomLogger.d("is checked unselected ");
                    isChecked = false;

                }
            }
        });


        continueBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityValue = quantityET.getText().toString().trim();
                commentValue = commentET.getText().toString().trim();
                if (isChecked) {
                    if (checkValidData(quantityValue, commentValue, durationValue)) {
                        postProductEnquiry();
                    } else {
                        SnackbarFactory.showSnackbar(getActivity(), errorMessage);
                    }
                } else {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_agree_to_share_info));
                }

            }
        });
    }

    public boolean checkValidData(String quantity, String comment, String purchaseFrequency) {
        if (!validateQuantity(quantity)) {
            return false;
        } else if (!validateComment(comment)) {
            return false;
        } else if (!validatePurchaseFrequency(purchaseFrequency)) {
            return false;
        }
        return true;
    }

    private boolean validateQuantity(String quantity) {
        if (quantity.isEmpty()) {
            quantityET.setError(getString(R.string.err_quantity_msg));
            errorMessage = getString(R.string.err_quantity_msg);
            requestFocus(quantityET);
            return false;
        } else {
            quantityTIL.setError(null);
            quantityTIL.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateComment(String comment) {
        if (comment.isEmpty()) {
            commentET.setError(getString(R.string.err_enquiry_msg));
            errorMessage = getString(R.string.err_enquiry_msg);
            requestFocus(commentET);
            return false;
        }
        return true;
    }

    private boolean validatePurchaseFrequency(String purchaseFrequency) {
        if (purchaseFrequency != null && !purchaseFrequency.equals("")) {
            return true;
        } else {
            errorMessage = getString(R.string.select_purchase_frequency);
            return false;
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String timeValue = adapterView.getItemAtPosition(position).toString();
        if(position==0)
        {
            durationValue = null;
        }
        if (timeValue.equals(ONE_WEEK)) {
            durationValue = "1w";
        }
        if (timeValue.equals(ONE_MONTH)) {
            durationValue = "1m";
        }
        if (timeValue.equals(THREE_MONTH)) {
            durationValue = "3m";
        }
        if (timeValue.equals(ONE_TIME)) {
            durationValue = "once";
        }
        CustomLogger.d("durationValue " + durationValue);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void postProductEnquiry() {
        showProgressDialog(getString(R.string.send_enquiry), false);
        Map params = WebServicePostParams.sendEnquiryParams(supplierId, commentValue, durationValue, productId, quantityValue);
        getNetworkManager().PostRequest(RequestIdentifier.PRODUCT_ENQUIRY_REQUEST_ID.ordinal(),
                WebServiceConstants.INQUIRY_PRODUCT, params, null, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.PRODUCT_ENQUIRY_REQUEST_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), ErrorHandlerUtility.getAPIErrorData(error).issue);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }

        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.PRODUCT_ENQUIRY_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            if (getActivity() != null) {
                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.thank_you_sales_enquiry), getString(R.string.OK)
                        , null, null, new DialogListener() {
                            @Override
                            public void onPositiveButton(DialogInterface dialog) {

                                KeyPadUtility.hideSoftKeypad(getActivity());
                                ((BaseActivity) getActivity()).onPopBackStack(true);
                            }
                        });
            }

        }

        return true;
    }


    public List<String> getTimelineList() {
        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add(ONE_TIME);
        list.add(ONE_WEEK);
        list.add(ONE_MONTH);
        list.add(THREE_MONTH);
        return list;
    }

    private class MyTextWatcher implements TextWatcher {
        private EditText view;

        public MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.quantityET:
                    String quantity = quantityET.getText().toString().trim();
                    validateQuantity(quantity);
                    break;
                case R.id.commentET:
                    String comment = commentET.getText().toString().trim();
                    validateComment(comment);
                    break;

            }
        }
    }
}