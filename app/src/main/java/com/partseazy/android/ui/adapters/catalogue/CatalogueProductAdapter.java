package com.partseazy.android.ui.adapters.catalogue;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.catalogue.Product;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by taushif on 20/1/17.
 */

public class CatalogueProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private Integer pageIndex;
    private int categoryId;


    public CatalogueProductAdapter(Context context, int categoryId) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        this.categoryId = categoryId;
        pageIndex = null;
        productList = new ArrayList<Product>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(mInflater.inflate(R.layout.row_catalog_product_item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ProductViewHolder productViewHolder = (ProductViewHolder) holder;
        final Product product = productList.get(position);
        productViewHolder.productNameTV.setTag(product);
        productViewHolder.productNameTV.setText(product.productMaster.name);

        // String productPrice = CommonUtility.getPriceRangeforPDP(product.price, product.priceHigh);
        String productPrice = String.valueOf(CommonUtility.convertionPaiseToRupee(product.price));
        final String mrpPrice = CommonUtility.convertionPaiseToRupeeString(product.priceHigh);

        if (productPrice != null && !productPrice.equals(0 + "")) {
            // productViewHolder.priceTV.setText(PartsEazyApplication.getInstance().getResources().getString(R.string.price_per_piece_string, productPrice + "", "piece"));
            productViewHolder.priceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
            productViewHolder.priceTV.setVisibility(View.VISIBLE);
        } else {
            productViewHolder.priceTV.setVisibility(View.GONE);
        }

        String formatedURL;

        if (product.productMaster.images != null && product.productMaster.images.size() > 0) {
            formatedURL = CommonUtility.getFormattedImageUrl(product.productMaster.images.get(0).src, productViewHolder.productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
        } else {
            formatedURL = CommonUtility.getFormattedImageUrl(null, productViewHolder.productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
        }

        CommonUtility.setImageSRC(imageLoader, productViewHolder.productIconIV, formatedURL);


        if (product.productMaster.categoryId == AppConstants.REFURBISH_CAT_ID || product.productMaster.categoryId == AppConstants.REFURBISH_CAT_2_ID) {
            //  productViewHolder.refurbishIcon.setVisibility(View.VISIBLE);
            productViewHolder.refurbishIcon.setVisibility(View.GONE);
        } else {
            productViewHolder.refurbishIcon.setVisibility(View.GONE);
        }

        if (product.productMaster.format.equals(AppConstants.INQUIRE_PRODUCT)) {
            productViewHolder.view_out_of_stock.setVisibility(View.GONE);
            productViewHolder.moqTV.setVisibility(View.GONE);
            productViewHolder.inquiryTV.setVisibility(View.VISIBLE);
        } else {
            //   productViewHolder.moqTV.setVisibility(View.VISIBLE);
            productViewHolder.inquiryTV.setVisibility(View.GONE);
            if (product.inStock == 0) {
                productViewHolder.view_out_of_stock.setVisibility(View.VISIBLE);
            } else {
                productViewHolder.view_out_of_stock.setVisibility(View.GONE);

            }

            if (product.productMaster.packOf > 1) {
                String packMessage = context.getString(R.string.MOQ, product.productMaster.minQty, context.getString(R.string.pack)) + "\n";
                packMessage = packMessage + context.getString(R.string.pack_contains, product.productMaster.packOf, "piece");
                productViewHolder.moqTV.setText(packMessage);
            } else {
                productViewHolder.moqTV.setText(context.getString(R.string.MOQ, product.productMaster.minQty, context.getString(R.string.pcs)));
            }
        }


        if (product.margin > 0) {
            productViewHolder.marginTV.setVisibility(View.VISIBLE);
            productViewHolder.marginTV.setText(context.getString(R.string.margin_fav, product.margin));
        } else {
            productViewHolder.marginTV.setVisibility(View.GONE);
        }


        final boolean isFavourite = FavouriteUtility.isFavByProductMasterId(product.productMaster.id);
        productViewHolder.favouriteIconCB.setSelected(isFavourite);
        if (isFavourite) {
            productViewHolder.favouriteIconCB.setSelected(true);
            productViewHolder.favouriteIconCB.invalidate();
        } else {
            productViewHolder.favouriteIconCB.setSelected(false);
            productViewHolder.favouriteIconCB.invalidate();
        }

        if (mrpPrice != null && !mrpPrice.equals(0 + "")) {
            if (product.priceHigh > product.price) {
                productViewHolder.priceMRP.setText(context.getString(R.string.MRP_Price,
                        mrpPrice));
                productViewHolder.priceMRP.setPaintFlags(productViewHolder.priceMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if (product.priceHigh.equals(product.price)) {
                productViewHolder.marginTV.setVisibility(View.GONE);
                productViewHolder.priceMRP.setText(context.getString(R.string.MRP_Price,
                        mrpPrice));
            }
        } else {
            productViewHolder.priceMRP.setVisibility(View.GONE);
        }

        productViewHolder.favouriteIconCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productViewHolder.favouriteIconCB.isSelected()) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CATALOGUE_ADD_TO_FAVOURITE, product.productMaster.id, position);
                } else {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CATALOGUE_REMOVE_FAVOURITE, product.productMaster.id, position);
                }
            }
        });


        productViewHolder.productIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment proFragment = ProductDetailFragment.newInstance(product.productMaster.id, product.productMaster.name);
                BaseFragment.addToBackStack((BaseActivity) context, proFragment, ProductDetailFragment.getTagName());

            }
        });


        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment proFragment = ProductDetailFragment.newInstance(product.productMaster.id, product.productMaster.name);
                BaseFragment.addToBackStack((BaseActivity) context, proFragment, ProductDetailFragment.getTagName());

            }
        });


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


        @BindView(R.id.view_out_of_stock)
        protected View view_out_of_stock;

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

        @BindView(R.id.priceMRP)
        protected TextView priceMRP;

        @BindView(R.id.refurbishIcon)
        protected ImageView refurbishIcon;


        public ProductViewHolder(View view) {
            super(view);

        }
    }

    public void clearAdapter() {
        productList.clear();
        pageIndex = null;
        this.notifyDataSetChanged();
    }


    public void updateProductList(int p, List<Product> newlist) {

        if (pageIndex == null || pageIndex != p) {
            pageIndex = p;
            CustomLogger.d("Page Index has been udpated  " + pageIndex);

        } else {
            CustomLogger.d("Page Index I am returning since I am already supplied " + pageIndex);
            return;
        }


        int index = productList.size();
        productList.addAll(newlist);
        this.notifyItemRangeInserted(index, newlist.size());
        notifyDataSetChanged();


    }


}
