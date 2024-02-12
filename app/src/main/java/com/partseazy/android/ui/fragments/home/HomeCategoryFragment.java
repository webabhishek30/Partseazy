package com.partseazy.android.ui.fragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.home.HomeAdapter;
import com.partseazy.android.ui.adapters.home.HorizontalProductAdapter;
import com.partseazy.android.ui.fragments.cart.CartHomeFragment;
import com.partseazy.android.ui.model.addtocart.AddToCartResponse;
import com.partseazy.android.ui.model.catalogue.Product;
import com.partseazy.android.ui.model.catalogue.Product_;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.fav.FavResultData;
import com.partseazy.android.ui.model.home.category.HomeCategoryResult;
import com.partseazy.android.ui.model.home.category.ProductData;
import com.partseazy.android.ui.model.home.category.Result;
import com.partseazy.android.ui.model.home.products.HomeProductsResult;
import com.partseazy.android.ui.model.myorders.ReorderData;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Naveen Kumar on 24/1/17.
 */

public class HomeCategoryFragment extends BaseFragment implements HomeAdapter.OnLoadProductsFromUrl, HorizontalProductAdapter.FavouriteListener,HorizontalProductAdapter.OnAddToCartReorder  {


    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noResultContinueBT)
    protected Button noResultContinueBT;

    private HomeAdapter homeAdapter;
    private List<Result> homeItemList;
    private int l2CategoryId;
    private int l3CategoryId;
    private int positionToNotify;
    private int adapterIndex;



    public static final String L2_CATEGORY_ID = "l2_category_id";
    public static final String L3_CATEGORY_ID = "l3_category_id";
    public static final String BROWSE_PAGE = "browse";
    public static final String PRODUCT_PAGE = "product";
    public static final String FINANCE_PAGE = "finance";
    public static final String BROWSE_BRAND = "browsebrand";
    private static final int PRODUCT_SKU_INDEX=1000;
    public static HomeCategoryFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeCategoryFragment fragment = new HomeCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    public static HomeCategoryFragment newInstance (int l2CategoryId,int l3CategoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(L2_CATEGORY_ID, l2CategoryId);
        bundle.putInt(L3_CATEGORY_ID, l3CategoryId);
        HomeCategoryFragment fragment = new HomeCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (homeItemList != null && homeItemList.size() > 0) {
            setHomeAdapter();
        } else {
            loadHomeCategory();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        l2CategoryId = getArguments().getInt(L2_CATEGORY_ID);
        l3CategoryId = getArguments().getInt(L3_CATEGORY_ID);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_category;
    }

    @Override
    protected String getFragmentTitle() {
        return HomeCategoryFragment.class.getSimpleName();
    }

    public static String getTagName() {
        return HomeCategoryFragment.class.getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyBoard(getView());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setHomeAdapter() {
        if (homeAdapter == null)
            homeAdapter = new HomeAdapter(getContext(),homeItemList, this, this,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(homeAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        hideKeyBoard(view);
        return view;
    }


    private void loadHomeCategory() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);

        String homeCategoryURL = WebServiceConstants.HOME_CATEGORY + l2CategoryId + "?page=home&active=1";
        getNetworkManager().GetRequest(RequestIdentifier.HOME_CATEGORY_REQUEST_ID.ordinal(),
                homeCategoryURL, null, params, this, this, false);
    }

    private void loadProductList(int requestId, String productDataUrl) {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(requestId, productDataUrl, null, params, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();

        if(request.getIdentifier() == RequestIdentifier.HOME_CATEGORY_REQUEST_ID.ordinal())
        {
            showError();
            if(apiError!=null)
            {
                showError(apiError.message,MESSAGETYPE.SNACK_BAR);
            }else{
                showError(getString(R.string.err_somthin_wrong),MESSAGETYPE.SNACK_BAR);
            }
        }else {
            if(getActivity() != null && isAdded()) {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.HOME_CATEGORY_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), HomeCategoryResult.class, new OnGsonParseCompleteListener<HomeCategoryResult>() {
                        @Override
                        public void onParseComplete(HomeCategoryResult data) {
                            hideProgressBar();
                            if (data.result != null && data.result.size() > 0) {
                                homeItemList = new ArrayList<Result>();
                                homeItemList.addAll(data.result);
                                setHomeAdapter();
                                hideNoResult(mRecyclerView);
                            }else{
                                if (getActivity() != null) {

                                    RelativeLayout.LayoutParams buttonLayoutParams  =  (RelativeLayout.LayoutParams) noResultContinueBT.getLayoutParams();
                                    buttonLayoutParams.setMargins(buttonLayoutParams.leftMargin, buttonLayoutParams.topMargin, buttonLayoutParams.rightMargin, 200);
                                    noResultContinueBT.setLayoutParams(buttonLayoutParams);
                                    showNoResult("", mRecyclerView, NORESULTBUTTON.CATALOGUE, l3CategoryId, "", false);
                                }


                            }


                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        } else if (request.getIdentifier() == RequestIdentifier.ADD_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            hideProgressDialog();
                            CustomLogger.d("Added to Favourite Item ");
                            if (data.result != null) {
                                FavouriteUtility.updateFavMapOnAddRemove(data);
                                if (homeAdapter != null)
                                    homeAdapter.updateHorizontalAdapter(positionToNotify, adapterIndex);

                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        } else if (request.getIdentifier() == RequestIdentifier.REMOVE_FAV_PRODUCT_REQUEST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), FavResultData.class, new OnGsonParseCompleteListener<FavResultData>() {
                        @Override
                        public void onParseComplete(FavResultData data) {
                            hideProgressDialog();

                            CustomLogger.d("Remmve  Favourite Item ");
                            if (data.result != null) {
                                FavouriteUtility.updateFavMapOnAddRemove(data);
                                if (homeAdapter != null)
                                    homeAdapter.updateHorizontalAdapter(positionToNotify,adapterIndex);

                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();

                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        } else  if (request.getIdentifier() == RequestIdentifier.ADD_PRODUCT_TO_CART.ordinal()) {
            hideProgressDialog();


            getGsonHelper().parse(responseObject.toString(), ReorderData.class, new OnGsonParseCompleteListener<ReorderData>() {

                        @Override
                        public void onParseComplete(ReorderData data) {
                            try {
                                if (data.error != null && data.error.equals("0"))
                                {
                                    redirectToCart();
                                    PartsEazyEventBus.getInstance().postEvent(EventConstant.UPDATE_TAB_COUNT_CART_ID, "0");                                }
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
        else {
            getGsonHelper().parse(responseObject.toString(), HomeProductsResult.class, new OnGsonParseCompleteListener<HomeProductsResult>() {
                        @Override
                        public void onParseComplete(HomeProductsResult data) {
                            hideProgressDialog();
                            if (data.productList != null) {
                                CustomLogger.d("dataSize " + data.productList.size());
                                CustomLogger.d("Positionn :::"+request.getIdentifier());
                                if(request.getIdentifier()-PRODUCT_SKU_INDEX<0)
                                {
                                    addProductList(request.getIdentifier(), data.productList,false);
                                }else {
                                    addProductList(request.getIdentifier()-PRODUCT_SKU_INDEX, data.productList,true);
                                }
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressDialog();
                            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );
        }

        return true;
    }

    @Override
    public void loadProducts(int position, String productUrl) {
        CustomLogger.d("Position " + position + "Url ::" + productUrl);
        loadProductList(position, productUrl);
    }

    @Override
    public void loadProductSingleSku(String widgetType, int position, String productUrl) {
        loadProductList(position+PRODUCT_SKU_INDEX, productUrl);
    }

    //  this will handle products crad data (Similar Products,Trending Products etc) it take the card position
    public void addProductList(int position, List<Product> productList,boolean isSingleSkuWidget) {
        List<ProductData> newProductList = new ArrayList<>();
        if (productList.size() > 0) {

            for (Product product : productList) {


                if(product.products!=null && product.products.size()>0 && isSingleSkuWidget)
                {

                    for(int i=0;i<product.products.size();i++) {
                        ProductData productData = getPopulatedProduct(product);
                        Product_ productSku = product.products.get(i);
                        productData.margin = productSku.margin;
                        productData.productPrice = productSku.price;
                        productData.isProductSku = true;
                        productData.productSkuDesc = productSku.desc;
                        productData.productPriceHigh = 0;
                        productData.productSkuId = productSku.id;
                        newProductList.add(productData);
                    }
                }else{
                    ProductData productData = getPopulatedProduct(product);
                    newProductList.add(productData);
                }

            }
        }
        homeItemList.get(position).productDataList = new ArrayList<>();
        homeItemList.get(position).productDataList.addAll(newProductList);
        homeAdapter.SetHomeItemList(homeItemList);
        homeAdapter.notifyItemChanged(position);
        // setHomeAdapter();
    }

    private ProductData getPopulatedProduct(Product product)
    {
        ProductData productData = new ProductData();
        productData.productName = product.productMaster.name;
        productData.productMasterId = product.productMaster.id;
        productData.margin = product.margin;
        productData.productPrice = product.price;
        productData.productPriceHigh = product.priceHigh;
        productData.minQty = product.productMaster.minQty;
        productData.packQty = product.productMaster.packOf;
        productData.inStock = product.inStock;
        productData.format = product.productMaster.format;
        productData.categoryId = product.productMaster.categoryId;
        if (product.productMaster.images != null) {
            productData.image = product.productMaster.images.get(0).src;
        }
        return productData;
    }

    @Override
    public void addFav(int id, int position, int adapterIndex) {

        positionToNotify = position;
        this.adapterIndex = adapterIndex;
        FavouriteUtility.callAddProductToFavourite(this, id);
    }

    @Override
    public void removeFav(int id, int position, int adapterIndex) {

        positionToNotify = position;
        this.adapterIndex = adapterIndex;
        FavouriteUtility.callRemovefavItemRequest(this, id);
    }

    @Override
    public void OnAddToCartReorder(int selectedProductMasterID) {
        // showProgressBar();
        showProgressDialog();
        getNetworkManager().PostRequest(RequestIdentifier.ADD_PRODUCT_TO_CART.ordinal(),
                WebServiceConstants.REORDER_PRODUCT, WebServicePostParams.getReorderProductData(selectedProductMasterID), null, this, this, false);

    }

    public void updateCart()
    {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fetchCartCountHandler();
            }
        });

    }


    private void redirectToCart()
    {
        SweetAlertDialog sweetAlertDialogView;
        sweetAlertDialogView = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialogView  .setTitleText("Thank you")
                .setContentText("Your Re-ordered item has been successfully added to cart")
                .setConfirmText("Buy Now")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        addToBackStack(getContext(), CartHomeFragment.newInstance(false), CartHomeFragment.getTagName());
                        sDialog.cancel();
                    }
                })
                .showCancelButton(true)

                .setCancelText("Shop More")

                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
        TextView textView = (TextView) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.content_text);
        if (textView != null) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setSingleLine(false);
            textView.setMaxLines(5);
            textView.setLines(3);
        }
        Button viewGroup = (Button) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
        if (viewGroup != null) {
            viewGroup.setBackground(viewGroup.getResources().getDrawable(R.drawable.red_border_rectangle));
            viewGroup.setTextColor(viewGroup.getResources().getColor(R.color.colorPrimary));
        }
        Button cancelViewGroup = (Button) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.cancel_button);
        if (cancelViewGroup != null) {
            cancelViewGroup.setBackground(cancelViewGroup.getResources().getDrawable(R.drawable.cart_continue_button_selector));
        }

    }
}