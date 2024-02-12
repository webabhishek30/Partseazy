package com.partseazy.android.utility;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.fragments.finance.FinancialApplicationFragment;
import com.partseazy.android.ui.model.deal.ContactModel;
import com.partseazy.android.ui.model.deal.selldeallist.Sku;
import com.partseazy.android.ui.model.home.usershop.Child;
import com.partseazy.android.ui.model.home.usershop.Child_;
import com.partseazy.android.ui.model.home.usershop.L1CategoryData;
import com.partseazy.android.ui.model.home.usershop.L2CategoryData;
import com.partseazy.android.ui.model.home.usershop.L3CategoryData;
import com.partseazy.android.ui.model.home.usershop.UserShopResult;
import com.partseazy.android.ui.model.prepaid.PrepaidData;
import com.partseazy.android.ui.model.supplier.shop.Address;
import com.partseazy.android.ui.model.supplier.shop.ShopInfo;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.partseazy.android.network.request.WebServiceConstants.ANDROID_HEADER_PLATFORM;


/**
 * Created by Pumpkin Guy on 20/11/16.
 */

public class CommonUtility {


    public enum IMGTYPE {
        FULLIMG,
        HALFIMG,
        QUARTERIMG,
        THUMBNAILIMG,


    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public static long getUnixTimeStamp() {
        Date date = new Date();
        long unixtime = 0;

        SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMddkkmmss");
        dfm.setTimeZone(TimeZone.getDefault());
        String strDefault = dfm.format(date);
        dfm.setTimeZone(TimeZone.getTimeZone("IST"));//Specify your timezone
        try {
            unixtime = dfm.parse(strDefault).getTime();
            unixtime = unixtime / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixtime;

    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager conMngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMngr == null ? null : conMngr.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    // method for network connectivity..
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }


    /**
     * @param pMobileNumber
     * @param pPlusSignNeeded
     * @param pMinLength
     * @return
     * @note maxDigit validation can be implemented by XML
     */
    public static boolean isValidMobileNumber(String pMobileNumber, boolean pPlusSignNeeded, int pMinLength, int maxLength) {

        if (TextUtils.isEmpty(pMobileNumber)) {
            return false;
        }
        if (pMobileNumber.trim().contains("+")) {
            pMobileNumber = pMobileNumber.replace("+", "");
        }

        if (!pMobileNumber.matches("^[0-9]+$")) {
            return false;
        }

        if (Integer.parseInt(pMobileNumber.substring(0, 1)) < 6)
            return false;
        if (pMobileNumber.trim().length() < pMinLength) {
            return false;
        }
        if (pMobileNumber.trim().length() > maxLength) {
            return false;
        }
        return true;
    }

    public static String getVerificationCode(String message) {
        String code = null;
        int startIndex = AppConstants.OTP_START_INDEX;
        int codeLength = AppConstants.OTP_LENGTH;
        if (startIndex != -1) {
            code = message.substring(startIndex, startIndex + codeLength);
            return code;
        }
        return code;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean isValidPANNumber(String panNumber) {
        if (panNumber.length() == 10) {
            String s = panNumber.toString(); // get your editext value here
            Pattern pattern = Pattern.compile("[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                panNumber = panNumber.toString();
                return true;
            }
        }
        return false;
    }

    public static boolean isValidGSTIN(String gstin) {
        if (gstin.length() == 15) {
            String s = gstin.toString(); // get your editext value here
            Pattern pattern = Pattern.compile("[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9a-zA-Z]{1}");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                gstin = gstin.toString();
                return true;
            }
        }
        return false;
    }


    public static String getEmailFromMobile(Context context) {
        String email = null;
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(context).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    email = account.name;
                }
            }
        }
        return email == null ? "" : email;

    }

    /**
     * This code does not always work, since Cell phone number is dependent on the SIM Card and the Network
     * operator / Cell phone carrier.
     * Requires runt time permission <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     *
     * @param context
     * @return - phone number if found, null otherwise
     */
    public static String getDevicePhoneNUmber(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        String phone = null;
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager)
                    context.getSystemService(TELEPHONY_SERVICE);
            phone = mTelephonyMgr.getLine1Number();
        }
        return phone == null ? "" : phone;
    }

    public static void animateLayoutBottomToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void animateLayoutTopToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void animateLayoutRightToLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(view.getWidth(), 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void animateLayoutLeftToRight(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public static void dialPhoneNumber(Context context, String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            if (PartsEazyApplication.partsEazyAppContext != null) {
                PartsEazyApplication.partsEazyAppContext.showToast("Feature is not supported");
            }
        }
    }

    public static String getImagePath(Context context, Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            CustomLogger.e(e.toString());
        }
        return "";
    }

    public static File convertBitmapToFile(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imageFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            imageFile.createNewFile();
            fo = new FileOutputStream(imageFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            CustomLogger.e("", e);
        }
        return imageFile;
    }

    public static int convertionPaiseToRupee(int paise) {
//        int valueTotal = (paise / 100);
//        int valueRest = paise % 100;
//        String totalValue = String.valueOf(valueTotal) + "." + String.valueOf(valueRest);
        int rupee = (int) Math.round(Float.valueOf(paise) / 100.0f);
        return rupee;
    }

    public static int convertionRupeeToPaise(int rupee) {
        return rupee * 100;
    }


    public static String convertionPaiseToRupeeString(int paise) {
        int valueTotal = (paise / 100);
        int valueRest = paise % 100;
        String resVal = String.valueOf(valueRest);
        if (resVal.length() == 1) {
            if (valueRest > 9) {
                resVal = resVal + "0";
            } else {
                resVal = "0" + resVal;
            }
        }
        String totalValue = String.valueOf(valueTotal) + "." + resVal;
        //float rupee=Math.round(Float.valueOf(totalValue));
        return totalValue;
    }

    public static String convertionPaiseToRupeeStringHTML(int paise) {
        int valueTotal = (paise / 100);
        int valueRest = paise % 100;
        String resVal = String.valueOf(valueRest);
        if (resVal.length() == 1) {
            if (valueRest > 9) {
                resVal = resVal + "0";
            } else {
                resVal = "0" + resVal;
            }
        }
        String totalValue = String.valueOf(valueTotal) + ".<small><small>" + resVal + "</small></small>";
        //float rupee=Math.round(Float.valueOf(totalValue));
        return totalValue;
    }


    public static String convertionPaiseToRupeeSupScript(int paise) {
        int valueTotal = (paise / 100);
        int valueRest = paise % 100;
        String resVal = String.valueOf(valueRest);
        if (resVal.length() == 1) {
            if (valueRest > 9) {
                resVal = resVal + "0";
            } else {
                resVal = "0" + resVal;
            }
        }

        String totalValue;

        totalValue = String.valueOf(valueTotal) + ".<small><small>" + resVal + "</small></small>";
        return totalValue;
    }


    public static String getFormattedImageUrl(String imageUrl, ImageView imageView, IMGTYPE imgtype) {

        if (imageUrl == null) {
            if (imageView instanceof NetworkImageView) {
                ((NetworkImageView) imageView).setDefaultImageResId(com.android.volley.R.drawable.b2c2_default_img);
                ((NetworkImageView) imageView).setErrorImageResId(com.android.volley.R.drawable.b2c2_default_img);
            }
            return "";
        }

        if (imageUrl.contains("http") || imageUrl.contains("https")) {
            return imageUrl;
        } else {
            return loadImage(imageUrl, imageView, imgtype);
        }
    }

    public static String getFormattedImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return "";
        }

        if (imageUrl.contains("http") || imageUrl.contains("https")) {
            return imageUrl;
        } else {
            if (BuildConfig.URL.contains("139.59.11.127")) {
                return "http://139.59.11.127:9090/display/fit/720x592/" + imageUrl;
            }
            return "http://cdn.b2c2.store/display/fit/720x592/" + imageUrl;
        }
    }

    public static String getFormattedImageUrlForSlider(String imageUrl, ImageView imageView, int ACTUALWIDTH, int ACTUALHEIGHT, int IMAGEVIEW_PX_HEIGHT) {

        if (imageUrl == null) {
            if (imageView instanceof NetworkImageView) {
                ((NetworkImageView) imageView).setDefaultImageResId(com.android.volley.R.drawable.b2c2_default_img);
                ((NetworkImageView) imageView).setErrorImageResId(com.android.volley.R.drawable.b2c2_default_img);
            }
            return "";
        }

        if (imageUrl.contains("http") || imageUrl.contains("https")) {
            return imageUrl;
        } else {
            return loadImageOnCustomSized(imageUrl, imageView, ACTUALWIDTH, ACTUALHEIGHT, IMAGEVIEW_PX_HEIGHT);
        }
    }

    private static String loadImageOnCustomSized(String imageUrl, ImageView imageView, int ACTUALWIDTH, int ACTUALHEIGHT, int IMAGEVIEW_PX_HEIGHT) {

        if (imageView != null) {
//            DisplayMetrics displaymetrics = getDisplayMatrices(imageView.getContext());
//            int width = displaymetrics.widthPixels;
//            int height = displaymetrics.heightPixels;
            int width = 0;
            int height = IMAGEVIEW_PX_HEIGHT;

            CustomLogger.d("The Image view width is " + width);
            CustomLogger.d("The Image view height is " + height);
            CustomLogger.d("The Image view ACTUALWIDTH  is " + ACTUALWIDTH);
            CustomLogger.d("The Image view ACTUALHEIGHT is " + ACTUALHEIGHT);

            double ratioToActual = ((double) ACTUALWIDTH / ACTUALHEIGHT);
            int customWidth = (int) Math.round(ratioToActual * height);
            CustomLogger.d("ratioToActual " + ratioToActual);

            imageUrl = imageUrl.replaceAll(" ", "");

            imageUrl = replaceHeightWidthImgURL(imageUrl, String.valueOf(customWidth), String.valueOf(height), true);

            if (imageView instanceof NetworkImageView) {
                ((NetworkImageView) imageView).setDefaultImageResId(com.android.volley.R.drawable.b2c2_default_img);
                ((NetworkImageView) imageView).setErrorImageResId(com.android.volley.R.drawable.b2c2_default_img);
            }


        }

        return imageUrl;
    }


    private static String loadImage(String imageUrl, ImageView imageView, IMGTYPE imgtype) {

        if (imageView != null) {
            DisplayMetrics displaymetrics = getDisplayMatrices(imageView.getContext());
            int width = displaymetrics.widthPixels;
            int height = displaymetrics.heightPixels;

            CustomLogger.d("The Image view width is " + width);
            CustomLogger.d("The Image view height is " + height);


            if (imgtype == IMGTYPE.FULLIMG) {

                width = width;
                height = height;
            } else if (imgtype == IMGTYPE.HALFIMG) {
                width = width;
                height = height / 2;
            } else if (imgtype == IMGTYPE.QUARTERIMG) {
                width = width / 2;
                height = height / 4;
            } else if (imgtype == IMGTYPE.THUMBNAILIMG) {
                width = width / 3;
                height = height / 5;
            }

            imageUrl = imageUrl.replaceAll(" ", "");

            imageUrl = replaceHeightWidthImgURL(imageUrl, String.valueOf(width), String.valueOf(height), false);

            if (imageView instanceof NetworkImageView) {
                ((NetworkImageView) imageView).setDefaultImageResId(com.android.volley.R.drawable.b2c2_default_img);
                ((NetworkImageView) imageView).setErrorImageResId(com.android.volley.R.drawable.b2c2_default_img);
            }


        }

        return imageUrl;
    }

    private static String replaceHeightWidthImgURL(String imgURl, String width, String height, boolean isStretchImg) {

        String imageURL;

        if (isStretchImg) {
            imageURL = StaticMap.IMG_STRECH_URL;
        } else {
            imageURL = StaticMap.IMG_BASE_URL;
        }
        CustomLogger.d("The Image view final width  is " + width);
        CustomLogger.d("The Image view final height is " + height);

        if (imageURL != null) {

            imageURL = imageURL.replaceAll(StaticMap.IMG_WIDTH, width);
            imageURL = imageURL.replaceAll(StaticMap.IMG_HEIGHT, height);
            imageURL = imageURL.replaceAll(StaticMap.IMG_SUB_PATH, imgURl);

        }

        imgURl = imageURL;
        return imgURl;
    }


    /**
     * Call this method to load image from remote server into ImageVew or NetworkImageView
     *
     * @param imageView - imageview to load image to
     * @param imageUrl  - remote url of the image
     */
    public static void setImageSRC(ImageLoader imageLoader, final View imageView, final String imageUrl) {
        try {
            if (!(imageView instanceof ImageView)) {
                CustomLogger.w("Inavlid ImageView reference");
                return;
            }
            //  NetworkImageView
            if (imageView instanceof NetworkImageView)
                ((NetworkImageView) imageView).setImageUrl(imageUrl, imageLoader);
            else {
                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            // load image into imageview
                            ((ImageView) imageView).setImageBitmap(response.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(Request request, VolleyError error) {
                        CustomLogger.w("Image Load Error: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            CustomLogger.e(e.toString());
        }
    }


    private static DisplayMetrics getDisplayMatrices(Context context) {
        DisplayMetrics displaymetrics = null;
        if (context instanceof BaseActivity) {
            displaymetrics = ((BaseActivity) context).getDeviceMatrices();
        } else {
            displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }
        return displaymetrics;
    }

    public static String getIEMINumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    public static String getEMMMddyyyyHHmmDateFromDateCreated(String dateCreated) {
        String convertedDate = null;
        //String initialDeliveryDate = "2017-01-29T17:01:41.191946+05:30";
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy HH:mm a");
        try {
            convertedDate = formatter.format(parser.parse(dateCreated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getddmmyyyyDateFromDateCreated(String dateCreated) {
        String convertedDate = null;
        //String initialDeliveryDate = "2017-01-29T17:01:41.191946+05:30";
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            convertedDate = formatter.format(parser.parse(dateCreated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String getPriceRangeforPDP(int min, int max) {
        CustomLogger.d("min ::" + min + "max :: " + max);
        int minRs = convertionPaiseToRupee(min);
        int maxRs = convertionPaiseToRupee(max);
        if (minRs == maxRs) {
            return minRs + "";
        } else if (minRs > maxRs) {
            return minRs + "";
        } else {
            return minRs + " - " + maxRs;
        }
//        if (min == max) {
//            return convertionPaiseToRupee(min) + "";
//        } else if (min > max) {
//            return convertionPaiseToRupee(min) + "";
//        } else {
//            return convertionPaiseToRupee(min) + " - " + convertionPaiseToRupee(max);
//        }
    }

    public static String getPriceRangeInHTMLforPDP(int min, int max) {
        CustomLogger.d("min ::" + min + "max :: " + max);
//        int minRs = convertionPaiseToRupee(min);
//        int maxRs = convertionPaiseToRupee(max);
        if (min == max) {
            return convertionPaiseToRupeeSupScript(min);
        } else if (min > min) {
            return convertionPaiseToRupeeSupScript(min);
        } else {
            return convertionPaiseToRupeeSupScript(min) + " - " + convertionPaiseToRupeeSupScript(max);
        }
    }

    public static void getPhoneData(Context context) {

        CustomLogger.d("Device  OS Version: " + System.getProperty("os.version"));
        CustomLogger.d("Device  Brand: " + Build.BRAND);
        CustomLogger.d("Device  Type: " + Build.TYPE);
        CustomLogger.d("Device  Model: " + Build.MODEL);
        CustomLogger.d("Device  Manufacturer: " + Build.MANUFACTURER);
        CustomLogger.d("Device  ID: " + Build.ID);
        CustomLogger.d("Device  id : " + CommonUtility.getDeviceId(context));
        CustomLogger.d("Device  IEMI : " + CommonUtility.getIEMINumber(context));
        CustomLogger.d("Device  OS API Level: " + android.os.Build.VERSION.RELEASE);
        CustomLogger.d("Device  MAC : " + getDeviceMacAddress(context));
    }


    public static boolean isValidPincode(String pincode) {
        if (pincode == null) {
            return false;
        } else {
            String PINCODE_PATTERN = "^[0-9]{6}$";

            Pattern pattern = Pattern.compile(PINCODE_PATTERN);
            Matcher matcher = pattern.matcher(pincode);
            return matcher.matches();
        }
    }

    public static boolean isValidAddress(String address) {
        if (address == null || address.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidLastName(String lastName) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (lastName == null) {
            return false;
        } else {
            Matcher m = p.matcher(lastName);
            return m.matches();
        }
    }

    public static boolean isValidFirstName(String firstname) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (firstname == null) {
            return false;
        } else {
            Matcher m = p.matcher(firstname);
            return m.matches();
        }
    }


    public static Bitmap decodeImage(Context context, int resourceId) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resourceId, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1000; // you are free to modify size as your requirement

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(context.getResources(), resourceId, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<ContactModel> getPhoneContactList(Context context) {
        List<ContactModel> contactModelList = new ArrayList<>();
        Map<String, ContactModel> contactMap = new LinkedHashMap<>();
        ContentResolver contentResolver = context.getContentResolver();
        try {
            String[] PROJECTION = new String[]{
                    ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,};

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI.buildUpon()
//                .appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES, "1")
//                .build();

            String filter = "" + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 ";
//        String filter = "" + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE + "=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            String order = ContactsContract.Contacts.DISPLAY_NAME + " ASC";


            Cursor cursor = contentResolver.query(uri, PROJECTION, filter, null, order);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    ContactModel contactModel = new ContactModel();

                    contactModel.contactName = name;
                    contactModel.contactPhoneNumber = getFormattedMobile(phoneNumber);
                    // contactModelSet.add(contactModel);
                    contactMap.put(name, contactModel);

                }
                cursor.close();
            }

            for (Map.Entry<String, ContactModel> mapItem : contactMap.entrySet()) {
                contactModelList.add(mapItem.getValue());
            }
        } catch (Exception e) {
            CustomLogger.e("Exception :-", e);
        }
        return contactModelList;
    }

    public static String getNameAbbrevation(String givenString) {

        String[] arr = givenString.trim().split(" ");

        StringBuffer sb = new StringBuffer();
        try {

            if (arr.length >= 2) {
                sb.append(Character.toUpperCase(arr[0].trim().charAt(0)));
                sb.append(Character.toUpperCase(arr[1].trim().charAt(0)));
            } else if (arr.length == 1) {
                sb.append(Character.toUpperCase(arr[0].trim().charAt(0)));
            }
        } catch (Exception e) {
            CustomLogger.e("Exception :-" + e);
        }
        return sb.toString().trim();

    }

    public static String getFormattedName(String givenString) {

        String[] arr = givenString.trim().split(" ");

        StringBuffer sb = new StringBuffer();
        try {

            for (int i = 0; i < arr.length; i++) {
                String name = arr[i];
                if (name.length() > 0) {
                    String namePart = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    sb.append(namePart + " ");
                } else {
                    sb.append(name.substring(0, 1).toUpperCase());
                }

            }

            String finalString = sb.toString().trim();
            String[] dotNameArr = finalString.split("\\.");
            int dotArrLen = dotNameArr.length;
            if (dotArrLen > 1) {
                sb = new StringBuffer();
                for (int i = 0; i < dotNameArr.length; i++) {

                    String name = dotNameArr[i];
                    if (name.length() == 1) {
                        String namePart = name.substring(0, 1).toUpperCase();
                        sb.append(namePart + ".");
                    } else {
                        sb.append(name);
                    }

                }

            }


        } catch (Exception e) {
            CustomLogger.e("Exception :-" + e);
        }
        return sb.toString().trim();

    }

    public static String getFormattedMobile(String mobileNumber) {
        mobileNumber = mobileNumber.trim();
        mobileNumber = mobileNumber.replace("+91", "");
        mobileNumber = mobileNumber.replaceAll("(?<=\\d) +(?=\\d)", "");

        if (mobileNumber.length() == 11) {
            mobileNumber = mobileNumber.substring(1, mobileNumber.length());
        }
        return mobileNumber.trim();
    }

    public static String getDateFromDateDailog(int dayOfMonth, int monthOfYear, int year) {

        Date date = null;
        String dateStr = "";
        String month = monthOfYear + "";
        String day = dayOfMonth + "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if ((monthOfYear + 1) < 10) {

            month = "0" + monthOfYear;
        }
        if (dayOfMonth < 10) {

            day = "0" + dayOfMonth;
        }
        dateStr = day + "/" + month + "/" + year;

        try {
            date = dateFormat.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    public static String getTimeFromTimeDailog(int selectedMinute, int selectedHour) throws ParseException {

        String aMpM = "AM";
        String hours = null;
        String minutes = null;
        String timeStr = null;

        //Make the 24 hour time format to 12 hour time format
        int currentHour = selectedHour;
        if (selectedHour > 11) {
            aMpM = "PM";
            currentHour = selectedHour - 12;

        }

        if (currentHour / 10 == 0) {
            hours = "0" + currentHour;
        } else {
            hours = currentHour + "";
        }


        if ((selectedMinute / 10) == 0) {
            minutes = "0" + selectedMinute;
        } else {
            minutes = selectedMinute + "";
        }

        timeStr = hours + ":" + minutes + " " + aMpM;
        return timeStr;
//        DateFormat mSDF = new SimpleDateFormat("hh:mm a");
//
//        //this format date actully present
//        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
//        return mSDF.format(formatter.parse(timeStr));
    }

    public static String getTimeHHmmss(int selectedHour, int selectedMinute) {
        String selectedTime = selectedHour + ":" + selectedMinute;
        String convertedDate = null;
        DateFormat parser = new SimpleDateFormat("HH:mm");
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            convertedDate = formatter.format(parser.parse(selectedTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CustomLogger.d("formatted Time ::" + convertedDate);
        return convertedDate;
    }

    public static String getTimeHHmmssFromAMPM(String time) {

        String convertedDate = null;
        DateFormat parser = new SimpleDateFormat("HH:mm a");
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            convertedDate = formatter.format(parser.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CustomLogger.d("formatted Time ::" + convertedDate);
        return convertedDate;
    }

    public static String getDateYYYYMMDDFormat(String date) {

        String convertedDate = null;
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            convertedDate = formatter.format(parser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CustomLogger.d("formatted Date ::" + convertedDate);
        return convertedDate;
    }

    public static long convertYYYYMMDDHHmmssZtoMilliSeconds(String dateStr) {
        long timeInterval = 0;
        Date startDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        String currentDateandTime = sdf.format(new Date());
        try {
            startDate = sdf.parse(currentDateandTime);
            Date endDate = sdf.parse(dateStr);
            timeInterval = (endDate.getTime()) - (startDate.getTime());
            CustomLogger.d("Time in milisec " + timeInterval);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeInterval;
    }

    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }


    public static float convertPixelsToDP(int px, Context context) {
        float dp = px / context.getResources().getDisplayMetrics().density;
        return px;
    }

    public static boolean isValidDealIntervalInHours(String startTime, String endTime, String startDate, String endDate) {
        boolean isValid = false;
        String startDateTime = null;
        String endDateTime = null;

        if (startDate != null && !startDate.equals("")) {
            if (startTime != null && !startTime.equals("")) {
                startDateTime = startDate + " " + startTime;
            } else {
                startDateTime = startDate + " " + "00:00:00";
            }

        } else {
            return false;
        }

        if (endDate != null && !endDate.equals("")) {

            if (endTime != null && !endTime.equals("")) {
                endDateTime = endDate + " " + endTime;
            } else {
                endDateTime = endDate + " " + "00:00:00";
            }
        } else {
            return false;
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;

        try {

            d1 = format.parse(startDateTime);
            d2 = format.parse(endDateTime);

            long diffHours = TimeUnit.MILLISECONDS.toHours(d2.getTime() - d1.getTime());
            CustomLogger.d("StartDate :: " + startDateTime + "  end Time::" + endDateTime + "Hours:" + diffHours);
            if (diffHours > 1) {
                isValid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public static String getLongDate(String date) {
        String convertedDate = null;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, LLLL dd, yyyy HH:mm aa", Locale.getDefault());
        try {
            convertedDate = formatter.format(parser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CustomLogger.d("formatted Date ::" + convertedDate);
        return convertedDate;
    }

    public static String getDateTimeYYYYMMDDHHMMSS(String date, String time) {
        String convertedDate = null;
        String dateTime = date + " " + time;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            convertedDate = formatter.format(formatter.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CustomLogger.d("formatted Date ::" + convertedDate);
        return convertedDate;
    }

    public static String getCurrentDateTimeYYYYMMDDHHMMSS() {
        String convertedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            convertedDate = format.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;

    }

    public static boolean isValidEndDealDate(String endDate, String endTime) {

        boolean isValid = false;
        String endDateTime = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDate = format.format(new Date());

        if (endDate != null && !endDate.equals("")) {

            if (endTime != null && !endTime.equals("")) {
                endDateTime = endDate + " " + endTime;
            } else {
                endDateTime = endDate + " " + "00:00:00";
            }
        } else {
            return false;
        }

        Date d1 = null;
        Date d2 = null;

        try {

            d1 = format.parse(todayDate);
            d2 = format.parse(endDateTime);

            long diffHours = TimeUnit.MILLISECONDS.toHours(d2.getTime() - d1.getTime());
            CustomLogger.d("todayDate :: " + todayDate + "  end Time::" + endDateTime + "Hours:" + diffHours);
            if (diffHours > 1) {
                isValid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateAfterNDays(int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, n); // Adding n days
        return sdf.format(c.getTime());
    }


    public static String getShopFormattedAddress(Address address) {
        StringBuffer adBuf = new StringBuffer();

        if (address.street != null)
            adBuf.append(address.street).append(", ");

        if (address.city != null)
            adBuf.append(address.city).append(", ");

        if (address.state != null)
            adBuf.append(address.state).append(", ");

        if (address.pincode != null)
            adBuf.append(address.pincode);

        return adBuf.toString();
    }


    // This is for sharing without image
    public static void shareData(final Context context, final String sharingSubject, final String shareUrl) {
        CustomLogger.d("shareUrl ::" + shareUrl);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        String shareText = sharingSubject + " " + shareUrl;
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sharingIntent.setType("text/*");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    // This is for sharing with image
    public static void shareData(final Context context, final String sharingSubject, final String shareUrl, String imageUrl) {
        CustomLogger.d("shareUrl ::" + shareUrl);

        imageUrl = getFormattedImageUrl(imageUrl);
        ImageLoader imageLoader = ImageManager.getInstance(context).getImageLoader();
        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Bitmap bmp = response.getBitmap();
                    Uri bmpUri;

                    // Store image to default external storage directory
                    try {
                        File file = convertBitmapToFile(context, bmp);
                        bmpUri = Uri.fromFile(file);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

                        String shareText = sharingSubject + " " + shareUrl;
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        sharingIntent.setType("image/*");
                        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onErrorResponse(Request request, VolleyError error) {
                CustomLogger.w("Image Load Error: " + error.getMessage());
            }
        });

    }

    public static void updateSet(Set<String> dataSet, String value) {
        if (dataSet != null) {
            if (dataSet.contains(value)) {
                dataSet.remove(value);
            } else {
                dataSet.add(value);
            }
        }
    }

    public static int getDealClaimedPercentage(List<Sku> skuList) {
        double totalSold = 0;
        double totalStock = 0;
        double percentageDouble = 0;
        for (int i = 0; i < skuList.size(); i++) {
            Sku sku = skuList.get(i);
            totalSold = totalSold + sku.sold;
            totalStock = totalStock + sku.stock;
        }

        if (totalStock == 0) {
            return 0;
        } else {
            percentageDouble = (totalSold * 100) / totalStock;
        }
        if (percentageDouble < 1) {
            percentageDouble = Math.ceil(percentageDouble);
        } else {
            percentageDouble = Math.floor(percentageDouble);
        }
        return (int) percentageDouble;
    }

    public static int getPercentage(double part, double total) {
        double percentage;
        percentage = (part * 100) / total;
        if (percentage < 1) {
            percentage = Math.ceil(percentage);
        } else {
            percentage = Math.floor(percentage);
        }
        return (int) percentage;
    }

    public static String getImageName() {

        String timeStamp = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy").format(new Date());
        return "img_" + timeStamp + ".jpg";

    }

    public static String financeDeviceDetail(Context context) {
        Map<String, String> deviceInfoMap = new HashMap<>();
        deviceInfoMap.put(FinancialApplicationFragment.DEVICE_IMEI_NUMBER, CommonUtility.getIEMINumber(context));
        deviceInfoMap.put(FinancialApplicationFragment.DEVICE_MODEL, Build.MODEL);
        deviceInfoMap.put(FinancialApplicationFragment.DEVICE_MANUFACTURER, Build.MANUFACTURER);
        deviceInfoMap.put(FinancialApplicationFragment.DEVICE_OS_VERSION, ANDROID_HEADER_PLATFORM);
        deviceInfoMap.put(FinancialApplicationFragment.DEVICE_MAC_ADDRESS, CommonUtility.getDeviceMacAddress(PartsEazyApplication.getInstance()));

        return new Gson().toJson(deviceInfoMap);
    }

    public static File getFileDirectory() {
        File directory = null;
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            directory = new File(Environment.getDataDirectory() + "/PartsEazy/");
            if (!directory.exists()) {
                directory.mkdir();
            }

        } else if (Environment.getExternalStorageState() != null) {
            directory = new File(Environment.getExternalStorageDirectory() + "/PartsEazy/");
            if (!directory.exists()) {
                directory.mkdir();
            }
        }

        return directory;
    }

    public static void exitApp(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static int getMinSKUDealPrice(List<Sku> skuList) {
        int minPrice = 10000;
        if (skuList != null) {
            minPrice = skuList.get(0).price;
            for (Sku sku : skuList) {
                if (minPrice > sku.price) {
                    minPrice = sku.price;
                }
            }
        }
        return minPrice;
    }

    public static List<PrepaidData> getPrepaidOptionList() {
        List<PrepaidData> dataList = new ArrayList<>();
        PrepaidData prepaidData;

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_card)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_CC_DC);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_CC_DC_KEY;
            dataList.add(prepaidData);
        }

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_nb)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_NB);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_NB_KEY;
            dataList.add(prepaidData);
        }

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.checkout_prepaid_upi)) {
            prepaidData = new PrepaidData();
            prepaidData.setOption(StaticMap.PAYMENY_PREPAID_UPI);
            prepaidData.methodId = StaticMap.PAYMENY_PREPAID_UPI_KEY;
            dataList.add(prepaidData);
        }

        return dataList;
    }


    public static void setUserL1L2L3CategoryList(Context context, UserShopResult userShopResult) {
        List<L1CategoryData> l1CategoryDataList = new ArrayList<>();
        List<L2CategoryData> l2CategoryDataList = new ArrayList<>();
        List<L3CategoryData> l3CategoryDataList = new ArrayList<>();

        for (int i = 0; i < userShopResult.result.size(); i++) {

            L1CategoryData l1CategoryData = new L1CategoryData();
            l1CategoryData.name = userShopResult.result.get(i).name;
            l1CategoryData.id = userShopResult.result.get(i).id;
            l1CategoryData.icon = userShopResult.result.get(i).icon.src;
            l1CategoryData.imageUrl = userShopResult.result.get(i).image.src;
//            l1CategoryData.icon = userShopResult.result.get(i).icon;
            l1CategoryData.l2CategoryDataList = new ArrayList<>();

            for (Child catChild : userShopResult.result.get(i).children) {

                L2CategoryData l2CategoryData = new L2CategoryData();
                l2CategoryData.id = catChild.id;
                l2CategoryData.name = catChild.name;
                if (catChild.icon != null) {
                    l2CategoryData.icon = catChild.icon.src;
                }
                if (catChild.image != null) {
                    l2CategoryData.imageUrl = catChild.image.src;
                }
                l2CategoryData.l3CategoryDataList = new ArrayList<>();

                for (Child_ l3CatChild : catChild.children) {
                    L3CategoryData l3CategoryData = new L3CategoryData();
                    l3CategoryData.id = l3CatChild.id;
                    l3CategoryData.name = l3CatChild.name;
                    if (l3CatChild.icon != null) {
                        l3CategoryData.icon = catChild.icon.src;
                    }
                    if (l3CatChild.image != null) {
                        l3CategoryData.imageUrl = l3CatChild.image.src;
                    }
                    l2CategoryData.l3CategoryDataList.add(l3CategoryData);
                    l3CategoryDataList.add(l3CategoryData);
                }
                l2CategoryDataList.add(l2CategoryData);
                l1CategoryData.l2CategoryDataList.add(l2CategoryData);
            }

            l1CategoryDataList.add(l1CategoryData);
        }

        String L1categoryListString = new Gson().toJson(l1CategoryDataList);
        DataStore.setL1CategoryList(context, L1categoryListString);

        String L2categoryListString = new Gson().toJson(l2CategoryDataList);
        DataStore.setL2CategoryList(context, L2categoryListString);

        String L3categoryListString = new Gson().toJson(l3CategoryDataList);
        DataStore.setL3CategoryList(context, L3categoryListString);

        CustomLogger.d("L1categoryListString ::" + L1categoryListString);
        CustomLogger.d("L2categoryListString ::" + L2categoryListString);
        CustomLogger.d("L3categoryListString ::" + L3categoryListString);
    }


    public static String getFormattedShopTime(int time) {
        String timeFormat = "am";
        String minStr;
        int hour = time / 100;
        int min = time % 100;
        int hourQ = hour / 12;

        hour = hour % 12;

        if (hourQ == 0 && hour == 0) {
            hour = 12;
        }
        if (hourQ > 0) {
            timeFormat = "pm";
            if (hourQ == 1 && hour == 0) {
                hour = 12;
                timeFormat = "am";
            } else if (hourQ == 2 && hour == 0) {
                hour = 12;
            }

        }

        if (min == 0)
            minStr = "00";
        else
            minStr = min + "";

        return hour + ":" + minStr + " " + timeFormat;

    }


    public static String convertListToCommaString(List<String> itemList) {
        return android.text.TextUtils.join(",", itemList);
    }

    public static String getShopOpeningClosingTime(ShopInfo shopInfo) {
        return getFormattedShopTime(shopInfo.openTime) + " - " + getFormattedShopTime(shopInfo.closeTime);
    }


    public static JsonArray getContactsList(Context context)
    {
        //  Find contact based on name.
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        JsonArray jsonElements = new JsonArray();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                JsonObject jsonObject = new JsonObject();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i("Names", name);
                String phoneNumber ="";
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    // Query phone here. Covered next
                    Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);

                    while (phones.moveToNext()) {
                        phoneNumber = phoneNumber +"," +phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("Number", phoneNumber);
                    }
                    phones.close();
                }
                jsonObject.addProperty(""+name,""+phoneNumber);
                jsonElements.add(jsonObject);
            }
        }
        cur.close();

        return jsonElements;
    }
}
