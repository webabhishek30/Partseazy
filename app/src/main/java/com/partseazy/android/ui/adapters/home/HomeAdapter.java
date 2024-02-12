package com.partseazy.android.ui.adapters.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicommons.people.contact.Contact;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.extraContent.OrderPrescriptionFragment;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueViewAllFragment;
import com.partseazy.android.ui.model.home.category.DataItem;
import com.partseazy.android.ui.model.home.category.Result;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.partseazy.android.base.BaseFragment.addToBackStack;

/**
 * Created by naveen on 27/12/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final static String TYPE_BANNER = "banner";
    final static String TYPE_PRODUCT = "products";
    final static String TYPE_PRODUCT_SKU = "pm.product";
    final static String TYPE_CATEGORY_GRID = "category.grid";
    final static String TYPE_BRAND_ROW = "brand.row";
    final static String TYPE_CARD_BANNER = "banner.vertical";
    final static String TYPE_INDEX_SEARCH = "index.search";
    final static String TYPE_CLICK_TO_ORDER = "order.click";
    final static String TYPE_CALL_TO_ORDER = "order.call";
    final static String TYPE_RECENT_ORDER_LIST = "order.list";
    private int catId;
    Contact contact;
    AppContactService contactService;

    private LayoutInflater mInflater;
    private final Context mContext;
    private LinearLayoutManager horizontalLayoutManager = null;

    private HomeBrandAdapter brandAdapter;
    private HomeLargeBannerAdapter homeLargeBannerAdapter;
    private HomeCategoryGridAdapter homeCategoryGridAdapter;
    private HomeCardBannerAdapter homeCardBannerAdapter;


    private HorizontalProductAdapter.FavouriteListener mFavouriteListener;
    private HorizontalProductAdapter.OnAddToCartReorder mAddToCartReorder;
    private List<HorizontalProductAdapter> horizontalProductAdapterFactory;

    private List<Result> homeItemList;
    private OnLoadProductsFromUrl listener;

    public void updateHorizontalAdapter(int positionToNotify, int adapterIndex) {

        if (horizontalProductAdapterFactory != null && horizontalProductAdapterFactory.get(adapterIndex) != null)
            horizontalProductAdapterFactory.get(adapterIndex).notifyItemChanged(positionToNotify);

    }

    public interface OnLoadProductsFromUrl {
        void loadProducts(int position, String productUrl);

        void loadProductSingleSku(String widgetType, int position, String productUrl);
    }


    public HomeAdapter(Context context, List<Result> homeItemList, OnLoadProductsFromUrl listener, HorizontalProductAdapter.FavouriteListener favouriteListener, HorizontalProductAdapter.OnAddToCartReorder addToCartReorder) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.homeItemList = homeItemList;
        this.listener = listener;
        this.mFavouriteListener = favouriteListener;
        this.mAddToCartReorder = addToCartReorder;
        horizontalProductAdapterFactory = new ArrayList<>();
        contactService = new AppContactService(mContext);
        contact = contactService.getContactById(MobiComUserPreference.getInstance(mContext).getUserId());

    }

    public void SetHomeItemList(List<Result> homeItemList) {
        this.homeItemList = homeItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];

        switch (whichView) {
            case VIEW_HOME_LARGE_BANNER:
                return new LargeBannerViewHolder(mInflater.inflate(R.layout.row_home_large_banner, parent, false));

            case VIEW_HOME_CATEGORY_GRID:
                return new CategoryGridViewHolder(mInflater.inflate(R.layout.row_home_l3_grid_view, parent, false));

            case VIEW_HOME_SHOP_BY_BRAND:
                return new BrandViewHolder(mInflater.inflate(R.layout.horizontal_home_brand_card_view, parent, false));

            case VIEW_HOME_PRODUCT_CARD:
                return new ProductsViewHolder(mInflater.inflate(R.layout.horizontal_product_card_view, parent, false));

            case VIEW_HOME_CARD_BANNER:
                return new BannerCardViewHolder(mInflater.inflate(R.layout.horizontal_product_card_view, parent, false));
            case VIEW_HOME_SEARCH_BY_PRODUCT:
                return new SearchProductIdViewHolder(mInflater.inflate(R.layout.search_by_id, parent, false));

            case VIEW_HOME_CLICK_TO_ORDER:
                return new ClickToOrderViewHolder(mInflater.inflate(R.layout.row_click_to_order, parent, false));

            case VIEW_HOME_CALL_TO_ORDER:
                return new ClickToCallViewHolder(mInflater.inflate(R.layout.row_click_to_call, parent, false));

            case VIEW_HOME_RECENT_ORDER:
                return new RecentViewHolder(mInflater.inflate(R.layout.horizontal_product_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof LargeBannerViewHolder) {
            if (homeItemList.get(position).datalist != null && homeItemList.get(position).datalist.size() > 0) {
                LargeBannerViewHolder bannerViewHolder = (LargeBannerViewHolder) holder;
                homeLargeBannerAdapter = new HomeLargeBannerAdapter(mContext, homeItemList.get(position).datalist);
                bannerViewHolder.viewPager.setAdapter(homeLargeBannerAdapter);
                bannerViewHolder.viewPager.setClipToPadding(false);
                bannerViewHolder.viewPager.setPageMargin(-60);
                bannerViewHolder.viewPager.setInterval(3000);
                bannerViewHolder.viewPager.startAutoScroll();
            }
        }

        if (holder instanceof BrandViewHolder) {
            if (homeItemList.get(position).datalist != null && homeItemList.get(position).datalist.size() > 0) {
                BrandViewHolder brandViewHolder = (BrandViewHolder) holder;
                brandViewHolder.headingTV.setText(homeItemList.get(position).label);
                horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                brandViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
                brandAdapter = new HomeBrandAdapter(mContext, homeItemList.get(position).datalist);
                brandViewHolder.recyclerView.setAdapter(brandAdapter);
                brandViewHolder.recyclerView.setNestedScrollingEnabled(false);
            }
        }

        if (holder instanceof ProductsViewHolder) {

            ProductsViewHolder productsViewHolder = (ProductsViewHolder) holder;
            final Result result = homeItemList.get(position);
            if (result.productDataList != null) {
                productsViewHolder.seeAllTV.setVisibility(View.VISIBLE);
                productsViewHolder.headingTV.setText(result.label);
                horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                productsViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
                HorizontalProductAdapter adapter= null;
                if (result.setting != null && result.setting.addcart !=null)
                    adapter= new HorizontalProductAdapter(mContext, result.productDataList, mFavouriteListener, mAddToCartReorder, horizontalProductAdapterFactory.size(), false,result.setting.addcart);
                else
                    adapter= new HorizontalProductAdapter(mContext, result.productDataList, mFavouriteListener, mAddToCartReorder, horizontalProductAdapterFactory.size(), false,"");
                horizontalProductAdapterFactory.add(adapter);
                productsViewHolder.recyclerView.setAdapter(adapter);
                productsViewHolder.recyclerView.setNestedScrollingEnabled(false);




                productsViewHolder.seeAllTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                  /*      BaseFragment cartFragment = SingleCatalogueFragment.newInstance(result.productDataList,false);
                        BaseFragment.addToBackStack((BaseActivity) mContext, cartFragment, SingleCatalogueFragment.getTagName());*/
                        BaseFragment catalogueFragment = CatalogueViewAllFragment.newInstance(catId, result.productDataList);
                        addToBackStack((BaseActivity) mContext, catalogueFragment, CatalogueViewAllFragment.getTagName());
                    }
                });


            } else {
                productsViewHolder.seeAllTV.setVisibility(View.GONE);
                if (result.type.equals(TYPE_PRODUCT_SKU)) {
                    listener.loadProductSingleSku(TYPE_PRODUCT_SKU, position, result.dataUrl);
                } else {
                    listener.loadProducts(position, result.dataUrl);
                }
                //  productsViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }

        if (holder instanceof RecentViewHolder) {

            RecentViewHolder recentViewHolder = (RecentViewHolder) holder;
            final Result result = homeItemList.get(position);
            if (result.productDataList != null && result.productDataList.size() > 0) {
                recentViewHolder.seeAllTV.setVisibility(View.VISIBLE);
                recentViewHolder.headingRL.setVisibility(View.VISIBLE);
                recentViewHolder.headingTV.setText(result.label);
                horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                recentViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
                HorizontalProductAdapter adapter = new HorizontalProductAdapter(mContext, result.productDataList, mFavouriteListener, mAddToCartReorder, horizontalProductAdapterFactory.size(), true, result.setting.addcart);
                horizontalProductAdapterFactory.add(adapter);
                recentViewHolder.recyclerView.setAdapter(adapter);
                recentViewHolder.recyclerView.setNestedScrollingEnabled(false);


                recentViewHolder.seeAllTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                  /*      BaseFragment cartFragment = SingleCatalogueFragment.newInstance(result.productDataList,false);
                        BaseFragment.addToBackStack((BaseActivity) mContext, cartFragment, SingleCatalogueFragment.getTagName());*/
                        BaseFragment catalogueFragment = CatalogueViewAllFragment.newInstance(catId, result.productDataList);
                        addToBackStack((BaseActivity) mContext, catalogueFragment, CatalogueViewAllFragment.getTagName());
                    }
                });


            } else {
                recentViewHolder.seeAllTV.setVisibility(View.GONE);
                recentViewHolder.headingRL.setVisibility(View.GONE);
                if (result.type.equals(TYPE_RECENT_ORDER_LIST)) {
                    if (result.dataUrl != null)
                        listener.loadProductSingleSku(TYPE_RECENT_ORDER_LIST, position, result.dataUrl);
                } else {
                    listener.loadProducts(position, result.dataUrl);
                }
                //  productsViewHolder.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }

        if (holder instanceof CategoryGridViewHolder) {
            if (homeItemList.get(position).datalist != null && homeItemList.get(position).datalist.size() > 0)
                catId = homeItemList.get(position).datalist.get(0).refId;
            CategoryGridViewHolder categoryGridViewHolder = (CategoryGridViewHolder) holder;
            List<DataItem> categoryList = new ArrayList<>();
            categoryList.addAll(homeItemList.get(position).datalist);
            int gridRow = 4;
            if ((categoryList.size() == 1)) {
                gridRow = 1;
            }
            if (categoryList.size() == 2 || categoryList.size() == 4) {
                gridRow = 2;
            }
            if ((categoryList.size() == 3) || (categoryList.size() < 7 && categoryList.size() > 4)) {
                gridRow = 3;
            }
            //GridLayoutManager layoutManager = new GridLayoutManager(mContext, gridRow);
            //categoryGridViewHolder.recyclerView.setLayoutManager(layoutManager);
            horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            categoryGridViewHolder.recyclerView.setLayoutManager(horizontalLayoutManager);
            homeCategoryGridAdapter = new HomeCategoryGridAdapter(mContext, homeItemList.get(position).datalist);
            categoryGridViewHolder.recyclerView.setAdapter(homeCategoryGridAdapter);
            categoryGridViewHolder.recyclerView.setNestedScrollingEnabled(false);


        }

        if (holder instanceof BannerCardViewHolder) {

            BannerCardViewHolder categoryGridViewHolder = (BannerCardViewHolder) holder;
            categoryGridViewHolder.headingTV.setText(homeItemList.get(position).label);
            horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            categoryGridViewHolder.recyclerViewBrandGrid.setLayoutManager(horizontalLayoutManager);
            homeCardBannerAdapter = new HomeCardBannerAdapter(mContext, homeItemList.get(position).datalist);
            categoryGridViewHolder.recyclerViewBrandGrid.setAdapter(homeCardBannerAdapter);
            categoryGridViewHolder.recyclerViewBrandGrid.setNestedScrollingEnabled(false);


        }
        if (holder instanceof SearchProductIdViewHolder) {
            final SearchProductIdViewHolder searchProductIdViewHolder = (SearchProductIdViewHolder) holder;

            searchProductIdViewHolder.btn_search_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomLogger.d("go button ");

                    String searchQuery = searchProductIdViewHolder.et_search_home.getText().toString().trim();
                    if (searchQuery != null) {
                        addToBackStack((BaseActivity) mContext, CatalogueFragment.newInstance(searchQuery, true, true), CatalogueFragment.getTagName());
                    } else {

                    }

                }
            });
        }
        if (holder instanceof ClickToCallViewHolder) {
            final ClickToCallViewHolder clickToCallViewHolder = (ClickToCallViewHolder) holder;

            clickToCallViewHolder.ll_click_to_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtility.dialPhoneNumber(mContext, StaticMap.CC_PHONE);
                }
            });
        }

        if (holder instanceof ClickToOrderViewHolder) {
            final ClickToOrderViewHolder clickToOrderViewHolder = (ClickToOrderViewHolder) holder;

            clickToOrderViewHolder.ll_click_to_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFragment orderPrescriptionFragment = OrderPrescriptionFragment.newInstance();
                    addToBackStack((BaseActivity) mContext, orderPrescriptionFragment, OrderPrescriptionFragment.getTagName());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Result homeItem = homeItemList.get(position);
        switch (homeItem.type) {

            case TYPE_BANNER:
                return HolderType.VIEW_HOME_LARGE_BANNER.ordinal();

            case TYPE_CATEGORY_GRID:
                return HolderType.VIEW_HOME_CATEGORY_GRID.ordinal();

            case TYPE_BRAND_ROW:
                return HolderType.VIEW_HOME_SHOP_BY_BRAND.ordinal();

            case TYPE_PRODUCT:
                return HolderType.VIEW_HOME_PRODUCT_CARD.ordinal();

            case TYPE_PRODUCT_SKU:
                return HolderType.VIEW_HOME_PRODUCT_CARD.ordinal();

            case TYPE_CARD_BANNER:
                return HolderType.VIEW_HOME_CARD_BANNER.ordinal();
            case TYPE_INDEX_SEARCH:
                return HolderType.VIEW_HOME_SEARCH_BY_PRODUCT.ordinal();
            case TYPE_CLICK_TO_ORDER:
                return HolderType.VIEW_HOME_CLICK_TO_ORDER.ordinal();
            case TYPE_CALL_TO_ORDER:
                return HolderType.VIEW_HOME_CALL_TO_ORDER.ordinal();
            case TYPE_RECENT_ORDER_LIST:
                return HolderType.VIEW_HOME_RECENT_ORDER.ordinal();

            default:
                return 0;
        }

    }

    class LargeBannerViewHolder extends BaseViewHolder {

        @BindView(R.id.viewpager)
        public AutoScrollPager viewPager;

        public LargeBannerViewHolder(View view) {
            super(view);
        }
    }

    class CategoryGridViewHolder extends BaseViewHolder {
        @BindView(R.id.recylerView)
        protected RecyclerView recyclerView;

        public CategoryGridViewHolder(View itemView) {
            super(itemView);
        }
    }


    class BrandViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;
        @BindView(R.id.headingTV)
        public TextView headingTV;

        public BrandViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ProductsViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;
        @BindView(R.id.headingTV)
        public TextView headingTV;
        @BindView(R.id.seeAllTV)
        public TextView seeAllTV;


        public ProductsViewHolder(View itemView) {
            super(itemView);
        }
    }

    class RecentViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerView;
        @BindView(R.id.headingTV)
        public TextView headingTV;
        @BindView(R.id.seeAllTV)
        public TextView seeAllTV;
        @BindView(R.id.headingRL)
        public RelativeLayout headingRL;


        public RecentViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BannerCardViewHolder extends BaseViewHolder {
        @BindView(R.id.recyclerView)
        public RecyclerView recyclerViewBrandGrid;
        @BindView(R.id.headingTV)
        public TextView headingTV;

        public BannerCardViewHolder(View itemView) {
            super(itemView);
        }
    }

    class SearchProductIdViewHolder extends BaseViewHolder {
        @BindView(R.id.et_search_home)
        protected EditText et_search_home;
        @BindView(R.id.btn_search_home)
        protected Button btn_search_home;

        public SearchProductIdViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ClickToOrderViewHolder extends BaseViewHolder {
        @BindView(R.id.ll_click_to_order)
        protected LinearLayout ll_click_to_order;

        public ClickToOrderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ClickToCallViewHolder extends BaseViewHolder {
        @BindView(R.id.ll_click_to_call)
        protected LinearLayout ll_click_to_call;

        public ClickToCallViewHolder(View itemView) {
            super(itemView);
        }
    }


}
