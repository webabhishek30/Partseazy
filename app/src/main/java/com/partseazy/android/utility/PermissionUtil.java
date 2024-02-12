package com.partseazy.android.utility;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.partseazy.android.base.BaseActivity;

/**
 * Created by Pumpkin Guy on 19/12/16.
 */

public class PermissionUtil {

    public static final int REQUEST_PERMISSION = 99;
    public static final int REQUEST_CAMERA_PERMISSION = 100;
    public static final int REQUEST_CONTACTS_PERMISSION = 101;
    public static final int REQUEST_LOCATION_PERMISSION = 102;
    public static final int REQUEST_STORAGE_PERMISSION = 103;
    public static String[] PERMISSIONS = { Manifest.permission.READ_CONTACTS,Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE};
    public static String[] CAMERA_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String[] STORAGE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String[] CONTACTS_PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};
    public static String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static void requestPermission(BaseActivity activity) {

        if (!hasPermissions(activity.getBaseContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_PERMISSION);
        }

    }

    public static void requestCameraPermission(BaseActivity activity) {

        if (!hasPermissions(activity.getBaseContext(), CAMERA_PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, CAMERA_PERMISSIONS, REQUEST_CAMERA_PERMISSION);
        }

    }

    public static void requestStoragePermission(BaseActivity activity) {

        if (!hasPermissions(activity.getBaseContext(), STORAGE_PERMISSION)) {
            ActivityCompat.requestPermissions(activity, STORAGE_PERMISSION, REQUEST_STORAGE_PERMISSION);
        }

    }

    public static void requestContactPermission(BaseActivity activity) {

        if (!hasPermissions(activity.getBaseContext(), CONTACTS_PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, CONTACTS_PERMISSIONS, REQUEST_CONTACTS_PERMISSION);
        }

    }

    public static void requestLocationPermission(BaseActivity activity) {
        if (!hasPermissions(activity.getBaseContext(), LOCATION_PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSION);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}


