package com.partseazy.android.ui.fragments.finance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.MultipartRequestMap;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.finance.EducationAdapter;
import com.partseazy.android.ui.adapters.finance.FinancialDocumentTypeAdapter;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.financialapplication.Document;
import com.partseazy.android.ui.model.financialapplication.DocumentList;
import com.partseazy.android.ui.model.financialapplication.DocumentResponse;
import com.partseazy.android.ui.model.financialapplication.DocumentType;
import com.partseazy.android.ui.model.financialapplication.FinanceAppResponse;
import com.partseazy.android.ui.model.shippingaddress.CityDetail;
import com.partseazy.android.ui.widget.SeekbarWithIntervals;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by naveen on 4/1/17.
 */

public class FinancialApplicationFragment extends BaseFragment implements View.OnClickListener, Response.StringListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.continueBTN)
    protected Button continueBTN;
    @BindView(R.id.seekbarWithIntervals)
    protected SeekbarWithIntervals seekbarWithIntervals;
    @BindView(R.id.checkBoxBtn)
    protected CheckBox checkBoxBtn;


    @BindView(R.id.dobBTN)
    protected TextView dobBTN;
    @BindView(R.id.incorporationDateTN)
    protected TextView incorporationDateTN;

    @BindView(R.id.nameET)
    protected EditText nameET;
    @BindView(R.id.companyET)
    protected EditText companyET;
    @BindView(R.id.shopEstdET)
    protected EditText shopEstdET;

    @BindView(R.id.panET)
    protected EditText panET;
    @BindView(R.id.shopPanET)
    protected EditText shopPanET;
    @BindView(R.id.shopPincodeET)
    protected EditText shopPincodeET;
    @BindView(R.id.shopAddressET)
    protected EditText shopAddressET;
    @BindView(R.id.shopCityET)
    protected EditText shopCityET;
    @BindView(R.id.shopStateET)
    protected EditText shopStateET;

    @BindView(R.id.pincodeET)
    protected EditText pincodeET;
    @BindView(R.id.addressET)
    protected EditText addressET;
    @BindView(R.id.cityET)
    protected EditText cityET;
    @BindView(R.id.stateET)
    protected EditText stateET;

    @BindView(R.id.nameTIL)
    protected TextInputLayout nameTIL;
    @BindView(R.id.companyTIL)
    protected TextInputLayout companyTIL;
    @BindView(R.id.shopEstdTIL)
    protected TextInputLayout shopEstdTIL;
    @BindView(R.id.panTIL)
    protected TextInputLayout panTIL;
    @BindView(R.id.shopPanTIL)
    protected TextInputLayout shopPanTIL;
    @BindView(R.id.shopPincodeTIL)
    protected TextInputLayout shopPincodeTIL;
    @BindView(R.id.shopAddressTIL)
    protected TextInputLayout shopAddressTIL;
    @BindView(R.id.shopCityTIL)
    protected TextInputLayout shopCityTIL;
    @BindView(R.id.shopStateTIL)
    protected TextInputLayout shopStateTIL;

    @BindView(R.id.pincodeTIL)
    protected TextInputLayout pincodeTIL;
    @BindView(R.id.addressTIL)
    protected TextInputLayout addressTIL;
    @BindView(R.id.cityTIL)
    protected TextInputLayout cityTIL;
    @BindView(R.id.stateTIL)
    protected TextInputLayout stateTIL;

    @BindView(R.id.femaleRadioBtn)
    RadioButton femaleRadioBtn;
    @BindView(R.id.maleRadioBtn)
    RadioButton maleRadioBtn;
    @BindView(R.id.genderRadioGrp)
    RadioGroup genderRadioGrp;

    @BindView(R.id.epayWebView)
    protected WebView epayWebView;

    @BindView(R.id.educationSpinner)
    protected Spinner educationSpinner;

    private List<Education> educationList;
    private EducationAdapter educationSpinnerAdapter;

    private FinancialDocumentTypeAdapter documentTypeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<DocumentType> documentsList = new ArrayList<>();
    public static final String ID_PROOF = "Any one Identity proof";
    public static final String ADDRESS_PROOF = "Any one Address proof";
    public static final String BUSINESS_PROOF = "Any one Business proof";

    // for financial app epaylater
    public static final String DEVICE_IMEI_NUMBER = "imei";
    public static final String DEVICE_MAC_ADDRESS = "mac_address";
    public static final String DEVICE_MANUFACTURER = "manufacturer";
    public static final String DEVICE_MODEL = "model";
    public static final String DEVICE_NUMBER = "deviceNumber";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String DEVICE_OS_VERSION = "os";


    public static final int OPEN_GALLERY = 1;
    public static final int OPEN_CAMERA = 2;

    private ImageView uploadedImage;
    private RelativeLayout progressBarLyt;

    private int identityDocId, addressDocId, businessDocId;
    private int documentID;
    private String documentType;
    private boolean isCBSelected;
    private String creditLimit;
    private boolean isBusinessAddress = false;
    private RetailerData retailerData;

    private int mYear, mMonth, mDay;

    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;

    private FeatureMap.Feature fMap;

    public static FinancialApplicationFragment newInstance() {
        Bundle bundle = new Bundle();
        FinancialApplicationFragment fragment = new FinancialApplicationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retailerData = new RetailerData();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_financial_application;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.fill_credit_detail_info);
    }

    public static String getTagName() {
        return FinancialApplicationFragment.class.getSimpleName();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (documentsList != null && documentsList.size() > 0) {
            setDocumentAdapter();
        } else {
            loadDocumentList();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isCBSelected = checkBoxBtn.isChecked();
        creditLimit = StaticMap.credit_limit_15K;
        priceSeekBarHandling();

        educationList = getEducationList();
        if (educationList != null && educationList.size() > 0) {

            educationSpinnerAdapter = new EducationAdapter(getContext(), educationList);
            educationSpinner.setAdapter(educationSpinnerAdapter);

//            educationSpinnerAdapter = new ArrayAdapter<Education>(getContext(), android.R.layout.simple_spinner_item, educationList);
//            educationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            educationSpinner.setAdapter(educationSpinnerAdapter);
        }

        educationSpinner.setOnItemSelectedListener(this);

        continueBTN.setOnClickListener(this);
        checkBoxBtn.setOnClickListener(this);
        dobBTN.setOnClickListener(this);
        incorporationDateTN.setOnClickListener(this);
        nameET.addTextChangedListener(new MyTextWatcher(nameET, nameTIL));
        companyET.addTextChangedListener(new MyTextWatcher(companyET, companyTIL));
        shopEstdET.addTextChangedListener(new MyTextWatcher(shopEstdET, shopEstdTIL));
        panET.addTextChangedListener(new MyTextWatcher(panET, panTIL));
        shopPanET.addTextChangedListener(new MyTextWatcher(shopPanET, shopPanTIL));
        shopPincodeET.addTextChangedListener(new MyTextWatcher(shopPincodeET, shopPincodeTIL));
        shopAddressET.addTextChangedListener(new MyTextWatcher(shopAddressET, shopAddressTIL));
        shopCityET.addTextChangedListener(new MyTextWatcher(shopCityET, shopCityTIL));
        shopStateET.addTextChangedListener(new MyTextWatcher(shopStateET, shopStateTIL));
        pincodeET.addTextChangedListener(new MyTextWatcher(pincodeET, pincodeTIL));
        addressET.addTextChangedListener(new MyTextWatcher(addressET, addressTIL));
        cityET.addTextChangedListener(new MyTextWatcher(cityET, cityTIL));
        stateET.addTextChangedListener(new MyTextWatcher(stateET, stateTIL));
        handleGenderClicks();

        fMap = FeatureMap.getFeatureMap(FeatureMapKeys.finance_form_webview);
        if (fMap.isActive()) {
            setupWebView();
            hideToolbar();
            epayWebView.setVisibility(View.VISIBLE);
            epayWebView.loadUrl(StaticMap.credit_epay_form);
            if (!PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                PermissionUtil.requestCameraPermission((BaseActivity) context);
            }
        }

        return view;

    }

    private void setDocumentAdapter() {
        if (documentTypeAdapter == null)
            documentTypeAdapter = new FinancialDocumentTypeAdapter(this, documentsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(documentTypeAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    public void handleGenderClicks() {

        maleRadioBtn.setChecked(true);
        retailerData.gender = getString(R.string.male);
        genderRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == maleRadioBtn.getId()) {
                    retailerData.gender = getString(R.string.male);
                } else if (checkedId == femaleRadioBtn.getId()) {
                    retailerData.gender = getString(R.string.female);

                }
            }
        });
    }


    //TODO replace it with the real Values
    private List<String> getIntervals() {
        return new ArrayList<String>() {{
            add("15 K");
            add("25 K");
            add("50 K");
            add("1 Lac");
        }};
    }

    public List<Education> getEducationList() {
        List<Education> list = new ArrayList<Education>();
        list.add(new Education("10th", StaticMap.EDUCATION_10TH));
        list.add(new Education("12th", StaticMap.EDUCATION_12TH));
        list.add(new Education("graduation", StaticMap.EDUCATION_GRADUATION));
        list.add(new Education("post_graduation", StaticMap.EDUCATION_POST_GRADUATION));
        list.add(new Education("other", StaticMap.EDUCATION_OTHER));
        return list;
    }

    public void priceSeekBarHandling() {
        List<String> seekbarIntervals = getIntervals();
        seekbarWithIntervals.setIntervals(seekbarIntervals);
        seekbarWithIntervals.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                CustomLogger.d("progress Value " + i);
                switch (i) {
                    case 0:
                        creditLimit = StaticMap.credit_limit_15K;
                        break;

                    case 1:
                        creditLimit = StaticMap.credit_limit_25K;
                        break;

                    case 2:
                        creditLimit = StaticMap.credit_limit_50K;
                        break;

                    case 3:
                        creditLimit = StaticMap.credit_limit_1L;
                        break;

                    default:
                        creditLimit = StaticMap.credit_limit_25K;
                        break;

                }

                CustomLogger.d("credit Limit " + creditLimit);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void getPincodeDetail(String pincode) {
        showProgressDialog();
        String completeUrl = WebServiceConstants.GET_CITY + pincode;
        getNetworkManager().GetRequest(RequestIdentifier.GET_CITY_ID.ordinal(),
                completeUrl, null, null, this, this, false);
    }

    private void loadDocumentList() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.FINANCIAL_DOCUMENT_TYPE_LIST_REQUEST_ID.ordinal(),
                WebServiceConstants.FINANCIAL_DOCUMENT_LIST, null, params, this, this, false);
    }

    private void uploadDocument(int documentId, File file) {
        progressBarLyt.setVisibility(View.VISIBLE);
        MultipartRequestMap params = WebServicePostParams.multiRequestMap(documentId, file);


        getNetworkManager().MultipartRequest(RequestIdentifier.UPLOAD_DOCUMENT_REQUEST_ID.ordinal(), WebServiceConstants.FILE_UPLOAD, params, null, this, this, false);


    }

    private void postFinanceAppDocumentIds() {
        showProgressDialog(getString(R.string.uploading), false);
        int creditLimitInt = Integer.parseInt(creditLimit);
        String deviceinfo = CommonUtility.financeDeviceDetail(context);
        Map params = WebServicePostParams.financeApplicationParams(identityDocId, addressDocId, businessDocId, creditLimitInt, isCBSelected, deviceinfo, retailerData);
        getNetworkManager().PostRequest(RequestIdentifier.FINANCIAL_APPLICATION_REQUEST_ID.ordinal(),
                WebServiceConstants.FINANCIAL_APPLICATION, params, null, this, this, false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            // check first if the result is coming for webview
            if (requestCode == FCR) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Uri[] results = null;
                    if (null == mUMA) {
                        return;
                    }
                    if (data == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                    mUMA.onReceiveValue(results);
                    mUMA = null;
                } else {
                    if (null == mUM) return;
                    Uri result = data.getData();
                    mUM.onReceiveValue(result);
                    mUM = null;
                }
            }

            if (requestCode == OPEN_GALLERY && !fMap.isActive()) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    String selectedImagePath = CommonUtility.getImagePath(getContext(), selectedImageUri);
//                    uploadedImage.setImageURI(selectedImageUri);
                    File imageFile = new File(selectedImagePath);
                    imageFile = CommonUtility.saveBitmapToFile(imageFile);
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    uploadedImage.setImageBitmap(bitmap);

                    if (documentID != -1) {
                        //uploadDocument(documentID, imageFile);
                    }
                } else {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_upload_document));
                }
            }

            if (requestCode == OPEN_CAMERA && !fMap.isActive()) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    File imageFile = CommonUtility.convertBitmapToFile(getContext(), bitmap);
                    if (imageFile != null && documentID != -1) {
                        uploadedImage.setImageBitmap(bitmap);
                        // uploadDocument(documentID, imageFile);
                    }
                } else {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_take_a_picture));
                }
            }
        }
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        hideProgressBar();
        if (request.getIdentifier() == RequestIdentifier.FINANCIAL_DOCUMENT_TYPE_LIST_REQUEST_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }

        if (request.getIdentifier() == RequestIdentifier.UPLOAD_DOCUMENT_REQUEST_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }

        if (request.getIdentifier() == RequestIdentifier.FINANCIAL_APPLICATION_REQUEST_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }
        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            clearAddressFiled();
            if (apiError != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.message);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.FINANCIAL_DOCUMENT_TYPE_LIST_REQUEST_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), DocumentList.class, new OnGsonParseCompleteListener<DocumentList>() {
                        @Override
                        public void onParseComplete(DocumentList data) {
                            //  hideProgressDialog();
                            hideProgressBar();
                            if (data.result != null && data.result.size() > 0) {
                                List<Document> idProofList = new ArrayList<Document>();
                                List<Document> addressProofList = new ArrayList<Document>();
                                List<Document> businessProofList = new ArrayList<Document>();
                                for (int i = 0; i < data.result.size(); i++) {

                                    if (data.result.get(i).isIdProof == 1) {
                                        idProofList.add(data.result.get(i));
                                    }
                                    if (data.result.get(i).isAddressProof == 1) {
                                        addressProofList.add(data.result.get(i));
                                    }
                                    if (data.result.get(i).isBusinessProof == 1) {
                                        businessProofList.add(data.result.get(i));
                                    }
                                }
                                if (idProofList.size() > 0) {
                                    DocumentType documenttype = new DocumentType();
                                    documenttype.documentName = ID_PROOF;
                                    documenttype.documentList = new ArrayList<Document>();
                                    documenttype.documentList.addAll(idProofList);
                                    documentsList.add(documenttype);
                                }
                                if (addressProofList.size() > 0) {
                                    DocumentType documenttype = new DocumentType();
                                    documenttype.documentName = ADDRESS_PROOF;
                                    documenttype.documentList = new ArrayList<Document>();
                                    documenttype.documentList.addAll(addressProofList);
                                    documentsList.add(documenttype);
                                }
                                if (businessProofList.size() > 0) {
                                    DocumentType documenttype = new DocumentType();
                                    documenttype.documentName = BUSINESS_PROOF;
                                    documenttype.documentList = new ArrayList<Document>();
                                    documenttype.documentList.addAll(businessProofList);
                                    documentsList.add(documenttype);
                                }
                                idProofList = null;
                                addressProofList = null;
                                businessProofList = null;
                                setDocumentAdapter();
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            //hideProgressDialog();
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }


        if (request.getIdentifier() == RequestIdentifier.FINANCIAL_APPLICATION_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FinanceAppResponse.class, new OnGsonParseCompleteListener<FinanceAppResponse>() {
                        @Override
                        public void onParseComplete(FinanceAppResponse data) {
                            hideProgressDialog();
                            if (data != null) {
                                if (getActivity() != null) {
                                    DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.financial_documents_uploaded), getString(R.string.OK)
                                            , null, null, new DialogListener() {
                                                @Override
                                                public void onPositiveButton(DialogInterface dialog) {

                                                    KeyPadUtility.hideSoftKeypad(getActivity());
                                                    ((BaseActivity) getActivity()).onPopBackStack(true);
                                                }
                                            });
                                }
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

        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), CityDetail.class, new OnGsonParseCompleteListener<CityDetail>() {
                @Override
                public void onParseComplete(CityDetail getCity) {
                    hideProgressDialog();
                    if (getCity != null) {
                        setCityStateAddress(getCity, isBusinessAddress);
                    } else {
                        clearAddressFiled();
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    hideProgressDialog();
                    KeyPadUtility.hideSoftKeypad(getActivity());
                    pincodeTIL.setError(getString(R.string.change_pincode));
                }


            });
        }
        return true;
    }

    @Override
    public void onStringResponse(Request<String> request, String responseObject, Response<String> response) {
        hideProgressDialog();


        if (request.getIdentifier() == RequestIdentifier.UPLOAD_DOCUMENT_REQUEST_ID.ordinal()) {
            CustomLogger.d("Response image:: " + responseObject.toString());
            getGsonHelper().parse(responseObject.toString(), DocumentResponse.class, new OnGsonParseCompleteListener<DocumentResponse>() {
                        @Override
                        public void onParseComplete(DocumentResponse data) {
                            try {
                                progressBarLyt.setVisibility(View.GONE);
                                uploadedImage.setVisibility(View.VISIBLE);
                                if (data != null) {
                                    if (documentType != null) {
                                        if (documentType.equals(ID_PROOF)) {
                                            identityDocId = data.id;
                                        }
                                        if (documentType.equals(ADDRESS_PROOF)) {
                                            addressDocId = data.id;
                                        }
                                        if (documentType.equals(BUSINESS_PROOF)) {
                                            businessDocId = data.id;
                                        }
                                    }
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            progressBarLyt.setVisibility(View.GONE);
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.SEND_DOCUMENT_ID) {
            int documentId = (Integer) eventObject.objects[0];
            String docType = (String) eventObject.objects[1];
            uploadedImage = (ImageView) eventObject.objects[2];
            progressBarLyt = (RelativeLayout) eventObject.objects[3];
            documentID = documentId;
            ;
            if (docType.equals(ID_PROOF)) {
                //identityDocId = documentId;
                documentType = docType;
            }
            if (docType.equals(ADDRESS_PROOF)) {
                //addressDocId = documentId;
                documentType = docType;
            }
            if (docType.equals(BUSINESS_PROOF)) {
                //  businessDocId = documentId;
                documentType = docType;
            }
            CustomLogger.d("document selected :: " + documentId + " :" + documentType);

        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == continueBTN.getId()) {

            if (identityDocId != 0 && addressDocId != 0 && businessDocId != 0 && isCBSelected) {
                if (getValuesFromViews()) {
                    postFinanceAppDocumentIds();
                }
            } else {
                if (identityDocId == 0) {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_select_id_proof));
                } else if (addressDocId == 0) {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_select_address_proof));
                } else if (businessDocId == 0) {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_select_business_proof));
                } else if (!isCBSelected) {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.select_check_box));
                }
            }
        }

        if (view.getId() == checkBoxBtn.getId()) {
            isCBSelected = checkBoxBtn.isChecked();
            CustomLogger.d("isCBSelected  " + isCBSelected);
        }
        if (view.getId() == dobBTN.getId()) {
            //showTimePickerDialog();
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            final String[] dob = {null};

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String month = monthOfYear + "";
                            String day = dayOfMonth + "";
                            if ((monthOfYear + 1) < 10) {

                                month = "0" + monthOfYear;
                            }
                            if (dayOfMonth < 10) {

                                day = "0" + dayOfMonth;
                            }
                            dob[0] = year + "-" + month + "-" + day;
                            dobBTN.setText(dob[0]);
                            retailerData.dob = dob[0];

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        }

        if (view.getId() == incorporationDateTN.getId()) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            final String[] incorporateDate = {null};

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String month = monthOfYear + "";
                            String day = dayOfMonth + "";
                            if ((monthOfYear + 1) < 10) {

                                month = "0" + monthOfYear;
                            }
                            if (dayOfMonth < 10) {

                                day = "0" + dayOfMonth;
                            }
                            incorporateDate[0] = year + "-" + month + "-" + day;
                            incorporationDateTN.setText(incorporateDate[0]);
                            retailerData.incorporationDate = incorporateDate[0];

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        }
    }

    private void setCityStateAddress(CityDetail cityDetail, boolean isBusinessAddress) {
        if (isBusinessAddress) {
            retailerData.cityId = cityDetail.cityId;
            retailerData.stateId = cityDetail.stateId;
            shopCityET.setText(cityDetail.city);
            shopStateET.setText(cityDetail.state);
        } else {
            retailerData.shopCityId = cityDetail.cityId;
            retailerData.shopStateId = cityDetail.stateId;
            cityET.setText(cityDetail.city);
            stateET.setText(cityDetail.state);
        }
    }

    private void clearAddressFiled() {
        if (isBusinessAddress) {


            shopStateET.setText("");
            shopCityET.setText("");
        } else {
            cityET.setText("");
            stateET.setText("");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Education education = (Education) adapterView.getItemAtPosition(i);
        retailerData.education = education.educationKey;
        CustomLogger.d("Education Selected ::" + retailerData.education);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    boolean getValuesFromViews() {
        boolean isValidData = true;

        if (nameET.getText().toString().trim().length() > 0) {
            retailerData.name = nameET.getText().toString();

        } else {
            isValidData = false;
            nameTIL.setError(getString(R.string.enter_name));
        }

        if (companyET.getText().toString().trim().length() > 0) {
            retailerData.companyName = companyET.getText().toString();

        } else {
            isValidData = false;
            companyET.setError(getString(R.string.enter_company_name));
        }

        if (shopEstdET.getText().toString().trim().length() > 0) {
            retailerData.shopEstablishmentNumber = shopEstdET.getText().toString();

        }
        if (CommonUtility.isValidPANNumber(panET.getText().toString().trim())) {
            retailerData.panNumber = panET.getText().toString();

        } else {
            isValidData = false;
            panTIL.setError(getString(R.string.invalid_PAN));
        }
        if (CommonUtility.isValidPANNumber(shopPanET.getText().toString().trim())) {
            retailerData.shopPanNumber = shopPanET.getText().toString();
        } else {
            isValidData = false;
            shopPanTIL.setError(getString(R.string.invalid_shop_PAN));
        }
        if (shopPincodeET.getText().toString().trim().length() > 0) {
            retailerData.shopPincode = shopPincodeET.getText().toString();
        } else {
            isValidData = false;
            shopPincodeTIL.setError(getString(R.string.enter_pincode));
        }
        if (shopAddressET.getText().toString().trim().length() > 0) {
            retailerData.shopAddress = shopAddressET.getText().toString();
        } else {
            isValidData = false;
            shopAddressTIL.setError(getString(R.string.enter_address));
        }
        if (shopCityET.getText().toString().trim().length() > 0) {
            retailerData.shopCity = shopCityET.getText().toString();
        } else {
            shopCityTIL.setError(getString(R.string.enter_city));
        }
        if (shopStateET.getText().toString().trim().length() > 0) {
            retailerData.shopState = shopStateET.getText().toString();
        } else {
            shopStateTIL.setError(getString(R.string.enter_state));
        }

        if (pincodeET.getText().toString().trim().length() > 0) {
            retailerData.pincode = pincodeET.getText().toString();
        } else {
            isValidData = false;
            pincodeTIL.setError(getString(R.string.enter_pincode));
        }
        if (addressET.getText().toString().trim().length() > 0) {
            retailerData.address = addressET.getText().toString();
        } else {
            isValidData = false;
            addressTIL.setError(getString(R.string.enter_address));
        }
        if (cityET.getText().toString().trim().length() > 0) {
            retailerData.city = cityET.getText().toString();
        } else {
            cityTIL.setError(getString(R.string.enter_city));
        }
        if (stateET.getText().toString().trim().length() > 0) {
            retailerData.state = stateET.getText().toString();
        } else {
            stateTIL.setError(getString(R.string.enter_state));
        }

        if (retailerData.dob == null || (retailerData.dob.equals(""))) {
            isValidData = false;
            showError(getString(R.string.enter_date_of_birthday), MESSAGETYPE.SNACK_BAR);
        }

        if (retailerData.incorporationDate == null || (retailerData.incorporationDate.equals(""))) {
            isValidData = false;
            showError(getString(R.string.select_date_of_incorporation), MESSAGETYPE.SNACK_BAR);
        }


        return isValidData;

    }

    private class MyTextWatcher implements TextWatcher {
        private EditText editText;
        private TextInputLayout textInputLayout;

        public MyTextWatcher(EditText view, TextInputLayout textInputLayout) {
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
                case R.id.shopPincodeET:
                    if (editable.length() == 6) {
                        String pincode = editable.toString();
                        isBusinessAddress = true;
                        getPincodeDetail(pincode);
                    }
                    break;

                case R.id.pincodeET:
                    if (editable.length() == 6) {
                        String pincode = editable.toString();
                        isBusinessAddress = false;
                        getPincodeDetail(pincode);
                    }
                    break;
            }


        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    private void setupWebView() {
        WebSettings webSettings = epayWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            epayWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            epayWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            epayWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        epayWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showProgressDialog();
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                hideProgressDialog();
            }
        });

        epayWebView.setWebChromeClient(new WebChromeClient() {
            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                context.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        CustomLogger.e("Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
