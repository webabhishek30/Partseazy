package com.partseazy.android.ui.fragments.product;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.product.ProductDetailAdapter;
import com.partseazy.android.ui.model.productdetail.ProductBag;
import com.partseazy.android.ui.model.productdetail.Product_;
import com.partseazy.android.ui.model.productdialog.ProductDetailItem;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 7/1/17.
 */

public class ProductSampleDialog extends DialogFragment implements View.OnClickListener {
    protected Button buyBTN;
    protected Button addToCartBTN;
    protected NetworkImageView productImage;
    protected TextView productNameTV;
    protected TextView productPriceTV;
    protected TextView totalPriceHeadingTV, moqTV, marginTV;
    protected RecyclerView recylerView;
    protected LinearLayout emptyLL;

    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_IMAGE = "PRODUCT_IMAGE";
    public static final String PRODUCT_MOQ = "PRODUCT_MOQ";
    public static final String PRODUCT_MARGIN = "PRODUCT_MARGIN";
    public static final String SAMPLE_PRODUCT = "SAMPLE_PRODUCT";

    private Product_ sampleProduct;
    private String productMasterName;
    private String productImageUrl, moqStr;
    private int margin;
    private ProductDetailAdapter productDetailAdapter;
    private List<ProductDetailItem> detailItemList;
    private ImageLoader imageLoader;


    public static ProductSampleDialog newInstance(String productName, String productImageUrl, String moqString, int margin, String sampleProduct) {

        ProductSampleDialog dialogFragment = new ProductSampleDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_NAME, productName);
        bundle.putString(PRODUCT_IMAGE, productImageUrl);
        bundle.putString(PRODUCT_MOQ, moqString);
        bundle.putInt(PRODUCT_MARGIN, margin);
        bundle.putString(SAMPLE_PRODUCT, sampleProduct);
        dialogFragment.setArguments(bundle);
        return dialogFragment;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sampleProductJson = getArguments().getString(SAMPLE_PRODUCT);
        sampleProduct = new Gson().fromJson(sampleProductJson, Product_.class);
        productMasterName = getArguments().getString(PRODUCT_NAME);
        productImageUrl = getArguments().getString(PRODUCT_IMAGE);
        moqStr = getArguments().getString(PRODUCT_MOQ);
        margin = getArguments().getInt(PRODUCT_MARGIN);
        imageLoader = ImageManager.getInstance(getActivity()).getImageLoader();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_product_sample, new LinearLayout(getActivity()), false);
        initViews(view);
        setViews();
        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme) {
            @Override
            public void onBackPressed() {
                dissmissDialog();
            }
        };
        builder.setContentView(view);


        return builder;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void initViews(View view) {

        buyBTN = (Button) view.findViewById(R.id.buyBTN);
        productImage = (NetworkImageView) view.findViewById(R.id.productIconIV);
        productNameTV = (TextView) view.findViewById(R.id.productNameTV);
        productPriceTV = (TextView) view.findViewById(R.id.productPriceTV);
        totalPriceHeadingTV = (TextView) view.findViewById(R.id.totalPriceHeadingTV);
        moqTV = (TextView) view.findViewById(R.id.moqTV);
        marginTV = (TextView) view.findViewById(R.id.marginTV);
        recylerView = (RecyclerView) view.findViewById(R.id.scrollable);
        emptyLL = (LinearLayout) view.findViewById(R.id.emptyLL);
        addToCartBTN = (Button) view.findViewById(R.id.addToCartBTN);
        buyBTN.setOnClickListener(this);
        emptyLL.setOnClickListener(this);
        addToCartBTN.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLL.setBackgroundColor(0X70000000);
                emptyLL.invalidate();
            }
        }, 0);

    }

    public void setViews() {

        productNameTV.setText(productMasterName);
        productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, CommonUtility.convertionPaiseToRupeeString(sampleProduct.samplePrice))));
        totalPriceHeadingTV.setText(PartsEazyApplication.getInstance().getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(sampleProduct.samplePrice)));
        moqTV.setText(moqStr);
        if (margin != 0) {
            marginTV.setText(PartsEazyApplication.getInstance().getString(R.string.margin, "" + margin));
            marginTV.setVisibility(View.VISIBLE);
        } else {
            marginTV.setVisibility(View.GONE);
        }

        if (productImageUrl != null && !productImageUrl.equals("")) {

            String formatedURL = CommonUtility.getFormattedImageUrl(productImageUrl, productImage, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, productImage, formatedURL);

//            productImage.setImageUrl(productImageUrl, imageLoader);

        }

        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (detailItemList == null) {
            loadDetailList();
        }
        productDetailAdapter = new ProductDetailAdapter(getActivity(), detailItemList, false);
        recylerView.setAdapter(productDetailAdapter);
    }

    public void loadDetailList() {

        detailItemList = new ArrayList<>();
        for (ProductBag item : sampleProduct.productBag) {
            ProductDetailItem productDetailItem = new ProductDetailItem(item.name, item.value);
            detailItemList.add(productDetailItem);
        }

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buyBTN) {
            getProductToAdd(true);
            dissmissDialog();
        }
        if (view.getId() == R.id.emptyLL) {
            dissmissDialog();
        }
        if (view.getId() == R.id.addToCartBTN) {
            getProductToAdd(false);
            dissmissDialog();
        }
    }

    private void getProductToAdd(boolean isLaunchCart) {
        int productSKUID = sampleProduct.skuId;
        int quantity = 1;
        PartsEazyEventBus.getInstance().postEvent(EventConstant.PRODUCT_SAMPLE_SELECTED, productSKUID, quantity, isLaunchCart);
    }


    private void dissmissDialog() {

        emptyLL.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        }, 200);
    }

}