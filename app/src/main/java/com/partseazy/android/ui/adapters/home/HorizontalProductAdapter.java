package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.home.category.ProductData;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 27/12/16.
 */

public class HorizontalProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ProductData> productList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private FavouriteListener mFavouriteListener;
    private OnAddToCartReorder mAddToCartReorder;
    private int mAdapterIndex;
    private boolean reOrder;
    private String showAddToCart;
    public HorizontalProductAdapter(Context context, List<ProductData> productList, FavouriteListener favouriteListener, OnAddToCartReorder addToCartReorder, int adapterIndex, boolean reOrder, String showAddToCart) {
        this.productList = productList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(mContext).getImageLoader();
        mFavouriteListener = favouriteListener;
        mAddToCartReorder = addToCartReorder;
        mAdapterIndex = adapterIndex;
        this.reOrder = reOrder;
        this.showAddToCart = showAddToCart;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(mInflater.inflate(R.layout.row_product_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ProductViewHolder productViewHolder = (ProductViewHolder) holder;

        if (showAddToCart.equals("1"))
            productViewHolder.btnAddToCartReOrder.setVisibility(View.VISIBLE);
        else
            productViewHolder.btnAddToCartReOrder.setVisibility(View.GONE);

        final ProductData productData = productList.get(position);
        productViewHolder.productNameTV.setText(productData.productName);
        //final String productPrice = CommonUtility.getPriceRangeforPDP(productData.productPrice, productData.productPriceHigh);

        final String productPrice = String.valueOf(CommonUtility.convertionPaiseToRupee(productData.productPrice));
        final String mrpPrice =  CommonUtility.convertionPaiseToRupeeString(productData.productPriceHigh);
        if (productPrice != null && !productPrice.equals(0 + "")) {
            productViewHolder.priceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
            productViewHolder.priceTV.setVisibility(View.VISIBLE);
        } else {
            productViewHolder.priceTV.setVisibility(View.GONE);
        }

        if (productData.categoryId == AppConstants.REFURBISH_CAT_ID || productData.categoryId == AppConstants.REFURBISH_CAT_2_ID) {
            //  productViewHolder.refurbishIcon.setVisibility(View.VISIBLE);
            //     detailVH.refurbishIcon.setVisibility(View.VISIBLE);
            productViewHolder.refurbishIcon.setVisibility(View.GONE);
        } else {
            productViewHolder.refurbishIcon.setVisibility(View.GONE);
        }


        if (productData.isProductSku) {
            productViewHolder.productSkuDescTV.setVisibility(View.VISIBLE);
            productViewHolder.productSkuDescTV.setText(productData.productSkuDesc);

        } else {
            productViewHolder.productSkuDescTV.setVisibility(View.GONE);
        }

        if (productData.format.equals(AppConstants.INQUIRE_PRODUCT)) {
            productViewHolder.moqTV.setVisibility(View.GONE);
            productViewHolder.view_out_of_stock.setVisibility(View.GONE);
            productViewHolder.inquiryTV.setVisibility(View.VISIBLE);

        } else {
            //      productViewHolder.moqTV.setVisibility(View.VISIBLE);
            productViewHolder.inquiryTV.setVisibility(View.GONE);

            if (productData.inStock == 0) {
                productViewHolder.view_out_of_stock.setVisibility(View.VISIBLE);
            } else {
                productViewHolder.view_out_of_stock.setVisibility(View.GONE);

            }

            if (productData.packQty > 1) {
                productViewHolder.moqTV.setText(mContext.getString(R.string.MOQ, productData.minQty, mContext.getString(R.string.pack)));
            } else {
                productViewHolder.moqTV.setText(mContext.getString(R.string.MOQ, productData.minQty, mContext.getString(R.string.pcs)));
            }
        }

        //    productViewHolder.priceTV.setText(PartsEazyApplication.getInstance().getResources().getString(R.string.price_per_piece_string, productData.productPrice + "", "piece"));

        /*  if (productData.mrp > 0) {
                productInfoViewHolder.productMRPTV.setText(mContext.getString(R.string.MRP, CommonUtility.convertionPaiseToRupee(productDetailedData.mrp)));
                productInfoViewHolder.productMRPTV.setPaintFlags(productInfoViewHolder.productMRPTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productInfoViewHolder.productMRPTV.setVisibility(View.VISIBLE);
            }
            else
            {
                productInfoViewHolder.productMRPTV.setText(mContext.getString(R.string.MRP, CommonUtility.convertionPaiseToRupee(productDetailedData.mrp)));
                productInfoViewHolder.productMRPTV.setVisibility(View.VISIBLE);

            }*/

        if (productData.margin < 1) {
            productViewHolder.marginTV.setVisibility(View.GONE);
        } else {
            productViewHolder.marginTV.setVisibility(View.VISIBLE);
            productViewHolder.marginTV.setText(mContext.getString(R.string.margin_amount, productData.margin));
        }
        String formatedURL = null;
        if (productData.image != null) {
            formatedURL = CommonUtility.getFormattedImageUrl(productData.image, productViewHolder.productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
        }
        CommonUtility.setImageSRC(imageLoader, productViewHolder.productIconIV, formatedURL);

        final boolean isFavSelected = FavouriteUtility.isFavByProductMasterId(productData.productMasterId);
        productViewHolder.favouriteIconCB.setSelected(isFavSelected);
        if (mrpPrice != null && !mrpPrice.equals(0 + "")) {

        if (productData.productPriceHigh >  productData.productPrice) {
            productViewHolder.priceMRP.setText(mContext.getString(R.string.MRP_Price, mrpPrice));
            productViewHolder.priceMRP.setPaintFlags(productViewHolder.priceMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else if (productData.productPriceHigh == productData.productPrice)
        {
            productViewHolder.marginTV.setVisibility(View.GONE);
            productViewHolder.priceMRP.setText(mContext.getString(R.string.MRP_Price,
                    mrpPrice));
        }
        } else {
            productViewHolder.priceMRP.setVisibility(View.GONE);
        }

        if (isFavSelected) {
            productViewHolder.favouriteIconCB.setSelected(true);
            productViewHolder.favouriteIconCB.invalidate();

        } else {
            productViewHolder.favouriteIconCB.setSelected(false);
            productViewHolder.favouriteIconCB.invalidate();
        }

        productViewHolder.favouriteIconCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productViewHolder.favouriteIconCB.isSelected()) {
                    mFavouriteListener.addFav(productData.productMasterId, position, mAdapterIndex);
                } else {
                    mFavouriteListener.removeFav(productData.productMasterId, position, mAdapterIndex);
                }
            }
        });

       productViewHolder.btnAddToCartReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAddToCartReorder.OnAddToCartReorder(productData.productMasterId);

            }
        });


        productViewHolder.productIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment proFragment = ProductDetailFragment.newInstance(productData.productMasterId, productData.productName);
                if (productData.isProductSku) {
                    proFragment = ProductDetailFragment.newInstance(productData.productMasterId, productData.productName, productData.productSkuId);
                }
                BaseFragment.addToBackStack((BaseActivity) mContext, proFragment, ProductDetailFragment.getTagName());


            }
        });


        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Moengage product clicked : banner

                PayloadBuilder builder = new PayloadBuilder();
                builder.putAttrInt(MoengageConstant.Events.PRODUCT_ID, productData.categoryId)
                        .putAttrInt(MoengageConstant.Events.CATEGORY,productData.categoryId)
                        .putAttrString(MoengageConstant.Events.SECTION,productData.format)
                        .putAttrInt(MoengageConstant.Events.INSTOCK,productData.inStock)
                        .putAttrString(MoengageConstant.Events.PRODUCT_LOCATION,productData.format)
                        .putAttrString(MoengageConstant.Events.POSITION,productData.productSkuDesc)
                        .putAttrInt(MoengageConstant.Events.PRICE,productData.productPrice)
                        .putAttrString(MoengageConstant.Events.VARIANT,productData.format)
                        .putAttrInt(MoengageConstant.Events.BRAND,productData.productMasterId)
                        .putAttrInt(MoengageConstant.Events.SKU,productData.productSkuId)
                        .putAttrString(MoengageConstant.Events.NAME,productData.productName)
                        .putAttrInt(MoengageConstant.Events.QUANTITY,productData.packQty)
                        .putAttrString(MoengageConstant.Events.PROMOTION_TYPE,"banner");
                BaseFragment proFragment = ProductDetailFragment.newInstance(productData.productMasterId, productData.productName);
                if (productData.isProductSku) {
                    proFragment = ProductDetailFragment.newInstance(productData.productMasterId, productData.productName, productData.productSkuId);
                }
                BaseFragment.addToBackStack((BaseActivity) mContext, proFragment, ProductDetailFragment.getTagName());
                MoEHelper.getInstance(mContext).trackEvent(MoengageConstant.Events.PRODUCT_CLICKED, builder);

            }

        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends BaseViewHolder {
        @BindView(R.id.productNameTV)
        protected TextView productNameTV;
        @BindView(R.id.priceTV)
        protected TextView priceTV;
        @BindView(R.id.productIconIV)
        protected NetworkImageView productIconIV;
        @BindView(R.id.productLL)
        protected LinearLayout productLL;
        @BindView(R.id.marginTV)
        protected TextView marginTV;
        @BindView(R.id.favouriteIconCB)
        protected ImageView favouriteIconCB;
        @BindView(R.id.moqTV)
        protected TextView moqTV;
        @BindView(R.id.inquiryTV)
        protected TextView inquiryTV;
        @BindView(R.id.refurbishIcon)
        protected ImageView refurbishIcon;

        @BindView(R.id.productSkuDescTV)
        protected TextView productSkuDescTV;

        @BindView(R.id.priceMRP)
        protected TextView priceMRP;

        @BindView(R.id.view_out_of_stock)
        protected View view_out_of_stock;
        @BindView(R.id.btnAddToCartReOrder)
        protected Button btnAddToCartReOrder;

        public ProductViewHolder(View view) {
            super(view);

        }
    }


    public interface FavouriteListener {

        public void addFav(int id, int position, int adapterIndex);

        public void removeFav(int id, int position, int adapterIndex);
    }

    public interface OnAddToCartReorder {
        public void OnAddToCartReorder(int selectedProductMasterID);
    }



}
