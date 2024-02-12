package com.partseazy.android.ui.fragments.product;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.model.productdetail.ProductSkuPriceDetail;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by shubhang on 10/05/17.
 */

public class ProductPriceEditDialog extends DialogFragment implements View.OnClickListener {

    private Button updateBTN;
    private TextView productNameTV, descriptionTV, sourceTV, lastUpdatedTV, marginTV, afterTaxTV, mrpTV, errorTV, noteTV;
    private EditText tpET, spET, sourceET;

    public static final String SKU_PRICE_DETAIL = "SKU_PRICE_DETAIL";

    private ProductSkuPriceDetail skuPriceDetail;
    private float margin;
    private int afterTax;

    public static ProductPriceEditDialog newInstance(String sku) {
        ProductPriceEditDialog dialogFragment = new ProductPriceEditDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SKU_PRICE_DETAIL, sku);
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

        String skuDetailJson = getArguments().getString(SKU_PRICE_DETAIL);
        skuPriceDetail = new Gson().fromJson(skuDetailJson, ProductSkuPriceDetail.class);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_product_edit_price, new RelativeLayout(getActivity()), false);
        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme) {
            @Override
            public void onBackPressed() {
                getDialog().dismiss();
            }
        };
        builder.setContentView(view);

        initViews(view);
        setViews();

        return builder;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void initViews(View view) {

        updateBTN = (Button) view.findViewById(R.id.updateBTN);
        productNameTV = (TextView) view.findViewById(R.id.productNameTV);
        descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
        sourceTV = (TextView) view.findViewById(R.id.sourceTV);
        lastUpdatedTV = (TextView) view.findViewById(R.id.lastUpdatedTV);
        marginTV = (TextView) view.findViewById(R.id.marginTV);
        errorTV = (TextView) view.findViewById(R.id.errorTV);
        noteTV = (TextView) view.findViewById(R.id.noteTV);
        mrpTV = (TextView) view.findViewById(R.id.mrpTV);
        tpET = (EditText) view.findViewById(R.id.tpET);
        spET = (EditText) view.findViewById(R.id.spET);
        sourceET = (EditText) view.findViewById(R.id.sourceET);

        updateBTN.setOnClickListener(this);

        tpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 0)
                    s = "0";
                errorTV.setVisibility(View.GONE);
                skuPriceDetail.cost = CommonUtility.convertionRupeeToPaise(Integer.parseInt(s.toString()));
                calculateMargin(skuPriceDetail.cost, skuPriceDetail.price);
            }
        });

        spET.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 0)
                    s = "0";
                errorTV.setVisibility(View.GONE);
                skuPriceDetail.price = CommonUtility.convertionRupeeToPaise(Integer.parseInt(s.toString()));
                calculateMargin(skuPriceDetail.cost, skuPriceDetail.price);
            }
        });
    }

    private void setViews() {

        productNameTV.setText(skuPriceDetail.name);
        descriptionTV.setText(skuPriceDetail.description);
        mrpTV.setText(PartsEazyApplication.getInstance().getString(R.string.mrp,
                CommonUtility.convertionPaiseToRupeeString(skuPriceDetail.mrp)));
        mrpTV.setPaintFlags(  mrpTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        if (skuPriceDetail.info != null) {
            sourceTV.setText(PartsEazyApplication.getInstance().getString(R.string.source, "" + skuPriceDetail.info.get("source")));
            sourceTV.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date updatedAt = sdf.parse(skuPriceDetail.updatedAt);
            Date now = new Date();

            long diff = now.getTime() - updatedAt.getTime();//as given

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            if (days > 0) {
                lastUpdatedTV.setText(PartsEazyApplication.getInstance().getString(R.string.last_updated,
                        days + " days"));
            } else if (hours > 0) {
                lastUpdatedTV.setText(PartsEazyApplication.getInstance().getString(R.string.last_updated,
                        hours + " hours"));
            } else if (minutes > 0) {
                lastUpdatedTV.setText(PartsEazyApplication.getInstance().getString(R.string.last_updated,
                        minutes + " minutes"));
            } else {
                lastUpdatedTV.setText(PartsEazyApplication.getInstance().getString(R.string.last_updated,
                        seconds + " seconds"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tpET.setText(String.valueOf(CommonUtility.convertionPaiseToRupee(skuPriceDetail.cost)));
        spET.setText(String.valueOf(CommonUtility.convertionPaiseToRupee(skuPriceDetail.price)));

        calculateMargin(skuPriceDetail.cost, skuPriceDetail.price);

        if (skuPriceDetail.isSpecialPrice == 1) {
            updateBTN.setEnabled(false);
            updateBTN.setClickable(false);
            tpET.setKeyListener(null);
            spET.setKeyListener(null);
            showError("SKU is on special price. Cannot update price. Please contact admin");
        }

        if (skuPriceDetail.divergent == 0) {
            noteTV.setVisibility(View.VISIBLE);
        }

        if (skuPriceDetail.info != null && !skuPriceDetail.info.isEmpty()) {
            if (skuPriceDetail.info.containsKey("source")) {
                sourceET.setText(String.valueOf(skuPriceDetail.info.get("source")));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.updateBTN) {
            String source = sourceET.getText().toString();
            if ("".equals(source)) {
                showError("Please enter source");
            }

            PartsEazyEventBus.getInstance().postEvent(EventConstant.PUT_PRODUCT_SKU_PRICE_DETAIL, String.valueOf(skuPriceDetail.id),
                    skuPriceDetail.cost, skuPriceDetail.price, source);
//            getDialog().dismiss();
        }
    }

    public void onEvent(EventObject eventObject) {

    }

    private void calculateMargin(int cost, int sp) {
        margin = ((float) sp / cost - 1) * 100;
        marginTV.setText(PartsEazyApplication.getInstance().getString(R.string.margin, String.format("%.2f", margin) + ""));

        if (margin < 0) {
            marginTV.setTextColor(getResources().getColor(R.color.red_error));
            showError("SP cannot be less than TP");
        } else {
            marginTV.setTextColor(getResources().getColor(R.color.green_success));
            updateBTN.setClickable(true);
            updateBTN.setEnabled(true);
        }
    }

    private void showError(String error) {
        errorTV.setText(error);
        errorTV.setVisibility(View.VISIBLE);
        updateBTN.setEnabled(false);
        updateBTN.setClickable(false);
    }
}
