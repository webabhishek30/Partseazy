package com.partseazy.android.ui.fragments.deals.sell_deal;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.sell.SellDealDetailAdapter;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.model.deal.SkuDealModel;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.deal.deal_detail.DemoRequest;
import com.partseazy.android.ui.model.deal.deal_detail.Image;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.ui.widget.CircleIndicator;
import com.partseazy.android.utility.CommonTextWatcher;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 13/5/17.
 */

public class SellDealDetailFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.appbar)
    protected AppBarLayout appBar;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.collapsingToolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

//    @BindView(R.id.toolbarRecyclerView)
//    protected RecyclerView toolbarRecyclerView;

    @BindView(R.id.viewpager)
    public AutoScrollPager toolBarViewPager;
    @BindView(R.id.indicator)
    public CircleIndicator circleIndicator;


    @BindView(R.id.editDealBT)
    protected Button editDealBT;

    @BindView(R.id.closeDealBT)
    protected Button closeDealBT;

    @BindView(R.id.dealBTLL)
    protected LinearLayout dealBTLL;

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.inActiveLL)
    protected LinearLayout inActiveLL;

    @BindView(R.id.inactiveTV)
    protected TextView inactiveTV;

    @BindView(R.id.closeDealLL)
    protected LinearLayout closeDealLL;


    private EditText descET, mrpET, remainingStockET, minQtyET, priceET;
    private TextInputLayout mrpTIL, priceTIL, remainingStockTIL, minQtyTIL, descTIL;

    private SellDealDetailAdapter sellDealDetailAdapter;
    private ProductBannerAdapter dealBannerAdapter;

    public static final String DEAL_ID = "deal_id";
    public static final String DEAL_NAME = "deal_name";

    private int mDealId;
    private String mDealName;
    private SkuDealModel updatedSKUData;

    private Deal dealDetailHolder;
    private List<String> bannerList;
    private ImageLoader imageLoader;


    public static SellDealDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        SellDealDetailFragment fragment = new SellDealDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SellDealDetailFragment newInstance(int dealId, String dealName) {

        Bundle bundle = new Bundle();
        bundle.putInt(DEAL_ID, dealId);
        bundle.putString(DEAL_NAME, dealName);
        SellDealDetailFragment fragment = new SellDealDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealId = getArguments().getInt(DEAL_ID);
        mDealName = getArguments().getString(DEAL_NAME);
        updatedSKUData = new SkuDealModel();
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();
    }


    public static String getTagName() {
        return SellDealDetailFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (dealDetailHolder != null) {
            setDealAdapter();
            parseBanner();
            setBannerViewPager();
            handleButtonVisibilty();
        } else {
            loadDealDetail();
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sell_deal_detail;
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
        toolbarTextAppernce();
        closeDealBT.setOnClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadDealDetail() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_DETAIL_ID.ordinal(),
                WebServiceConstants.GET_DEAL + mDealId, null, null, this, this, false);

    }


    private void updateDealCall(int isActive) {
        Map<String, Object> paramMap = WebServicePostParams.activeDealParams(isActive);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_SELL_DEAL.ordinal(),
                WebServiceConstants.UPDATE_DEAL + mDealId, paramMap, params, this, this, false);
    }

    private void callUpdateSKU(int skuId, SkuDealModel skuData) {
        Map<String, Object> paramMap = WebServicePostParams.updateDealSKUParams(skuData);
        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_DEAL_SKU_ID.ordinal(),
                WebServiceConstants.UPDATE_DEAL_SKU + skuId, paramMap, null, this, this, false);
    }


    private void setDealAdapter() {
        if (sellDealDetailAdapter == null)
            sellDealDetailAdapter = new SellDealDetailAdapter(SellDealDetailFragment.this, mDealId, dealDetailHolder);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(sellDealDetailAdapter);
    }

    private void setBannerViewPager() {
        dealBannerAdapter = new ProductBannerAdapter(context, bannerList, ProductBannerAdapter.DEAL_SELL_LAUNCH_FOR_YOUTUBE, dealDetailHolder);
        toolBarViewPager.setAdapter(dealBannerAdapter);
        circleIndicator.setViewPager(toolBarViewPager);
    }

    private void toolbarTextAppernce() {
        if (toolbar != null)
            toolbar.setTitle(mDealName);

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                } else {
                    if (getActivity() != null && isAdded()) {
                        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        });
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

    private void handleButtonVisibilty() {
        if (dealDetailHolder.trade.active == 0) {
            inActiveLL.setVisibility(View.VISIBLE);
            closeDealLL.setVisibility(View.GONE);
        } else if (CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealDetailHolder.trade.endingAt) < 0) {
            inActiveLL.setVisibility(View.VISIBLE);
            closeDealLL.setVisibility(View.GONE);
            inactiveTV.setText(getString(R.string.deal_is_over));
        } else {
            closeDealLL.setVisibility(View.VISIBLE);
            inActiveLL.setVisibility(View.GONE);
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
        if (request.getIdentifier() == RequestIdentifier.UPDATE_SELL_DEAL.ordinal()) {

            showError();
        }

        if (request.getIdentifier() == RequestIdentifier.UPDATE_DEAL_SKU_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
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
                    if (getActivity() != null && isAdded()) {
                        dealDetailHolder = dealData;
                        setDealAdapter();
                        parseBanner();
                        setBannerViewPager();
                        handleButtonVisibilty();
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

        if (request.getIdentifier() == RequestIdentifier.UPDATE_DEAL_SKU_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), Sku.class, new OnGsonParseCompleteListener<Sku>() {
                @Override
                public void onParseComplete(Sku skuData) {

                    if (skuData != null) {
                        CustomLogger.d("Update Sku Id  ::" + skuData.stock);
                        dealBannerAdapter = null;
                        sellDealDetailAdapter = null;
                        loadDealDetail();
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

        if (request.getIdentifier() == RequestIdentifier.UPDATE_SELL_DEAL.ordinal()) {
            popToHome(getActivity());
            addToBackStack(getContext(), DealHomeFragment.newInstance(true), DealHomeFragment.class.getSimpleName());
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == closeDealBT.getId()) {
            if (getActivity() != null && isAdded()) {
                DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.deal_close_msg), getString(R.string.YES)
                        , null, null, new DialogListener() {
                            @Override
                            public void onPositiveButton(DialogInterface dialog) {
                                updateDealCall(0);
                            }
                        });
            }
        }
    }


    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);


        if (eventObject.id == EventConstant.EDIT_DEAL_SKU) {
            Sku skuItem = (Sku) eventObject.objects[0];
            if (getActivity() != null && isAdded()) {
                showEditSkuDailog(skuItem);
            }
        }

        if (eventObject.id == EventConstant.SHOW_MEETING_DAILOG) {
            DemoRequest demoRequest = (DemoRequest) eventObject.objects[0];
            if (getActivity() != null && isAdded()) {
                showMeetingAddressDailog(demoRequest);
            }
        }
    }


    private void showMeetingAddressDailog(final DemoRequest demoRequest) {

        View view = context.getLayoutInflater().inflate(R.layout.dailog_meeting_detail, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        final AlertDialog dailogSKU = dialog.show();

        ImageView crossIV = (ImageView) view.findViewById(R.id.crossIV);
        TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        TextView mobileTV = (TextView) view.findViewById(R.id.mobileTV);
        TextView addressTV = (TextView) view.findViewById(R.id.addressTV);
        NetworkImageView cardIV = (NetworkImageView) view.findViewById(R.id.cardIV);
        LinearLayout addressLL = (LinearLayout) view.findViewById(R.id.addressLL);

        nameTV.setText(getString(R.string.str, demoRequest.name));

        Spannable wordtoSpan = new SpannableString(getString(R.string.str, demoRequest.mobile));
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mobileTV.setText(wordtoSpan);

        if (demoRequest.address != null && !demoRequest.address.equals("")) {
            addressTV.setText(getString(R.string.str, demoRequest.address));
            addressLL.setVisibility(View.VISIBLE);
        } else {
            addressLL.setVisibility(View.GONE);
        }

        if (demoRequest.image != null) {
            cardIV.setVisibility(View.VISIBLE);
            String formatedURL = CommonUtility.getFormattedImageUrl(demoRequest.image.src, cardIV, CommonUtility.IMGTYPE.HALFIMG);
            CustomLogger.d("The formatted URL is  " + formatedURL);
            CommonUtility.setImageSRC(imageLoader, cardIV, formatedURL);
        } else {
            cardIV.setVisibility(View.GONE);
        }

        mobileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.dialPhoneNumber(getActivity(), demoRequest.mobile);
            }
        });

        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailogSKU.dismiss();
            }
        });

    }

    private void showEditSkuDailog(final Sku skuItem) {

        View view = context.getLayoutInflater().inflate(R.layout.dailog_deal_sku_edit, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        final AlertDialog dailogSKU = dialog.show();

        descET = (EditText) view.findViewById(R.id.descET);
        mrpET = (EditText) view.findViewById(R.id.mrpET);
        remainingStockET = (EditText) view.findViewById(R.id.remainingStockET);
        minQtyET = (EditText) view.findViewById(R.id.minQtyET);
        priceET = (EditText) view.findViewById(R.id.priceET);
        Button saveBTN = (Button) view.findViewById(R.id.saveBTN);
        TextView stockSoldTV = (TextView) view.findViewById(R.id.stockSoldTV);


        mrpTIL = (TextInputLayout) view.findViewById(R.id.mrpTIL);
        priceTIL = (TextInputLayout) view.findViewById(R.id.priceTIL);
        remainingStockTIL = (TextInputLayout) view.findViewById(R.id.remainingStockTIL);
        minQtyTIL = (TextInputLayout) view.findViewById(R.id.minQtyTIL);
        descTIL = (TextInputLayout) view.findViewById(R.id.descTIL);


        priceET.addTextChangedListener(new CommonTextWatcher(priceET, priceTIL));
        mrpET.addTextChangedListener(new CommonTextWatcher(mrpET, mrpTIL));
        remainingStockET.addTextChangedListener(new CommonTextWatcher(remainingStockET, remainingStockTIL));
        minQtyET.addTextChangedListener(new CommonTextWatcher(minQtyET, minQtyTIL));

        descET.setText(skuItem.desc);
        mrpET.setText(CommonUtility.convertionPaiseToRupee(skuItem.mrp) + "");
        priceET.setText(CommonUtility.convertionPaiseToRupee(skuItem.price) + "");
        remainingStockET.setText(skuItem.remaining + "");
        minQtyET.setText(skuItem.minQty + "");
        stockSoldTV.setText(skuItem.sold + " SKU's have already been sold");

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData(skuItem)) {
                    dailogSKU.dismiss();
                    callUpdateSKU(skuItem.id, updatedSKUData);
                }
            }
        });

    }


    private boolean isValidData(Sku skuItem) {
        String desc = descET.getText().toString().trim();
        String mrp = mrpET.getText().toString().trim();
        String remainingStock = remainingStockET.getText().toString().trim();
        String minQty = minQtyET.getText().toString().trim();
        String price = priceET.getText().toString().trim();

        if (desc != null && !desc.equals("")) {
            updatedSKUData.desc = desc;
        } else {
            descTIL.setError(getString(R.string.enter_product_description));
            return false;
        }

        if (mrp != null && !mrp.equals("") && !mrp.equals("0")) {
            int mrpInt = Integer.parseInt(mrp);
            updatedSKUData.mrp = mrpInt * 100;
        } else {
            mrpTIL.setError(getString(R.string.err_mrp_valid));
            return false;
        }

        if (price != null && !price.equals("") && !price.equals("0")) {
            int priceInt = Integer.parseInt(price);
            updatedSKUData.price = priceInt * 100;
        } else {
            priceTIL.setError(getString(R.string.enter_price));
            return false;
        }

        if (remainingStock != null && !remainingStock.equals("")) {
            updatedSKUData.remainingQty = Integer.parseInt(remainingStock);
        } else {
            remainingStockTIL.setError(getString(R.string.err_remaining_stock_valid));
            return false;
        }

        if (minQty != null && !minQty.equals("") && !minQty.equals("0")) {
            updatedSKUData.minQty = Integer.parseInt(minQty);
        } else {
            if (updatedSKUData.remainingQty == 0) {
                updatedSKUData.minQty = skuItem.minQty;
            } else {
                minQtyTIL.setError(getString(R.string.err_quantity_valid));
                return false;
            }
        }


        if (updatedSKUData.price > updatedSKUData.mrp) {
            mrpTIL.setError(getString(R.string.err_mrp_price));
            return false;
        }

        updatedSKUData.stock = updatedSKUData.remainingQty + skuItem.sold;
        if ((updatedSKUData.remainingQty > 0) && (updatedSKUData.minQty > updatedSKUData.remainingQty)) {
            minQtyTIL.setError(getString(R.string.err_qty_deal));
            return false;
        }

        if (updatedSKUData.stock < skuItem.sold) {
            remainingStockTIL.setError(getString(R.string.stock_cannot_less_sold));
            return false;
        }


        return true;
    }


}

