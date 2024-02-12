package com.partseazy.android.ui.adapters.product.attribute;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.productdetail.ProductAttribute;
import com.partseazy.android.ui.widget.EditTextImeBackListener;
import com.partseazy.android.ui.widget.NumberPicketEditText;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.android.utility.CommonUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 29/12/16.
 */


public class ProductAttribColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ProductAttribute> attributeList;
    private List<ProductAttribute> lastattributeList;

    private LayoutInflater mInflater;
    private boolean hideFirstAttributeList = false;
    private boolean showSecondAttributeList = false;
    private int totalPrice = 0;
    private int totalQuantity = 0;
    private int packOf;
    private onQuantityChangedListener listener;
    private int enableProductID;
    private ProductDetailFragment.OnAttributeItemSelectedListener onAttributeItemSelectedListener;
    private int moq;
    private int MAXQTY;
    public FinalSelectedProduct finalSelectedProduct;

    public interface onQuantityChangedListener {
        void onQuantityChanged(int totalSum, int totalQuantity);
    }


    public ProductAttribColumnAdapter(Context context,
                                      List<ProductAttribute> attributeList,
                                      boolean hideFirstAttributeList,
                                      boolean showSecondAttributeList, int minProductQuantity, int packof,
                                      int MAXQTY,
                                      onQuantityChangedListener onQuantityChangedListener,
                                      ProductDetailFragment.OnAttributeItemSelectedListener itemSelectedListener) {
        this.attributeList = attributeList;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.hideFirstAttributeList = hideFirstAttributeList;
        this.showSecondAttributeList = showSecondAttributeList;
        this.listener = onQuantityChangedListener;
        this.onAttributeItemSelectedListener = itemSelectedListener;
        this.moq = minProductQuantity;
        this.packOf = packof;
        this.MAXQTY = MAXQTY;
        finalSelectedProduct = new FinalSelectedProduct();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductAttributeViewHolder(mInflater.inflate(R.layout.row_product_attribute_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProductAttributeViewHolder productAttributeViewHolder = (ProductAttributeViewHolder) holder;
        final ProductAttribute model = attributeList.get(position);
        handleClickEvent(productAttributeViewHolder, model);
    }

    public void handleClickEvent(final ProductAttributeViewHolder productAttributeViewHolder, final ProductAttribute model) {

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);
//        YOUR_VIEW.setLayoutParams(param);


        if (hideFirstAttributeList) {
            productAttributeViewHolder.firstAttibuteNameTV.setVisibility(View.GONE);
            productAttributeViewHolder.secondAttibuteNameTV.setLayoutParams(param);
        } else {
            productAttributeViewHolder.firstAttibuteNameTV.setVisibility(View.VISIBLE);
        }

        if (showSecondAttributeList) {
            productAttributeViewHolder.secondAttibuteNameTV.setVisibility(View.VISIBLE);
        } else {
            productAttributeViewHolder.secondAttibuteNameTV.setVisibility(View.GONE);
            productAttributeViewHolder.firstAttibuteNameTV.setLayoutParams(param);
        }


        productAttributeViewHolder.firstAttibuteNameTV.setText(model.value);
        productAttributeViewHolder.secondAttibuteNameTV.setText(model.value1);
        productAttributeViewHolder.firstAttibuteNameTV.setTag(model);

        //Both are not visible
//        if (hideFirstAttributeList && !showSecondAttributeList) {
//            param = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
//            productAttributeViewHolder.firstSeondAtrrbLL.setLayoutParams(param);
//        }


        //Out of stock handling
        if (model.stockQuantity == 0) {

            productAttributeViewHolder.qtLL.setVisibility(View.GONE);
            productAttributeViewHolder.errorLL.setVisibility(View.VISIBLE);
        } else {
            productAttributeViewHolder.qtLL.setVisibility(View.VISIBLE);
            productAttributeViewHolder.errorLL.setVisibility(View.GONE);

        }

        if (finalSelectedProduct.selectedAttrbiteMap.get(model.productID) != null) {
            FinalProductAttribute atr = finalSelectedProduct.selectedAttrbiteMap.get(model.productID);
            productAttributeViewHolder.quantityET.setText(atr.selectedQuanitity + "");
            int selectedProductTotalPrice = model.price * atr.selectedQuanitity * packOf;
            productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(selectedProductTotalPrice)));
        } else {
            productAttributeViewHolder.quantityET.setText(0 + "");
            productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(model.price * packOf)));

        }


        productAttributeViewHolder.plusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductAttribute model = (ProductAttribute) productAttributeViewHolder.firstAttibuteNameTV.getTag();

                if (!isItemAvailableToBuy(model))
                    return;

                if (productAttributeViewHolder.quantityET.getText() == null ||
                        TextUtils.isEmpty(productAttributeViewHolder.quantityET.getText())) {
                    productAttributeViewHolder.quantityET.clearFocus();

                    return;
                }

                int quantity = Integer.parseInt(productAttributeViewHolder.quantityET.getText().toString());

                //is Product more than available quantity reached


                if (totalQuantity >= MAXQTY) {
                    Toast.makeText(mContext, mContext.getString(R.string.total_max_message, MAXQTY), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (quantity >= model.stockQuantity) {

                    Toast.makeText(mContext, mContext.getString(R.string.stock_max_message, model.stockQuantity), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (quantity == 0) {
                    quantity = quantity + model.minQuantity;
                    totalPrice = totalPrice + model.price * model.minQuantity;
                    totalQuantity = totalQuantity + model.minQuantity;
                } else {
                    quantity++;
                    totalPrice = totalPrice + model.price;
                    totalQuantity = totalQuantity + 1;
                }

                int totalItemPrice = quantity * model.price * packOf;
                productAttributeViewHolder.quantityET.setText(quantity + "");
                productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(totalItemPrice)));
                listener.onQuantityChanged(totalPrice * packOf, totalQuantity);

                FinalProductAttribute product = new FinalProductAttribute();
                product.productID = model.productID;
                product.price = model.price;
                product.selectedQuanitity = quantity;
                product.isSampleType = model.isSampleProduct;
                product.isSaleAllowed = model.isSaleAllowed;
                product.stock = model.stockQuantity;
                finalSelectedProduct.selectedAttrbiteMap.put(model.productID, product);

            }
        });

        productAttributeViewHolder.minusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductAttribute model = (ProductAttribute) productAttributeViewHolder.firstAttibuteNameTV.getTag();

                if (!isItemAvailableToBuy(model))
                    return;

                if (productAttributeViewHolder.quantityET.getText() == null ||
                        TextUtils.isEmpty(productAttributeViewHolder.quantityET.getText())) {
                    productAttributeViewHolder.quantityET.clearFocus();

                    return;
                }


                int quantity = Integer.parseInt(productAttributeViewHolder.quantityET.getText().toString());

                if (quantity > 0) {

                    if (quantity == model.minQuantity) {

                        quantity = 0;
                        totalPrice = totalPrice - model.price * model.minQuantity;
                        totalQuantity = totalQuantity - model.minQuantity;

                    }
                    else
                    {
                        quantity--;
                        totalPrice = totalPrice - model.price;
                        totalQuantity = totalQuantity - 1;
                    }

                    if (quantity == 0)
                        finalSelectedProduct.selectedAttrbiteMap.remove(model.productID);

                    if (totalPrice < 0)
                        totalPrice = 0;

                    if (totalQuantity < 0)
                        totalQuantity = 0;
                }


                int totalItemPrice = quantity * model.price * packOf;

                if (totalItemPrice == 0) {
                    totalItemPrice = model.price * packOf;
                }

                productAttributeViewHolder.quantityET.setText(quantity + "");
                productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(totalItemPrice)));


                if (quantity > 0) {
                    FinalProductAttribute product = new FinalProductAttribute();
                    product.productID = model.productID;
                    product.price = model.price;
                    product.selectedQuanitity = quantity;
                    product.isSampleType = model.isSampleProduct;
                    product.isSaleAllowed = model.isSaleAllowed;
                    product.stock = model.stockQuantity;
                    finalSelectedProduct.selectedAttrbiteMap.put(model.productID, product);
                }
                listener.onQuantityChanged(totalPrice * packOf, totalQuantity);
            }
        });

        final String oldText = productAttributeViewHolder.quantityET.getText().toString();

        productAttributeViewHolder.quantityET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT)) {

                    productAttributeViewHolder.quantityET.clearFocus();
                    ProductAttribute model = (ProductAttribute) productAttributeViewHolder.firstAttibuteNameTV.getTag();

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if (!TextUtils.isEmpty(productAttributeViewHolder.quantityET.getText().toString()) &&
                            productAttributeViewHolder.quantityET.getText().toString().length() < AppConstants.MAX_QTY_LENGTH) {

                        int quantity = Integer.parseInt(productAttributeViewHolder.quantityET.getText().toString());
                        CustomLogger.d("quantity is " + quantity);
                        updatePriceQuantityEditTextBox(productAttributeViewHolder, quantity, model,true);

                    } else {

                        productAttributeViewHolder.quantityET.setText(oldText);
                    }

                }
                return false;
            }
        });


        productAttributeViewHolder.quantityET.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(NumberPicketEditText ctrl, String text) {
                if (!TextUtils.isEmpty(text.trim())) {
                    if (oldText != null) {
                        productAttributeViewHolder.quantityET.clearFocus();
                        ProductAttribute model = (ProductAttribute) productAttributeViewHolder.firstAttibuteNameTV.getTag();

                        int quantity = Integer.parseInt(text);
                        updatePriceQuantityEditTextBox(productAttributeViewHolder, quantity, model,false);

                    } else {

                        productAttributeViewHolder.quantityET.clearFocus();
                    }
                } else {
                    productAttributeViewHolder.quantityET.setText(oldText);
                }
            }
        });

        // set on click listener on price to handle update of price by employee
        if (DataStore.isAgentType(mContext)) {
            productAttributeViewHolder.priceTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.GET_PRODUCT_SKU_PRICE_DETAIL, String.valueOf(model.productID));
                }
            });
        }
    }


    private void updatePriceQuantityEditTextBox(ProductAttributeViewHolder productAttributeViewHolder, int quantity, ProductAttribute model,boolean showToast) {
       int prevSelectedQty =0;
        FinalProductAttribute atr = null;
        if(finalSelectedProduct.selectedAttrbiteMap.get(model.productID)!=null)
        {
            atr = finalSelectedProduct.selectedAttrbiteMap.get(model.productID);
            prevSelectedQty = atr.selectedQuanitity;
        }

        if (quantity >= 0 && quantity>=model.minQuantity) {

            productAttributeViewHolder.quantityET.setText(String.valueOf(quantity));

            if(quantity>model.stockQuantity)
            {
                quantity = model.stockQuantity;
                Toast.makeText(mContext, mContext.getString(R.string.stock_max_message, model.stockQuantity), Toast.LENGTH_SHORT).show();
            }
            int itemPrice = quantity * model.price;

            if (atr!= null) {

//                FinalProductAttribute atr = finalSelectedProduct.selectedAttrbiteMap.get(model.productID);
                int diffQuantity = quantity - atr.selectedQuanitity;
                int diffPrice = diffQuantity * atr.price;
                totalQuantity = totalQuantity + diffQuantity;
                totalPrice = totalPrice + diffPrice;

            } else {

                totalQuantity = totalQuantity + quantity;
                totalPrice = totalPrice + itemPrice;
            }

            if (quantity != 0) {
                int price = itemPrice * packOf;
                productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(price)));
                productAttributeViewHolder.quantityET.setText(quantity+"");
                FinalProductAttribute product = new FinalProductAttribute();
                product.productID = model.productID;
                product.price = model.price;
                product.selectedQuanitity = quantity;
                product.isSampleType = model.isSampleProduct;
                product.isSaleAllowed = model.isSaleAllowed;
                product.stock = model.stockQuantity;
                finalSelectedProduct.selectedAttrbiteMap.put(model.productID, product);
            } else {
                productAttributeViewHolder.priceTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(model.price * packOf)));
                finalSelectedProduct.selectedAttrbiteMap.remove(model.productID);

            }

            listener.onQuantityChanged(totalPrice * packOf, totalQuantity);

        }
//        else if(quantity > model.stockQuantity){
//            productAttributeViewHolder.quantityET.setText(prevSelectedQty+"");
//            Toast.makeText(mContext, mContext.getString(R.string.stock_max_message, model.stockQuantity), Toast.LENGTH_SHORT).show();
//            return;
//        }
        else if(quantity < model.minQuantity && showToast){
            productAttributeViewHolder.quantityET.setText(prevSelectedQty+"");
            Toast.makeText(mContext, mContext.getString(R.string.min_quantity_message, model.minQuantity), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private boolean isItemAvailableToBuy(ProductAttribute model) {


        //Is product sellable
        if (!model.isSaleAllowed) {

            Toast.makeText(mContext, mContext.getString(R.string.buy_not_allowed), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Is product sellable
        if (!model.isSaleAllowed) {

            Toast.makeText(mContext, mContext.getString(R.string.buy_not_allowed), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    @Override
    public int getItemCount() {
        return attributeList.size();
    }

    class ProductAttributeViewHolder extends BaseViewHolder {

        @BindView(R.id.firstAttibuteNameTV)
        public TextView firstAttibuteNameTV;
        @BindView(R.id.secondAttibuteNameTV)
        public TextView secondAttibuteNameTV;
        @BindView(R.id.priceTV)
        public TextView priceTV;
        @BindView(R.id.plusBT)
        public TextView plusBT;
        @BindView(R.id.minusBT)
        public TextView minusBT;
        @BindView(R.id.plusView)
        public View plusView;
        @BindView(R.id.minusView)
        public View minusView;
        @BindView(R.id.quantityET)
        public NumberPicketEditText quantityET;
        @BindView(R.id.erLL)
        public LinearLayout errorLL;
        @BindView(R.id.errorTV)
        public TextView errorTV;
        @BindView(R.id.qtyLL)
        public LinearLayout qtLL;

        @BindView(R.id.firstsecondatributeLL)
        public LinearLayout firstSeondAtrrbLL;


        public ProductAttributeViewHolder(View view) {
            super(view);

        }
    }

    public void onSelectedProductIDs(final List<Integer> productIDList, String Originator) {

        if (lastattributeList == null) {
            lastattributeList = new ArrayList<>();
            for (ProductAttribute attribute : attributeList) {
                lastattributeList.add(attribute);
            }

        }
        if (lastattributeList != null) {
            attributeList.clear();
            attributeList.addAll(lastattributeList);
        }

        if (productIDList.size() == 0)
            return;

        String idResult = "";
        for (Integer ids : productIDList) {

            idResult = idResult + " " + ids;
        }


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                List<ProductAttribute> productsToRetain = new ArrayList<>();


                for (int i = 0; i < attributeList.size(); i++) {

                    if (productIDList.contains(attributeList.get(i).productID)) {
                        productsToRetain.add(attributeList.get(i));
                    }

                }

                attributeList = productsToRetain;
                notifyDataSetChanged();

            }
        });


        CustomLogger.d("The selected list size is " + attributeList.size());


    }

    public int getSelectedQuantity() {
        int count = 0;
        for (Map.Entry<Integer, FinalProductAttribute> entry : finalSelectedProduct.selectedAttrbiteMap.entrySet()) {
            count = count + entry.getValue().selectedQuanitity;
        }

        return count;
    }

    public Map<Integer, FinalProductAttribute> getSelectedItemMap() {

        return finalSelectedProduct.selectedAttrbiteMap;
    }

    class FinalSelectedProduct {
        public Map<Integer, FinalProductAttribute> selectedAttrbiteMap = new HashMap<>();
    }


    public class FinalProductAttribute {

        public int productID;
        public int price;
        public String firstAttribute;
        public String secondAttribute;
        public int selectedQuanitity;
        public boolean isSampleType;
        public boolean isSaleAllowed;
        public int stock;

    }
}