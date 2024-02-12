package com.partseazy.android.ui.fragments.product;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.product.attribute.ProductAttribColumnAdapter;
import com.partseazy.android.ui.adapters.product.attribute.ProductAttribTabAdapter;
import com.partseazy.android.ui.model.productdetail.AttrProduct;
import com.partseazy.android.ui.model.productdetail.Product;
import com.partseazy.android.ui.model.productdetail.ProductAttribute;
import com.partseazy.android.ui.model.productdetail.ProductBag;
import com.partseazy.android.ui.model.productdetail.Product_;
import com.partseazy.android.ui.model.productdetail.TabbedProductAttribute;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaurav on 06/01/17.
 */

public class ProductAttributeAdapterFactory implements
        ProductAttribColumnAdapter.onQuantityChangedListener,
        ProductDetailFragment.OnAttributeItemSelectedListener, View.OnClickListener {

    protected NetworkImageView productIconIV;
    protected RecyclerView columRV;
    protected RecyclerView tab1RV;
    private Context mContext;
    protected RecyclerView tab2RV;
    private TextView firstAttributeListTV, secondAttributeListTV, totalPriceHeadingTV;
    private TextView productNameTV, productPriceTV, marginTV;
    private TextView totalQuantityTV, totalPiecesTV;
    private TextView minusBT, plusBT;
    private TextView quantityET;
    private TextView tab1TV, tab2TV;
    private TextView sameAttrValueTV, sameAttrNameTV;
    private TextView noAttrItemPriceTV, moqTV;
    private String tab1Name, tab2Name, column1Name, column2Name, column1Value;
    private List<TabbedProductAttribute> tab1List, tab2List;
    private List<ProductAttribute> columnList;
    private ProductDetailFragment.ATTRIBUTE_VIEW mAttribute_view;
    private LinearLayout firstAttributeLL, secondAttributeLL, noAttributeLL, attributeNameLL, sameAttributeLL;
    private List<Product_> products;
    private int moq;
    private int packOf;
    private int MAXQTY;
    private ImageLoader imageLoader;
    private Product_ productNoAttribute;
    private int totalPrice = 0;
    private int totalQuantity = 0;
    private int productNoAttributePrice = 0;
    private boolean isSameValueAttriInColumn1 = false;
    private Product productDetailObj;
    private boolean isCartLaunch;
    protected Button addToCartBTN;
    protected Button buyBTN;
    private String topAttriName;
    private String topAttriValue;


    private ProductAttribTabAdapter firstTabAdapter, secondTabAdapter;
    private ProductAttribColumnAdapter productAttribColumnAdapter;

    public ProductAttributeAdapterFactory(Context context,
                                          Product products,
                                          boolean isCartLaunch,
                                          View attribDialogView) {
        mContext = context;
        this.products = products.products;
        this.MAXQTY = products.productMaster.maxQty;
        this.productDetailObj = products;
        this.isCartLaunch = isCartLaunch;
        this.moq = products.productMaster.minQty;
        this.packOf = products.productMaster.packOf;
        CustomLogger.d("Pack of " + packOf);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        initiliseAllViews(attribDialogView);
        mAttribute_view = getDiloagType(products);
        setTopLevelView(products);

        if (this.products.size() != 0)
            parseAndSetView(products);

        addQuantity();


    }

    private void initiliseAllViews(View view) {
        columRV = (RecyclerView) view.findViewById(R.id.scrollable);
        tab2RV = (RecyclerView) view.findViewById(R.id.secondAttributeRecylerView);
        tab1RV = (RecyclerView) view.findViewById(R.id.firstAttributeRecylerView);
        firstAttributeListTV = (TextView) view.findViewById(R.id.firstAttributeListTV);
        secondAttributeListTV = (TextView) view.findViewById(R.id.secondAttributeListTV);
        totalPriceHeadingTV = (TextView) view.findViewById(R.id.totalPriceHeadingTV);
        firstAttributeLL = (LinearLayout) view.findViewById(R.id.firstAttributeLL);
        secondAttributeLL = (LinearLayout) view.findViewById(R.id.secondAttributeLL);
        tab1TV = (TextView) view.findViewById(R.id.tab1NameTV);
        tab2TV = (TextView) view.findViewById(R.id.tab2NameTV);
        productNameTV = (TextView) view.findViewById(R.id.productNameTV);
        sameAttributeLL = (LinearLayout) view.findViewById(R.id.sameAttributeLL);
        sameAttrValueTV = (TextView) view.findViewById(R.id.sameAttrValueTV);
        sameAttrNameTV = (TextView) view.findViewById(R.id.sameAttrNameTV);
        productPriceTV = (TextView) view.findViewById(R.id.productPriceTV);
        totalQuantityTV = (TextView) view.findViewById(R.id.totalQuantityTV);
        totalPiecesTV = (TextView) view.findViewById(R.id.totalPiecesTV);
        noAttrItemPriceTV = (TextView) view.findViewById(R.id.noAttrItemPriceTV);
        moqTV = (TextView) view.findViewById(R.id.moqTV);
        productIconIV = (NetworkImageView) view.findViewById(R.id.productIconIV);
        marginTV = (TextView) view.findViewById(R.id.marginTV);
        addToCartBTN = (Button) view.findViewById(R.id.addToCartBTN);
        buyBTN = (Button) view.findViewById(R.id.buyBTN);


        noAttributeLL = (LinearLayout) view.findViewById(R.id.noAttributeLL);
        attributeNameLL = (LinearLayout) view.findViewById(R.id.attributeNameLL);
        minusBT = (TextView) view.findViewById(R.id.minusBT);
        plusBT = (TextView) view.findViewById(R.id.plusBT);
        quantityET = (TextView) view.findViewById(R.id.quantityET);
        minusBT.setOnClickListener(this);
        plusBT.setOnClickListener(this);
        addToCartBTN.setOnClickListener(this);
        buyBTN.setOnClickListener(this);
        noAttrItemPriceTV.setOnClickListener(this);

    }

    private void setTopLevelView(Product product) {

        String productPrice = CommonUtility.getPriceRangeInHTMLforPDP(product.min, product.max);
        productNameTV.setText(product.productMaster.name);
        marginTV.setText(mContext.getString(R.string.margin, product.margin + ""));
        if (product.margin == 0) {
            marginTV.setVisibility(View.GONE);
        }
//        productPriceTV.setText(mContext.getString(R.string.price_per_piece_string, productPrice, product.type));
        if (product.productMaster.format.equals(AppConstants.INQUIRE_PRODUCT)) {
            productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
        } else {
            productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece_tax_extra, productPrice)));
        }
        totalPriceHeadingTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(0)));
        totalQuantityTV.setText(mContext.getString(R.string.quantityValue, 0));
        totalPiecesTV.setText(mContext.getString(R.string.quantityValue, 0));
        if (product.productMaster.packOf > 1) {
            moqTV.setText(mContext.getString(R.string.MOQ_Pack_contains, product.productMaster.minQty, product.productMaster.packOf));
        } else {
            moqTV.setText(mContext.getString(R.string.MOQ_no_Pack_contains, product.productMaster.minQty));
        }
        if (product.productMaster.images != null) {
            String formatedURL = CommonUtility.getFormattedImageUrl(product.productMaster.images.get(0).src, productIconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, productIconIV, formatedURL);

        }


    }

    private void parseAndSetView(Product productDetail) {
        if (productDetail.attrProduct.size() > 0) {
            parseAttribList(productDetail.products);
            setAdapter();
            columRV.setVisibility(View.VISIBLE);
            attributeNameLL.setVisibility(View.VISIBLE);
            noAttributeLL.setVisibility(View.GONE);
        } else {
            productNoAttribute = products.get(0);
            productNoAttributePrice = productNoAttribute.price;
            noAttrItemPriceTV.setText(mContext.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(productNoAttributePrice * packOf)));
            columRV.setVisibility(View.GONE);
            attributeNameLL.setVisibility(View.GONE);
            secondAttributeLL.setVisibility(View.GONE);
            firstAttributeLL.setVisibility(View.GONE);
            noAttributeLL.setVisibility(View.VISIBLE);

        }
    }

    private boolean isSameAttributeInColumn1(List<ProductAttribute> columnList) {
        if (columnList != null && columnList.size() > 0) {
            column1Value = columnList.get(0).value;
            CustomLogger.d("ColumnName " + column1Value);
            for (int i = 0; i < columnList.size(); i++) {
                String attributeName = columnList.get(i).value;
                if (column1Value != null && attributeName != null) {
                    if (!column1Value.equals(attributeName)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void parseAttribList(List<Product_> products) {


        for (Product_ product_ : products) {

            ProductAttribute productAttribute = new ProductAttribute();

            for (ProductBag item : product_.productBag) {

                if (tab1Name != null && item.name.equals(tab1Name)) {
                    tab1List = addProductIdToList(tab1List, item, product_);
                }

                if (tab2Name != null && item.name.equals(tab2Name)) {
                    tab2List = addProductIdToList(tab2List, item, product_);
                }

                //Either of bellow would be always filled

                if (column1Name != null && item.name.equals(column1Name)) {
                    productAttribute.name = item.name;
                    productAttribute.value = item.value;
                }

                if (column2Name != null && item.name.equals(column2Name)) {
                    productAttribute.name1 = item.name;
                    productAttribute.value1 = item.value;
                }
                CustomLogger.d("Attribute Value" + product_.price);
                productAttribute.stockQuantity = product_.stock;
                productAttribute.productID = product_.skuId;
                productAttribute.isSelected = false;
                productAttribute.price = product_.price;
                productAttribute.minQuantity = product_.minQty;
                productAttribute.isSaleAllowed = (product_.allowSale == 1) ? true : false;
                productAttribute.isSampleProduct = (product_.allowSample == 1) ? true : false;


            }

            if (columnList == null)
                columnList = new ArrayList<ProductAttribute>();

            columnList.add(productAttribute);


        }


    }

    private List<TabbedProductAttribute> addProductIdToList(List<TabbedProductAttribute> tabbedList, ProductBag item, Product_ product_) {

        if (tabbedList == null)
            tabbedList = new ArrayList<TabbedProductAttribute>();

        boolean isFound = false;
        if (tabbedList.size() > 0) {
            for (TabbedProductAttribute attribute : tabbedList) {

                if (attribute.value != null && attribute.value.equals(item.value)) {
                    attribute.productIDList.add(product_.skuId);
                    isFound = true;
                    break;
                }
            }
        }

        if (tabbedList.size() == 0 || !isFound) {
            List<Integer> idList = new ArrayList<>();
            idList.add(product_.skuId);
            tabbedList.add(new TabbedProductAttribute(item.name, item.value, idList, false));

        }

        return tabbedList;
    }


    private void setAdapter() {


        firstAttributeLL.setVisibility(View.GONE);
        secondAttributeLL.setVisibility(View.GONE);

        if (tab1Name != null) {
            String constant = (tab1Name.equals("Color") ? AppConstants.ATTRIBUTE_COLOR_TAG : AppConstants.ATTRIBUTE_TAG);
            firstTabAdapter = new ProductAttribTabAdapter(mContext, tab1List, constant, this);
            tab1RV.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            tab1RV.setAdapter(firstTabAdapter);
            firstAttributeLL.setVisibility(View.VISIBLE);


        }

        if (tab2Name != null) {
            String constant = (tab2Name.equals("Color") ? AppConstants.ATTRIBUTE_COLOR_TAG : AppConstants.ATTRIBUTE_TAG);
            secondTabAdapter = new ProductAttribTabAdapter(mContext, tab2List, constant, this);
            tab2RV.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            tab2RV.setAdapter(secondTabAdapter);
            secondAttributeLL.setVisibility(View.VISIBLE);

        }

        boolean areTwoColumns = (column2Name != null) ? true : false;

//        Collections.sort(columnList, new ProductStockComparator());
//        isSameValueAttriInColumn1 = isSameAttributeInColumn1(columnList);
//        CustomLogger.d("isSameVAlue  ::" + isSameValueAttriInColumn1);
        productAttribColumnAdapter = new ProductAttribColumnAdapter(mContext, columnList, isSameValueAttriInColumn1, areTwoColumns, moq, packOf, MAXQTY, this, this);
        columRV.setLayoutManager(new LinearLayoutManager(mContext));
        columRV.setNestedScrollingEnabled(false);
        columRV.setAdapter(productAttribColumnAdapter);

        if (isSameValueAttriInColumn1) {
            sameAttributeLL.setVisibility(View.VISIBLE);
            sameAttrNameTV.setText(topAttriName + " : ");
            sameAttrValueTV.setText(topAttriValue);
            firstAttributeListTV.setVisibility(View.GONE);
            if (areTwoColumns) {
                secondAttributeListTV.setVisibility(View.VISIBLE);
            } else {
                secondAttributeListTV.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onQuantityChanged(int totalSum, int totalQuantity) {
        CustomLogger.d("total Price ::" + totalSum + "total Quanity :" + totalQuantity);
        int totalPieces = totalQuantity * productDetailObj.productMaster.packOf;
        totalPriceHeadingTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(totalSum)));
        totalQuantityTV.setText(mContext.getString(R.string.quantityValue, totalQuantity));
        totalPiecesTV.setText(mContext.getString(R.string.quantityValue, totalPieces));
    }

    public void quantityChangedNoAttribute(int totalPrice, int totalQuantity, int etQuantity) {
        int totalPieces = totalQuantity * productDetailObj.productMaster.packOf;
        totalPriceHeadingTV.setText(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(totalPrice)));
        totalQuantityTV.setText(mContext.getString(R.string.quantityValue, totalQuantity));
        totalPiecesTV.setText(mContext.getString(R.string.quantityValue, totalPieces));
        quantityET.setText(etQuantity + "");
    }

    private ProductDetailFragment.ATTRIBUTE_VIEW getDiloagType(Product pdpData) {

        int tabCount = 0, totalAttribCount = 0;

        if (pdpData.attrProduct != null && pdpData.attrProduct.size() > 0) {
            for (AttrProduct attrProduct : pdpData.attrProduct) {

                tabCount = (attrProduct.tabbed == 1) ? tabCount + 1 : tabCount;

                if (attrProduct.tabbed == 1) {
                    if (tab1Name == null) {
                        tab1Name = attrProduct.name;
                        tab1TV.setText(tab1Name);

                    } else {
                        tab2Name = attrProduct.name;
                        tab2TV.setText(tab2Name);

                    }
                } else {

                    if (column1Name == null) {
                        column1Name = attrProduct.name;
                        firstAttributeListTV.setText(column1Name);
                    } else {
                        column2Name = attrProduct.name;
                        secondAttributeListTV.setText(column2Name);
                        secondAttributeListTV.setVisibility(View.VISIBLE);
                    }

                }

                if (attrProduct.ditto) {
                    if (attrProduct.name != null) {
                        if (column1Name != null && column1Name.equals(attrProduct.name)) {
                            isSameValueAttriInColumn1 = true;
                            topAttriName = attrProduct.name;
                            topAttriValue = attrProduct._default;
                        }
                        if (tab1Name != null && tab1Name.equals(attrProduct.name)) {
                            topAttriName = attrProduct.name;
                            topAttriValue = attrProduct._default;
                        }
                    }
                }

                ++totalAttribCount;
            }
        }


        if (tabCount == 0 && totalAttribCount == 2) {
            return ProductDetailFragment.ATTRIBUTE_VIEW.NOTAB_TWOCOLUMN;
        }

        if (tabCount == 1 && totalAttribCount == 2) {
            return ProductDetailFragment.ATTRIBUTE_VIEW.ONETAB_ONECOLUM;
        }

        if (tabCount == 1 && totalAttribCount == 3) {
            return ProductDetailFragment.ATTRIBUTE_VIEW.ONETAB_TWOCOLUMN;
        }

        if (tabCount == 2 && totalAttribCount == 1) {
            return ProductDetailFragment.ATTRIBUTE_VIEW.TWOTAB_ONECOLUM;
        }

        if (tabCount == 0 && totalAttribCount == 1) {
            return ProductDetailFragment.ATTRIBUTE_VIEW.NOTAB_ONECOLUMN;
        }

        CustomLogger.d("totalAttribCount ::" + totalAttribCount + "tabCount:" + tabCount);
        return ProductDetailFragment.ATTRIBUTE_VIEW.NOTAB_NOCOLUMN;


    }

    @Override
    public void onItemSelected(List<Integer> productIDList, String originator) {

        productAttribColumnAdapter.onSelectedProductIDs(productIDList, originator);

    }


    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.addToCartBTN || (view.getId() == R.id.buyBTN)) {

            boolean shouldLaunchCart = (view.getId() == R.id.addToCartBTN) ? false : true;


            if (productDetailObj.attrProduct.size() == 0) {
                addProductToCardProduct(shouldLaunchCart);
            } else {
                getProductToAdd(productAttribColumnAdapter.getSelectedItemMap(), shouldLaunchCart);
            }

        }


        if (view.getId() == R.id.plusBT) {

            addQuantity();
        }

        if (view.getId() == R.id.minusBT) {
            int etQuantity = Integer.parseInt(quantityET.getText().toString());
            if (etQuantity > 0) {

                if (etQuantity == productNoAttribute.minQty) {

                    etQuantity = 0;
                    totalPrice = totalPrice - productNoAttributePrice * productNoAttribute.minQty * packOf;
                    totalQuantity = totalQuantity - productNoAttribute.minQty;

                } else {
                    etQuantity--;
                    totalPrice = totalPrice - productNoAttributePrice * packOf;
                    totalQuantity = totalQuantity - 1;
                }

                //  etQuantity--;
                if (etQuantity == 0) {
                    etQuantity = 0;
                }
                // totalPrice = totalPrice - productNoAttributePrice*packOf;
                // totalQuantity--;

                totalPrice = (totalPrice < 0) ? 0 : totalPrice;
                totalQuantity = (totalQuantity < 0) ? 0 : totalQuantity;
                if (etQuantity == 0) {
                    noAttrItemPriceTV.setText(mContext.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(productNoAttributePrice * packOf)));
                } else {
                    noAttrItemPriceTV.setText(mContext.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalPrice)));
                }
                quantityChangedNoAttribute(totalPrice, totalQuantity, etQuantity);
            }
        }

        if (view.getId() == R.id.noAttrItemPriceTV) {
            if (DataStore.isAgentType(mContext)) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.GET_PRODUCT_SKU_PRICE_DETAIL,
                        String.valueOf(productNoAttribute.skuId));
            }
        }
    }

//    void handleEditBoxClicks() {
//        final String oldText = quantityET.getText().toString();
//
//        quantityET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
//
//                    quantityET.clearFocus();
//
//                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                    if (!TextUtils.isEmpty(quantityET.getText().toString())) {
//
//                        int quantity = Integer.parseInt(quantityET.getText().toString());
//                        CustomLogger.d("quantity is " + quantity);
//
//
//                    } else {
//
//                        quantityET.setText("1");
//                    }
//
//                }
//                return false;
//            }
//        });
//
//
//        quantityET.setOnEditTextImeBackListener(new EditTextImeBackListener() {
//            @Override
//            public void onImeBack(NumberPicketEditText ctrl, String text) {
//                if (!TextUtils.isEmpty(text.trim())) {
//                    if (oldText != null) {
//                        quantityET.clearFocus();
//                        int quantity = Integer.parseInt(text);
//
//
//                    } else {
//
//                        quantityET.clearFocus();
//                    }
//                } else {
//                    quantityET.setText("1");
//                }
//            }
//        });
//    }

    private void addProductToCardProduct(boolean shouldLaunchCart) {
        try {
            int productSKUID = productNoAttribute.skuId;

            //TODO: should apply logic for isSale allowed

            if (totalQuantity < moq) {

                Toast.makeText(mContext, mContext.getString(R.string.add_min_quantity, moq), Toast.LENGTH_SHORT).show();
            } else {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.NO_ATTRIBUTE_PRODUCT_SELECTED, productSKUID, totalQuantity, shouldLaunchCart);
            }
        } catch (Exception e) {
            CustomLogger.e("Exception ", e);
        }
    }

    private void getProductToAdd(final Map<Integer, ProductAttribColumnAdapter.FinalProductAttribute> selectedAttributeMap, final boolean shouldLaunchCart) {

        if (selectedAttributeMap == null)
            return;

        final ProductToAddInCart productToAddInCart = new ProductToAddInCart();

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                for (Map.Entry<Integer, ProductAttribColumnAdapter.FinalProductAttribute> entry : selectedAttributeMap.entrySet()) {
                    productToAddInCart.selectedProductSKUIds.add(entry.getValue().productID);
                    productToAddInCart.selectedProductQuantity.add(entry.getValue().selectedQuanitity);
                    if (!entry.getValue().isSaleAllowed && productToAddInCart.isSaleAllowed != null)
                        productToAddInCart.isSaleAllowed.add(entry.getValue().isSaleAllowed);
                    productToAddInCart.type = entry.getValue().isSampleType;

                }

                if (productAttribColumnAdapter.getSelectedQuantity() < moq) {

                    Toast.makeText(mContext, mContext.getString(R.string.add_min_quantity, moq), Toast.LENGTH_SHORT).show();
                } else {

                    PartsEazyEventBus.getInstance().postEvent(EventConstant.PRODUCT_ATTRIBUTE_SELECTED, productToAddInCart, shouldLaunchCart);
                }

            }
        });

    }

    class ProductToAddInCart {

        List<Integer> selectedProductSKUIds = new ArrayList<>();
        List<Integer> selectedProductQuantity = new ArrayList<>();
        List<Boolean> isSaleAllowed;
        boolean type;

    }


    void addQuantity()
    {
        int etQuantity = Integer.parseInt(quantityET.getText().toString());
        if (etQuantity >= productNoAttribute.stock) {

            Toast.makeText(mContext, mContext.getString(R.string.stock_max_message, productNoAttribute.stock), Toast.LENGTH_SHORT).show();
            return;
        }

        if (etQuantity == 0) {
            etQuantity = etQuantity + productNoAttribute.minQty;
            totalPrice = totalPrice + productNoAttributePrice * packOf * productNoAttribute.minQty;
            totalQuantity = totalQuantity + productNoAttribute.minQty;
        } else {
            etQuantity++;
            totalPrice = totalPrice + productNoAttributePrice * packOf;
            totalQuantity = totalQuantity + 1;
        }

//
//            totalQuantity++;
//            totalPrice = totalPrice + productNoAttributePrice*packOf;
        noAttrItemPriceTV.setText(mContext.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(totalPrice)));
        quantityChangedNoAttribute(totalPrice, totalQuantity, etQuantity);
    }


}
