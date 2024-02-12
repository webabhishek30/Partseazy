package com.partseazy.android.ui.fragments.deals.buy_deal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.MultipartRequestMap;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.buy.BuyDealDetailAdapter;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.deals.booking_deal.CreateBookingDealFragment;
import com.partseazy.android.ui.fragments.shippingaddress.ShippingAddressFragment;
import com.partseazy.android.ui.model.common.SuccessResponse;
import com.partseazy.android.ui.model.deal.FileData;
import com.partseazy.android.ui.model.deal.FinalDealSKU;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.deal.deal_detail.Image;
import com.partseazy.android.ui.model.deal.demo.DemoData;
import com.partseazy.android.ui.model.deal.demo.DemoPostData;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressList;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.ui.widget.CircleIndicator;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonTextWatcher;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.partseazy.android.R.id.collapsingToolbar;

/**
 * Created by naveen on 29/5/17.
 */

public class BuyDealDetailFragment extends BaseFragment implements View.OnClickListener, Response.StringListener {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.appbar)
    protected AppBarLayout appBar;

    @BindView(collapsingToolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.viewpager)
    public AutoScrollPager toolBarViewPager;
    @BindView(R.id.indicator)
    public CircleIndicator circleIndicator;


    @BindView(R.id.editDealBT)
    protected Button editDealBT;

    @BindView(R.id.bookDealBT)
    protected Button bookDealBT;

    @BindView(R.id.dealBTLL)
    protected LinearLayout dealBTLL;

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.buttonLL)
    protected LinearLayout buttonLL;

    private BuyDealDetailAdapter buyDealDetailAdapter;
    private ProductBannerAdapter dealBannerAdapter;
    private Boolean launchFromApp = false;


    public static final String DEAL_ID = "deal_id";
    public static final String DEAL_NAME = "deal_name";
    public static final String DEAL_LAUNCH = "deal_launch";

    private static final String VISITNG_CARD_IMAGE_NAME = "visting_card.jpg";

    public static final int OPEN_CAMERA = 1;

    private int mDealId;
    private String mDealName;

    private List<String> bannerList;
    private Deal dealDetailHolder;

    private Map<Integer, FinalDealSKU> selectedSKUMap;

    private EditText nameET, mobileET, addressDescET;
    private TextInputLayout nameTIL, mobileTIL;
    private RelativeLayout cardPicRL;
    private LinearLayout uploadLL;
    private NetworkImageView cardImageView;
    private ImageLoader imageLoader;

    private DemoPostData demoPostData;


    public static BuyDealDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        BuyDealDetailFragment fragment = new BuyDealDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BuyDealDetailFragment newInstance(int dealId, String dealName) {

        Bundle bundle = new Bundle();
        bundle.putInt(DEAL_ID, dealId);
        bundle.putString(DEAL_NAME, dealName);
        BuyDealDetailFragment fragment = new BuyDealDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BuyDealDetailFragment newInstance(int dealId, String dealName, boolean launchFromApp) {

        Bundle bundle = new Bundle();
        bundle.putInt(DEAL_ID, dealId);
        bundle.putString(DEAL_NAME, dealName);
        bundle.putBoolean(DEAL_LAUNCH, launchFromApp);
        BuyDealDetailFragment fragment = new BuyDealDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealId = getArguments().getInt(DEAL_ID);
        mDealName = getArguments().getString(DEAL_NAME);
        launchFromApp = getArguments().getBoolean(DEAL_LAUNCH, false);
        selectedSKUMap = new HashMap<>();
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        demoPostData = new DemoPostData();
    }


    public static String getTagName() {
        return BuyDealDetailFragment.class.getSimpleName();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (dealDetailHolder != null) {
            setDealAdapter();
            setBannerViewPager();
            buttonLL.setVisibility(View.VISIBLE);
        } else {
            loadDealDetail();
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_buy_deal_detail;
    }


    @Override
    protected String getFragmentTitle() {
        return mDealName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        toolbarAppernce();
        callTradeMetricsApi(mDealId);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookDealBT.setOnClickListener(this);
        editDealBT.setOnClickListener(this);

    }

    private void loadDealDetail() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_DETAIL_ID.ordinal(),
                WebServiceConstants.GET_DEAL + mDealId, null, null, this, this, false);

    }

    private void callShippingAddressList() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.SHIPPING_ADDRESS_LIST_ID.ordinal(),
                WebServiceConstants.GET_ADDRESS_LIST, null, null, this, this, false);
    }

    private void callAllowbooking() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_ALLOW_BOOKING.ordinal(),
                WebServiceConstants.GET_ALLOW_BOOKING + dealDetailHolder.trade.trin, null, null, this, this, false);
    }

    private void callTradeMetricsApi(int tradeId) {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        Map<String, Object> requestParams = WebServicePostParams.dealViewOpenMetricsParams(tradeId, false);
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_DEAL_OPEN_METRICS.ordinal(), WebServiceConstants.UPDATE_DEAL_METRICS, requestParams, params, this, this, false);
    }

    private void callPostDemoMeeting() {
        showProgressDialog();
        MultipartRequestMap params = WebServicePostParams.postDemoMeeting(demoPostData);
        getNetworkManager().MultipartRequest(RequestIdentifier.DEMO_MEETING_ID.ordinal(), WebServiceConstants.POST_DEMO_MEETING, params, null, this, this, false);

    }


    private void uploadPicture(int documentId, final File file) {
        demoPostData.file = file;
        showProgressDialog(getString(R.string.upload_photo), false);
        MultipartRequestMap params = WebServicePostParams.onBoardShop(documentId, file, "meeting_visiting_card");
        RequestParams requestParams = new RequestParams();
        requestParams.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(requestParams.headerMap);
        getNetworkManager().MultipartRequest(RequestIdentifier.TAKE_CARD_PIC_ID.ordinal(), WebServiceConstants.FILE_UPLOAD, params, requestParams, this, this, false);
    }


    private void setDealAdapter() {

        if (buyDealDetailAdapter == null)
            buyDealDetailAdapter = new BuyDealDetailAdapter(BuyDealDetailFragment.this, dealDetailHolder);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(buyDealDetailAdapter);
        selectedSKUMap = new HashMap<>();
    }

    private void setBannerViewPager() {
        dealBannerAdapter = new ProductBannerAdapter(context, bannerList, ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE, dealDetailHolder);
        toolBarViewPager.setAdapter(dealBannerAdapter);
        circleIndicator.setViewPager(toolBarViewPager);
    }

    private void toolbarAppernce() {
        try {
            if (toolbar != null)
                toolbar.setTitle(mDealName);

            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

            if (getActivity() != null && isAdded()) {
                appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (getActivity() != null && isAdded()) {
                            if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                            } else {
                                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            CustomLogger.e(e.toString());
        }
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());
        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_DETAIL_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        if (request.getIdentifier() == RequestIdentifier.UPDATE_DEAL_OPEN_METRICS.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        if (request.getIdentifier() == RequestIdentifier.DEMO_MEETING_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }
        if (request.getIdentifier() == RequestIdentifier.GET_ALLOW_BOOKING.ordinal()) {
            if (apiError != null)
                DialogUtil.showAlertDialog(getActivity(), true, apiError.message, apiError.issue, "OK", null, null, null);
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

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_DETAIL_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), Deal.class, new OnGsonParseCompleteListener<Deal>() {
                @Override
                public void onParseComplete(Deal dealData) {
                    dealDetailHolder = dealData;
                    setDealAdapter();
                    parseBanner();
                    setBannerViewPager();
                    buttonLL.setVisibility(View.VISIBLE);
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

        if (request.getIdentifier() == RequestIdentifier.UPDATE_DEAL_OPEN_METRICS.ordinal()) {
            //Nothing to do

        }

        if (request.getIdentifier() == RequestIdentifier.GET_ALLOW_BOOKING.ordinal()) {
            hideProgressDialog();
            hideProgressBar();
            getGsonHelper().parse(responseObject.toString(), SuccessResponse.class, new OnGsonParseCompleteListener<SuccessResponse>() {
                        @Override
                        public void onParseComplete(SuccessResponse data) {
                            hideProgressDialog();
                            if (data.success == 1) {
                                callShippingAddressList();
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            APIError er = new APIError();
                            er.message = exception.getMessage();

                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.SHIPPING_ADDRESS_LIST_ID.ordinal()) {
            hideProgressDialog();
            hideProgressBar();
            getGsonHelper().parse(responseObject.toString(), ShippingAddressList.class, new OnGsonParseCompleteListener<ShippingAddressList>() {
                        @Override
                        public void onParseComplete(ShippingAddressList data) {
                            if (data.result.size() == 0) {
                                launchAddressFragment();
                            } else {
                                launchCreateBookingDeal(data.result.get(0));
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {

                            APIError er = new APIError();
                            er.message = exception.getMessage();
                        }
                    }
            );
        }

        return true;
    }

    @Override
    public void onStringResponse(Request<String> request, String responseObject, Response<String> response) {

        if (request.getIdentifier() == RequestIdentifier.TAKE_CARD_PIC_ID.ordinal()) {
            hideProgressDialog();
            CustomLogger.d("Response image CARD:: Ta " + responseObject.toString());

            getGsonHelper().parse(responseObject.toString(), FileData.class, new OnGsonParseCompleteListener<FileData>() {
                        @Override
                        public void onParseComplete(FileData data) {

                            if (data != null) {
                                demoPostData.imageFile = new FileData();
                                demoPostData.imageFile.fileId = data.fileId;
                                demoPostData.imageFile.src = data.src;


                                String formatedURL = CommonUtility.getFormattedImageUrl(data.src, cardImageView, CommonUtility.IMGTYPE.THUMBNAILIMG);
                                CustomLogger.d("Image url :: " + formatedURL);
                                CommonUtility.setImageSRC(imageLoader, cardImageView, formatedURL);
                                cardPicRL.setVisibility(View.VISIBLE);
                                uploadLL.setVisibility(View.GONE);
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

        if (request.getIdentifier() == RequestIdentifier.DEMO_MEETING_ID.ordinal()) {

            hideProgressDialog();
            CustomLogger.d("Response IMage  :: " + responseObject.toString());
            getGsonHelper().parse(responseObject.toString(), DemoData.class, new OnGsonParseCompleteListener<DemoData>() {

                        @Override
                        public void onParseComplete(DemoData data) {

                            if (getActivity() != null && isAdded() && data != null) {
                                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.demo_acknowledgment_msg), getString(R.string.OK)
                                        , null, null, new DialogListener() {
                                            @Override
                                            public void onPositiveButton(DialogInterface dialog) {
                                            }
                                        });


                            }

                        }

                        @Override
                        public void onParseFailure(Exception exception) {

                            APIError er = new APIError();
                            er.message = exception.getMessage();

                        }
                    }
            );
        }


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == bookDealBT.getId()) {
            if (selectedSKUMap.size() > 0) {
                //  callAllowbooking();
                callShippingAddressList();
            } else {
                Toast.makeText(context, context.getString(R.string.please_add_skus), Toast.LENGTH_SHORT).show();

            }
        }

        if (v.getId() == editDealBT.getId()) {
            this.showProgressDialog();
            ChatUtility chat = new ChatUtility(this, dealDetailHolder.user.id,
                    dealDetailHolder.user.name, dealDetailHolder.trade.images.get(0).src,
                    dealDetailHolder.trade.name, dealDetailHolder.trade.trin + "");
            chat.startChatting();
        }

    }

    @Override
    public void onEvent(EventObject eventObject) {


        if (eventObject.id == EventConstant.SELECT_DEAL_SKU) {
            Sku skuItem = (Sku) eventObject.objects[0];
            int quantity = (Integer) eventObject.objects[1];

            updateSelectedMap(skuItem, quantity);
            CustomLogger.d("Selected MAp " + selectedSKUMap.size());

        }

        if (eventObject.id == EventConstant.DEMO_MEETING_DEAL) {
            showMeetingAddressDailog();
        }

    }

    private void parseBanner() {
        if (bannerList != null)
            bannerList.clear();
        else
            bannerList = new ArrayList<>();

        if (dealDetailHolder.trade.images != null && dealDetailHolder.trade.images.size() > 0) {
            for (Image dealImage : dealDetailHolder.trade.images) {
                bannerList.add(dealImage.src);
            }
        }

    }


    private void launchAddressFragment() {
        ShippingAddressFragment fragment = ShippingAddressFragment.newInstance(true, new Gson().toJson(dealDetailHolder), new Gson().toJson(selectedSKUMap));
        addToBackStack((BaseActivity) getActivity(), fragment, ShippingAddressFragment.getTagName());
    }

    private void launchCreateBookingDeal(ShippingAddressDetail shippingAddressDetail) {

        if (dealDetailHolder.info != null && dealDetailHolder.info.youtubeId != null && !dealDetailHolder.info.youtubeId.equals("")) {
            removeTopAndAddToBackStack((BaseActivity) context, CreateBookingDealFragment.newInstance(new Gson().toJson(dealDetailHolder), new Gson().toJson(selectedSKUMap), shippingAddressDetail, ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE), CreateBookingDealFragment.getTagName());
        } else {
            addToBackStack((BaseActivity) context, CreateBookingDealFragment.newInstance(new Gson().toJson(dealDetailHolder), new Gson().toJson(selectedSKUMap), shippingAddressDetail), CreateBookingDealFragment.getTagName());
        }

    }


    private void updateSelectedMap(Sku sku, int quantity) {
        FinalDealSKU finalDealSKU = new FinalDealSKU();
        finalDealSKU.skuId = sku.id;
        finalDealSKU.price = sku.price;
        finalDealSKU.selectedQty = quantity;
        selectedSKUMap.put(finalDealSKU.skuId, finalDealSKU);
        if (finalDealSKU.selectedQty == 0) {
            selectedSKUMap.remove(finalDealSKU.skuId);
        }


    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        if (!launchFromApp) {
            addToBackStack((BaseActivity) getActivity(), DealHomeFragment.newInstance(), DealHomeFragment.getTagName());
            return false;
        }
        return true;

    }

    private void showMeetingAddressDailog() {

        View view = context.getLayoutInflater().inflate(R.layout.dailog_deal_meeting_address, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        final AlertDialog dailogSKU = dialog.show();

        nameET = (EditText) view.findViewById(R.id.nameET);
        mobileET = (EditText) view.findViewById(R.id.mobileET);

        mobileTIL = (TextInputLayout) view.findViewById(R.id.mobileTIL);
        nameTIL = (TextInputLayout) view.findViewById(R.id.nameTIL);

        addressDescET = (EditText) view.findViewById(R.id.addressDescET);

        ImageView crossIV = (ImageView) view.findViewById(R.id.crossIV);
        ImageView cardCrossIV = (ImageView) view.findViewById(R.id.cardCrossIV);
        Button saveBTN = (Button) view.findViewById(R.id.saveBTN);
        uploadLL = (LinearLayout) view.findViewById(R.id.uploadLL);

        cardPicRL = (RelativeLayout) view.findViewById(R.id.cardPicRL);
        cardImageView = (NetworkImageView) view.findViewById(R.id.iconIV);


        nameET.setText(DataStore.getUserName(context));
        mobileET.setText(DataStore.getUserPhoneNumber(context));

        nameET.addTextChangedListener(new CommonTextWatcher(nameET, nameTIL));
        mobileET.addTextChangedListener(new CommonTextWatcher(mobileET, mobileTIL));

        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailogSKU.dismiss();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData()) {
                    dailogSKU.dismiss();
                    callPostDemoMeeting();
                }
            }
        });

        uploadLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                    openCamera();
                } else {
                    PermissionUtil.requestCameraPermission((BaseActivity) getActivity());
                }
            }
        });

        cardCrossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadLL.setVisibility(View.VISIBLE);
                cardPicRL.setVisibility(View.GONE);
            }
        });


    }


    private boolean isValidData() {
        boolean isValidData = true;

        demoPostData.tradeId = dealDetailHolder.trade.id;

        if (addressDescET.getText().toString().trim().length() > 0) {
            demoPostData.address = addressDescET.getText().toString();
        }

        if (nameET.getText().toString().trim().length() > 0) {
            demoPostData.name = nameET.getText().toString();
        } else {
            nameTIL.setError(context.getString(R.string.err_name_edittext));
            return false;
        }


        String mobileNumber = mobileET.getText().toString().trim();
        if (CommonUtility.isValidMobileNumber(mobileNumber, false, 10, 10)) {
            demoPostData.mobile = mobileET.getText().toString();
        } else {
            mobileTIL.setError(context.getString(R.string.enter_valid_mobile));
            return false;
        }

        if ((demoPostData.file == null) && (demoPostData.address == null || demoPostData.address.equals(""))) {
            showError(context.getString(R.string.either_upload_pic_or_address), MESSAGETYPE.SNACK_BAR);
            return false;
        }


        return isValidData;
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(CommonUtility.getFileDirectory() + File.separator + VISITNG_CARD_IMAGE_NAME);
        CustomLogger.d("File Pathh :: " + file.getAbsolutePath());
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, OPEN_CAMERA);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            int documentID = 1;
            if (requestCode == OPEN_CAMERA) {
                //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                File file = new File(CommonUtility.getFileDirectory() + File.separator + VISITNG_CARD_IMAGE_NAME);
                file = CommonUtility.saveBitmapToFile(file);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {

                    if (file != null && documentID != -1) {
                        uploadPicture(documentID, file);
                    }
                } else {
                    SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_take_a_picture));
                }
            }

        }
    }

}
