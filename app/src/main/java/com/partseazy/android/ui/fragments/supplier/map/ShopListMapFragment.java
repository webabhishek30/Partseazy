package com.partseazy.android.ui.fragments.supplier.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.suppliers.map.ShopSliderMapAdapter;
import com.partseazy.android.ui.fragments.supplier.shop.ShopsDetailBaseFragment;
import com.partseazy.android.ui.model.supplier.shop.Shop;
import com.partseazy.android.ui.model.supplier.shop.ShopResultData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 7/9/17.
 */

public class ShopListMapFragment extends ShopsDetailBaseFragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {


    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    private static final String SHOP_RESULT = "shop_result";

    private MapView mMapView;
    private GoogleMap mMap;
    private List<Marker> markerList;

    private ShopResultData shopResultData;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private ShopSliderMapAdapter shopSliderMapAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shop_list_map;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String shopDataStr = getArguments().getString(SHOP_RESULT);
        shopResultData = new Gson().fromJson(shopDataStr, ShopResultData.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (shopResultData != null) {
            setMapAndAdapter(shopResultData);
        }
    }


    @Override
    protected String getFragmentTitle() {
        return ShopListMapFragment.class.getSimpleName();
    }


    public static ShopListMapFragment newInstance(String shopResult) {
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_RESULT, shopResult);
        ShopListMapFragment fragment = new ShopListMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            mMapView = (MapView) view.findViewById(R.id.map_dashBoard);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        mMapView.setNestedScrollingEnabled(false);
        setHorinotalImageAdapter();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeMap(mMap);
                } else {
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                initializeMap(mMap);
            }
        } else {
            initializeMap(mMap);
        }

        setMapAndAdapter(shopResultData);
    }

    private void initializeMap(GoogleMap mMap) {
        if (mMap != null) {
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }


    @Override
    protected boolean handleBrowseShopsDataResponse(ShopResultData data) {
        hideProgressBar();
        setMapAndAdapter(data);
        return true;
    }


    private void setMapAndAdapter(ShopResultData data) {
        //retailerData = data;
        if (data.shops != null) {

            shopSliderMapAdapter.updateAdapter(data.meta.page, data);


            for (Shop shop : data.shops) {

                if (shop.address.location != null) {
                    drawMarker(new LatLng(shop.address.location.lat, shop.address.location.lon), shop.shopInfo.name);
                }
            }

            if (data.shops.get(0).address != null) {
                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                            LatLng(data.shops.get(0).address.location.lat,
                            data.shops.get(0).address.location.lon), 10));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }


    private void drawMarker(LatLng point, String name) {

        if (markerList == null)
            markerList = new ArrayList<>();
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.title(name);
        markerOptions.position(point);
        markerOptions.anchor(0.0f, 1.0f);

        // Adding marker on the Google Map
        if (mMap != null) {
            Marker marker = mMap.addMarker(markerOptions);
//        marker.showInfoWindow();
            markerList.add(marker);
        }


    }


    @Override
    public void onCameraMove() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

    }


    private void setHorinotalImageAdapter() {

        mRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new LinearSnapHelper();
        mRecyclerView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(mRecyclerView);


        if (shopSliderMapAdapter == null) {
            List<String> list = new ArrayList<>();
            list.add("B2c2dummydata");
            shopSliderMapAdapter = new ShopSliderMapAdapter(context);
        }

        mRecyclerView.setAdapter(shopSliderMapAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                CustomLogger.d("positionof tooltip" + position);

                try {
                    if (shopResultData.shops.get(position).address != null) {
//                        Toast.makeText(context, "The visible position is" + layoutManager.findFirstCompletelyVisibleItemPosition(), Toast.LENGTH_SHORT).show();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                                LatLng(shopResultData.shops.get(position).address.location.lat,
                                shopResultData.shops.get(position).address.location.lon), 13));
                        markerList.get(position).showInfoWindow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
