package com.partseazy.android.ui.fragments.product;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.model.productdetail.Product;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;


/**
 * Created by naveen on 29/12/16.
 */

public class ProductAttributeDialog extends DialogFragment implements DialogInterface.OnDismissListener {
    public static final String PRODUCT_DATA_KEY = "_PRODUCT_DATA_KEY";
    public static final String CART_LAUNCH = "CART_LAUNCH";
    private LinearLayout emptyLayout;

    private Product pdpData;
    private boolean isCartLaunch;


    protected RelativeLayout dialogattributeRL;


    public static ProductAttributeDialog newInstance(String productHolder, boolean isCartLaunch) {

        ProductAttributeDialog dialogFragment = new ProductAttributeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_DATA_KEY, productHolder);
        bundle.putBoolean(CART_LAUNCH, isCartLaunch);
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
        PartsEazyEventBus.getInstance().addObserver(this);

        String productData = getArguments().getString(PRODUCT_DATA_KEY);
        pdpData = new Gson().fromJson(productData, Product.class);
        isCartLaunch = getArguments().getBoolean(CART_LAUNCH, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_product_variation, new LinearLayout(getActivity()), false);
        emptyLayout = (LinearLayout) view.findViewById(R.id.emptyLL);
        dialogattributeRL = (RelativeLayout) view.findViewById(R.id.dialogattributeRL);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout.setBackgroundColor(0X70000000);
                emptyLayout.invalidate();
            }
        }, 0);

        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme) {
            @Override
            public void onBackPressed() {
                dissmissDialog();
            }
        };
        builder.setContentView(view);
        onClickEmptySpace();
        ProductAttributeAdapterFactory productAttributeAdapterFactory = new ProductAttributeAdapterFactory(this.getActivity(), pdpData, isCartLaunch, view);

        return builder;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void onClickEmptySpace() {
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDialog();

            }
        });
    }

    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.PRODUCT_ATTRIBUTE_SELECTED) {
            dissmissDialog();
        }

        if (eventObject.id == EventConstant.NO_ATTRIBUTE_PRODUCT_SELECTED) {
            dissmissDialog();
        }
    }

    private void dissmissDialog() {

        emptyLayout.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        }, 200);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
