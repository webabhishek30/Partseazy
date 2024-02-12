package com.partseazy.android.ui.fragments.deals.create_deal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.MultipartRequestMap;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.create.CustomSpinnerAdapter;
import com.partseazy.android.ui.adapters.deals.create.DealImageAdapter;
import com.partseazy.android.ui.model.deal.FileData;
import com.partseazy.android.ui.model.deal.SpinnerModel;
import com.partseazy.android.ui.model.deal.category.DealCategory;
import com.partseazy.android.ui.model.deal.category.DealCategoryResult;
import com.partseazy.android.ui.model.deal.subcategory.DealSubCategory;
import com.partseazy.android.ui.model.deal.subcategory.DealSubCategoryResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by naveen on 25/4/17.
 */

public class NewDealBasicInfoFragment extends NewDealBaseFragment implements View.OnClickListener, Response.StringListener {

    @BindView(R.id.continueBT)
    protected Button continueBT;

    @BindView(R.id.categorySpinner)
    protected Spinner categorySpinner;

    @BindView(R.id.subCatSpinner)
    protected Spinner subCatSpinner;


    @BindView(R.id.productNameET)
    protected EditText productNameET;

    @BindView(R.id.productNameTIL)
    protected TextInputLayout productNameTIL;

    @BindView(R.id.productDescET)
    protected EditText productDescET;

    @BindView(R.id.termsConditionET)
    protected EditText termsConditionET;


    @BindView(R.id.endDateRL)
    protected RelativeLayout endDateRL;

    @BindView(R.id.endDateTV)
    protected TextView endDateTV;

    @BindView(R.id.endTimeRL)
    protected RelativeLayout endTimeRL;

    @BindView(R.id.endTimeTV)
    protected TextView endTimeTV;


    @BindView(R.id.picRecylerView)
    protected RecyclerView picRecylerView;

    @BindView(R.id.publicSwitch)
    protected Switch publicSwitch;

    @BindView(R.id.allowDemoCB)
    protected CheckBox allowDemoCB;

    @BindView(R.id.subCatSpinnerLL)
    protected LinearLayout subCatSpinnerLL;


    private List<SpinnerModel> categoryList;
    private CustomSpinnerAdapter categoryAdapter;

    private List<SpinnerModel> subCategoryList;
    private CustomSpinnerAdapter subCategoryAdapter;

    private List<FileData> fileDataList;
    private DealImageAdapter dealImageAdapter;


    public static final int OPEN_GALLERY = 1;
    public static final int OPEN_CAMERA = 2;

    private static final String DEAL_IMAGE_NAME = "deal_image.jpg";

    public static NewDealBasicInfoFragment newInstance() {
        Bundle bundle = new Bundle();
        NewDealBasicInfoFragment fragment = new NewDealBasicInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_basic_new_deal;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.create_deal);
    }

    public static String getTagName() {
        return NewDealBasicInfoFragment.class.getSimpleName();
    }

    @Override
    public DealData getdealData() {
        //Everytime new Final  attribute on starting
        return new DealData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileDataList = new ArrayList<>();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (categoryList != null && categoryList.size() > 0) {
            setCategorySpinner();
        } else {
            categoryList = new ArrayList<>();
            categoryList.add(new SpinnerModel("0", "Select Category"));
            loadCategoryList();
        }

        setImageAdapter();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        setDefaultData();
        productNameET.addTextChangedListener(new EditTextWatcher(productNameET, productNameTIL));

        continueBT.setOnClickListener(this);
        endDateRL.setOnClickListener(this);
        endTimeRL.setOnClickListener(this);


        setSwitchButton();

        setAllowDemoCB();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void loadCategoryList() {
        showProgressDialog(getString(R.string.loading), false);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_CATEGORY_LIST.ordinal(),
                WebServiceConstants.DEAL_CATEGORY_LIST, null, params, this, this, true);
    }

    private void loadSubCategoryList(String categoryId) {
        showProgressDialog(getString(R.string.loading), false);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        String subCategoryUrl = WebServiceConstants.DEAL_SUBCATEGORY_LIST + categoryId + "/children";
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_SUBCATEGORY_LIST.ordinal(),
                subCategoryUrl, null, params, this, this, false);
    }

    private void setSwitchButton() {
        publicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    dealData.isPublic = 1;
                } else {
                    dealData.isPublic = 0;
                }

            }
        });
    }

    private void setAllowDemoCB() {
        if (allowDemoCB.isChecked())
            dealData.allowDemo = 1;
        else
            dealData.allowDemo = 0;

        allowDemoCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowDemoCB.isChecked())
                    dealData.allowDemo = 1;
                else
                    dealData.allowDemo = 0;
            }
        });
    }

    private void setCategorySpinner() {

        if (categoryAdapter == null)
            categoryAdapter = new CustomSpinnerAdapter(getContext(), categoryList);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new SpinnerListener(categorySpinner));

    }

    private void setSubCategorySpinner() {

        if (subCategoryAdapter == null)
            subCategoryAdapter = new CustomSpinnerAdapter(getContext(), subCategoryList);
        subCatSpinner.setAdapter(subCategoryAdapter);
        subCatSpinner.setOnItemSelectedListener(new SpinnerListener(subCatSpinner));

    }

    private void setImageAdapter() {
        if (dealImageAdapter == null)
            dealImageAdapter = new DealImageAdapter(context, fileDataList);
        picRecylerView.setLayoutManager(new GridLayoutManager(context, 4));
        picRecylerView.setAdapter(dealImageAdapter);

    }


    private void uploadDocument(int documentId, File file) {

        showProgressDialog(getString(R.string.upload_photo), false);
        MultipartRequestMap params = WebServicePostParams.onBoardShop(documentId, file, "trade");
        RequestParams requestParams = new RequestParams();
        requestParams.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(requestParams.headerMap);

        getNetworkManager().MultipartRequest(RequestIdentifier.UPLOAD_PICTURE_ID.ordinal(), WebServiceConstants.FILE_UPLOAD, params, requestParams, this, this, false);


//        getNetworkManager().MultipartRequest(RequestIdentifier.UPLOAD_PICTURE_ID.ordinal(),
//                WebServiceConstants.FILE_UPLOAD, params, requestParams, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(Request<String> request, String responseObject, Response<String> response) {
//
//                        if (request.getIdentifier() == RequestIdentifier.UPLOAD_PICTURE_ID.ordinal()) {
//                            hideProgressDialog();
//                            CustomLogger.d("Response image:: " + responseObject.toString());
//                            getGsonHelper().parse(responseObject.toString(), FileData.class, new OnGsonParseCompleteListener<FileData>() {
//                                        @Override
//                                        public void onParseComplete(FileData data) {
//
//                                            if (data != null) {
//                                                if (fileDataList == null) {
//                                                    fileDataList = new ArrayList<FileData>();
//                                                }
//                                                fileDataList.add(data);
//
//                                                if (dealImageAdapter != null)
//                                                    dealImageAdapter.notifyDataSetChanged();
//                                                else
//                                                    setImageAdapter();
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onParseFailure(Exception exception) {
//                                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
//                                            CustomLogger.e("Exception ", exception);
//                                        }
//                                    }
//
//                            );
//
//                        }
//
//                    }
//                }, this, false);

    }

    @Override
    public void onStringResponse(Request<String> request, String responseObject, Response<String> response) {
        if (request.getIdentifier() == RequestIdentifier.UPLOAD_PICTURE_ID.ordinal()) {
            hideProgressDialog();
            CustomLogger.d("Response image:: " + responseObject.toString());
            getGsonHelper().parse(responseObject.toString(), FileData.class, new OnGsonParseCompleteListener<FileData>() {
                        @Override
                        public void onParseComplete(FileData data) {
                            try {
                                if (data != null) {
                                    if (fileDataList == null) {
                                        fileDataList = new ArrayList<FileData>();
                                    }
                                    fileDataList.add(data);

                                    if (dealImageAdapter != null)
                                        dealImageAdapter.notifyDataSetChanged();
                                    else
                                        setImageAdapter();
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

    }


    private void setDefaultData() {

        String endDateInSlashFromat = CommonUtility.getDateAfterNDays(StaticMap.EXPIRY_DAY);
        endDateTV.setText(endDateInSlashFromat);
        dealData.endDate = CommonUtility.getDateYYYYMMDDFormat(endDateInSlashFromat);

        try {
            String endTime = CommonUtility.getTimeFromTimeDailog(59, 23); // 23 hours 59 minutes
            endTimeTV.setText(endTime);
            dealData.endTime = CommonUtility.getTimeHHmmss(59, 23);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidData() {
        boolean isValidData = true;


        if (productNameET.getText().toString().trim().length() > 0) {
            dealData.productName = productNameET.getText().toString();
        } else {
            productNameTIL.setError(getString(R.string.enter_deal_name));
            return false;
        }

        if (productDescET.getText().toString().trim().length() > 0) {
            dealData.productDescription = productDescET.getText().toString();
        } else {
            productDescET.setError(context.getString(R.string.enter_product_description));
            return false;
        }

        if (termsConditionET.getText().toString().trim().length() > 0) {
            dealData.termsCondition = termsConditionET.getText().toString();
        }

        boolean isValidEndDate = CommonUtility.isValidEndDealDate(dealData.endDate, dealData.endTime);
        if (isValidEndDate) {
            dealData.endDateTime = CommonUtility.getDateTimeYYYYMMDDHHMMSS(dealData.endDate, dealData.endTime);

        } else {
            showError(getString(R.string.invalid_deal_end_date_time), MESSAGETYPE.SNACK_BAR);
            return false;
        }

        if (fileDataList != null && fileDataList.size() > 0) {
            dealData.fileDataList.addAll(fileDataList);
        } else {
            showError(getString(R.string.please_upload_product_picture), MESSAGETYPE.SNACK_BAR);
            return false;
        }

        if (dealData.l3Id == null || dealData.l3Id.equals("0")) {
            showError(getString(R.string.please_select_category), MESSAGETYPE.SNACK_BAR);
            return false;
        }

        if (dealData.l2Id == null || dealData.l2Id.equals("0")) {
            showError(getString(R.string.please_select__sub_category), MESSAGETYPE.SNACK_BAR);
            return false;
        }

        CustomLogger.d("Data Json:: " + new Gson().toJson(dealData));

        return true;
    }


    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {


        if (request.getIdentifier() == RequestIdentifier.GET_CATEGORY_LIST.ordinal()) {
            hideProgressBar();
            hideProgressDialog();


            getGsonHelper().parse(responseObject.toString(), DealCategoryResult.class, new OnGsonParseCompleteListener<DealCategoryResult>() {
                        @Override
                        public void onParseComplete(DealCategoryResult data) {
                            try {
                                if (data != null && data.result.size() > 0) {
                                    for (DealCategory dealCategory : data.result) {
                                        categoryList.add(new SpinnerModel(dealCategory.id + "", dealCategory.name));
                                    }
                                    setCategorySpinner();
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                        }
                    }
            );


        }

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_SUBCATEGORY_LIST.ordinal()) {
            hideProgressBar();
            hideProgressDialog();


            getGsonHelper().parse(responseObject.toString(), DealSubCategoryResult.class, new OnGsonParseCompleteListener<DealSubCategoryResult>() {
                        @Override
                        public void onParseComplete(DealSubCategoryResult data) {
                            try {
                                if (data != null && data.result.size() > 0) {
                                    if (subCategoryList == null) {
                                        subCategoryList = new ArrayList<SpinnerModel>();
                                    } else {
                                        subCategoryList.clear();
                                    }
                                    for (DealSubCategory dealCategory : data.result) {
                                        subCategoryList.add(new SpinnerModel(dealCategory.id + "", dealCategory.name));
                                    }
                                    setSubCategorySpinner();
                                    subCatSpinnerLL.setVisibility(View.VISIBLE);
                                }
                            }catch (Exception exception){
                                CustomLogger.e("Exception ", exception);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                        }
                    }
            );


        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.GET_CATEGORY_LIST.ordinal()) {
            showError();

        }
        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_SUBCATEGORY_LIST.ordinal()) {
            showError();

        }
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.continueBT:
                if (isValidData()) {
                    addToBackStack((BaseActivity) context, NewDealPriceFragment.newInstance(), NewDealPriceFragment.getTagName());
                }
                break;


            case R.id.endDateRL:
                showDateDailog(endDateTV, true, StaticMap.EXPIRY_DAY);
                break;


            case R.id.endTimeRL:
                showTimePickerDailog(endTimeTV);
                break;

        }
    }

    protected void showDateDailog(final TextView textView, boolean isRestricted, int nDays) {
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
                        setDealDate(textView, CommonUtility.getDateYYYYMMDDFormat(date));

                    }
                }, mYear, mMonth, mDay);
        if (isRestricted) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        datePickerDialog.show();

    }

    private void showUploadPicDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dailog_upload_file, null);
        TextView takePicTV = (TextView) view.findViewById(R.id.takePicTV);
        TextView uploadPicTV = (TextView) view.findViewById(R.id.uploadPicTV);

        final Dialog dailog = new Dialog(context);
        dailog.setContentView(view);
        dailog.setCancelable(true);
        dailog.show();

        takePicTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
                openCamera();
            }
        });

        uploadPicTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
                openGallery();
            }
        });

    }


    private void openCamera() {

//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
//            startActivityForResult(cameraIntent, OPEN_CAMERA);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(CommonUtility.getFileDirectory() + File.separator + DEAL_IMAGE_NAME);
        CustomLogger.d("File Pathh :: " + file.getAbsolutePath());
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, OPEN_CAMERA);
    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, OPEN_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            try {
                int documentID = 1;
                if (requestCode == OPEN_GALLERY) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        String selectedImagePath = CommonUtility.getImagePath(getContext(), selectedImageUri);
//                    uploadedImage.setImageURI(selectedImageUri);
                        if ("".equals(selectedImagePath)) {
                            showError();
                            return;
                        }
                        File imageFile = new File(selectedImagePath);
                        imageFile = CommonUtility.saveBitmapToFile(imageFile);

                        if (documentID != -1 && imageFile != null) {
                            uploadDocument(documentID, imageFile);
                        } else {
                            showError();
                        }
                    } else {
                        SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_upload_document));
                    }


                }

                if (requestCode == OPEN_CAMERA) {
                    //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    File file = new File(CommonUtility.getFileDirectory() + File.separator + DEAL_IMAGE_NAME);
                    file = CommonUtility.saveBitmapToFile(file);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bitmap != null) {

                        if (file != null && documentID != -1) {
                            uploadDocument(documentID, file);
                        }
                    } else {
                        SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_take_a_picture));
                    }
                }
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }
        }
    }


    class SpinnerListener implements AdapterView.OnItemSelectedListener {

        private Spinner spinner;

        public SpinnerListener(Spinner spinner) {
            this.spinner = spinner;
        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            switch (spinner.getId()) {

                case R.id.categorySpinner:
                    SpinnerModel selectedItem = (SpinnerModel) adapterView.getItemAtPosition(position);
                    if (!selectedItem.spinnerId.equals(0 + "")) {
                        dealData.l2Id = selectedItem.spinnerId;
                        loadSubCategoryList(selectedItem.spinnerId);
                    } else {
                        subCatSpinnerLL.setVisibility(View.GONE);
                        dealData.l2Id = null;
                        dealData.l3Id = null;
                    }
                    break;

                case R.id.subCatSpinner:
                    SpinnerModel subCategoryModel = (SpinnerModel) adapterView.getItemAtPosition(position);
                    dealData.l3Id = subCategoryModel.spinnerId;
                    break;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.DEAL_PRODUCT_IMAGE) {
            int position = (int) eventObject.objects[0];
            fileDataList.remove(position);
            dealImageAdapter.notifyDataSetChanged();
            CustomLogger.d("Hello position " + position);
        }

        if (eventObject.id == EventConstant.DEAL_UPLOAD_PIC_EVENT) {
            if (PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                showUploadPicDialog();
            } else {
                PermissionUtil.requestCameraPermission((BaseActivity) getActivity());
            }
        }
    }
}
