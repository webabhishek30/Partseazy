package com.partseazy.android.ui.adapters.navigation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.account.OrderHomeFragment;
import com.partseazy.android.ui.fragments.consumer_finance.ConsumerFinanceFragment;
import com.partseazy.android.ui.fragments.customer_management.CustomerManagementFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealFragment;
import com.partseazy.android.ui.fragments.finance.CreditFacilityFragment;
import com.partseazy.android.ui.fragments.fos.AgentAppFragment;
import com.partseazy.android.ui.fragments.product.WebViewFragment;
import com.partseazy.android.ui.fragments.registration.SettingL1SegmentFragment;
import com.partseazy.android.ui.fragments.supplier.search.ShopsSearchFragment;
import com.partseazy.android.ui.widget.NavigationDrawer;
import com.partseazy.android.utility.HolderType;
import com.yariksoffice.lingver.Lingver;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Naveen Kumar on 31/1/17.
 */


public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NavL2CategoryAdapter.DrawerL2CallBack {
    private Context context;
    private LayoutInflater mInflater;
    private List<NavigationDrawer.DrawerData> itemList;
    protected DrawerLayout mDrawer;
    private static final int DRAWER_CLOSE_DURATION = 600;


    public final static String TYPE_HEADER = "header";
    public final static String TYPE_CATEGORY = "category";
    public final static String TYPE_ICON_TYPE = "icon_type";
    public final static String TYPE_APP_INFO = "app_info_type";
    public final static String TYPE_HOME = "app_home_type";

    public NavigationDrawerAdapter(Context context, List<NavigationDrawer.DrawerData> itemList, DrawerLayout drawerLayout) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.mDrawer = drawerLayout;
        CustomLogger.d("ItemList " + itemList.size());

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];

        switch (whichView) {

            case VIEW_NAV_DRAWER_HEADER:
                return new HeaderVH(mInflater.inflate(R.layout.row_nav_header_item, parent, false));

            case VIEW_NAV_DRAWER_HOME_ITEM:
                return new HomeVH(mInflater.inflate(R.layout.row_nav_icon_home_item, parent, false));

            case VIEW_NAV_DRAWER_CATEGORY_ITEM:
                return new CatergoryVH(mInflater.inflate(R.layout.row_nav_category_item, parent, false));

            case VIEW_NAV_DRAWER_ICON_ITEM:
                return new IconInfoVH(mInflater.inflate(R.layout.row_nav_icon_info_item, parent, false));

            case VIEW_NAV_DRAWER_INFO_ITEM:
                return new InfoVH(mInflater.inflate(R.layout.row_nav_info_item, parent, false));


            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof HeaderVH) {

            HeaderVH headerVH = (HeaderVH) viewHolder;
            headerVH.headingNameTV.setText(DataStore.getUserName(context));
            headerVH.headingMobileTV.setText("( " + DataStore.getUserPhoneNumber(context) + " )");

            if (DataStore.getUserCredit(context) != null && !DataStore.getUserCredit(context).equals("")) {
                headerVH.creditTV.setText(context.getString(R.string.credit_available_str, DataStore.getUserCredit(context)));
                headerVH.creditTV.setVisibility(View.VISIBLE);
            } else {
                headerVH.creditTV.setVisibility(View.GONE);
            }

//            headerVH.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    BaseFragment fosFragment = RetailerProfileFragment.newInstance();
//                    launchFragment(fosFragment, RetailerProfileFragment.getTagName());
//                }
//            });
        }


        if (viewHolder instanceof CatergoryVH) {

            final CatergoryVH catergoryVH = (CatergoryVH) viewHolder;

            catergoryVH.myhomeRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (catergoryVH.categoryViewLL.getVisibility() == View.GONE) {
                        catergoryVH.categoryViewLL.setVisibility(View.VISIBLE);
                        catergoryVH.myHomeIV.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

                    } else {
                        catergoryVH.categoryViewLL.setVisibility(View.GONE);
                        catergoryVH.myHomeIV.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                    }
                }
            });

            catergoryVH.categoryNameTV.setText(itemList.get(position).l1CategoryData.name);
            catergoryVH.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            NavL2CategoryAdapter navL2CategoryAdapter = new NavL2CategoryAdapter(context, itemList.get(position).l1CategoryData.l2CategoryDataList, this);
            catergoryVH.recyclerView.setAdapter(navL2CategoryAdapter);
        }


        if (viewHolder instanceof HomeVH) {

            final HomeVH homeVH = (HomeVH) viewHolder;

            if (FeatureMap.isFeatureEnabled(FeatureMapKeys.booster_navigation)) {
                homeVH.myShopRL.setVisibility(View.VISIBLE);
            } else {
                homeVH.myShopRL.setVisibility(View.GONE);
            }

            if (DataStore.isPremiumUser(context)) {
                homeVH.customerManagementRL.setVisibility(View.VISIBLE);
            } else {
                homeVH.customerManagementRL.setVisibility(View.GONE);
            }

            if (DataStore.isConsumerFinancingApplicable(context)) {
                homeVH.consumerFinanceRL.setVisibility(View.VISIBLE);
            } else {
                homeVH.consumerFinanceRL.setVisibility(View.GONE);
            }

            homeVH.myShopRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDrawer();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            BaseFragment.popToHome((FragmentActivity) context);
                            BaseFragment.addToBackStack((BaseActivity) context, ShopsSearchFragment.newInstance(), ShopsSearchFragment.getTagName());
                        }
                    }, DRAWER_CLOSE_DURATION);


                }
            });

            homeVH.dealRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDrawer();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BaseFragment.addToBackStack((BaseActivity) context, DealHomeFragment.newInstance(), DealHomeFragment.getTagName());
                        }
                    }, DRAWER_CLOSE_DURATION);
                }
            });

            homeVH.reportCardRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDrawer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BaseFragment.addToBackStack((BaseActivity) context, SellDealFragment.newInstance(true), SellDealFragment.getTagName());
                        }
                    }, DRAWER_CLOSE_DURATION);
                }
            });


            homeVH.customerManagementRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDrawer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BaseFragment.addToBackStack((BaseActivity) context, CustomerManagementFragment.newInstance(), CustomerManagementFragment.getTagName());
                        }
                    }, DRAWER_CLOSE_DURATION);
                }
            });

            homeVH.consumerFinanceRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDrawer();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BaseFragment.addToBackStack((BaseActivity) context, ConsumerFinanceFragment.newInstance(), ConsumerFinanceFragment.getTagName());
                        }
                    }, DRAWER_CLOSE_DURATION);
                }
            });
        }


        if (viewHolder instanceof IconInfoVH) {

            IconInfoVH iconInfoVH = (IconInfoVH) viewHolder;
            iconInfoVH.creditFacilityRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseFragment fragment = CreditFacilityFragment.newInstance();
                    launchFragment(fragment, fragment.getTag());
                }
            });

            iconInfoVH.myOrdersRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseFragment myOrderFragment = OrderHomeFragment.newInstance(false);
                    launchFragment(myOrderFragment, OrderHomeFragment.getTagName());
                }
            });

            iconInfoVH.myChatsRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    BaseFragment myOrderFragment = FinancialApplicationFragment.newInstance();
//                    launchFragment(myOrderFragment, FinancialApplicationFragment.getTagName());
                    if (context != null) {
                        closeDrawer();
                        Intent intent = new Intent(context, ConversationActivity.class);
                        if (ApplozicClient.getInstance(context).isContextBasedChat()) {
                            intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                        }
                        context.startActivity(intent);
                    }
                }
            });

            MessageDatabaseService messageDatabaseService = new MessageDatabaseService(context);
            int unreadCount = messageDatabaseService.getTotalUnreadCount();

            if (unreadCount > 0) {
                iconInfoVH.chatCountTV.setText(String.valueOf(unreadCount));
                iconInfoVH.chatCountTV.setVisibility(View.VISIBLE);
            }
        }

        if (viewHolder instanceof InfoVH) {

            InfoVH iconInfoVH = (InfoVH) viewHolder;
            if (DataStore.isAgentType(context)) {
                iconInfoVH.findRetailerTV.setVisibility(View.VISIBLE);
            } else {
                iconInfoVH.findRetailerTV.setVisibility(View.GONE);
            }

            iconInfoVH.changeLanguageTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLocaleDialog();
                }
            });

            iconInfoVH.myCategoryRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseFragment myOrderFragment = SettingL1SegmentFragment.newInstance();
                    launchFragment(myOrderFragment, SettingL1SegmentFragment.getTagName());

                }
            });


            openWebView(iconInfoVH.contactUsTV, context.getString(R.string.contact_us), AppConstants.CONTACT_US_URL);
            openWebView(iconInfoVH.termsConditionTV, context.getString(R.string.terms_condition), AppConstants.TERMS_AND_CONDITION_URL);
            openWebView(iconInfoVH.privacyPolicyTV, context.getString(R.string.privacy_policy), AppConstants.PRIVACY_POLICY_URL);
            openWebView(iconInfoVH.faqTV, context.getString(R.string.FAQ), AppConstants.FAQ_URL);
            openWebView(iconInfoVH.aboutUsTV, context.getString(R.string.about_us), AppConstants.ABOUT_US_URL);
            iconInfoVH.findRetailerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeDrawer();
                    BaseFragment fosFragment = AgentAppFragment.newInstance();
                    launchFragment(fosFragment, AgentAppFragment.getTagName());
                }
            });

        }


    }

    protected void launchFragment(final BaseFragment fragment, final String tagName) {


        if (context != null) {

            closeDrawer();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseFragment.popToHome((BaseActivity) context);
                    BaseFragment.addToBackStack((BaseActivity) context, fragment, tagName);

                }
            }, DRAWER_CLOSE_DURATION);


        }
    }

    public void openWebView(TextView rowView, final String titleName, final String webUrl) {
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                BaseFragment fragment = WebViewFragment.newInstance(titleName, webUrl);
                launchFragment(fragment, WebViewFragment.getTagName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public int getItemViewType(int position) {
        NavigationDrawer.DrawerData drawerData = itemList.get(position);
        switch (drawerData.type) {

            case TYPE_HEADER:
                return HolderType.VIEW_NAV_DRAWER_HEADER.ordinal();

            case TYPE_CATEGORY:
                return HolderType.VIEW_NAV_DRAWER_CATEGORY_ITEM.ordinal();

            case TYPE_ICON_TYPE:
                return HolderType.VIEW_NAV_DRAWER_ICON_ITEM.ordinal();

            case TYPE_APP_INFO:
                return HolderType.VIEW_NAV_DRAWER_INFO_ITEM.ordinal();

            case TYPE_HOME:
                return HolderType.VIEW_NAV_DRAWER_HOME_ITEM.ordinal();

            default:
                return 0;
        }

    }

    @Override
    public void launchFragmentByL2Nav(BaseFragment fragment, String tag) {
        closeDrawer();
        launchFragment(fragment, tag);

    }


    class HeaderVH extends BaseViewHolder {
        @BindView(R.id.headingNameTV)
        protected TextView headingNameTV;

        @BindView(R.id.headingMobileTV)
        protected TextView headingMobileTV;

        @BindView(R.id.creditTV)
        protected TextView creditTV;

        public HeaderVH(View itemView) {
            super(itemView);
        }
    }

    class CatergoryVH extends BaseViewHolder {

        @BindView(R.id.myhomeRL)
        protected RelativeLayout myhomeRL;

        @BindView(R.id.myHomeIV)
        protected ImageView myHomeIV;

        @BindView(R.id.categoryViewLL)
        protected LinearLayout categoryViewLL;


        @BindView(R.id.categoryNameTV)
        protected TextView categoryNameTV;
        @BindView(R.id.recyclerView)
        protected RecyclerView recyclerView;

        public CatergoryVH(View itemView) {
            super(itemView);
        }
    }


    class IconInfoVH extends BaseViewHolder {
        @BindView(R.id.creditFacilityRL)
        protected RelativeLayout creditFacilityRL;
        @BindView(R.id.myOrdersRL)
        protected RelativeLayout myOrdersRL;
        @BindView(R.id.myChatsRL)
        protected RelativeLayout myChatsRL;
        @BindView(R.id.chatCountTV)
        protected TextView chatCountTV;

        public IconInfoVH(View itemView) {
            super(itemView);
        }
    }

    class InfoVH extends BaseViewHolder {
        @BindView(R.id.termsConditionTV)
        protected TextView termsConditionTV;
        @BindView(R.id.privacyPolicyTV)
        protected TextView privacyPolicyTV;
        @BindView(R.id.faqTV)
        protected TextView faqTV;
        @BindView(R.id.aboutUsTV)
        protected TextView aboutUsTV;
        @BindView(R.id.findRetailerTV)
        protected TextView findRetailerTV;
        @BindView(R.id.contactUsTV)
        protected TextView contactUsTV;
        @BindView(R.id.changeLanguageTV)
        protected TextView changeLanguageTV;
        @BindView(R.id.myCategoryRL)
        protected RelativeLayout myCategoryRL;

        public InfoVH(View itemView) {
            super(itemView);
        }
    }


    class HomeVH extends BaseViewHolder {

        @BindView(R.id.dealRL)
        protected RelativeLayout dealRL;

        @BindView(R.id.myShopRL)
        protected RelativeLayout myShopRL;

        @BindView(R.id.reportCardRL)
        protected RelativeLayout reportCardRL;

        @BindView(R.id.customerManagementRL)
        protected RelativeLayout customerManagementRL;

        @BindView(R.id.consumerFinanceRL)
        protected RelativeLayout consumerFinanceRL;

        public HomeVH(View itemView) {
            super(itemView);
        }
    }

    public void closeDrawer() {
        mDrawer.closeDrawers();
    }


    private void showLocaleDialog() {
        final Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_locale_selection);
        dialog.show();

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        Button btnHindi = dialog.findViewById(R.id.btnHindi);
        Button btnEnglish = dialog.findViewById(R.id.btnEnglish);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Lingver.getInstance().setLocale(context, "en");
                Intent intent = ((Activity) context).getIntent();
                ((Activity) context).finish();
                ((Activity) context).startActivity(intent);
            }
        });
        btnHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Lingver.getInstance().setLocale(context, "hi");
                Intent intent = ((Activity) context).getIntent();
                ((Activity) context).finish();
                ((Activity) context).startActivity(intent);
            }
        });
    }
}
