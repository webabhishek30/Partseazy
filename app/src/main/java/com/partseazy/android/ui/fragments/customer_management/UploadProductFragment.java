package com.partseazy.android.ui.fragments.customer_management;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.categoryleveltwo.Category;
import com.partseazy.android.ui.model.categoryleveltwo.L3CategoryList;
import com.partseazy.android.ui.model.customer_management.BrandList;
import com.partseazy.android.ui.model.customer_management.Product;
import com.partseazy.android.ui.model.customer_management.ProductList;
import com.partseazy.android.ui.model.customer_management.ProductMasterList;
import com.partseazy.android.ui.model.customer_management.ProductSKU;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.productdetail.Brand;
import com.partseazy.android.ui.model.productdetail.ProductMaster;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shubhang on 14/02/18.
 */

public class UploadProductFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.categorySpinner)
    protected Spinner categorySpinner;

    @BindView(R.id.brandSpinner)
    protected Spinner brandSpinner;

    @BindView(R.id.masterSpinner)
    protected Spinner masterSpinner;

    @BindView(R.id.productSpinner)
    protected Spinner productSpinner;

    @BindView(R.id.priceET)
    protected EditText priceET;

    @BindView(R.id.costET)
    protected EditText costET;

    @BindView(R.id.stockET)
    protected EditText stockET;

    @BindView(R.id.continueBT)
    protected Button continueBT;

    @BindView(R.id.priceLL)
    protected LinearLayout priceLL;

    private ArrayAdapter catAdapter;
    private List<String> categories;
    private List<Category> categoryData;

    private ArrayAdapter brandAdapter;
    private List<String> brands;
    private List<Brand> brandsData;

    private ArrayAdapter masterAdapter;
    private List<String> masters;
    private List<ProductMaster> masterData;

    private ArrayAdapter productAdapter;
    private List<String> products;
    private List<Product> prodData;

    private int selectedCat = 0;
    private int selectedBrand = 0;
    private int selectedMaster = 0;
    private int selectedProduct = 0;

    private boolean gotPrice;
    private boolean gotCost;
    private boolean gotStock;

    private final String HINT_CATEGORY = "Select Category";
    private final String HINT_BRAND = "Select Brand";
    private final String HINT_PRODUCT = "Select Product";
    private final String HINT_SPECIFICATION = "Select Specification";
    private final static String SUPPLIER_ID = "supplier_id";

    private int suppID;

    public static UploadProductFragment newInstance(int suppId) {
        Bundle bundle = new Bundle();
        bundle.putInt(SUPPLIER_ID, suppId);
        UploadProductFragment fragment = new UploadProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_upload_product;
    }

    public static String getTagName() {
        return UploadProductFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.upload_product);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCategoryList();
        loadBrandList();

        suppID = getArguments().getInt(SUPPLIER_ID);

        masters = new ArrayList<>();
        masters.add(HINT_PRODUCT);

        products = new ArrayList<>();
        products.add(HINT_SPECIFICATION);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();

        setupAdapter("master");
        setupAdapter("product");

        masterSpinner.setEnabled(false);
        productSpinner.setEnabled(false);

        priceET.addTextChangedListener(new EditTextWatcher(priceET));
        costET.addTextChangedListener(new EditTextWatcher(costET));
        stockET.addTextChangedListener(new EditTextWatcher(stockET));

        continueBT.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == continueBT.getId()) {
            int cost = CommonUtility.convertionRupeeToPaise(Integer.parseInt(costET.getText().toString()));
            int price = CommonUtility.convertionRupeeToPaise(Integer.parseInt(priceET.getText().toString()));
            int stock = Integer.parseInt(stockET.getText().toString());
            if (price < cost) {
                showError("Price should be greater than cost", MESSAGETYPE.TOAST);
                return;
            }

            showProgressDialog();
            Map params = WebServicePostParams.insertSku(selectedProduct, suppID, price, cost, stock);
            getNetworkManager().PostRequest(RequestIdentifier.POST_SKU.ordinal(),
                    WebServiceConstants.POST_SKU,
                    params, null, this, this, false);
        }
    }

    class EditTextWatcher implements TextWatcher {

        private EditText editText;

        private EditTextWatcher(EditText view) {
            this.editText = view;
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
                    }
                    break;

                case R.id.costET:
                    gotCost = false;
                    if (!s.toString().isEmpty()) {
                        gotCost = true;
                    }
                    break;

                case R.id.stockET:
                    gotStock = false;
                    if (!s.toString().isEmpty()) {
                        gotStock = true;
                    }
                    break;
            }
            checkBtnStatus();
        }
    }

    private void checkBtnStatus() {
        if (gotPrice && gotCost && gotStock && selectedProduct != 0) {
            continueBT.setEnabled(true);
        } else {
            continueBT.setEnabled(false);
        }
    }

    private void loadCategoryList() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_L3_CATS.ordinal(),
                WebServiceConstants.L3_LIST, null, params, this, this, false);
    }

    private void loadBrandList() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_BRANDS.ordinal(),
                WebServiceConstants.GET_BRANDS, null, params, this, this, false);
    }

    private void loadMasters() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        String url = WebServiceConstants.GET_PRODUCT_MASTERS + "?";
        if (selectedCat != 0) {
            url += "category_id=" + String.valueOf(selectedCat) + "&";
        }
        if (selectedBrand != 0) {
            url += "brand_id=" + String.valueOf(selectedBrand);
        }
        getNetworkManager().GetRequest(RequestIdentifier.GET_PRODUCT_MASTERS.ordinal(),
                url, null, params, this, this, false);
    }

    private void loadProducts() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.GET_PRODUCTS.ordinal(),
                WebServiceConstants.GET_PRODUCTS + "?active=1&product_master_id=" +
                        String.valueOf(selectedMaster), null, params, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.GET_L3_CATS.ordinal()
                || request.getIdentifier() == RequestIdentifier.GET_BRANDS.ordinal()
                || request.getIdentifier() == RequestIdentifier.GET_PRODUCT_MASTERS.ordinal()
                || request.getIdentifier() == RequestIdentifier.GET_PRODUCTS.ordinal()
                || request.getIdentifier() == RequestIdentifier.POST_SKU.ordinal()) {
            if (apiError != null) {
                showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
            } else {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.GET_L3_CATS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), L3CategoryList.class, new OnGsonParseCompleteListener<L3CategoryList>() {
                        @Override
                        public void onParseComplete(L3CategoryList data) {
                            if (data != null) {
                                hideProgressDialog();
                                categoryData = data.result;
                                categories = new ArrayList<>();
                                categories.add(HINT_CATEGORY);
                                for (Category cat : data.result) {
                                    categories.add(cat.name);
                                }
                                setupAdapter("category");
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        if (request.getIdentifier() == RequestIdentifier.GET_BRANDS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), BrandList.class, new OnGsonParseCompleteListener<BrandList>() {
                @Override
                public void onParseComplete(BrandList data) {
                    if (data != null) {
                        hideProgressDialog();
                        brandsData = data.result;
                        brands = new ArrayList<>();
                        brands.add(HINT_BRAND);
                        for (Brand br : data.result) {
                            brands.add(br.name);
                        }
                        setupAdapter("brand");
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    hideProgressDialog();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }

        if (request.getIdentifier() == RequestIdentifier.GET_PRODUCT_MASTERS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), ProductMasterList.class, new OnGsonParseCompleteListener<ProductMasterList>() {
                @Override
                public void onParseComplete(ProductMasterList data) {
                    if (data != null && data.result.size() != 0) {
                        hideProgressDialog();
                        masterData = data.result;
                        masters = new ArrayList<>();
                        masters.add(HINT_PRODUCT);
                        for (ProductMaster pm : data.result) {
                            masters.add(pm.name);
                        }
                        setupAdapter("master");
                    } else {
                        clearMasterAdapter();
                        clearProductAdapter();
                        showError("No products found", MESSAGETYPE.TOAST);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    hideProgressDialog();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }

        if (request.getIdentifier() == RequestIdentifier.GET_PRODUCTS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), ProductList.class, new OnGsonParseCompleteListener<ProductList>() {
                @Override
                public void onParseComplete(ProductList data) {
                    if (data != null && data.result.size() != 0) {
                        hideProgressDialog();
                        prodData = data.result;
                        products = new ArrayList<>();
                        products.add(HINT_SPECIFICATION);
                        for (Product p : data.result) {
                            StringBuilder sb = new StringBuilder();
                            try {
                                Collection c = p.bag.values();
                                Iterator itr = c.iterator();
                                while (itr.hasNext()) {
                                    sb = sb.append((String) itr.next()).append(" ");
                                }
                            } catch (Exception e) {
                                CustomLogger.e("Exception ", e);
                            }
                            products.add(sb.toString());
                        }
                        setupAdapter("product");
                    } else {
                        clearProductAdapter();
                        showError("No products found", MESSAGETYPE.TOAST);
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    hideProgressDialog();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }

        if (request.getIdentifier() == RequestIdentifier.POST_SKU.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), ProductSKU.class, new OnGsonParseCompleteListener<ProductSKU>() {
                @Override
                public void onParseComplete(ProductSKU data) {
                    hideProgressDialog();
                    if (data != null) {
                        DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.product_uploaded), getString(R.string.OK)
                                , null, null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        ((BaseActivity) getActivity()).onPopBackStack(false);
                                    }
                                });
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    hideProgressDialog();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }

        return true;
    }

    private void clearMasterAdapter() {
        if (masterAdapter != null) {
            masterAdapter.clear();
            masters.add(HINT_PRODUCT);
            masterAdapter.notifyDataSetChanged();
        }
        masterSpinner.setEnabled(false);
    }

    private void clearProductAdapter() {
        if (productAdapter != null) {
            productAdapter.clear();
            products.add(HINT_SPECIFICATION);
            productAdapter.notifyDataSetChanged();
        }
        productSpinner.setEnabled(false);
        priceLL.setVisibility(View.GONE);
    }

    private void setupAdapter(String source) {
        switch (source) {
            case "category":
                catAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_dropdown_item_1line, categories);
                catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(catAdapter);
                categorySpinner.setOnItemSelectedListener(this);
                break;

            case "brand":
                brandAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_dropdown_item_1line, brands);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);
                brandSpinner.setOnItemSelectedListener(this);
                break;

            case "master":
                masterAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_dropdown_item_1line, masters);
                masterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                masterSpinner.setAdapter(masterAdapter);
                masterSpinner.setOnItemSelectedListener(this);
                masterSpinner.setEnabled(true);
                break;
            case "product":
                productAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_dropdown_item_1line, products);
                productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productSpinner.setAdapter(productAdapter);
                productSpinner.setOnItemSelectedListener(this);
                productSpinner.setEnabled(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.categorySpinner:
                if (position > 0) {
                    selectedCat = categoryData.get(position - 1).id;
                    loadMasters();
                } else if (selectedBrand == 0) {
                    selectedCat = 0;
                    clearMasterAdapter();
                    clearProductAdapter();
                } else {
                    selectedCat = 0;
                    loadMasters();
                }
                break;
            case R.id.brandSpinner:
                if (position > 0) {
                    selectedBrand = brandsData.get(position - 1).id;
                    loadMasters();
                } else if (selectedCat == 0) {
                    selectedBrand = 0;
                    clearMasterAdapter();
                    clearProductAdapter();
                } else {
                    selectedBrand = 0;
                    loadMasters();
                }
                break;
            case R.id.masterSpinner:
                if (position > 0) {
                    selectedMaster = masterData.get(position - 1).id;
                    loadProducts();
                } else {
                    selectedMaster = 0;
                    clearProductAdapter();
                }
                break;
            case R.id.productSpinner:
                if (position > 0) {
                    selectedProduct = prodData.get(position - 1).id;
                    priceLL.setVisibility(View.VISIBLE);
                } else {
                    selectedProduct = 0;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
//    public class CategoryAdapter extends ArrayAdapter<Category> {
//
//        private final LayoutInflater mInflater;
//        private final Context mContext;
//        private final List<Category> items;
//        private final int mResource;
//        private Filter filter;
//
//        public CategoryAdapter(@NonNull Context context, @LayoutRes int resource,
//                               @NonNull List objects) {
//            super(context, resource, 0, objects);
//
//            mContext = context;
//            mInflater = LayoutInflater.from(context);
//            mResource = resource;
//            items = objects;
//        }
//
//        @Override
//        public View getDropDownView(int position, @Nullable View convertView,
//                                    @NonNull ViewGroup parent) {
//            return createItemView(position, convertView, parent);
//        }
//
//        @Override
//        public @NonNull
//        View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return createItemView(position, convertView, parent);
//        }
//
//        private View createItemView(int position, View convertView, ViewGroup parent) {
//            final View view = mInflater.inflate(mResource, parent, false);
//            TextView catName = (TextView) view.findViewById(android.R.id.text1);
//            Category cat = items.get(position);
//            catName.setText(cat.name);
//
//            return view;
//        }
//
//        @Override
//        public Filter getFilter() {
//            if (filter == null) {
//                filter = new CategoryFilter();
//            }
//            return filter;
//        }
//    }
//
//    private class CategoryFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Category> list = new ArrayList<Category>(categories);
//            FilterResults result = new FilterResults();
//            String substr = constraint.toString().toLowerCase();
//            // if no constraint is given, return the whole list
//            if (substr.length() == 0) {
//                result.values = list;
//                result.count = list.size();
//            } else {
//                final ArrayList<Category> retList = new ArrayList<Category>();
//                for (Category cat : list) {
//                    try {
//                        if (cat.name.toLowerCase().contains(constraint)) {
//                            retList.add(cat);
//                        }
//                    } catch (Exception e) {
//                        CustomLogger.e("Exception ", e);
//                    }
//                }
//                result.values = retList;
//                result.count = retList.size();
//            }
//            return result;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            // we clear the adapter and then pupulate it with the new results
//            adapter.clear();
//            if (results.count > 0) {
//                for (Category o : (ArrayList<Category>) results.values) {
//                    adapter.add(o);
//                }
//            }
//        }
//    }
}
