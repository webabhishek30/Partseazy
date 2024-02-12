package com.partseazy.android.ui.fragments.deals.booking_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.booking.BookingDealAdapter;
import com.partseazy.android.ui.adapters.product.ProductBannerAdapter;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.model.deal.FinalDealSKU;
import com.partseazy.android.ui.model.deal.booking.BookingData;
import com.partseazy.android.ui.model.deal.booking.BookingItem;
import com.partseazy.android.ui.model.deal.booking.BookingResult;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 1/6/17.
 */

public class CreateBookingDealFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.scrollable)
    protected RecyclerView bookingRV;

    @BindView(R.id.createDealBT)
    protected Button createDealBT;

    public static String DEAL_HOLDER = "deal_holder";
    public static String SELECTED_SKU_MAP = "sku_map";
    private static final String LAUNCH_SCREEN_NAME = "launchScreenName";
    private final static String ADDRESS = "address";

    private Map<Integer, FinalDealSKU> selectedSKUMap;


    private BookingDealAdapter bookingDealAdapter;

    private Deal dealDetailHolder;
    private ShippingAddressDetail addressDetails;

    private int totalPayment = 0;
    private String shippingMethod = null;
    private List<BookingItem> bookingItemList;
    private String launchScreenName;


    public static CreateBookingDealFragment newInstance() {
        Bundle bundle = new Bundle();
        CreateBookingDealFragment fragment = new CreateBookingDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CreateBookingDealFragment newInstance(String dealdetailHolder, String selectedMap) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DEAL_HOLDER, dealdetailHolder);
        bundle.putString(SELECTED_SKU_MAP, selectedMap);
        CreateBookingDealFragment fragment = new CreateBookingDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CreateBookingDealFragment newInstance(String dealdetailHolder, String selectedMap, ShippingAddressDetail addressDetails) {
        Bundle bundle = new Bundle();
        bundle.putString(DEAL_HOLDER, dealdetailHolder);
        bundle.putString(SELECTED_SKU_MAP, selectedMap);
        bundle.putSerializable(ADDRESS, (Serializable) addressDetails);
        CreateBookingDealFragment fragment = new CreateBookingDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CreateBookingDealFragment newInstance(String dealdetailHolder, String selectedMap, ShippingAddressDetail addressDetails, String launchScreen) {
        Bundle bundle = new Bundle();
        bundle.putString(DEAL_HOLDER, dealdetailHolder);
        bundle.putString(SELECTED_SKU_MAP, selectedMap);
        bundle.putSerializable(ADDRESS, (Serializable) addressDetails);
        bundle.putString(LAUNCH_SCREEN_NAME, launchScreen);
        CreateBookingDealFragment fragment = new CreateBookingDealFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String detailDetailJson = getArguments().getString(DEAL_HOLDER);
        String selectedSkuMap = getArguments().getString(SELECTED_SKU_MAP);
        launchScreenName = getArguments().getString(LAUNCH_SCREEN_NAME, null);


        dealDetailHolder = new Gson().fromJson(detailDetailJson, Deal.class);

        Type dealSku = new TypeToken<Map<String, FinalDealSKU>>() {
        }.getType();
        selectedSKUMap = new Gson().fromJson(selectedSkuMap, dealSku);

        addressDetails = (ShippingAddressDetail) getArguments().getSerializable(ADDRESS);
        bookingItemList = new ArrayList<>();
        parseSelectSKU();

    }


    public static String getTagName() {
        return BuyDealDetailFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_booking_deal;
    }


    @Override
    protected String getFragmentTitle() {
        return getString(R.string.review_booking);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDealAdapter();
        createDealBT.setOnClickListener(this);

    }


    private void setDealAdapter() {
        if (bookingDealAdapter == null)
            bookingDealAdapter = new BookingDealAdapter(context, dealDetailHolder, selectedSKUMap, addressDetails);
        bookingRV.setLayoutManager(new LinearLayoutManager(context));
        bookingRV.setAdapter(bookingDealAdapter);
    }


    private void createDealCall() {
        Map<String, Object> paramMap = WebServicePostParams.createBookingParams(addressDetails.id, shippingMethod, bookingItemList);

        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);

        getNetworkManager().PostRequest(RequestIdentifier.CREATE_BOOKING_ID.ordinal(),
                WebServiceConstants.POST_BOOKING_DEAL + dealDetailHolder.trade.trin, paramMap, params, this, this, false);
    }


    private void launchThankYouBooking(BookingResult bookingResult) {
        totalPayment = bookingDealAdapter.getTotalAmount();
        addToBackStack((BaseActivity) getActivity(), ThankYouBookingFragment.newInstance(new Gson().toJson(bookingResult), new Gson().toJson(dealDetailHolder)), ThankYouBookingFragment.getTagName());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == createDealBT.getId()) {
            createDealCall();
        }
    }

    @Override
    public void onEvent(EventObject eventObject) {


        if (eventObject.id == EventConstant.SELECT_PICK_UP || eventObject.id == EventConstant.SELECT_DROP_SHIP) {
            shippingMethod = (String) eventObject.objects[0];
        }

    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());
        if (request.getIdentifier() == RequestIdentifier.CREATE_BOOKING_ID.ordinal()) {
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

        if (request.getIdentifier() == RequestIdentifier.CREATE_BOOKING_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), BookingData.class, new OnGsonParseCompleteListener<BookingData>() {
                @Override
                public void onParseComplete(BookingData bookingData) {
                    if (bookingData.result != null) {
                        launchThankYouBooking(bookingData.result);
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


    private void parseSelectSKU() {

        for (Map.Entry<Integer, FinalDealSKU> skuEntry : selectedSKUMap.entrySet()) {
            BookingItem bookingItem = new BookingItem();
            FinalDealSKU finalDealSKU = skuEntry.getValue();
            bookingItem.skuId = finalDealSKU.skuId;
            bookingItem.qty = finalDealSKU.selectedQty;
            bookingItemList.add(bookingItem);
        }
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();


        if (launchScreenName != null && launchScreenName.equals(ProductBannerAdapter.DEAL_BUY_LAUNCH_FOR_YOUTUBE)) {
            addToBackStack((BaseActivity) getActivity(), BuyDealDetailFragment.newInstance(dealDetailHolder.trade.id, dealDetailHolder.trade.name), BuyDealDetailFragment.getTagName());
            return false;
        }
        return true;

    }
}
