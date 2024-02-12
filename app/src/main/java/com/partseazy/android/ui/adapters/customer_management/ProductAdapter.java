package com.partseazy.android.ui.adapters.customer_management;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.customer_management.CustomerProduct;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by shubhang on 16/02/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CustomerProduct> products;
    private BaseFragment baseFragment;

    private boolean gotPrice, gotCost, gotStock, update;

    public ProductAdapter(BaseFragment baseFragment, List<CustomerProduct> products) {
        this.products = products;
        mInflater = LayoutInflater.from(baseFragment.getContext());
        this.baseFragment = baseFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductAdapter.ProductViewHolder(mInflater.inflate(R.layout.row_customer_product_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProductAdapter.ProductViewHolder productViewHolder = (ProductAdapter.ProductViewHolder) holder;
        final CustomerProduct product = products.get(position);

        productViewHolder.priceET.addTextChangedListener(new EditTextWatcher(productViewHolder.priceET, product, productViewHolder.updateBTN));
        productViewHolder.costET.addTextChangedListener(new EditTextWatcher(productViewHolder.costET, product, productViewHolder.updateBTN));
        productViewHolder.stockET.addTextChangedListener(new EditTextWatcher(productViewHolder.stockET, product, productViewHolder.updateBTN));

        productViewHolder.productNameTV.setText(product.productName);
        productViewHolder.specificationTV.setText(product.specification);
        productViewHolder.priceET.setText(String.valueOf(CommonUtility.convertionPaiseToRupee(product.price)));
        productViewHolder.costET.setText(String.valueOf(CommonUtility.convertionPaiseToRupee(product.cost)));
        productViewHolder.stockET.setText(String.valueOf(product.stock));

        if (product.allowCustomerSale == 1) {
            productViewHolder.productSwitch.setChecked(true);
        }

        productViewHolder.updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.EDIT_PRODUCT, productViewHolder.priceET.getText().toString(),
                        productViewHolder.costET.getText().toString(), productViewHolder.stockET.getText().toString(),
                        product.productSkuId, product.productItemId);
            }
        });

        productViewHolder.productSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SWITCH_PRODUCT, isChecked, product.productSkuId);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class EditTextWatcher implements TextWatcher {

        private EditText editText;
        private CustomerProduct product;
        private Button updateBTN;

        private EditTextWatcher(EditText view, CustomerProduct product, Button updateBTN) {
            this.editText = view;
            this.product = product;
            this.updateBTN = updateBTN;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (editText.getId()) {
                case R.id.priceET:
                    gotPrice = false;
                    if (!s.toString().isEmpty()) {
                        gotPrice = true;
                        update = false;
                        if (!s.toString().equals(String.valueOf(CommonUtility.convertionPaiseToRupee(product.price)))) {
                            update = true;
                        }
                    }
                    break;

                case R.id.costET:
                    gotCost = false;
                    if (!s.toString().isEmpty()) {
                        gotCost = true;
                        update = false;
                        if (!s.toString().equals(String.valueOf(CommonUtility.convertionPaiseToRupee(product.cost)))) {
                            update = true;
                        }
                    }
                    break;

                case R.id.stockET:
                    gotStock = false;
                    if (!s.toString().isEmpty()) {
                        gotStock = true;
                        update = false;
                        if (!s.toString().equals(String.valueOf(product.stock))) {
                            update = true;
                        }
                    }
                    break;
            }
            checkBtnStatus(updateBTN);
        }
    }

    private void checkBtnStatus(Button updateBTN) {
        if (gotPrice && gotCost && gotStock && update) {
            updateBTN.setVisibility(View.VISIBLE);
        } else {
            updateBTN.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends BaseViewHolder {

        @BindView(R.id.productNameTV)
        protected TextView productNameTV;

        @BindView(R.id.specificationTV)
        protected TextView specificationTV;

        @BindView(R.id.productSwitch)
        protected Switch productSwitch;

        @BindView(R.id.priceET)
        protected EditText priceET;

        @BindView(R.id.costET)
        protected EditText costET;

        @BindView(R.id.stockET)
        protected EditText stockET;

        @BindView(R.id.updateBTN)
        protected Button updateBTN;

        public ProductViewHolder(View view) {
            super(view);
        }
    }
}