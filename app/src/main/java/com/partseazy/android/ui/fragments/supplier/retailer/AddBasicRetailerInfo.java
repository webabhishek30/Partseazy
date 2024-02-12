package com.partseazy.android.ui.fragments.supplier.retailer;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.utility.CommonTextWatcher;

import butterknife.BindView;

/**
 * Created by naveen on 11/9/17.
 */

public class AddBasicRetailerInfo extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.ownerNameTIL)
    protected TextInputLayout ownerNameTIL;
    @BindView(R.id.shopNameTIL)
    protected TextInputLayout shopNameTIL;
    @BindView(R.id.shopPincodeTIL)
    protected TextInputLayout shopPincodeTIL;
    @BindView(R.id.shopAddressTIL)
    protected TextInputLayout shopAddressTIL;

    @BindView(R.id.whatsAppTIL)
    protected TextInputLayout whatsAppTIL;
    @BindView(R.id.pocNameTIL)
    protected TextInputLayout pocNameTIL;
    @BindView(R.id.pocNumberTIL)
    protected TextInputLayout pocNumberTIL;


    @BindView(R.id.ownerNameET)
    protected EditText ownerNameET;
    @BindView(R.id.shopNameET)
    protected EditText shopNameET;
    @BindView(R.id.shopPincodeET)
    protected EditText shopPincodeET;
    @BindView(R.id.shopAddressET)
    protected EditText shopAddressET;
    @BindView(R.id.mobileET)
    protected EditText mobileET;

    @BindView(R.id.whatsAppET)
    protected EditText whatsAppET;
    @BindView(R.id.pocNameET)
    protected EditText pocNameET;
    @BindView(R.id.pocNumberET)
    protected EditText pocNumberET;

    @BindView(R.id.businessRecyclerView)
    protected RecyclerView businessRecyclerView;


    @BindView(R.id.cityNameTV)
    protected TextView cityNameTV;


    @BindView(R.id.nextSubmitBT)
    protected Button nextSubmitBT;

    @BindView(R.id.picRecyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.shopFloorSpinner)
    protected Spinner shopFloorSpinner;

    @BindView(R.id.latitudeTV)
    protected TextView latitudeTV;

    @BindView(R.id.longitudeTV)
    protected TextView longitudeTV;

    @BindView(R.id.updateLocationTV)
    protected TextView updateLocationTV;

    @BindView(R.id.noRadioBtn)
    protected RadioButton noRadioBtn;

    @BindView(R.id.yesRadioBtn)
    protected RadioButton yesRadioBtn;

    @BindView(R.id.tinRadioGrp)
    protected RadioGroup tinRadioGrp;

    @BindView(R.id.tinTIL)
    protected TextInputLayout tinTIL;

    @BindView(R.id.tinET)
    protected EditText tinET;

//    private ShopData shopData;
//    private boolean isDeliveredAtShop = true;
//    private Set<String> selectBusinessType;
//    private List<PictureModel> pictureModelList;
//
//    private PictureModel pictureModel;
//    private int picPositionOnCard;
//    private int itemPosition;
//    private Bitmap picBitmap;
//    private String imageName;
//
//    private List<CheckBoxModel> checkBusinessType;
//    private CheckboxAdapter checkboxAdapter;
//    private DocumentPictureAdapter documentPictureAdapter;
//
//    private Map<String, PictureFactoryModel> pictureMap;
//
//    private String mobileNumber;
//    private Dialog otpDailog;
//
//
//    private GPSTracker gps;

    public static AddBasicRetailerInfo newInstance() {
        Bundle bundle = new Bundle();
        AddBasicRetailerInfo fragment = new AddBasicRetailerInfo();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_basic_retailer_info;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.basic_info);
    }

    public static String getTagName() {
        return AddBasicRetailerInfo.class.getSimpleName();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        shopData = new ShopData();
//        gps = new GPSTracker(getContext());
//        checkBusinessType = new ArrayList<>();
//        selectBusinessType = new HashSet<>();
//        pictureMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

//        if (pictureModelList != null && pictureModelList.size() > 0) {
//            setAdapter();
//        } else {
//            loadListData();
//        }
//        handleDeliveryCheckbox();
//        checkGPSPermission();
//        loadBusinessTypeList();
//        setShopFloorSpinner();
//        showGPSLocation();
//        handleTinRadioGrpClicks();
        nextSubmitBT.setOnClickListener(this);
        updateLocationTV.setOnClickListener(this);
        ownerNameET.addTextChangedListener(new CommonTextWatcher(ownerNameET, ownerNameTIL));
        shopNameET.addTextChangedListener(new CommonTextWatcher(shopNameET, shopNameTIL));
        shopPincodeET.addTextChangedListener(new CommonTextWatcher(shopPincodeET, shopPincodeTIL));
        shopAddressET.addTextChangedListener(new CommonTextWatcher(shopAddressET, shopAddressTIL));

        return view;
    }

    @Override
    public void onClick(View view) {

    }

/*
    private void loadListData() {
        pictureModelList = new ArrayList<>();
        PictureModel dataInvoice   = new PictureModel(getString(R.string.data_invoice),DocumentsConstants.INVOICE_ID);
        dataInvoice.isHidden = true;
        pictureModelList.add(dataInvoice);
        pictureModelList.add(new PictureModel(getString(R.string.card_shop), DocumentsConstants.CARD_ID));

        setAdapter();

    }

    private void setAdapter() {
        if (documentPictureAdapter == null)
            documentPictureAdapter = new DocumentPictureAdapter(this, pictureModelList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(documentPictureAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }


    public void handleTinRadioGrpClicks() {

        PictureModel invoiceModel = null;
        noRadioBtn.setChecked(true);
        tinRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == yesRadioBtn.getId()) {
                    pictureModelList.get(0).isHidden = false;
                    documentPictureAdapter.notifyItemChanged(0);
                    tinTIL.setVisibility(View.VISIBLE);
                } else if (checkedId == noRadioBtn.getId()) {
                    tinTIL.setVisibility(View.GONE);
                    tinET.setText("");
                    pictureModelList.get(0).isHidden = true;
                    documentPictureAdapter.notifyItemChanged(0);
                }
            }
        });
    }

    private void checkGPSPermission() {


        if (PermissionUtil.hasPermissions(context, PermissionUtil.LOCATION_PERMISSIONS)) {
        } else {
            PermissionUtil.requestLocationPermission((BaseActivity) context);
        }

    }

    private void openGPSSetting() {
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        }

    }

    private void handleDeliveryCheckbox() {

        sameAddressCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckBox checkBox = ((CheckBox) view);
                if (checkBox.isChecked()) {
                    isDeliveredAtShop = true;
                    deliveryAddLL.setVisibility(View.GONE);
                    deliveryPincodeET.setText("");
                    deliveryAddET.setText("");
                } else {
                    isDeliveredAtShop = false;
                    deliveryAddLL.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void callOTPRequest(String mobileNumber) {
        if (CommonUtility.isValidMobileNumber(mobileNumber.trim(), false, 10, 10)) {

            hideKeyBoard(getView());
            if (isAdded()) {
                showProgressDialog(getString(R.string.send_otp), false);
            }
            RequestParams params = new RequestParams();
            params.headerMap = new HashMap<>();
            WebServicePostParams.addResultWrapHeader(params.headerMap);
            getNetworkManager().PutRequest(RequestIdentifier.OTP_REQUEST_ID.ordinal(),
                    WebServiceConstants.SEND_RETAILER_OTP + mobileNumber, null, params, this, this, false);
        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.invalid_number));
        }
    }

    private void postRetailerDetails() {
        showProgressDialog(getString(R.string.uploading), false);
        Map params = WebServicePostParams.shopDetailParams(shopData);
        getNetworkManager().PutRequest(RequestIdentifier.RETAILER_BASIC_INFO_ID.ordinal(),
                WebServiceConstants.PUT_RETAILER_DATA + shopData.shopMobileNumber, params, null, this, this, false);
    }

    protected void callVerifyOTP(String otpCode) {
        hideKeyBoard(getView());
        showProgressDialog(getString(R.string.verification_code), false);
        Map params = WebServicePostParams.retailerVerifyOTPParams(otpCode);
        RequestParams requestParams = new RequestParams();
        requestParams.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(requestParams.headerMap);
        getNetworkManager().PutRequest(RequestIdentifier.OTP_VERIFICATION_ID.ordinal(),
                WebServiceConstants.VERIFY_RETAILER_OTP + mobileNumber, params, requestParams, this, this, false);

    }


    void getPincodeDetail(String pincode) {
        showProgressDialog();
        String completeUrl = WebServiceConstants.GET_CITY + pincode;
        getNetworkManager().GetRequest(RequestIdentifier.GET_CITY_ID.ordinal(),
                completeUrl, null, null, this, this, false);
    }


    private void setCityStateAddress(CityDetail cityDetail) {
        shopData.shopCityId = cityDetail.cityId;
        shopData.shopStateId = cityDetail.stateId;
        cityNameTV.setText(cityDetail.city + "," + cityDetail.state);
        cityNameTV.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressDialog();
        hideProgressBar();
        if (request.getIdentifier() == RequestIdentifier.RETAILER_BASIC_INFO_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.issue);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }
        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.issue);
                cityNameTV.setText(apiError.issue);
                cityNameTV.setVisibility(View.VISIBLE);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_REQUEST_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.issue);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_VERIFICATION_ID.ordinal()) {
            if (error != null) {
                SnackbarFactory.showSnackbar(getActivity(), apiError.issue);
            } else {
                SnackbarFactory.showSnackbar(getActivity(), getString(R.string.err_somthin_wrong));
            }
        }


        return true;
    }


    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.RETAILER_BASIC_INFO_ID.ordinal()) {

            getGsonHelper().parse(responseObject.toString(), RetailerDataResult.class, new OnGsonParseCompleteListener<RetailerDataResult>() {
                @Override
                public void onParseComplete(RetailerDataResult retailerDataResult) {
                    if (retailerDataResult.id != null) {
                        if (retailerDataResult.mobileVerified == 1) {
                            launchShopProfileFragment(retailerDataResult.mobile);
                        } else {
                            callOTPRequest(retailerDataResult.mobile);
                            showOTPDialog();
                        }
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {

                    KeyPadUtility.hideSoftKeypad(getActivity());
                    showError();
                    CustomLogger.e("Exception ", exception);
                }


            });
        }
        if (request.getIdentifier() == RequestIdentifier.GET_CITY_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), CityDetail.class, new OnGsonParseCompleteListener<CityDetail>() {
                @Override
                public void onParseComplete(CityDetail getCity) {
                    if (getCity != null) {
                        setCityStateAddress(getCity);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    KeyPadUtility.hideSoftKeypad(getActivity());
                    cityNameTV.setText("");
                    cityNameTV.setVisibility(View.GONE);
                }


            });
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            CustomLogger.d("OTP Hit Response");
//            showOTPDialog();
        }

        if (request.getIdentifier() == RequestIdentifier.OTP_VERIFICATION_ID.ordinal()) {
            dismissOTPDailog();
            getGsonHelper().parse(responseObject.toString(), OTPVerificationResult.class, new OnGsonParseCompleteListener<OTPVerificationResult>() {
                @Override
                public void onParseComplete(OTPVerificationResult otpVerificationResult) {
                    if (otpVerificationResult.result.success == 1) {
                        launchShopProfileFragment(mobileNumber);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    KeyPadUtility.hideSoftKeypad(getActivity());

                }


            });
        }


        return true;
    }


    private void loadBusinessTypeList() {
        checkBusinessType = new ArrayList<>();
        checkBusinessType.add(new CheckBoxModel("retailer", "Retailer"));
        checkBusinessType.add(new CheckBoxModel("wholesaler", "Wholesaler"));
        checkBusinessType.add(new CheckBoxModel("retailer_repair", "Retailer & Repair"));
        checkBusinessType.add(new CheckBoxModel("trader", "Trader"));
        checkBusinessType.add(new CheckBoxModel("repair", "Repair"));
        setFeatureAdapter();
    }

    private void setFeatureAdapter() {
        if (checkboxAdapter == null)
            checkboxAdapter = new CheckboxAdapter(getContext(), checkBusinessType, ShopProfilingFragment.CHECKBOXTYPE.BUSINESS_TYPE);
        businessRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        businessRecyclerView.setAdapter(checkboxAdapter);
        businessRecyclerView.setNestedScrollingEnabled(false);
    }


    private void setShopFloorSpinner() {

        List<SpinnerModel> floorList = new ArrayList<>();
        floorList.add(new SpinnerModel("na", "Select Shop Floor"));
        floorList.add(new SpinnerModel("-1", "Basement Floor"));
        floorList.add(new SpinnerModel("0", "Ground Floor"));
        floorList.add(new SpinnerModel("1", "First Floor"));
        floorList.add(new SpinnerModel("2", "Second Floor"));
        floorList.add(new SpinnerModel("3", "Third Floor"));
        floorList.add(new SpinnerModel("4", "Fourth Floor"));
        floorList.add(new SpinnerModel("5", "Fifth Floor"));


        CustomSpinnerAdapter closingTimeAdapter = new CustomSpinnerAdapter(getContext(), floorList);
        shopFloorSpinner.setAdapter(closingTimeAdapter);
        shopFloorSpinner.setOnItemSelectedListener(new SpinnerListener(shopFloorSpinner));

    }


    boolean getValuesFromViews() {
        boolean isValidData = true;

        String shopEmailAdd = emailET.getText().toString().trim();
        String shopPinCode = shopPincodeET.getText().toString().trim();
        String shopMobileNumber = mobileET.getText().toString().trim();
        String shopMobile2Number = mobile2ET.getText().toString().trim();

        String whatsAppNumber = whatsAppET.getText().toString().trim();
        String deliveryPincode = deliveryPincodeET.getText().toString().trim();
        String gstinNumber = tinET.getText().toString().trim();

        mobileNumber = shopMobileNumber;
        shopData.businessType = CommonUtility.convertSetToString(selectBusinessType);

        if (ownerNameET.getText().toString().trim().length() > 0) {
            shopData.ownerName = ownerNameET.getText().toString();
        } else {
            isValidData = false;
            ownerNameTIL.setError(getString(R.string.enter_name));
        }

        if (shopNameET.getText().toString().trim().length() > 0) {
            shopData.shopName = shopNameET.getText().toString();

        } else {
            isValidData = false;
            shopNameTIL.setError(getString(R.string.enter_shop_name));
        }


        if (shopPinCode != null && CommonUtility.isValidPincode(shopPinCode)) {
            shopData.shopPincode = shopPinCode;
        } else {
            isValidData = false;
            shopPincodeTIL.setError(getString(R.string.invalid_shop_pincode));
        }

        if (shopAddressET.getText().toString().trim().length() > 0) {
            shopData.shopAddress = shopAddressET.getText().toString();
        }


        if (CommonUtility.isValidEmail(shopEmailAdd)) {
            shopData.emailAddress = shopEmailAdd;
        } else {
            emailTIL.setError(getString(R.string.invalid_email_address));
        }

        if (websiteET.getText().toString().trim().length() > 0) {
            shopData.shopWebSite = websiteET.getText().toString();
        }

        if (shopMobileNumber != null && CommonUtility.isValidPhoneNumber(shopMobileNumber)) {
            shopData.shopMobileNumber = shopMobileNumber;
        } else {
            isValidData = false;
            mobileTIL.setError(getString(R.string.invalid_shop_mobile));
        }

        if (whatsAppNumber != null && CommonUtility.isValidPhoneNumber(whatsAppNumber)) {
            shopData.whatsAppNumber = whatsAppNumber;
        }

        if (shopMobile2Number != null && CommonUtility.isValidPhoneNumber(shopMobile2Number)) {
            shopData.shopMobile2Number = shopMobile2Number;
        }

        if (stdCodeET.getText().toString().trim().length() > 0) {
            shopData.stdCode = stdCodeET.getText().toString();
        }

        if (landlineET.getText().toString().trim().length() > 0) {
            shopData.landline = landlineET.getText().toString();
        }

        if (pocNameET.getText().toString().trim().length() > 0) {
            shopData.pointOfContact = pocNameET.getText().toString();
        }

        if (pocNumberET.getText().toString().trim().length() > 0) {
            shopData.pocMobile = pocNumberET.getText().toString();
        }

        if(gstinNumber!=null && !gstinNumber.equals(""))
        {
            if(CommonUtility.isValidGSTIN(gstinNumber)) {
                shopData.gstnNumber = gstinNumber;
            }else{
                tinTIL.setError(getString(R.string.invalid_gstin));
                isValidData=false;
            }
        }

        if (isDeliveredAtShop) {

            shopData.deliveryAtShop = 1;
            if (shopData.shopAddress != null) {
                shopData.deliveryAddress = shopData.shopAddress;
            }
//            else {
//                showError(getString(R.string.enter_shop_address), MESSAGETYPE.SNACK_BAR);
//                isValidData = false;
//            }

            if (shopData.shopPincode != null) {
                shopData.deliveryPincode = shopData.shopPincode;
            }
//            else {
//                showError(getString(R.string.enter_shop_pincode), MESSAGETYPE.SNACK_BAR);
//                isValidData = false;
//            }


        } else {
            shopData.deliveryAtShop = 0;
            if (deliveryAddET.getText().toString().trim().length() > 0) {
                shopData.deliveryAddress = deliveryAddET.getText().toString();
            }
            if (deliveryPincode != null && CommonUtility.isValidPincode(deliveryPincode)) {
                shopData.deliveryPincode = deliveryPincode;
            }
        }


        if (pictureMap != null) {
            for (Map.Entry<String, PictureFactoryModel> entry : pictureMap.entrySet()) {
                String key = entry.getKey();
                PictureFactoryModel value = entry.getValue();
                if (key.equals(DocumentsConstants.CARD_ID)) {
                    shopData.imgCardList = populateUserPicList(value);
                }
                if (key.equals(DocumentsConstants.INVOICE_ID)) {
                    shopData.imgInvoiceList = populateUserPicList(value);
                }
            }

        }

        if (gps != null) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if (latitude == 0 || longitude == 0) {
                isValidData = false;
                showError(getString(R.string.location_message), MESSAGETYPE.SNACK_BAR);
            } else {
                shopData.latitude = latitude + "";
                shopData.longitude = longitude + "";
            }
        }

        return isValidData;

    }

    private void showGPSLocation() {

        gps = new GPSTracker(getContext());
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {
            if (gps != null) {
                latitudeTV.setText(getString(R.string.latitude, gps.getLatitude() + ""));
                longitudeTV.setText(getString(R.string.longitude, gps.getLongitude() + ""));
                if (gps.getLatitude() == 0 || gps.getLongitude() == 0) {
                    updateLocationTV.setVisibility(View.VISIBLE);
                } else {
                    updateLocationTV.setVisibility(View.GONE);
                }
            }
        }

    }


    private void uploadDocument(int documentId, File file) {
        showProgressDialog(getString(R.string.upload_photo), false);
        MultipartRequestMap params = WebServicePostParams.onBoardShop(documentId, file, "onboard_shop");
        RequestParams requestParams = new RequestParams();

        getNetworkManager().MultipartRequest(RequestIdentifier.UPLOAD_PICTURE_ID.ordinal(),
                WebServiceConstants.FILE_UPLOAD, params, requestParams, new Response.Listener<String>() {
                    @Override
                    public void onResponse(Request<String> request, String responseObject, Response<String> response) {
                        if (request.getIdentifier() == RequestIdentifier.UPLOAD_PICTURE_ID.ordinal()) {
                            hideProgressDialog();
                            CustomLogger.d("Response image:: " + responseObject.toString());
                            getGsonHelper().parse(responseObject.toString(), FileData.class, new OnGsonParseCompleteListener<FileData>() {
                                        @Override
                                        public void onParseComplete(FileData data) {

                                            if (data!= null) {
                                                setDataToPictureMap(pictureModel.documentKey, data.fileId, picPositionOnCard);
                                                documentPictureAdapter.updateList(picBitmap, itemPosition, picPositionOnCard);
                                            }

                                        }

                                        @Override
                                        public void onParseFailure(Exception exception) {
                                            showError(getString(R.string.parsingErrorMessage),MESSAGETYPE.SNACK_BAR);
                                            CustomLogger.e("Exception ", exception);
                                        }
                                    }

                            );

                        }

                    }
                }, this, false);

    }

    private void setDataToPictureMap(String documentKey, int documentID, int picPosition) {

        if (pictureMap == null) {
            PictureFactoryModel pictureFactoryModel = new PictureFactoryModel();
            pictureFactoryModel.pictureKey = documentKey;
            populatePicFactoryModel(pictureFactoryModel, documentID, picPosition);
            pictureMap.put(documentKey, pictureFactoryModel);
        } else {
            if (pictureMap.containsKey(documentKey)) {
                PictureFactoryModel model = pictureMap.get(documentKey);
                populatePicFactoryModel(model, documentID, picPosition);
                pictureMap.put(documentKey, model);
            } else {
                PictureFactoryModel pictureFactoryModel = new PictureFactoryModel();
                pictureFactoryModel.pictureKey = documentKey;
                populatePicFactoryModel(pictureFactoryModel, documentID, picPosition);
                pictureMap.put(documentKey, pictureFactoryModel);
            }
        }

        if (pictureMap != null) {
            for (Map.Entry<String, PictureFactoryModel> entry : pictureMap.entrySet()) {
                String key = entry.getKey();
                PictureFactoryModel value = entry.getValue();
                CustomLogger.d("Key ::" + key + "value ::" + value.pictureKey + " ::" + value.firstPicId + " ::" + value.secondPicId + "::" + value.thirdPicId);
            }
        }
    }

    private void populatePicFactoryModel(PictureFactoryModel pictureFactoryModel, int documentID, int picPosition) {
        if (picPosition == DocumentPictureAdapter.FIRST_POSITION)
            pictureFactoryModel.firstPicId = documentID;
        if (picPosition == DocumentPictureAdapter.SECOND_POSITION)
            pictureFactoryModel.secondPicId = documentID;
        if (picPosition == DocumentPictureAdapter.THIRD_POSITION)
            pictureFactoryModel.thirdPicId = documentID;
    }


    public void handleCapturePicture(Intent data) {
        File file = new File(CommonUtility.getFileDirectory() + File.separator + imageName);
        file = saveBitmapToFile(file);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap != null) {
            if (file != null && picPositionOnCard != -1) {
                //imageView.setImageBitmap(bitmap);
                picBitmap = bitmap;
                uploadDocument(picPositionOnCard, file);
            }
        } else {
            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_take_a_picture));
        }

    }


    public void handleGalleryPicture(Intent data) {

        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            String selectedImagePath = CommonUtility.getImagePath(getContext(), selectedImageUri);
            File imageFile = new File(selectedImagePath);
            imageFile = saveBitmapToFile(imageFile);
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (picPositionOnCard != -1) {
                picBitmap = bitmap;
                uploadDocument(picPositionOnCard, imageFile);
            }
        } else {
            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_upload_document));
        }

    }



    private List<Integer> populateUserPicList(PictureFactoryModel model) {
        List<Integer> list = new ArrayList<>();
        if (model.firstPicId > 0)
            list.add(model.firstPicId);
        if (model.secondPicId > 0)
            list.add(model.secondPicId);
        if (model.thirdPicId > 0)
            list.add(model.thirdPicId);

        return list;
    }

    @Override
    public void onOpenCamera(PictureModel pictureModel, int position, int picPosition, String imageName) {
        this.pictureModel = pictureModel;
        this.itemPosition = position;
        this.picPositionOnCard = picPosition;
        this.imageName = imageName;
    }


    class MyTextWatcher implements TextWatcher {

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
                        getPincodeDetail(pincode);
                    } else {
                        cityNameTV.setVisibility(View.GONE);
                    }
                    break;

            }

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.updateLocationTV:
                showGPSLocation();
                break;

            case R.id.nextSubmitBT:

                if (getValuesFromViews()) {
                    String shopJson = new Gson().toJson(shopData);
                    CustomLogger.d("ShopJson ::" + shopJson);
                    postRetailerDetails();
                }
                break;
        }
    }

    public void launchShopProfileFragment(String retailerMobile) {
        ShopProfilingFragment shopProfilingFragment = ShopProfilingFragment.newInstance(retailerMobile);
        addToBackStack((BaseActivity) getActivity(), shopProfilingFragment, shopProfilingFragment.getTag());

    }

    public void launchBusinessPictureFragment(String retailerMobile) {
        CreditDetailFragment shopProfilingFragment = CreditDetailFragment.newInstance(retailerMobile);
        addToBackStack((BaseActivity) getActivity(), shopProfilingFragment, shopProfilingFragment.getTag());
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);


        if (eventObject.id == EventConstant.SELECT_BUSINESS_TYPE) {
            String brandId = (String) eventObject.objects[0];
            selectBusinessType = getCheckSet(selectBusinessType, brandId);
            CustomLogger.d("selectBusinessType ::" + selectBusinessType.toString());
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == OPEN_CAMERA) {
                handleCapturePicture(data);
            }

            if(requestCode == OPEN_GALLERY)
            {
                handleGalleryPicture(data);
            }

        }
    }


    private Set<String> getCheckSet(Set<String> set, String item) {
        if (set != null) {
            if (set.contains(item)) {
                set.remove(item);
            } else {
                set.add(item);
            }
        }
        return set;
    }


    private void showOTPDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dailog_mobile_otp, null);
        final EditText otpET = (EditText) view.findViewById(R.id.otpET);
        TextView otpTextTV = (TextView) view.findViewById(R.id.otpTextTV);
        TextView resendOTPTV = (TextView) view.findViewById(R.id.resendOTPTV);
        TextView skipOTPTV = (TextView) view.findViewById(R.id.skipOTPTV);
        Button otpSubmitBT = (Button) view.findViewById(R.id.otpSubmitBT);
        mobileNumber = shopData.shopMobileNumber;
        otpTextTV.setText(context.getString(R.string.cod_otp_text, mobileNumber));
        otpDailog = new Dialog(context);
        otpDailog.setContentView(view);
        otpDailog.setCancelable(false);
        otpDailog.show();

        otpSubmitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode = otpET.getText().toString().trim();
                if (otpCode.length() > 0) {
                    callVerifyOTP(otpCode);
                } else {
                    showError(getString(R.string.please_enter_otp),MESSAGETYPE.TOAST);
                }
            }
        });

        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOTPRequest(mobileNumber);
            }
        });

        skipOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchShopProfileFragment(mobileNumber);
                otpDailog.dismiss();
            }
        });
    }

    private void dismissOTPDailog() {
        otpDailog.dismiss();
    }


    class SpinnerListener implements AdapterView.OnItemSelectedListener {

        private Spinner spinner;

        public SpinnerListener(Spinner spinner) {
            this.spinner = spinner;
            ;
        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            switch (spinner.getId()) {

                case R.id.shopFloorSpinner:
                    SpinnerModel selectedItem = (SpinnerModel) adapterView.getItemAtPosition(position);
                    shopData.shopFloor = selectedItem.spinnerId;
                    break;


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        context.finish();
        return true;

    }

    */
}

