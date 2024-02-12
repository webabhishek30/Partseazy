package com.partseazy.android.ui.adapters.product;


import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.datastore.FavouriteUtility;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailCardFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.fragments.product.ProductSampleDialog;
import com.partseazy.android.ui.fragments.product.WebViewFragment;
import com.partseazy.android.ui.model.productdetail.Image_;
import com.partseazy.android.ui.model.productdetail.Item;
import com.partseazy.android.ui.model.productdetail.Product;
import com.partseazy.android.ui.model.productdetail.ProductMasterBag;
import com.partseazy.android.ui.model.productdetail.Product_;
import com.partseazy.android.ui.model.productdialog.ProductDetailItem;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.ui.widget.CircleIndicator;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 23/12/16.
 */

public class ProductDetailFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private final BaseFragment mContext;
    private ProductBannerAdapter productBannerAdapter;
    private ProductDetailAdapter productDetailAdapter;
    private int mTotalItems = 8;
    private static final int PRODUCT_DETAIL_ITEM_NUM = 4;
    private Product productDetailedData;
    private List<String> nameList = new ArrayList<>();
    private ArrayList<String> bannerList = new ArrayList();
    private List<ProductDetailItem> detailItemsListM;
    private Product_ sampleProduct;
    private UserLoginTask.TaskListener listener;
    private FavouriteListener mFavouriteListener;

    public ProductDetailFragmentAdapter(ProductDetailFragment context, Product productDetailData, FavouriteListener favouriteListener) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        productDetailedData = productDetailData;
        mFavouriteListener = favouriteListener;

        parseProductDetailComponents();
        DataStore dataStore = new DataStore();
        productBannerAdapter = new ProductBannerAdapter(context.getContext(), bannerList,ProductBannerAdapter.PRODUCT_LAUNCH);
        sampleProduct = getSampleProduct(productDetailedData);
        // productCardAdapter = new ProductCardAdapter(context.getContext(), nameList);

    }


    private void parseProductDetailComponents() {
        parseBanner();

    }

    private void parseBanner() {
        bannerList.clear();

        if (productDetailedData.productMaster.images != null && productDetailedData.productMaster.images.size() > 0) {
            for (Image_ productImage : productDetailedData.productMaster.images) {

                String formattedStringUrl = productImage.src;
                CustomLogger.d("ImageUrl " + formattedStringUrl);
                bannerList.add(formattedStringUrl);
            }
        }

    }

    public Product_ getSampleProduct(Product pdpData) {
        if (pdpData.products != null) {
            for (Product_ product : pdpData.products) {
                if (product.allowSample == 1) {
                    return product;
                }
            }
        }
        return null;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_PRODUCT_BANNER:
                return new ProductBannerViewHolder(mInflater.inflate(R.layout.view_product_banner, parent, false));
            case VIEW_PRODUCT_INFO:
                return new ProductInfoViewHolder(mInflater.inflate(R.layout.row_product_item_info, parent, false));
            case VIEW_PRODUCT_REFURBISHED:
                return new ProductRefurbishedViewHolder(mInflater.inflate(R.layout.row_product_item_discount, parent, false));
            case VIEW_PRODUCT_DETAILS:
                return new ProductDetailViewHolder(mInflater.inflate(R.layout.row_product_item_detail, parent, false));
            case VIEW_PRODUCT_SUPPLIER:
                return new ProductSupplierViewHolder(mInflater.inflate(R.layout.row_product_item_supplier, parent, false));
            case VIEW_PRODUCT_RETURN_POLICY:
                return new StockCorrectionViewHolder(mInflater.inflate(R.layout.row_product_item_stock_correction, parent, false));
            case VIEW_PRODUCT_SIMILAR:
                return new ProductSimilarViewHolder(mInflater.inflate(R.layout.row_product_item_similar, parent, false));
            case VIEW_PRODUCT_SAMPLE:
                return new ProductSampleViewHolder(mInflater.inflate(R.layout.row_product_item_enquiry, parent, false));

            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (position) {
            case 0:
                ProductBannerViewHolder bannerViewHolder = (ProductBannerViewHolder) viewHolder;
                bannerViewHolder.viewPager.setAdapter(productBannerAdapter);
                bannerViewHolder.circleIndicator.setViewPager(bannerViewHolder.viewPager);

                if (productDetailedData.category.id == AppConstants.REFURBISH_CAT_ID || productDetailedData.category.id == AppConstants.REFURBISH_CAT_2_ID) {
                   // bannerViewHolder.refurbishIcon.setVisibility(View.VISIBLE);
                    bannerViewHolder.refurbishIcon.setVisibility(View.GONE);

                } else {
                    bannerViewHolder.refurbishIcon.setVisibility(View.GONE);
                }
                //TODO: Have to handle banner handling
                return;
            case 1: // ImageView
                final ProductInfoViewHolder productInfoViewHolder = (ProductInfoViewHolder) viewHolder;
                productInfoViewHolder.nameTV.setText(productDetailedData.productMaster.name);
                productInfoViewHolder.productIDTV.setText(mContext.getString(R.string.code, productDetailedData.productMaster.bcin));
                if (productDetailedData.margin >= 1) {
                    productInfoViewHolder.marginTV.setText(PartsEazyApplication.getInstance().getString(R.string.margin, "" + productDetailedData.margin));
                    productInfoViewHolder.marginTV.setVisibility(View.VISIBLE);
                } else {
                    productInfoViewHolder.marginTV.setVisibility(View.GONE);
                }

                String productPrice = CommonUtility.getPriceRangeInHTMLforPDP(productDetailedData.min, productDetailedData.max);
                if (productPrice != null && !productPrice.equals(0 + "")) {
                    if (productDetailedData.productMaster.format.equals(AppConstants.INQUIRE_PRODUCT)) {
                        productInfoViewHolder.productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece, productPrice)));
                    }else{
                        productInfoViewHolder.productPriceTV.setText(Html.fromHtml(PartsEazyApplication.getInstance().getString(R.string.rs_per_piece_tax_extra, productPrice)));
                    }
                    productInfoViewHolder.productPriceTV.setVisibility(View.VISIBLE);
                } else {
                    productInfoViewHolder.productPriceTV.setVisibility(View.GONE);
                }
//                 productInfoViewHolder.productPriceTV.setText(Html.fromHtml(productPrice+"<sup><small>20</small></sup>"));

                // productInfoViewHolder.productPriceTypeTv.setText(PartsEazyApplication.getInstance().getString(R.string.per, productDetailedData.type));
                if (productDetailedData.mrp > 0) {
                    if (productDetailedData.margin >= 1) {
                        productInfoViewHolder.productMRPTV.setText(mContext.getString(R.string.MRP, CommonUtility.convertionPaiseToRupee(productDetailedData.mrp)));
                        productInfoViewHolder.productMRPTV.setPaintFlags(productInfoViewHolder.productMRPTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        productInfoViewHolder.productMRPTV.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        productInfoViewHolder.productMRPTV.setText(mContext.getString(R.string.MRP, CommonUtility.convertionPaiseToRupee(productDetailedData.mrp)));
                        productInfoViewHolder.productMRPTV.setVisibility(View.VISIBLE);

                    }
                    } else {
                    if (productDetailedData.mop > 0) {
                        productInfoViewHolder.productMRPTV.setText(mContext.getString(R.string.MOP, CommonUtility.convertionPaiseToRupee(productDetailedData.mop)));
                        productInfoViewHolder.productMRPTV.setVisibility(View.VISIBLE);
                    } else {
                        productInfoViewHolder.productMRPTV.setVisibility(View.GONE);
                    }
                }


                if (productDetailedData.productMaster.packOf > 1) {
                    productInfoViewHolder.moqTV.setText(PartsEazyApplication.getInstance().getString(R.string.MOQ, productDetailedData.productMaster.minQty, mContext.getString(R.string.pack)));
                    productInfoViewHolder.sizeTV.setText(PartsEazyApplication.getInstance().getString(R.string.pack_contains, productDetailedData.productMaster.packOf, mContext.getString(R.string.pcs)));
                    productInfoViewHolder.sizeTV.setVisibility(View.VISIBLE);
                    productInfoViewHolder.marginLineView.setVisibility(View.VISIBLE);
                } else {
                    productInfoViewHolder.moqTV.setText(PartsEazyApplication.getInstance().getString(R.string.MOQ, productDetailedData.productMaster.minQty, productDetailedData.type));
                    productInfoViewHolder.sizeTV.setVisibility(View.GONE);
                    productInfoViewHolder.marginLineView.setVisibility(View.GONE);
                }

                if (productDetailedData.warranty != null) {
                    productInfoViewHolder.warrantyTV.setText(productDetailedData.warranty);
                    productInfoViewHolder.warrantyLL.setVisibility(View.VISIBLE);
                } else {
                    productInfoViewHolder.warrantyLL.setVisibility(View.GONE);
                }

                if (FeatureMap.isFeatureEnabled(FeatureMapKeys.pdp_show_ratings)) {
                    productInfoViewHolder.ratingBar.setRating(productDetailedData.rating);
                    productInfoViewHolder.ratingTV.setText(PartsEazyApplication.getInstance().getString(R.string.rating_number, "" + productDetailedData.rating));
                    productInfoViewHolder.ratingBar.setVisibility(View.VISIBLE);
                    productInfoViewHolder.ratingTV.setVisibility(View.VISIBLE);
                } else {
                    productInfoViewHolder.ratingBar.setVisibility(View.GONE);
                    productInfoViewHolder.ratingTV.setVisibility(View.GONE);
                }


                final boolean isFavourite = FavouriteUtility.isFavByProductMasterId(productDetailedData.productMaster.id);
                productInfoViewHolder.favouriteIconCB.setSelected(isFavourite);
                if (isFavourite) {
                    productInfoViewHolder.favouriteIconCB.setSelected(true);
                    productInfoViewHolder.favouriteIconCB.invalidate();
                } else {
                    productInfoViewHolder.favouriteIconCB.setSelected(false);
                    productInfoViewHolder.favouriteIconCB.invalidate();
                }
                productInfoViewHolder.favouriteIconCB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!productInfoViewHolder.favouriteIconCB.isSelected()) {
                            mFavouriteListener.addFav(productDetailedData.productMaster.id);
                        } else {
                            mFavouriteListener.removeFav(productDetailedData.productMaster.id);

                        }
                    }
                });


                return;
            case 2: // Discounts
                ProductRefurbishedViewHolder productHolder = (ProductRefurbishedViewHolder) viewHolder;
                // productHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                if (productDetailedData.category.id == AppConstants.REFURBISH_CAT_ID || productDetailedData.category.id == AppConstants.REFURBISH_CAT_2_ID) {
                    handleRefurbishDetailCardClick(productHolder);
                } else {
                    productHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                }
                return;
            case 3:
                ProductDetailViewHolder productDetailViewHolder = (ProductDetailViewHolder) viewHolder;
                productDetailViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext.getContext()));
                if (detailItemsListM == null) {
                    detailItemsListM = getProductDetailList(productDetailedData);
                }

                List<ProductDetailItem> detailCardList = getProductCardDetailList(detailItemsListM, PRODUCT_DETAIL_ITEM_NUM);
                if (detailCardList != null && detailCardList.size() > 0) {
                    productDetailAdapter = new ProductDetailAdapter(mContext.getContext(), detailCardList, false);
                    productDetailViewHolder.recyclerView.setAdapter(productDetailAdapter);
                    handleProductDetailCardClick(productDetailViewHolder);
                } else {
                    productDetailViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                }

                return;
            case 4:

                if (productDetailedData.supplier != null) {
                    ProductSupplierViewHolder supplierViewHolder = (ProductSupplierViewHolder) viewHolder;
                    handleSupplierCardClick(supplierViewHolder);
                } else {
                    viewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

                }
                return;
            case 5:
                StockCorrectionViewHolder stockCorrectionViewHolder = (StockCorrectionViewHolder) viewHolder;
                if (FeatureMap.isFeatureEnabled(FeatureMapKeys.pdp_stock_correction)) {
                    if (productDetailedData.stockCorrection == null) {
                        stockCorrectionViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    } else {
                        handleStockCorrectionCard(stockCorrectionViewHolder, productDetailedData);
                    }
                } else {
                    stockCorrectionViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                }

                return;
            case 6:
                ProductSimilarViewHolder similarViewHolder = (ProductSimilarViewHolder) viewHolder;
                similarViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext.getContext(), LinearLayoutManager.HORIZONTAL, false);
                similarViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
//                productCardAdapter = new ProductCardAdapter(mContext.getContext(), nameList);
//                similarViewHolder.recyclerView.setAdapter(productCardAdapter);
                return;

            case 7:
                ProductSampleViewHolder productSampleViewHolder = (ProductSampleViewHolder) viewHolder;
                if (productDetailedData.isActive && !productDetailedData.productMaster.format.equals(AppConstants.INQUIRE_PRODUCT) && productDetailedData.inStock) {
                    handleProductSampleCard(productSampleViewHolder, productDetailedData);
                } else {
                    // productSampleViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    productSampleViewHolder.sampleOrderTV.setVisibility(View.GONE);
                    productSampleViewHolder.callRL.setVisibility(View.GONE);
                }
                return;
        }

    }

    @Override
    public int getItemCount() {
        return mTotalItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HolderType.VIEW_PRODUCT_BANNER.ordinal();
        }
        if (position == 1) {
            return HolderType.VIEW_PRODUCT_INFO.ordinal();
        }
        if (position == 2) {
            return HolderType.VIEW_PRODUCT_REFURBISHED.ordinal();
        }
        if (position == 3) {
            return HolderType.VIEW_PRODUCT_DETAILS.ordinal();
        }
        if (position == 4) {
            return HolderType.VIEW_PRODUCT_SUPPLIER.ordinal();
        }
        if (position == 5) {
            return HolderType.VIEW_PRODUCT_RETURN_POLICY.ordinal();
        }
        if (position == 6) {
            return HolderType.VIEW_PRODUCT_SIMILAR.ordinal();
        }
        if (position == 7) {
            return HolderType.VIEW_PRODUCT_SAMPLE.ordinal();
        }
        return 0;
    }

    class ProductBannerViewHolder extends BaseViewHolder {

        @BindView(R.id.viewpager)
        public AutoScrollPager viewPager;
        @BindView(R.id.indicator)
        public CircleIndicator circleIndicator;

        @BindView(R.id.refurbishIcon)
        public ImageView refurbishIcon;

        public ProductBannerViewHolder(View view) {
            super(view);
        }
    }

    class ProductInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.productNameTV)
        public TextView nameTV;

        @BindView(R.id.productIDTV)
        public TextView productIDTV;
        @BindView(R.id.marginTV)
        public TextView marginTV;
        @BindView(R.id.productPriceTV)
        public TextView productPriceTV;
        @BindView(R.id.productMRPTV)
        public TextView productMRPTV;
        @BindView(R.id.moqTV)
        public TextView moqTV;
        @BindView(R.id.sizeTV)
        public TextView sizeTV;
        @BindView(R.id.ratingBar)
        public RatingBar ratingBar;
        @BindView(R.id.ratingTV)
        public TextView ratingTV;
        @BindView(R.id.favouriteIconCB)
        public ImageView favouriteIconCB;
        @BindView(R.id.marginLineView)
        public View marginLineView;
        @BindView(R.id.warrantyLL)
        public LinearLayout warrantyLL;
        @BindView(R.id.warrantyTV)
        public TextView warrantyTV;


        public ProductInfoViewHolder(View itemView) {
            super(itemView);

            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(PartsEazyApplication.getInstance().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(PartsEazyApplication.getInstance().getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(PartsEazyApplication.getInstance().getResources().getColor(R.color.black_70_opacity), PorterDuff.Mode.SRC_ATOP);
        }
    }

    class ProductRefurbishedViewHolder extends BaseViewHolder {

        @BindView(R.id.viewMoreLL)
        public LinearLayout viewMoreLL;

        @BindView(R.id.pointTV)
        public TextView pointTV;

        public ProductRefurbishedViewHolder(View itemView) {
            super(itemView);
        }
    }

    class StockCorrectionViewHolder extends BaseViewHolder {
        @BindView(R.id.minQuantityTV)
        public TextView minQuantityTV;
        @BindView(R.id.correctionAcceptedTV)
        public TextView correctionAcceptedTV;
        @BindView(R.id.timelineTV)
        public TextView timelineTV;
        @BindView(R.id.expandIV)
        public ImageView expandIV;
        @BindView(R.id.compressIV)
        public ImageView compressIV;
        @BindView(R.id.stockCorrectionLL)
        public LinearLayout stockCorrectionLL;
        @BindView(R.id.headingRL)
        public RelativeLayout headingRL;
        @BindView(R.id.informationIV)
        public ImageView informationIV;


        public StockCorrectionViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductDetailViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;
        @BindView(R.id.viewMoreLL)
        public LinearLayout viewMoreLL;

        public ProductDetailViewHolder(View itemView) {
            super(itemView);
        }
    }


    class ProductSupplierViewHolder extends BaseViewHolder {
        @BindView(R.id.sellerCircularImage)
        CircularImageView supplierImageview;

        @BindView(R.id.supplierNameTv)
        TextView supplierNameTv;

        @BindView(R.id.supplierRatingTV)
        TextView supplierRatingTV;

        @BindView(R.id.regionalSupplierTV)
        protected TextView regionalSupplierTV;

        @BindView(R.id.chatNowRL)
        public RelativeLayout chatNowRL;

        public ProductSupplierViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductSimilarViewHolder extends BaseViewHolder {
        @BindView(R.id.similarRecylerView)
        public RecyclerView recyclerView;

        public ProductSimilarViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductSampleViewHolder extends BaseViewHolder {
        @BindView(R.id.sampleOrderTV)
        public TextView sampleOrderTV;
        @BindView(R.id.callRL)
        protected RelativeLayout callRL;
        @BindView(R.id.callTV)
        protected TextView callTV;

        public ProductSampleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void handleSupplierCardClick(ProductSupplierViewHolder viewHolder) {
        viewHolder.supplierImageview.setDefaultImageResId(R.drawable.supplier_icon);

        if (productDetailedData.supplier != null) {
            viewHolder.supplierNameTv.setText(productDetailedData.supplier.companyName);
            viewHolder.supplierRatingTV.setText(mContext.getString(R.string.supplier_rating, "" + productDetailedData.supplierRating));
            if (productDetailedData.isRegionalSupplier) {
                viewHolder.regionalSupplierTV.setVisibility(View.VISIBLE);
            } else {
                viewHolder.regionalSupplierTV.setVisibility(View.GONE);
            }
        }

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.pdp_supplier_chat)) {
            viewHolder.chatNowRL.setVisibility(View.VISIBLE);
        } else {
            viewHolder.chatNowRL.setVisibility(View.GONE);
        }

        viewHolder.chatNowRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.showProgressDialog();
                ChatUtility chat = new ChatUtility(mContext, productDetailedData.supplier.userId,
                        productDetailedData.supplier.companyName, productDetailedData.productMaster.images.get(0).src,
                        productDetailedData.productMaster.name, productDetailedData.productMaster.bcin);
                chat.startChatting();
            }
        });
    }

    public void handleRefurbishDetailCardClick(ProductRefurbishedViewHolder viewHolder) {

        viewHolder.viewMoreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment fragment = WebViewFragment.newInstance(mContext.getString(R.string.refurbished_policy), AppConstants.REFURBISH_URL);
                BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), fragment, ProductDetailCardFragment.getTagName());

            }
        });
    }


    public void handleProductDetailCardClick(ProductDetailViewHolder viewHolder) {
        viewHolder.viewMoreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment fragment = ProductDetailCardFragment.newInstance(detailItemsListM);
                BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), fragment, ProductDetailCardFragment.getTagName());

            }
        });
    }


    public void handleStockCorrectionCard(final StockCorrectionViewHolder holder, Product productDetailData) {

        holder.minQuantityTV.setText(mContext.getString(R.string.valid_on_min, productDetailData.stockCorrection.minPieces));
        holder.correctionAcceptedTV.setText(mContext.getString(R.string.correction_accepted_here, productDetailData.stockCorrection.returnable));
        holder.timelineTV.setText(mContext.getString(R.string.maximum_correction_days, productDetailData.stockCorrection.maxDays));

        holder.headingRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.stockCorrectionLL.getVisibility() == View.GONE) {
                    holder.expandIV.setVisibility(View.GONE);
                    holder.compressIV.setVisibility(View.VISIBLE);
                    holder.stockCorrectionLL.setVisibility(View.VISIBLE);
                } else {
                    holder.expandIV.setVisibility(View.VISIBLE);
                    holder.compressIV.setVisibility(View.GONE);
                    holder.stockCorrectionLL.setVisibility(View.GONE);
                }
            }
        });

        holder.informationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFragment fragment = WebViewFragment.newInstance(mContext.getString(R.string.stock_correction), AppConstants.STOCK_CORRECTION_URL);
                BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), fragment, WebViewFragment.getTagName());

            }
        });
    }

    public void handleProductSampleCard(ProductSampleViewHolder viewHolder, final Product productDetailedData) {

        if (sampleProduct != null) {
            viewHolder.sampleOrderTV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.sampleOrderTV.setVisibility(View.GONE);
        }
        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.pdp_order_on_phone)) {
            viewHolder.callRL.setVisibility(View.VISIBLE);
        } else {
            viewHolder.callRL.setVisibility(View.GONE);
        }

        viewHolder.sampleOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sampleProductJson = new Gson().toJson(sampleProduct);
                String moqString = mContext.getString(R.string.MOQ_no_Pack_contains, 1);
                ProductSampleDialog productSampleDialog = ProductSampleDialog.newInstance(productDetailedData.productMaster.name, productDetailedData.productMaster.images.get(0).src, moqString, productDetailedData.margin, sampleProductJson);
                productSampleDialog.show(mContext.getActivity().getFragmentManager(), "dialog");
            }
        });

        viewHolder.callRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.dialPhoneNumber(mContext.getActivity(), StaticMap.CC_PHONE);
            }
        });
    }


    public List<ProductDetailItem> getProductDetailList(Product pdpData) {
        List<ProductDetailItem> itemList = new ArrayList<>();

        for (ProductMasterBag productMasterBag : pdpData.productMasterBag) {
            for (int i = 0; i < productMasterBag.items.size(); i++) {
                Item item = productMasterBag.items.get(i);
                if (i == 0) {
                    itemList.add(new ProductDetailItem(item.name, item.value + "", productMasterBag.name, true));
                } else {
                    itemList.add(new ProductDetailItem(item.name, item.value + ""));
                }
            }

        }
        return itemList;
    }


    public List<ProductDetailItem> getProductCardDetailList(List<ProductDetailItem> detailList, int itemNum) {
        List<ProductDetailItem> items = new ArrayList<>();
        if (detailList.size() > itemNum) {
            for (int i = 0; i < itemNum; i++) {
                items.add(detailList.get(i));
            }
        } else {
            items.addAll(detailList);
        }

        return items;
    }


    public interface FavouriteListener {

        public void addFav(int id);

        public void removeFav(int id);
    }

       /*............................... Chat Implementation End..........................*/


}
