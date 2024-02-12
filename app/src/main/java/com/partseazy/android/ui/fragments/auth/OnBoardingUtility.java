package com.partseazy.android.ui.fragments.auth;

import android.content.Context;
import android.content.Intent;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.appintro.AppIntroductionFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealFragment;
import com.partseazy.android.ui.fragments.forceupgrade.AppExitDailog;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.fragments.registration.RegisterBasicDetailsFragment;
import com.partseazy.android.ui.fragments.registration.RegisterL1SegmentFragment;
import com.partseazy.android.ui.fragments.registration.RegisterL2SegmentFragment;
import com.partseazy.android.ui.fragments.registration.RegisterL3SegmentFragment;
import com.partseazy.android.ui.fragments.registration.RegisterMobileFragment;
import com.partseazy.android.ui.fragments.registration.RegisterUserTypeFragment;
import com.partseazy.android.ui.model.registration.OnBoardingStatus;
import com.partseazy.android.utility.BlockUserStatus;


/**
 * Created by gaurav on 17/02/17.
 */

public class OnBoardingUtility {


    public static OnBoardFragments onBoardingNavigation(Context context, OnBoardingStatus data) {

        BaseFragment fragment = null;
        String tagName = "";


        if (data.forced_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN1.getStatus())
                || "0".equals(DataStore.getUserID(context))) {

            fragment = RegisterMobileFragment.newInstance(false);
            tagName = RegisterMobileFragment.getTagName();

        } else if (data.actual_status.equals(BlockUserStatus.BLOCK_CUSTOMER.getStatus())) {

            AppExitDailog appExitDailog = new AppExitDailog();
            appExitDailog.showUpgradeDialog(context);

        } else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN1.getStatus())) {

            fragment = RegisterMobileFragment.newInstance(false);
            tagName = RegisterMobileFragment.getTagName();

        } else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN2.getStatus())) {

            fragment = RegisterMobileFragment.newInstance(true);
            tagName = RegisterMobileFragment.getTagName();
        }
        else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SPLASHBANNER.getStatus())) {
             fragment = AppIntroductionFragment.newInstance();;
             tagName = AppIntroductionFragment.getTagName();;
        }
        else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN3.getStatus())) {

            fragment = RegisterUserTypeFragment.newInstance();
            tagName = RegisterUserTypeFragment.getTagName();


        } else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN4.getStatus())) {

            fragment = RegisterBasicDetailsFragment.newInstance(data.role);
            tagName = RegisterBasicDetailsFragment.getTagName();

        }
        else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN5.getStatus()) && (data.storeId != 0)) {
            int storeId = data.storeId;
            fragment = RegisterL2SegmentFragment.newInstance(storeId);
            tagName = RegisterL2SegmentFragment.getTagName();

        } else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN6.getStatus()) && (data.storeId != 0)) {
            int storeId = data.storeId;
            fragment = RegisterL3SegmentFragment.newInstance(storeId);
            tagName = RegisterL3SegmentFragment.getTagName();

        } else if (data.actual_status.equals(BaseAuthFragment.OnBoardingSt.SCREEN7.getStatus())) {

            if (DataStore.getProductId(context) != null && !DataStore.getProductId(context).equals("")) {
                fragment = ProductDetailFragment.newInstance(Integer.parseInt(DataStore.getProductId(context)), "");
                tagName = ProductDetailFragment.getTagName();
                DataStore.setProductId(context, null);
            } else if ("1".equals(DataStore.getGoToDeals(context))) {
/*** Code comment bcoz of we want to transfer page direct to homefragment

                fragment = DealHomeFragment.newInstance();
                tagName = DealHomeFragment.getTagName();
                DataStore.setGoToDeals(context, "0");*/
                fragment = HomeFragment.newInstance();
                tagName = HomeFragment.getTagName();
                DataStore.setGoToDeals(context, "0");
            } else if ("2".equals(DataStore.getGoToDeals(context))) {
         /*
              Code comment bcoz of we want to transfer page direct to homefragmen
                fragment = DealHomeFragment.newInstance(true);
                tagName = DealHomeFragment.getTagName(); */
                fragment = HomeFragment.newInstance();
                tagName = HomeFragment.getTagName();
                DataStore.setGoToDeals(context, "0");
            } else if (DataStore.getDealId(context) != null && !DataStore.getDealId(context).equals("")) {
            /*
              Code comment bcoz of we dont want anything else from here
            fragment = BuyDealDetailFragment.newInstance(Integer.parseInt(DataStore.getDealId(context)), "");
                tagName = BuyDealDetailFragment.getTagName();*/
                DataStore.setDealId(context, null);
            } else if (DataStore.getGoToReport(context)) {
                fragment = SellDealFragment.newInstance(true);
                tagName = SellDealFragment.getTagName();
                DataStore.setGoToReport(context, false);
            } else {
                if (DataStore.getSupplierId(context) != null && !DataStore.getSupplierId(context).equals("")) {
                    fragment = DealHomeFragment.newInstance(DataStore.getSupplierId(context), DealHomeFragment.SUPPLIER_FILTER_PARAM_DEEP_LINK);
                }else if (DataStore.getCategoryId(context) != null && !DataStore.getCategoryId(context).equals("")){
                    fragment = DealHomeFragment.newInstance(DataStore.getCategoryId(context), DealHomeFragment.CATEGORY_FILTER_PARAM_DEEP_LINK);
                } else {
                    fragment = HomeFragment.newInstance();// to changed
                    //  fragment = DealHomeFragment.newInstance();// to changed
                }
                tagName = HomeFragment.getTagName();// to changed
                // tagName = DealHomeFragment.getTagName();// to changed

                if (DataStore.getGoToChat(context)) {
                    DataStore.setGoToChat(context, false);
                    Intent intent = new Intent(context, ConversationActivity.class);
                    if (ApplozicClient.getInstance(context).isContextBasedChat()) {
                        intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                    }
                    context.startActivity(intent);
                }
            }
        } else {

            //Default Case

            CustomLogger.d("Can't Launch any screen, so by deafault going back to Mobile Screen");
            fragment = RegisterMobileFragment.newInstance(false);
            tagName = RegisterMobileFragment.getTagName();

        }


        CustomLogger.d("Going to Launch " + tagName);


        return new OnBoardFragments(fragment, tagName);


    }


    public static class OnBoardFragments {

        public BaseFragment fragment;
        public String tagName;

        public OnBoardFragments(BaseFragment fragment, String tagName) {
            this.fragment = fragment;
            this.tagName = tagName;
        }

    }

}
