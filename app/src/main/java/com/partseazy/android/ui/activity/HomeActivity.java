package com.partseazy.android.ui.activity;


import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.AnalyticsManager;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.pushnotification.PushNotificationTracker;
import com.partseazy.android.recievers.WakefulReceiver;
import com.partseazy.android.services.UserActiveService;
import com.partseazy.android.ui.appintro.AppIntroductionFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.fragments.deals.buy_deal.BuyDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealDetailFragment;
import com.partseazy.android.ui.fragments.deals.sell_deal.SellDealFragment;
import com.partseazy.android.ui.fragments.finance.CreditFacilityFragment;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.fragments.registration.RegisterL1SegmentFragment;
import com.partseazy.android.ui.fragments.splash.NoUISplashScreen;
import com.partseazy.android.ui.fragments.splash.SplashFragment;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.yariksoffice.lingver.Lingver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;

import static com.partseazy.android.utility.PermissionUtil.REQUEST_CONTACTS_PERMISSION;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class HomeActivity extends BaseActivity {


    public enum ENTRYPOINT {

        DEEPLINK(0),
        PUSHNOTIF(1),
        DEFAULT(2);

        private final int value;

        ENTRYPOINT(final int newValue) {
            value = newValue;
        }
    }

    private boolean isPermissionShownFirstTime;
    private UserActiveService userActiveService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomLogger.d("Inside Home Activity");
        setContentView(R.layout.activity_home);



      /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CONTACTS_PERMISSION);
            }
        }*/
        initAllSDKs();
        goToLauncherScreen();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

     /*   //moengage login in home activity
        MoEHelper.getInstance(getApplicationContext()).setUniqueId(DataStore.getUserPhoneNumber(this));
*/


//        startBackgroundService();
        // NOTE : Uncomment when you want to test the install referrer scenario
//        Intent i = new Intent("com.android.vending.INSTALL_REFERRER");
//        i.setPackage("com.partseazy.android");
//        i.putExtra("referrer", "chat,1");
//        sendBroadcast(i);
    }

    private void initAllSDKs() {
        initApplozic();
        initGA();
    }

    private void initApplozic() {
        ApplozicClient client = ApplozicClient.getInstance(this);
        client.setAppName("PARTZEAZY");
    }

    private void initGA() {
        //To start Google Analytics
        AnalyticsManager.createInstance(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public boolean isDeepLink() {
        //Handling for  Deep Link
        if (getIntent().getData() != null) {

            Uri intUri = getIntent().getData();
            try {

                final String uri = intUri.toString().trim();
                CustomLogger.d("The Deep Link URL is " + uri);

                if (uri.contains("r.partzeazy.in") || (uri.contains("nav_source") && uri.contains("MOE_NOTIFICATION_ID")))
                    return true;

            } catch (Exception ex) {
                CustomLogger.e("Exception in parsing URL");
                return false;
            }
        }
        return false;
    }

    public boolean isPushNotification() {
        //Handling for Push Notification
        if (getIntent() != null && getIntent().getExtras() != null &&
                ((getIntent().getExtras().containsKey("MOE_NOTIFICATION_ID")) || ((getIntent().getExtras().containsKey("push_from"))
                        && getIntent().getStringExtra("push_from").equals("moengage")))) {
            return true;

        }
        return false;
    }


    private void goToLauncherScreen() {

        ENTRYPOINT entrypoint = checkforEntryPoint();
     //  BaseFragment.addToBackStack(this, AppIntroductionFragment.newInstance(), AppIntroductionFragment.getTagName(), false);

        switch (entrypoint) {

            case DEFAULT:
                launchSplashFragment();
                break;

            case DEEPLINK:
                launchNoUISplashFragment();
                break;

            case PUSHNOTIF:
                launchSplashFragment();
                break;


        }
    }

    public ENTRYPOINT checkforEntryPoint() {

        boolean fromDeepLink = false;
        boolean isPush = false;

        ENTRYPOINT entryPoint;
        Intent intent = getIntent();
        if (intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_VIEW)) {
            fromDeepLink = !TextUtils.isEmpty(intent.getData() == null
                    ? null
                    : intent.getData().toString());
        }

        isPush = isPushNotification();


        if (fromDeepLink) {
            if (DataStore.getUserName(this) != null && !DataStore.getUserName(this).equals("")) {
                entryPoint = ENTRYPOINT.DEEPLINK; // launch from Deep link
                CustomLogger.d("checkforEntryPoint - Deep Link");
            } else {
                final Uri uri = Uri.parse(intent.getData().toString().trim());
                final List<String> pathSegments = uri.getPathSegments();

                if (pathSegments.size() > 1) {
                    for (int i = 1; i < pathSegments.size(); i++) {
                        if (i % 2 != 0) {
                            switch (pathSegments.get(i)) {
                                case "inv":
                                    DataStore.setInvitationCode(this, pathSegments.get(i + 1));
                                    break;
                                case "product":
                                    DataStore.setProductId(this, pathSegments.get(i + 1));
                                    break;
                                case "deal":
                                    DataStore.setDealId(this, pathSegments.get(i + 1));
                                    break;
                                case "deals":
                                    DataStore.setGoToDeals(this, "1");
                                    break;
                                case "chat":
                                    DataStore.setGoToChat(this, true);
                                    break;
                                case "supp":
                                    DataStore.setSupplierId(this, pathSegments.get(i + 1));
                                    break;
                                case "cat":
                                    DataStore.setCategoryId(this, pathSegments.get(i + 1));
                                case "sell":
                                    DataStore.setGoToDeals(this, "2");
                                case "report":
                                    DataStore.setGoToReport(this, true);
                            }
                        }
                    }
                }

                entryPoint = ENTRYPOINT.DEFAULT;
                CustomLogger.d("checkforEntryPoint - Default");
            }
        } else if (isPush) { // Need to put some logic for push notifications
            entryPoint = ENTRYPOINT.PUSHNOTIF;
            CustomLogger.d("checkforEntryPoint - Push Notification");
        } else { // Default regular launch
            entryPoint = ENTRYPOINT.DEFAULT;
            CustomLogger.d("checkforEntryPoint - Default");
        }
        return entryPoint;

    }

    private void goToSplashFragment() {

        BaseFragment.addToBackStack(this, SplashFragment.newInstance(), SplashFragment.getTagName(), false);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PermissionUtil.REQUEST_CAMERA_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        DialogUtil.showAlertDialog(HomeActivity.this, true, null, getString(R.string.device_permission_message), getString(R.string.OK)
                                , null, null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        PermissionUtil.requestCameraPermission(HomeActivity.this);
                                    }
                                });
                    }
                } else {
                    DialogUtil.showAlertDialog(HomeActivity.this, true, null, getString(R.string.device_permission_message), getString(R.string.OK)
                            , null, null, new DialogListener() {
                                @Override
                                public void onPositiveButton(DialogInterface dialog) {
                                    PermissionUtil.requestCameraPermission(HomeActivity.this);
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });
                }
                return;
            case PermissionUtil.REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        DialogUtil.showAlertDialog(HomeActivity.this, false, null, getString(R.string.device_permission_message), getString(R.string.OK)
                                , "Settings", null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        PermissionUtil.requestPermission(HomeActivity.this);
                                    }

                                    @Override
                                    public void onNegativeButton(DialogInterface dialog) {
                                        PermissionUtil.requestPermission(HomeActivity.this);
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                });
                    }
                } else {
//                    DialogUtil.showAlertDialog(HomeActivity.this, false, null, getString(R.string.device_permission_message), getString(R.string.OK)
//                            , null, null, new DialogListener() {
//                                @Override
//                                public void onPositiveButton(DialogInterface dialog) {
//                                    PermissionUtil.requestPermission(HomeActivity.this);
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                }
//                            });
                }
                break;
            case REQUEST_CONTACTS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        DialogUtil.showAlertDialog(HomeActivity.this, true, null, getString(R.string.contact_permission_message), getString(R.string.OK)
                                , null, null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        PermissionUtil.requestContactPermission(HomeActivity.this);
                                    }
                                });
                    }
                }
                return;

            case PermissionUtil.REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.STORAGE_PERMISSION, true);
                    } else {
                        DialogUtil.showAlertDialog(HomeActivity.this, true, null, getString(R.string.storage_permission_message), getString(R.string.OK)
                                , null, null, new DialogListener() {
                                    @Override
                                    public void onPositiveButton(DialogInterface dialog) {
                                        PermissionUtil.requestStoragePermission(HomeActivity.this);
                                    }
                                });
                    }
                }
                return;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if ((checkforEntryPoint() == HomeActivity.ENTRYPOINT.DEEPLINK)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    BaseFragment.popToHome(HomeActivity.this);
                    launchFragmentFromDeeplink(true);
                }
            }, 100);
        }

        if ((checkforEntryPoint() == ENTRYPOINT.PUSHNOTIF)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    BaseFragment.popToHome(HomeActivity.this);
                    launchFragmentFromPushNotif(true);
                }
            }, 100);
        }

    }


    private void launchSplashFragment() {


        BaseFragment.addToBackStack(this, SplashFragment.newInstance(), SplashFragment.getTagName(), false);


    }

    private void launchNoUISplashFragment() {

        BaseFragment.addToBackStack(this, NoUISplashScreen.newInstance(), NoUISplashScreen.getTagName(), false);

    }

    public boolean launchFragmentFromPushNotif(boolean isBacktoStack) {

        CustomLogger.d("Launching Fragment from Push Notifications");

        Bundle bundle = getIntent().getExtras();


        try {
            if (null == bundle) {
                CustomLogger.d("No Intent extra available");
                return false;
            } else if (bundle.containsKey("product")) {

                String secondElement = bundle.getString("product");

                if (!TextUtils.isEmpty(secondElement)) {
                    parseProductLink(Integer.parseInt(secondElement), "", isBacktoStack);
                    return true;
                }

            } else if (bundle.containsKey("catalogue")) {

                String catID = bundle.getString("catalogue");
                if (catID != null) {

                    String params = bundle.getString("params");
                    if (params != null) {
                        parseCatalogueLink(Integer.parseInt(catID), params, isBacktoStack);
                    } else {
                        parseCatalogueLink(Integer.parseInt(catID), null, isBacktoStack);
                    }

                    return true;
                }

            } else if (bundle.containsKey("search")) {

                String secondElement = bundle.getString("search");
                if (!TextUtils.isEmpty(secondElement)) {

                    String searchKeyword = secondElement;
                    parseSearchLink(searchKeyword, isBacktoStack);
                    return true;

                }
            } else if (bundle.containsKey("deals")) {
                parseDealsLink(true);
                return true;
            } else if (bundle.containsKey("deal")) {
                String secondElement = bundle.getString("deal");
                if (!TextUtils.isEmpty(secondElement)) {

                    String supplierId = bundle.getString("supp");
                    if (supplierId != null) {
                        DataStore.setSupplierId(HomeActivity.this, supplierId);
                    }
                    parseDealLink(Integer.parseInt(secondElement), "", true);

                    return true;
                }
            } else if (bundle.containsKey("finance")) {
                parseFinanceLink(true);
                return true;
//                String secondElement = bundle.getString("finance");
//                if (!TextUtils.isEmpty(secondElement)) {
//                    parseFinanceLink(true);
//                    return true;
//                }
            } else if (bundle.containsKey("supp")) {
                String secondElement = bundle.getString("supp");
                if (!TextUtils.isEmpty(secondElement)) {
                    parseSupplierLink(secondElement, true);
                    return true;
                }
            } else if (bundle.containsKey("cat")) {
                String secondElement = bundle.getString("cat");
                if (!TextUtils.isEmpty(secondElement)) {
                    parseCategoryLink(secondElement, true);
                    return true;
                }
            } else if (bundle.containsKey("report")) {
                parseReportLink(true);
                return true;
            }

        } catch (Exception ex) {

            CustomLogger.e("Exception in parsing Push Notification", ex);

        }


        return false;

    }

    public boolean launchFragmentFromDeeplink(boolean isBacktoStack) {


        if (!FeatureMap.isFeatureEnabled(FeatureMapKeys.deep_linking))
            return false;

        Uri intUri = getIntent().getData();
        try {
            CustomLogger.d("Launching Fragment from DeepLink" + URLDecoder.decode(intUri.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {


            final Uri uri = Uri.parse(intUri.toString().trim());
            final List<String> pathSegments = uri.getPathSegments();

            for (String val : pathSegments) {
                CustomLogger.d("pathSegment  of deeplink " + val);
            }

            /**

             To launch from adb command to test deep links

             For Search without filters
             adb shell am start  -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d partseazy://partseazy.in/search/mobile com.partseazy.android


             Deep Link URL
             android-app://com.partseazy.android/partseazy/partseazy.in/search/mobile



             -------->>

             For Catalogue with filters
             adb shell am start  -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d
             partseazy://partseazy.in/catalogue/12/%5B%7B%22name%22%3A%22_f.brand%22%2C%22value%22%3A%5B%22Jivi%22%5D%7D%5D com.partseazy.android

             Deep Link URL
             android-app://com.partseazy.android/partseazy/partseazy.in/catalogue/12/[{"name":"_f.brand","value":["Jivi"]}]

             -------->>


             For Product
             adb shell am start  -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d partseazy://partseazy.in/product/72 com.partseazy.android

             Deep Link URL
             android-app://com.partseazy.android/partseazy/partseazy.in/product/72

             **/

            if (pathSegments.size() > 1) {
                final String firstElement = pathSegments.get(1);

                //for product URL schema would be partseazy://partseazy.in/product/12


                if ("product".equalsIgnoreCase(firstElement)) {

                    if (pathSegments.size() > 2) {

                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {
                            parseProductLink(Integer.parseInt(secondElement), "", isBacktoStack);
                            return true;
                        }
                    }

                    //for product URL schema would be  partseazy://partseazy.in/catalogue/12345
                } else if ("chat".equalsIgnoreCase(firstElement)) {
                    CustomLogger.d("Launching Deals Fragment from URL");
                    Fragment fragment = DealHomeFragment.newInstance();
                    launchFragment(fragment, fragment.getTag(), isBacktoStack);
                    DataStore.setGoToChat(this, false);
                    Intent intent = new Intent(this, ConversationActivity.class);
                    if (ApplozicClient.getInstance(this).isContextBasedChat()) {
                        intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                    }
                    this.startActivity(intent);
                    return true;
                } else if ("deals".equalsIgnoreCase(firstElement)) {
                    CustomLogger.d("Launching Deals Fragment from URL");
                    Fragment fragment = DealHomeFragment.newInstance();
                    launchFragment(fragment, fragment.getTag(), isBacktoStack);
                    return true;
                } else if ("report".equalsIgnoreCase(firstElement)) {
                    CustomLogger.d("Launching Report Fragment from URL");
                    Fragment fragment = SellDealFragment.newInstance(true);
                    launchFragment(fragment, fragment.getTag(), isBacktoStack);
                    return true;
                } else if (firstElement.equalsIgnoreCase("supp")) {
                    if (pathSegments.size() > 2) {
                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {
                            parseSupplierLink(secondElement, isBacktoStack);
                            return true;
                        }
                    }
                } else if (firstElement.equalsIgnoreCase("cat")) {
                    if (pathSegments.size() > 2) {
                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {
                            parseCategoryLink(secondElement, isBacktoStack);
                            return true;
                        }
                    }
                } else if ("selldeal".equalsIgnoreCase(firstElement)) {
                    if (pathSegments.size() > 2) {
                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {
                            parseSellDealLink(Integer.parseInt(secondElement), "", isBacktoStack);
                            return true;
                        }
                    }
                } else if ("sell".equalsIgnoreCase(firstElement)) {
                    CustomLogger.d("Launching Deals Fragment from URL");
                    Fragment fragment = DealHomeFragment.newInstance(true);
                    launchFragment(fragment, fragment.getTag(), isBacktoStack);
                    return true;
                } else if ("deal".equalsIgnoreCase(firstElement)) {
                    if (pathSegments.size() > 2) {

                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {

                            String thirdElement = null;

                            if (pathSegments.size() > 3) {
                                thirdElement = pathSegments.get(3);
                                if (!TextUtils.isEmpty(secondElement) && thirdElement.equalsIgnoreCase("supp")) {
                                    String supplierId = pathSegments.get(4);
                                    CustomLogger.d("Fourth Item  " + supplierId);
                                    DataStore.setSupplierId(HomeActivity.this, supplierId);

                                }

                            }

                            parseDealLink(Integer.parseInt(secondElement), "", isBacktoStack);
                            return true;
                        }
                    }
                } else if ("catalogue".equalsIgnoreCase(firstElement)) {

                    if (pathSegments.size() > 2) {

                        String secondElement = pathSegments.get(2);

                        if (!TextUtils.isEmpty(secondElement)) {


                            Integer catID = Integer.parseInt(secondElement);

                            String thirdElement = null;

                            if (pathSegments.size() > 3) {
                                thirdElement = pathSegments.get(3);
                            }

                            if (thirdElement != null && !TextUtils.isEmpty(thirdElement)) {
                                parseCatalogueLink(catID, thirdElement, isBacktoStack);

                            } else {
                                parseCatalogueLink(catID, null, isBacktoStack);
                            }
                        }
                    }

                    return true;

                    //for search URL schema would be  partseazy://partseazy.in/search/keyword

                } else if ("search".equalsIgnoreCase(firstElement)) {

                    if (pathSegments.size() > 2) {

                        String secondElement = pathSegments.get(2);
                        if (!TextUtils.isEmpty(secondElement)) {

                            String searchKeyword = secondElement;
                            parseSearchLink(searchKeyword, isBacktoStack);
                            return true;

                        }
                    }

                }
            }
        } catch (Exception ex) {
            CustomLogger.e("Exception in parsing Deep Link URL", ex);

            return false;
        }

        //By default, Home screen should be launched

        return false;

    }


    private void parseProductLink(Integer productID, String productName, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Product ID is " + productID);
        Fragment fragment = ProductDetailFragment.newInstance(productID, productName);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseSupplierLink(String supplierFilterId, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and supplierFilterId is " + supplierFilterId);
        Fragment fragment = DealHomeFragment.newInstance(supplierFilterId, DealHomeFragment.SUPPLIER_FILTER_PARAM_DEEP_LINK);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseCategoryLink(String categoryFilterId, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and categoryFilterId is " + categoryFilterId);
        Fragment fragment = DealHomeFragment.newInstance(categoryFilterId, DealHomeFragment.CATEGORY_FILTER_PARAM_DEEP_LINK);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseDealLink(Integer dealID, String dealName, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Deal ID is " + dealID);
        Fragment fragment = BuyDealDetailFragment.newInstance(dealID, dealName);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseSellDealLink(Integer dealID, String dealName, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Deal ID is " + dealID);
        Fragment fragment = SellDealDetailFragment.newInstance(dealID, dealName);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);
    }

    private void parseDealsLink(boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from Deals");
        Fragment fragment = DealHomeFragment.newInstance();
        launchFragment(fragment, fragment.getTag(), isBacktoStack);
    }

    private void parseFinanceLink(boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Credit facility ID ");
        Fragment fragment = CreditFacilityFragment.newInstance();
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseReportLink(boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Report Card ");
        Fragment fragment = SellDealFragment.newInstance(true);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }

    private void parseCatalogueLink(Integer catlogueID, String params, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Catalogue ID is " + catlogueID);
        Fragment fragment;
        if (params != null)
            fragment = CatalogueFragment.newInstance(catlogueID, params);
        else
            fragment = CatalogueFragment.newInstance(catlogueID);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);
    }

    private void parseSearchLink(String searchKeyword, boolean isBacktoStack) {
        CustomLogger.d("Launching Fragment from URL and Search keyword is " + searchKeyword);
        Fragment fragment = CatalogueFragment.newInstance(searchKeyword, true,false);
        launchFragment(fragment, fragment.getTag(), isBacktoStack);

    }


    private void launchFragment(Fragment fragment, String TAG, boolean isBacktoStack) {

//        BaseFragment.addToBackStack(this, HomeFragment.newInstance(), HomeFragment.class.getSimpleName());
        BaseFragment.replaceFragment(getSupportFragmentManager(),
                R.id.container, DealHomeFragment.newInstance(), true, true, DealHomeFragment.getTagName());

        BaseFragment.replaceFragment(getSupportFragmentManager(),
                R.id.container, (BaseFragment) fragment, isBacktoStack, true, TAG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void sendBroadcast() {

        sendBroadcast(new Intent(this, WakefulReceiver.class));
    }

    private void startBackgroundService() {

        userActiveService = new UserActiveService();
        Intent intent = new Intent(this, userActiveService.getClass());
        if (!isMyServiceRunning(userActiveService.getClass())) {
            startService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


}
