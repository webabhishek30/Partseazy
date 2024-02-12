package com.partseazy.android.ui.fragments.deals.create_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.SkuData;
import com.partseazy.android.utility.CommonUtility;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by naveen on 26/4/17.
 */

public class NewDealPriceFragment extends NewDealBaseFragment implements View.OnClickListener {


    @BindView(R.id.priceLineView)
    protected View priceLineView;

    @BindView(R.id.priceCheckIV)
    protected ImageView priceCheckIV;


    @BindView(R.id.multiSkuLL)
    protected LinearLayout multiSkuLL;

    @BindView(R.id.addMoreSkTV)
    protected TextView addMoreSkTV;

    @BindView(R.id.singleSkuLL)
    protected LinearLayout singleSkuLL;

    @BindView(R.id.continueBT)
    protected Button continueBT;


    @BindView(R.id.priceET)
    protected EditText priceET;

    @BindView(R.id.priceTIL)
    protected TextInputLayout priceTIL;

    @BindView(R.id.mrpET)
    protected EditText mrpET;

    @BindView(R.id.mrpTIL)
    protected TextInputLayout mrpTIL;

    @BindView(R.id.stockET)
    protected EditText stockET;

    @BindView(R.id.stockTIL)
    protected TextInputLayout stockTIL;

    @BindView(R.id.minQtyET)
    protected EditText minQtyET;

    @BindView(R.id.minQtyTIL)
    protected TextInputLayout minQtyTIL;

    @BindView(R.id.escrowCB)
    protected CheckBox escrowCB;

    @BindView(R.id.prepaidCB)
    protected CheckBox prepaidCB;

    @BindView(R.id.codCB)
    protected CheckBox codCB;

    @BindView(R.id.payLaterCB)
    protected CheckBox payLaterCB;







    private int skuViewCount = 2;

    private SkuData singleSkuData;






    public static NewDealPriceFragment newInstance() {
        Bundle bundle = new Bundle();
        NewDealPriceFragment fragment = new NewDealPriceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_price_new_deal;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.create_deal);
    }

    public static String getTagName() {
        return NewDealPriceFragment.class.getSimpleName();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        setPriceProgress();
        showFirstSkuProduct();
        addMoreSkTV.setOnClickListener(this);
        continueBT.setOnClickListener(this);

        prepaidCB.setOnClickListener(this);
        codCB.setOnClickListener(this);
        payLaterCB.setOnClickListener(this);

        priceET.addTextChangedListener(new MyTextWatcher(priceET, priceTIL));
        mrpET.addTextChangedListener(new MyTextWatcher(mrpET, mrpTIL));
        stockET.addTextChangedListener(new MyTextWatcher(stockET, stockTIL));
        minQtyET.addTextChangedListener(new MyTextWatcher(minQtyET, minQtyTIL));

        //TODO  REMOVE THIS ONCE PAYMENT METHODS WILL INTEGRATE  THESE ARE DEAFAULT VALUES FOR API
        dealData.paymentMethodList.add(DealConstants.PAYMENT_CASH);
        dealData.paymentMethodList.add(DealConstants.PAYMENT_DIRECT);


        return view;

    }



    private void setPriceProgress() {
        priceCheckIV.setImageResource(R.drawable.check_green_circle);
        priceLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));
    }


    private boolean checkDataValidity() {
        boolean isValidData = true;

        if (dealData.skuDataList.size() > 0)
            dealData.skuDataList.clear();

//        if (!isMultipleSku) {
//            isValidData = checkSingleSkuValid();
//            dealData.skuDataList.add(singleSkuData);
//        } else {
//
//        }

        isValidData = setAllMultiSkuData();
        CustomLogger.d("List SKu :: " + new Gson().toJson(dealData.skuDataList));
        return isValidData;
    }

    private boolean checkSingleSkuValid() {
        boolean isValidData = true;
        singleSkuData = new SkuData();
        singleSkuData.mrp="0";
        singleSkuData.price="0";
        singleSkuData.stock="0";
        singleSkuData.minQty="0";

        String price = priceET.getText().toString().trim();
        String mrp = mrpET.getText().toString().trim();
        String stock = stockET.getText().toString().trim();
        String minQty = minQtyET.getText().toString().trim();

        if (price != null && !price.equals("")) {
            singleSkuData.price = price;
        } else {
            priceTIL.setError(getString(R.string.enter_price));
            return false;
        }

        if (mrp != null && !mrp.equals("")) {
            singleSkuData.mrp = mrp;
        }else {
            mrpTIL.setError(getString(R.string.err_mrp_valid));
            return false;
        }

        if (stock != null && !stock.equals("")) {
            singleSkuData.stock = stock;
        }else{
            stockTIL.setError(getString(R.string.err_stock_valid));
            return false;
        }

        if (minQty != null && !minQty.equals("")) {
            singleSkuData.minQty = minQty;
        }else{
            minQtyTIL.setError(getString(R.string.err_quantity_valid));
            return false;
        }

        if(Integer.parseInt(singleSkuData.price)> Integer.parseInt(singleSkuData.mrp))
        {
            mrpTIL.setError(getString(R.string.err_mrp_price));
            return false;
        }

        if(Integer.parseInt(singleSkuData.minQty)> Integer.parseInt(singleSkuData.stock))
        {
            minQtyTIL.setError(getString(R.string.err_qty_deal));
            return false;
        }

        return isValidData;
    }

    private boolean setAllMultiSkuData() {
        boolean isValidData = true;

        final int childcount = multiSkuLL.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = multiSkuLL.getChildAt(i);

            EditText descET = (EditText) view.findViewById(R.id.descET);
            EditText mrpET = (EditText) view.findViewById(R.id.mrpET);
            EditText stockET = (EditText) view.findViewById(R.id.stockET);
            EditText minQtyET = (EditText) view.findViewById(R.id.minQtyET);
            EditText priceET = (EditText)view.findViewById(R.id.priceET);

            TextInputLayout mrpTIL = (TextInputLayout) view.findViewById(R.id.mrpTIL);
            TextInputLayout priceTIL = (TextInputLayout) view.findViewById(R.id.priceTIL);
            TextInputLayout stockTIL = (TextInputLayout) view.findViewById(R.id.stockTIL);
            TextInputLayout minQtyTIL = (TextInputLayout) view.findViewById(R.id.minQtyTIL);
            TextInputLayout descTIL = (TextInputLayout) view.findViewById(R.id.descTIL);


            priceET.addTextChangedListener(new MyTextWatcher(priceET, priceTIL));
            mrpET.addTextChangedListener(new MyTextWatcher(mrpET, mrpTIL));
            stockET.addTextChangedListener(new MyTextWatcher(stockET, stockTIL));
            minQtyET.addTextChangedListener(new MyTextWatcher(minQtyET, minQtyTIL));


            String desc = descET.getText().toString().trim();
            String mrp = mrpET.getText().toString().trim();
            String stock = stockET.getText().toString().trim();
            String minQty = minQtyET.getText().toString().trim();
            String price = priceET.getText().toString().trim();

            SkuData skuData = new SkuData();
            skuData.mrp="0";
            skuData.price="0";
            skuData.stock="0";
            skuData.minQty="0";


            if (desc != null && !desc.equals("")) {
                skuData.desc = desc;
            }
            else {
                descTIL.setError(getString(R.string.enter_product_description));
                return false;
            }

            if (mrp != null && !mrp.equals("") && !mrp.equals("0")) {
                int mrpInt = Integer.parseInt(mrp);
                mrpInt = mrpInt*100;
                skuData.mrp = mrpInt+"";
            }
            else{
                mrpTIL.setError(getString(R.string.err_mrp_valid));
                return false;
            }

            if(price!=null && !price.equals("") && !price.equals("0")) {
                int priceInt = Integer.parseInt(price);
                priceInt =priceInt*100;
                skuData.price = priceInt+"";
            }else{
                priceTIL.setError(getString(R.string.enter_price));
                return false;
            }

            if (stock != null && !stock.equals("") && !stock.equals("0")) {
                skuData.stock = stock;
            }else{
                stockTIL.setError(getString(R.string.err_stock_valid));
                return false;
            }

            if (minQty != null && !minQty.equals("")&& !minQty.equals("0")) {
                skuData.minQty = minQty;
            }else{
                minQtyTIL.setError(getString(R.string.err_quantity_valid));
                return false;
            }


            if(Integer.parseInt(skuData.price)> Integer.parseInt(skuData.mrp))
            {
                mrpTIL.setError(getString(R.string.err_mrp_price));
                return false;
            }

            if(Integer.parseInt(skuData.minQty)> Integer.parseInt(skuData.stock))
            {
                minQtyTIL.setError(getString(R.string.err_qty_deal));
                return false;
            }


            dealData.skuDataList.add(skuData);

        }
        return isValidData;
    }

    private void showFirstSkuProduct()
    {
        final View view = LayoutInflater.from(context).inflate(R.layout.row_multi_sku_item, null);
        TextView itemTV = (TextView) view.findViewById(R.id.headingTV);
        ImageView crossIV = (ImageView) view.findViewById(R.id.crossIV);
        crossIV.setVisibility(View.GONE);

        itemTV.setText(getString(R.string.sku_caps) + "  1");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        view.setLayoutParams(params);
        multiSkuLL.addView(view);
    }
    private void addMultiSkuProduct() {
        final View view = LayoutInflater.from(context).inflate(R.layout.row_multi_sku_item, null);
        TextView itemTV = (TextView) view.findViewById(R.id.headingTV);
        ImageView crossIV = (ImageView) view.findViewById(R.id.crossIV);


        itemTV.setText(getString(R.string.sku_caps) + " "+skuViewCount);
        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((LinearLayout) view.getParent()).removeView(view);
                //skuViewCount--;
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        view.setLayoutParams(params);
        multiSkuLL.addView(view);
        skuViewCount++;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.continueBT:
                if (checkDataValidity()) {
                    addToBackStack((BaseActivity) getActivity(), NewDealShippingFragment.newInstance(), NewDealShippingFragment.getTagName());
                }
                break;

            case R.id.addMoreSkTV:
                addMultiSkuProduct();
                break;

            case R.id.escrowCB:
                if(escrowCB.isChecked())
                  dealData.escrow = 1;
                else
                    dealData.escrow=0;
                break;

            case R.id.prepaidCB:
                if(prepaidCB.isChecked())
                CommonUtility.updateSet(dealData.paymentMethodList, DealConstants.PAYMENT_CASH);
                break;

            case R.id.codCB:
                CommonUtility.updateSet(dealData.paymentMethodList, DealConstants.PAYMENT_CASH);
                break;

            case R.id.payLaterCB:
               CommonUtility.updateSet(dealData.paymentMethodList,DealConstants.PAYMENT_DIRECT);
                break;

        }

    }

    private class MyTextWatcher implements TextWatcher {
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

        }
    }
}
