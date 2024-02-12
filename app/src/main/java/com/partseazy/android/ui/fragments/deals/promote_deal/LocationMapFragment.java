package com.partseazy.android.ui.fragments.deals.promote_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.utility.KeyPadUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;

/**
 * Created by naveen on 31/8/17.
 */

public class LocationMapFragment extends BaseFragment implements OnMapReadyCallback {

    public static final String SCREEN_TTTLE = "screen_title";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE ="longitude";


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected MapView mapView;
    protected GoogleMap mGoogleMap;


    private String mTitle;
    private double mLatitude,mLongitude;

    public static LocationMapFragment newInstance(String title,double latitude,double longitude) {
        Bundle bundle = new Bundle();
        bundle.putString(SCREEN_TTTLE, title);
        bundle.putDouble(LATITUDE, latitude);
        bundle.putDouble(LONGITUDE, longitude);
        LocationMapFragment fragment = new LocationMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(SCREEN_TTTLE);
        mLatitude = getArguments().getDouble(LATITUDE);
        mLongitude = getArguments().getDouble(LONGITUDE);
    }



    public static String getTagName() {
        return LocationMapFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_map_location;
    }

    @Override
    protected String getFragmentTitle() {
        return mTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showCrossNavigation();
        toolbar.setTitle(mTitle);

        mapView = (MapView)view.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return view;

    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude,mLongitude)).title(mTitle)).showInfoWindow();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude,mLongitude), 14));

    }
}
