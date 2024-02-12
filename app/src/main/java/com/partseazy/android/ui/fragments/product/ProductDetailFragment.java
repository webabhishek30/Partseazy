package com.partseazy.android.ui.fragments.product;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.analytics.Tracker;
import com.partseazy.android.analytics.TrackerConstants;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.product.ProductDetailFragmentAdapter;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.model.addtocart.AddToCartResponse;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.fav.FavResultData;
import com.partseazy.android.ui.model.productdetail.Product;
import com.partseazy.android.ui.model.productdetail.Product_;
import com.partseazy.android.ui.widget.ChangeScrollListener;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by naveen on 23/12/16.
 */

public class ProductDetailFragment extends BaseFragment implements OnClickListener, ProductDetailFragmentAdapter.FavouriteListener {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @BindView(R.id.gotoBagLL)
    protected LinearLayout gotoBagLL;

    @BindView(R.id.gotToBagBT)
    protected Button gotToBagBT;

    @BindView(R.id.notActiveLL)
    protected LinearLayout notActiveLL;

    @BindView(R.id.inquireLL)
    protected LinearLayout inquireLL;

    @BindView(R.id.inquireBTN)
    protected Button inquireBTN;

    @BindView(R.id.notActiveTV)
    protected TextView notActiveTV;

    @BindView(R.id.productContainerView)
    protected RelativeLayout productContainerView;

    private Menu menu;

    private ProductDetailFragmentAdapter productDetailFragmentAdapter;
    private ProductPriceEditDialog productPriceEditDialog;
    private ProductAttributeDialog productAttributeDialog;
    public static final String SELECTED_PRODUCT_ID = "prod_id";
    public static final String SELECTED_PRODUCT_NAME = "prod_nAME";
    public static final String SELECTED_PRODUCT_SKU_ID = "prod_sku_id";

    private Product productDetailHolder;

    AppEventsLogger logger ;

    private int productSkuId = 0;
    private int mProductID;
    private String mProductName;
    private boolean isCartLaunch = false;
    private Product_ sampleProduct;
    boolean isSaleable = false;
    boolean showDailogForDealSku = false;


    public enum ATTRIBUTE_VIEW {

        ONETAB_ONECOLUM, ONETAB_TWOCOLUMN, TWOTAB_ONECOLUM, NOTAB_ONECOLUMN, NOTAB_TWOCOLUMN, NOTAB_NOCOLUMN

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductID = getArguments().getInt(SELECTED_PRODUCT_ID, mProductID);
        mProductName = getArguments().getString(SELECTED_PRODUCT_NAME, "");
        productSkuId = getArguments().getInt(SELECTED_PRODUCT_SKU_ID, productSkuId);

        //Moengage Category data
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt(MoengageConstant.Events.PRODUCT_ID, mProductID)
                .putAttrString(MoengageConstant.Events.PAGE,"product_detail_page")
                .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"productDetailFragment")
                .putAttrString(MoengageConstant.Events.NAME,mProductName)
                .putAttrInt(MoengageConstant.Events.SKU,productSkuId);

        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.PRODUCT_DETAIL_VIEWED, builder);
        //init facebook looger
        logger = AppEventsLogger.newLogger(context);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (productDetailHolder != null) {
            setPDPAdapter();
        } else {
            loadPDP();
        }
    }

    public static ProductDetailFragment newInstance(Integer productId, String productName) {
        Bundle bundle = new Bundle();
        bundle.putInt(ProductDetailFragment.SELECTED_PRODUCT_ID, productId);
        bundle.putString(ProductDetailFragment.SELECTED_PRODUCT_NAME, productName);

        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProductDetailFragment newInstance(Integer productId, String productName, Integer productSkuId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ProductDetailFragment.SELECTED_PRODUCT_ID, productId);
        bundle.putString(ProductDetailFragment.SELECTED_PRODUCT_NAME, productName);
        bundle.putInt(ProductDetailFragment.SELECTED_PRODUCT_SKU_ID, productSkuId);

        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_details;
    }

    @Override
    protected String getFragmentTitle() {
        return mProductName;
    }

    public static String getTagName() {
        return ProductDetailFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        setHasOptionsMenu(true);
        hideKeyBoard(getView());
        KeyPadUtility.hideSoftKeypad(getActivity(), getView());
        showTransparentToolbar();
        gotToBagBT.setOnClickListener(this);
        inquireBTN.setOnClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void loadPDP() {

        showProgressBar();

        String productDetailUrl = WebServiceConstants.GET_PRODUCT + mProductID;
        if (productSkuId > 0) {
            productDetailUrl = productDetailUrl + "?product_id=" + productSkuId;
        }
        getNetworkManager().GetRequest(RequestIdentifier.GET_PRODUCT_ID.ordinal(),
                productDetailUrl, null, null, this, this, false);

    }

    private void callAddtoCart(List<Integer> selectedProductSKUIds, List<Integer> selectedProductQuantity) {

        showProgressDialog();
        getNetworkManager().PutRequest(RequestIdentifier.ADD_TO_CART.ordinal(),
                WebServiceConstants.PUT_ADD_MULTI_TO_CART, WebServicePostParams.productsToAddinCart(selectedProductSKUIds, selectedProductQuantity), null, this, this, false);

        //Moengage Category data
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(MoengageConstant.Events.SKU, selectedProductSKUIds.toString())
                .putAttrString(MoengageConstant.Events.NAME,mProductName)
                .putAttrString(MoengageConstant.Events.QUANTITY,selectedProductQuantity.toString())
                .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"ADD TO CART");

        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.ADD_TO_CART, builder);
    }

    private void callAddProductToCart(int selectedProductSKUIds, int selectedProductQuantity, boolean isSample) {

        // showProgressBar();
        showProgressDialog();
        getNetworkManager().PutRequest(RequestIdentifier.ADD_PRODUCT_TO_CART.ordinal(),
                WebServiceConstants.PUT_ADD_TO_CART, WebServicePostParams.productToAddinCart(selectedProductSKUIds, selectedProductQuantity, isSample), null, this, this, false);
        //Moengage Category data
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt(MoengageConstant.Events.SKU, selectedProductSKUIds)
                .putAttrInt(MoengageConstant.Events.QUANTITY,selectedProductQuantity)
                .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"ADD PRODUCT TO CART");

        MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.ADD_PRODUCT_TO_CART, builder);
    }


    private void registerSupplierChat(String supplierId) {

        showProgressDialog();
        Map<String, Object> requestParams = new HashMap<>();

        getNetworkManager().PostRequest(RequestIdentifier.REGSITER_SELLER_CHAT.ordinal(),
                WebServiceConstants.REGISTER_SELLER_CHAT + supplierId + WebServiceConstants.REGSITER_SELLER_CHAT2, requestParams, null, this, this, false);

    }

//    private void callAddToFavourite(int productMasterId) {
//        RequestParams params = new RequestParams();
//        params.headerMap = new HashMap<>();
//        WebServicePostParams.addResultWrapHeader(params.headerMap);
//        showProgressDialog();
//        getNetworkManager().PutRequest(RequestIdentifier.ADD_FAV_PRODUCT_REQUEST_ID.ordinal(),
//                WebServiceConstants.PUT_PRODUCT_TO_FAV, WebServicePostParams.addProductToFavouriteParams(productMasterId), params, this, this, false);
//
//    }
//
//    protected void callRemoveFromFavourite(int productMasterId) {
//        RequestParams paramsHeader = new RequestParams();
//        paramsHeader.headerMap = new HashMap<>();
//        WebServicePostParams.addResultWrapHeader(paramsHeader.headerMap);
//        Map params = WebServicePostParams.removeFavProduct(productMasterId);
//        getNetworkManager().PutRequest(RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal(),
//                WebServiceConstants.ITEM_COUNT, params, paramsHeader, this, this, false);
//    }

    protected void callProductSkuPriceDetails(String skuId) {

        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.GET_PRODUCT_SKU_PRICE_DETAILS.ordinal(),
                WebServiceConstants.PRODUCT_SKU_PRICE_DETAILS + skuId, null, null, this, this, false);
    }

    protected void callUpdateProductSkuPriceDetails(String skuId, int tp, int sp, String source) {

        showProgressDialog();
        getNetworkManager().PutRequest(RequestIdentifier.PUT_PRODUCT_SKU_PRICE_DETAILS.ordinal(),
                WebServiceConstants.UPDATE_PRODUCT_SKU_PRICE + skuId, WebServicePostParams.updateProductSkuPrice(tp, sp, source), null, this, this, false);
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        if (request.getIdentifier() == RequestIdentifier.GET_PRODUCT_ID.ordinal()) {
            hideProgressBar();


            getGsonHelper().parse(responseObject.toString(), Product.class, new OnGsonParseCompleteListener<Product>() {
                        @Override
                        public void onParseComplete(Product data) {
                            try {
                                if (data != null) {
                                    productDetailHolder = data;
                                    sampleProduct = getSampleProduct(productDetailHolder);


                                    handleActionBTNVisibility();
                                    setPDPAdapter();
                                    hideNoResult(productContainerView);
                                    if (productSkuId > 0 && showDailogForDealSku) {
                                        openProductAttributeDailog();
                                    }

                                } else {
                                    showNoResult(getString(R.string.err_somthin_wrong), productContainerView);
                                }
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );


        }


        if (request.getIdentifier() == RequestIdentifier.ADD_TO_CART.ordinal()) {
            hideProgressDialog();

            getGsonHelper().parse(responseObject.toString(), AddToCartResponse.class, new OnGsonParseCompleteListener<AddToCartResponse>() {
                        @Override
                        public void onParseComplete(AddToCartResponse data) {
                            try {
                                updateCartCountToolBar(data.items.size());
                                if (isCartLaunch) {
                                    hideKeyBoard(getView());
                                    launchCart();
                                    isCartLaunch = false;
                                } else {
                                    if (getActivity() != null && isAdded()) {
                                        hideKeyBoard(getView());
                                        showError(getString(R.string.products_added_to_cart), MESSAGETYPE.TOAST);
                                    }
                                }
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );


        }


        if (request.getIdentifier() == RequestIdentifier.ADD_PRODUCT_TO_CART.ordinal()) {
            hideProgressDialog();


            getGsonHelper().parse(responseObject.toString(), AddToCartResponse.class, new OnGsonParseCompleteListener<AddToCartResponse>() {

                        @Override
                        public void onParseComplete(AddToCartResponse data) {
                            try {
                                updateCartCountToolBar(data.items.size());
                                if (isCartLaunch) {
                                    hideKeyBoard(getView());
                                    launchCart();
                                    isCartLaunch = false;
                                } else {
                                    if (getActivity() != null && isAdded()) {
                                        hideKeyBoard(getView());
                                        showError(getString(R.string.products_added_to_cart), MESSAGETYPE.TOAST);
                                    }
                                }
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );


        }

        if (request.getIdentifier() == RequestIdentifier.ADD_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            try {
                                hideProgressDialog();
                                CustomLogger.d("Added to Favourite Item ");
                                if (data.result != null) {
                                    FavouriteUtility.updateFavMapOnAddRemove(data);
                                }
                                productDetailFragmentAdapter.notifyItemChanged(1);
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            hideProgressDialog();
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            try {
                                CustomLogger.d("Removed Favourite Item ");
                                if (data.result != null) {
                                    FavouriteUtility.updateFavMapOnAddRemove(data);
                                }
                                productDetailFragmentAdapter.notifyItemChanged(1);
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                            showError();
                        }
                    }
            );
        }


        if (request.getIdentifier() == RequestIdentifier.REGSITER_SELLER_CHAT.ordinal()) {
            try {
                hideProgressDialog();
//            showMessage("Supplier Regsitered for Chat ", MESSAGETYPE.TOAST);
                ChatUtility chat = new ChatUtility(this, productDetailHolder.supplier.userId,
                        productDetailHolder.supplier.companyName, productDetailHolder.productMaster.images.get(0).src,
                        productDetailHolder.productMaster.name, productDetailHolder.productMaster.bcin);
                chat.launchChatActivity();
            }catch (Exception e){
                CustomLogger.e("Exception ", e);
            }
        }

        if (request.getIdentifier() == RequestIdentifier.GET_PRODUCT_SKU_PRICE_DETAILS.ordinal()) {
            try {
                hideProgressDialog();

                productPriceEditDialog = ProductPriceEditDialog.newInstance(responseObject.toString());
                productPriceEditDialog.show(context.getFragmentManager(), "dialog");
            }catch (Exception e){
                CustomLogger.e("Exception ", e);
            }
        }

        if (request.getIdentifier() == RequestIdentifier.PUT_PRODUCT_SKU_PRICE_DETAILS.ordinal()) {
            try {
                hideProgressDialog();

                productAttributeDialog.dismiss();
                productDetailFragmentAdapter = null;
                loadPDP();
                productPriceEditDialog.dismiss();
                BaseActivity.showToast("Price updated successfully");
            }catch (Exception e){
                CustomLogger.e("Exception ", e);
            }
        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.GET_PRODUCT_ID.ordinal()) {
            try {
                showError();
                showColoredToolbar();
            } catch (Exception e) {
                CustomLogger.e("Exception ", e);
            }
        }

        if (request.getIdentifier() == RequestIdentifier.PUT_PRODUCT_SKU_PRICE_DETAILS.ordinal()) {
            try {
                productPriceEditDialog.dismiss();
                if (apiError.issue != null)
                    showError(apiError.issue, MESSAGETYPE.TOAST);
                else
                    showError("Something went wrong!", MESSAGETYPE.TOAST);
                showColoredToolbar();
            } catch (Exception e) {
                CustomLogger.e("Exception ", e);
            }
        }
        return true;
    }

    private void setPDPAdapter() {
        if (productDetailFragmentAdapter == null)
            productDetailFragmentAdapter = new ProductDetailFragmentAdapter(this, productDetailHolder, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(productDetailFragmentAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        handleActionBTNVisibility();

        //TODO : Not a good way, would change it to Collapsing toolbar

        mRecyclerView.setOnScrollListener(new ChangeScrollListener() {

            @Override
            public void setNewColor() {
                showColoredToolbar();
            }

            @Override
            public void setOldColor() {
                showTransparentToolbar();
            }

        });


    }

    @Override
    public void onResume() {
        super.onResume();
        KeyPadUtility.hideSoftKeypad(getActivity());


    }

    @Override
    public void onPause() {
        super.onPause();
        KeyPadUtility.hideSoftKeypad(getActivity());

    }

    private void showColoredToolbar() {
        if (toolbar != null) {
            toolbar.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle(mProductName);
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setNavigationIcon(R.drawable.back_arrow);


            if (menu != null) {
                showCartMenu(false);
            }
        }


        setOverflowButtonColor(getActivity(), Color.WHITE);


    }

    private void showTransparentToolbar() {
        if (toolbar != null) {
            toolbar.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.transparent));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

            if (menu != null) {
                showCartMenu(true);
            }
        }

        setOverflowButtonColor(getActivity(), Color.BLACK);

    }


    public static void setOverflowButtonColor(final Activity activity, final int color) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow = (AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(color); //changes color
                removeOnGlobalLayoutListener(decorView, this);
            }
        });


    }

    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }


    public Product_ getSampleProduct(Product pdpData) {
        Product_ sampleProduct = null;
        if (pdpData.products != null) {
            for (Product_ product : pdpData.products) {
                if (product.allowSample == 1) {
                    sampleProduct = product;
                    break;
                }
            }
            for (Product_ product : pdpData.products) {
                if (product.allowSale == 1) {
                    isSaleable = true;
                    break;
                }
            }

        }
        return sampleProduct;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        hideSearchMenu();
        hideFavMenu();
        hideDealFilterMenu();
        showCartMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void showCartMenu(boolean showBlackIcon) {
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        if (cartItem == null)
            return;

        if (showBlackIcon) {
            cartItem.setActionView(R.layout.cart_update_icon_white);
        } else {
            cartItem.setActionView(R.layout.cart_update_icon);
        }


        View cartView = MenuItemCompat.getActionView(cartItem);
        TextView tvCart = (TextView) cartView.findViewById(R.id.cartValueTV);

        int cartCount = PartsEazyApplication.getInstance().getCartCount();


        /* Hide cart count icon if cart count is 0 */
        if (cartCount > 0) {
            tvCart.setVisibility(View.VISIBLE);
            tvCart.setText(String.valueOf(cartCount));
        } else {
            tvCart.setVisibility(View.GONE);
        }

        /* cart view  click Listener*/
        cartView.findViewById(R.id.cartFL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCart();
            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == gotToBagBT.getId()) {
            hideKeyBoard(getView());
            openProductAttributeDailog();
        }

        if (view.getId() == inquireBTN.getId()) {
            hideKeyBoard(getView());
            String productPrice = CommonUtility.getPriceRangeforPDP(productDetailHolder.min, productDetailHolder.max);
            String imageUrl = null;
            if (productDetailHolder.productMaster.images != null && productDetailHolder.productMaster.images.size() > 0) {
                imageUrl = productDetailHolder.productMaster.images.get(0).src;
            }
            SalesEnquiryFragment salesEnquiryFragment = SalesEnquiryFragment.newInstance(productDetailHolder.supplier.id, productDetailHolder.productMaster.id,
                    productDetailHolder.productMaster.name, productPrice, imageUrl);
            BaseFragment.addToBackStack((BaseActivity) context, salesEnquiryFragment, salesEnquiryFragment.getTag());
            Tracker.trackEvent(TrackerConstants.Screen.PRODUCT_DETAIL, mProductName, TrackerConstants.Action.SALES_ENQUIRY, mProductID + "");

           // MoEHelper Event
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrInt("ProductID", mProductID)
                    .putAttrString("ProductName", ""+mProductName)
                    .putAttrDate("purchaseDate", new Date())
                    .putAttrString("Sales Enquiry", ""+ TrackerConstants.Action.SALES_ENQUIRY)
                    .putAttrString("Product Details", ""+TrackerConstants.Screen.PRODUCT_DETAIL);
            MoEHelper.getInstance(context).trackEvent("Purchase", builder);


        }
    }


    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logAddToCartEvent (String contentData, String contentId, String contentType, String currency, double price) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price, params);
    }

    private void openProductAttributeDailog() {
        if (getActivity() != null) {
            productAttributeDialog = ProductAttributeDialog.newInstance(new Gson().toJson(productDetailHolder), false);
            productAttributeDialog.show(getActivity().getFragmentManager(), "dialog");
        }

    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);


        if (eventObject.id == EventConstant.PRODUCT_ATTRIBUTE_SELECTED) {

            final ProductAttributeAdapterFactory.ProductToAddInCart
                    productToAddInCart = (ProductAttributeAdapterFactory.ProductToAddInCart) eventObject.objects[0];
            final boolean isLaunchCart = (Boolean) eventObject.objects[1];
            isCartLaunch = isLaunchCart;
            // MoEHelper ADD TO CART Event
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrInt("ProductID", mProductID)
                    .putAttrString(MoengageConstant.Events.PARAMS , eventObject.objects.toString())
                    .putAttrString(MoengageConstant.Events.PRODUCTS_TO_ADD_IN_CART , productToAddInCart.toString());
            MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.ADD_TO_CART, builder);

            callAddtoCart(productToAddInCart.selectedProductSKUIds, productToAddInCart.selectedProductQuantity);


        }

        if (eventObject.id == EventConstant.NO_ATTRIBUTE_PRODUCT_SELECTED) {

            final int selectedProductSKUId = (Integer) eventObject.objects[0];
            final int productQuantity = (Integer) eventObject.objects[1];
            final boolean isLaunchCart = (Boolean) eventObject.objects[2];
            isCartLaunch = isLaunchCart;
            callAddProductToCart(selectedProductSKUId, productQuantity, false);

        }

        if (eventObject.id == EventConstant.PRODUCT_SAMPLE_SELECTED) {
            final int selectedSampleProductSKUId = (Integer) eventObject.objects[0];
            final int sampleQuantity = (Integer) eventObject.objects[1];
            final boolean isLaunchCart = (Boolean) eventObject.objects[2];
            isCartLaunch = isLaunchCart;

            callAddProductToCart(selectedSampleProductSKUId, sampleQuantity, true);

        }


        if (eventObject.id == EventConstant.REGISTER_SELLER_TO_CHAT) {
            String supplId = (String) eventObject.objects[0];
            String supplName = (String) eventObject.objects[1];
            registerSupplierChat(supplId);
        }

        if (eventObject.id == EventConstant.GET_PRODUCT_SKU_PRICE_DETAIL) {
            String skuId = (String) eventObject.objects[0];
            callProductSkuPriceDetails(skuId);
        }

        if (eventObject.id == EventConstant.PUT_PRODUCT_SKU_PRICE_DETAIL) {
            String skuId = (String) eventObject.objects[0];
            int tp = (int) eventObject.objects[1];
            int sp = (int) eventObject.objects[2];
            String source = (String) eventObject.objects[3];
            callUpdateProductSkuPriceDetails(skuId, tp, sp, source);
        }
    }

    public interface OnAttributeItemSelectedListener {
        void onItemSelected(List<Integer> productIDList, String originator);
    }

    private void handleActionBTNVisibility() {
        try {
            if (!productDetailHolder.isActive) {
                gotoBagLL.setVisibility(View.GONE);
                notActiveLL.setVisibility(View.VISIBLE);
            } else {
                notActiveLL.setVisibility(View.GONE);
                gotoBagLL.setVisibility(View.GONE);
                if (productDetailHolder.productMaster.format.equals(AppConstants.INQUIRE_PRODUCT)) {
                    inquireLL.setVisibility(View.VISIBLE);
                } else {
                    if (isAdded() && getActivity() != null && (!productDetailHolder.inStock)) {
                        gotoBagLL.setVisibility(View.GONE);
                        notActiveTV.setText(getString(R.string.out_of_stock));
                        notActiveLL.setVisibility(View.VISIBLE);
                    } else {
                        if (sampleProduct != null && !isSaleable) {
                            gotoBagLL.setVisibility(View.GONE);
                        } else {
                            gotoBagLL.setVisibility(View.VISIBLE);
                            showDailogForDealSku = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            CustomLogger.e(e.toString());
        }
    }


    @Override
    public void addFav(int id) {

        FavouriteUtility.callAddProductToFavourite(this, id);
    }

    @Override
    public void removeFav(int id) {

        FavouriteUtility.callRemovefavItemRequest(this, id);
    }

    @Override
    protected void retryFailedRequest(int identifier, Request<?> oldRequest, VolleyError error) {
        if (oldRequest.getIdentifier() == RequestIdentifier.GET_PRODUCT_ID.ordinal()) {
            if (getActivity() != null && isAdded()) {
                showError(getString(R.string.wait_trying_to_reconnect), MESSAGETYPE.SNACK_BAR);
                loadPDP();
            }
        }

    }
}

