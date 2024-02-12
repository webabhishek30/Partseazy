package com.partseazy.android.utility.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.partseazy.android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by gaurav on 24/12/16.
 */

public class DialogUtil {
    /**
     * Show Material design style Alert dialog
     *
     * @param act         - activity reference
     * @param title       - title of dialog or {@literal null}. title will be hidden if {@literal null}
     * @param message     - message to show
     * @param positiveBtn - positive button text or {@literal null} - positive button [right side button] will be hidden if {@literal null}
     * @param negativeBtn -negative button text or {@literal null} - negative button [left side button] will be hidden if {@literal null}
     * @param neutralBtn -negative button text or {@literal null} - neutral button [middle button] will be hidden if {@literal null}
     * @param listener    -positive,negative, neutral button callbacks or {@literal null} - callback will not pass to caller if {@literal null}
     */
    public static void showAlertDialog(Activity act,
                                       boolean cancellable, @Nullable String title,
                                       String message,
                                       @Nullable String positiveBtn,
                                       @Nullable String negativeBtn,
                                       @Nullable String neutralBtn,
                                       @Nullable final DialogListener listener) {

//        MyMaterialDialog d = new MyMaterialDialog(act, cancellable, title, message, positiveBtn, negativeBtn, listener);
//        d.show();

        AlertDialog.Builder builder =
                new AlertDialog.Builder(act, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancellable);
        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onPositiveButton(dialog);
                dialog.dismiss();
            }
        });//second parameter used for onclicklistener
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onNegativeButton(dialog);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(neutralBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onNeutralButton(dialog);
                dialog.dismiss();
            }
        });
        AlertDialog d = builder.show();

    }

    public static void showMapDailog(Activity activity,final double lat,final double lng,final String markerTitle) {

        View view = activity.getLayoutInflater().inflate(R.layout.dailog_map_view, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setView(view);
        final AlertDialog alertDialog = dialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        MapView mMapView = (MapView) view.findViewById(R.id.mapView);
        MapsInitializer.initialize(activity);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng posisiabsen = new LatLng(lat, lng); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title(markerTitle));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        });


    }

}
