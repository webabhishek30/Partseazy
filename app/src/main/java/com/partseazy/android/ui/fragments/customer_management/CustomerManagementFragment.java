package com.partseazy.android.ui.fragments.customer_management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.fragments.product.WebViewFragment;
import com.partseazy.android.ui.model.customer_management.SupplierDetailsResult;
import com.partseazy.android.ui.model.deal.deal_detail.Address;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shubhang on 08/02/18.
 */

public class CustomerManagementFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.profileIV)
    protected CircleImageView profileIV;

    @BindView(R.id.shopRL)
    protected RelativeLayout shopRL;

    @BindView(R.id.shareShopIB)
    protected ImageButton shareShopIB;

    @BindView(R.id.shopNameTV)
    protected TextView shopNameTV;

    @BindView(R.id.addressTV)
    protected TextView addressTV;

    @BindView(R.id.googlePinTV)
    protected TextView googlePinTV;

    @BindView(R.id.editGooglePinBT)
    protected Button editGooglePinBT;

    @BindView(R.id.customersTV)
    protected TextView customersTV;

    @BindView(R.id.customersListBT)
    protected Button customersListBT;

    @BindView(R.id.productsListBT)
    protected Button productsListBT;

    @BindView(R.id.productsTV)
    protected TextView productsTV;

    @BindView(R.id.uploadProductCV)
    protected CardView uploadProductCV;

    @BindView(R.id.uploadCustomerCV)
    protected CardView uploadCustomerCV;

    @BindView(R.id.blastSmsCV)
    protected CardView blastSmsCV;

    @BindView(R.id.viewShopCV)
    protected CardView viewShopCV;

    @BindView(R.id.customerRL)
    protected RelativeLayout customerRL;

    @BindView(R.id.productRL)
    protected RelativeLayout productRL;

    private Address addr = null;
    private int suppId;
    private int userId;
    private String shopImage;
    private ImageLoader imageLoader;

    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

    final String GOOGLE_MAPS_URL_BASE = "http://maps.google.com/maps/search/?api=1";
    final String SHARING_URL = "http://shopdada.in/supplier/";

    public static CustomerManagementFragment newInstance() {
        Bundle bundle = new Bundle();
        CustomerManagementFragment fragment = new CustomerManagementFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_customer_management;
    }

    public static String getTagName() {
        return CustomerManagementFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.customer_management);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageManager.getInstance(getContext()).getImageLoader();
        loadSupplierDetails();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        googlePinTV.setOnClickListener(this);
        editGooglePinBT.setOnClickListener(this);
        uploadCustomerCV.setOnClickListener(this);
        uploadProductCV.setOnClickListener(this);
        blastSmsCV.setOnClickListener(this);
        viewShopCV.setOnClickListener(this);

        customerRL.setOnClickListener(this);
        productRL.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == googlePinTV.getId()) {
            if (googlePinTV.getText().toString().contains("View")) {
                showProgressDialog();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_MAPS_URL_BASE + "&query=" +
                                (String.valueOf(mLastLocation.getLatitude()) + "," +
                                        String.valueOf(mLastLocation.getLongitude()))));
                startActivity(intent);
            } else {
                checkLocationPermission();
            }
        }
        if (v.getId() == editGooglePinBT.getId()) {
            checkLocationPermission();
        }
        if (v.getId() == uploadCustomerCV.getId()) {
            RegisterCustomerFragment registerCustomerFragment = RegisterCustomerFragment.newInstance();
            BaseFragment.addToBackStack(getContext(), registerCustomerFragment, registerCustomerFragment.getTag());
        }
        if (v.getId() == uploadProductCV.getId()) {
            UploadProductFragment uploadProductFragment = UploadProductFragment.newInstance(suppId);
            BaseFragment.addToBackStack(getContext(), uploadProductFragment, uploadProductFragment.getTag());
        }
        if (v.getId() == customerRL.getId()) {
            CustomerListFragment customerListFragment = CustomerListFragment.newInstance();
            BaseFragment.addToBackStack(getContext(), customerListFragment, customerListFragment.getTag());
        }
        if (v.getId() == productRL.getId()) {
            ProductListFragment productListFragment = ProductListFragment.newInstance();
            BaseFragment.addToBackStack(getContext(), productListFragment, productListFragment.getTag());
        }
        if (v.getId() == blastSmsCV.getId()) {
            BlastSmsFragment blastSmsFragment = BlastSmsFragment.newInstance();
            BaseFragment.addToBackStack(getContext(), blastSmsFragment, blastSmsFragment.getTag());
        }
        if (v.getId() == viewShopCV.getId()) {
            BaseFragment fragment = WebViewFragment.newInstance("My Shop", SHARING_URL + userId + "?source=app");
            BaseFragment.addToBackStack(getContext(), fragment, fragment.getTag());
        }
        if (v.getId() == shareShopIB.getId()) {
            Context context = getContext();
            if (PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                CommonUtility.shareData(context, "Check out my shop on PartsEazy : " + shopNameTV.getText().toString(),
                        SHARING_URL + userId, shopImage);
            } else {
                CommonUtility.shareData(context, "Check out my shop on PartsEazy : " + shopNameTV.getText().toString(),
                        SHARING_URL + userId);
            }
        }
    }

    private void checkLocationPermission() {
        if (!PermissionUtil.hasPermissions(getContext(), PermissionUtil.LOCATION_PERMISSIONS)) {
            requestPermissions(PermissionUtil.LOCATION_PERMISSIONS, PermissionUtil.REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }
    }

    private void buildAlertMessageNoGps() {
        DialogUtil.showAlertDialog(getActivity(), true, "Turn on GPS",
                "Your GPS seems to be disabled, do you want to enable it?", "Yes", "no", null,
                new DialogListener() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
    }

    private void buildAlertMessageRationale() {
        DialogUtil.showAlertDialog(getActivity(), true, "Location Permissions",
                "We need location permissions to get your shop location." +
                        "This location will be used by your customers to reach you",
                "Open Settings", "no", null,
                new DialogListener() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        try {
            showProgressDialog();
            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                mFusedLocationClient.getLastLocation()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    mLastLocation = task.getResult();
                                    addr.longitude = mLastLocation.getLongitude();
                                    addr.latitude = mLastLocation.getLatitude();
                                    updateAddress();
                                    setGooglePin();
                                } else {
                                    CustomLogger.e("getLastLocation:exception", task.getException());
                                    showError();
                                }
                            }
                        });
            }
        } catch (Exception exception) {
            CustomLogger.e("Exception ", exception);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        buildAlertMessageRationale();
                    } else {
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                        showError("Permission is required", MESSAGETYPE.TOAST);
                    }
                }
            }
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.GET_SUPPLIER_DETAILS.ordinal()) {
            if (apiError != null) {
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            } else {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }

        if (request.getIdentifier() == RequestIdentifier.UPDATE_ADDRESS.ordinal()) {
            showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.GET_SUPPLIER_DETAILS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), SupplierDetailsResult.class, new OnGsonParseCompleteListener<SupplierDetailsResult>() {
                        @Override
                        public void onParseComplete(SupplierDetailsResult data) {
                            if (data != null) {
                                populateData(data);
                                hideProgressDialog();
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );

        }

        if (request.getIdentifier() == RequestIdentifier.UPDATE_ADDRESS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), Address.class, new OnGsonParseCompleteListener<Address>() {
                        @Override
                        public void onParseComplete(Address data) {
                            if (data != null) {
                                hideProgressDialog();
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }
        return true;
    }

    private void loadSupplierDetails() {
        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.GET_SUPPLIER_DETAILS.ordinal(),
                WebServiceConstants.GET_SUPPLIER_DETAILS, null, null, this, this, false);
    }

    private void setGooglePin() {
        googlePinTV.setText(R.string.view_google_pin);
        editGooglePinBT.setVisibility(View.VISIBLE);
    }

    private void updateAddress() {
        if (addr != null) {
            Map<String, Object> paramMap = WebServicePostParams.updateAddress(addr);
            getNetworkManager().PutRequest(RequestIdentifier.UPDATE_ADDRESS.ordinal(),
                    WebServiceConstants.PUT_ADDRESS + String.valueOf(addr.id), paramMap, null, this, this, false);
        }
    }

    private void populateData(SupplierDetailsResult data) {
        try {
            String address = "";
            suppId = data.supplierId;
            userId = data.userId;
            if (data.address != null) {
                addr = data.address;
                if (data.address.street != null && !"".equals(data.address.street)) {
                    address += data.address.street;
                }
                if (data.address.landmark != null && !"".equals(data.address.landmark)) {
                    address += ", " + data.address.landmark;
                }
                if (data.address.city != null && !"".equals(data.address.city)) {
                    address += ", " + data.address.city;
                }
                if (data.address.state != null && !"".equals(data.address.state)) {
                    address += ", " + data.address.state;
                }
                if (data.address.latitude != null && data.address.longitude != null) {
                    mLastLocation = new Location("self");
                    mLastLocation.setLatitude((Double) data.address.latitude);
                    mLastLocation.setLongitude((Double) data.address.longitude);
                    setGooglePin();
                }
            }
            if (!"".equals(address)) {
                addressTV.setText(address);
                addressTV.setVisibility(View.VISIBLE);
                googlePinTV.setVisibility(View.VISIBLE);
            }

            if (data.shopName != null && !"".equals(data.shopName)) {
                shopNameTV.setText(data.shopName);
                shopRL.setVisibility(View.VISIBLE);
                shareShopIB.setOnClickListener(this);
            }
            if (data.shopImage != null && !"".equals(data.shopImage)) {
                String formattedURL = CommonUtility.getFormattedImageUrl(data.shopImage, profileIV, CommonUtility.IMGTYPE.FULLIMG);
                CommonUtility.setImageSRC(imageLoader, profileIV, formattedURL);
                shopImage = formattedURL;
            }

            if (data.customers == 0) {
                customersListBT.setVisibility(View.GONE);
                customerRL.setOnClickListener(null);
            } else {
                customersListBT.setVisibility(View.VISIBLE);
                customerRL.setOnClickListener(this);
            }

            if (data.products == 0) {
                productsListBT.setVisibility(View.GONE);
                productRL.setOnClickListener(null);
            } else {
                productsListBT.setVisibility(View.VISIBLE);
                productRL.setOnClickListener(this);
            }

            customersTV.setText(String.valueOf(data.customers));
            productsTV.setText(String.valueOf(data.products));
        } catch (Exception exception) {
            CustomLogger.e("Exception ", exception);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgressDialog();
        loadSupplierDetails();
    }
}
