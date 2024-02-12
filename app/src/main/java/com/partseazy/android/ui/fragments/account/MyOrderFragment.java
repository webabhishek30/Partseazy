package com.partseazy.android.ui.fragments.account;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.partseazy.android.BuildConfig;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.InputStreamVolleyRequest;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebParamsConstants;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.account.MyOrderAdapter;
import com.partseazy.android.ui.fragments.cart.CartHomeFragment;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.myorders.OrderData;
import com.partseazy.android.ui.model.myorders.OrdersResult;
import com.partseazy.android.ui.model.myorders.ReorderData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Naveen Kumar on 29/1/17.
 */

public class MyOrderFragment extends BaseFragment {

    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.orderDataView)
    protected FrameLayout orderDataView;

    private MyOrderAdapter myOrderAdapter;
    private List<OrderData> orderList;


    public static MyOrderFragment newInstance() {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (orderList != null && orderList.size() > 0) {
            setOrderAdapter();
        } else {
            loadOrderList();
        }
    }

    public static String getTagName() {
        return MyOrderFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_orders;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.my_orders);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void setOrderAdapter() {
        if (myOrderAdapter == null)
            myOrderAdapter = new MyOrderAdapter(getContext(), orderList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(myOrderAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void loadOrderList() {
        showProgressBar();
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);
        getNetworkManager().GetRequest(RequestIdentifier.RECENT_ORDERS_LIST_ID.ordinal(),
                WebServiceConstants.GET_ORDER_RECENT, null, params, this, this, false);
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        SnackbarFactory.showSnackbar(getContext(), getString(R.string.err_somthin_wrong));
        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {
        if (request.getIdentifier() == RequestIdentifier.RECENT_ORDERS_LIST_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), OrdersResult.class, new OnGsonParseCompleteListener<OrdersResult>() {
                        @Override
                        public void onParseComplete(OrdersResult data) {
                           //load session again

                            loadSession();
                            hideProgressBar();
                            CustomLogger.d("data size ::" + data.orderDataList.size());
                            if (data.orderDataList != null && data.orderDataList.size() > 0) {
                                orderList = new ArrayList<OrderData>();
                                orderList.addAll(data.orderDataList);
                                setOrderAdapter();
                                hideNoResult(orderDataView);
                            } else {
                                showNoResult(getString(R.string.no_order_found), R.drawable.no_order_icon, orderDataView);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }
        if (request.getIdentifier() == RequestIdentifier.REORDER_ID.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), ReorderData.class, new OnGsonParseCompleteListener<ReorderData>() {
                        @Override
                        public void onParseComplete(ReorderData data) {
                            hideProgressBar();
                            if (data.error != null && data.error.equals("0"))
                            {
                                redirectToCart();
                            }
                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }   if (request.getIdentifier() == RequestIdentifier.ORDER_INVOICE.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), ReorderData.class, new OnGsonParseCompleteListener<ReorderData>() {
                        @Override
                        public void onParseComplete(ReorderData data) {
                            hideProgressBar();

                        }
                        @Override
                        public void onParseFailure(Exception exception) {
                            hideProgressBar();
                            SnackbarFactory.showSnackbar(getContext(), getString(R.string.parsingErrorMessage));
                            CustomLogger.e("Exception ", exception);
                        }
                    }

            );

        }

        return true;
    }
    @Override
    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.REORDER) {
           Log.e("reorderNumber",""+eventObject.objects[0]);
            if (eventObject.objects[0] != null) {
                String orderId = String.valueOf(eventObject.objects[0]);
                    sendReorderOdinNumber(orderId);
            }

        }
        if (eventObject.id == EventConstant.ORDER_INVOICE) {
            if (eventObject.objects[0] != null) {
                String orderId = String.valueOf(eventObject.objects[0]);
                if (PermissionUtil.hasPermissions(context, PermissionUtil.STORAGE_PERMISSION)) {
                    saveOrderInvoice(orderId);
                } else {
                    PermissionUtil.requestStoragePermission((BaseActivity) getActivity());
                }
            }

        }

    }
    private void sendReorderOdinNumber(String orderNumber) {
        if (orderNumber != null) {
            showProgressBar();
            Map requestParamsMap = WebServicePostParams.getReorderData(orderNumber);
            getNetworkManager().PostRequest(RequestIdentifier.REORDER_ID.ordinal(),
                    WebServiceConstants.REORDER, requestParamsMap, null, this, this, false);
        } else {
            SnackbarFactory.showSnackbar((BaseActivity) getActivity(), getString(R.string.please_select_product));
        }
    }

    protected void downloadOrderInvoice(String orderID) {

        if (orderID != null) {
            showProgressBar();
            getNetworkManager().GetRequest(RequestIdentifier.ORDER_INVOICE.ordinal(),
                    WebServiceConstants.ORDER_INVOICE + orderID, null, null, this, this, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getNetworkManager().cancelAll();
    }

    private void redirectToCart()
    {
        SweetAlertDialog sweetAlertDialogView;
        sweetAlertDialogView = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
              sweetAlertDialogView  .setTitleText("Thank you")
                .setContentText("Your Re-ordered item has been successfully added to cart")
                .setConfirmText("Buy Now")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        addToBackStack(getContext(), CartHomeFragment.newInstance(false), CartHomeFragment.getTagName());
                        sDialog.cancel();
                    }
                })
                .showCancelButton(true)

                .setCancelText("Shop More")

                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

        TextView textView = (TextView) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.content_text);
        if (textView != null) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setSingleLine(false);
            textView.setMaxLines(5);
            textView.setLines(3);
        }
        Button viewGroup = (Button) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
        if (viewGroup != null) {
            viewGroup.setBackground(viewGroup.getResources().getDrawable(R.drawable.red_border_rectangle));
            viewGroup.setTextColor(viewGroup.getResources().getColor(R.color.colorPrimary));
        }
        Button cancelViewGroup = (Button) sweetAlertDialogView.findViewById(cn.pedant.SweetAlert.R.id.cancel_button);
        if (cancelViewGroup != null) {
            cancelViewGroup.setBackground(cancelViewGroup.getResources().getDrawable(R.drawable.cart_continue_button_selector));
        }

    }

    private void saveOrderInvoice(final String orderID)
    {
        showProgressBar();
        HashMap<String, String> headersMap ;
        headersMap = new HashMap<String, String>();
        headersMap.put(WebServiceConstants.X2_PLATFORM, WebServiceConstants.ANDROID_HEADER_PLATFORM);
        headersMap.put(WebServiceConstants.X2_APP_INFO, WebServiceConstants.ANDROID_HEADER_APPINFO_VERSION_NUMBER);
        headersMap.put(WebServiceConstants.X2_FINGERPRINT, WebServiceConstants.ANDROID_DEVICE_ID);

        headersMap.put("accept-encoding", "gzip");
        headersMap.put("Content-Type", "application/json");
        headersMap.put("charset", "utf-8");

        if (DataStore.getSessionID(context) != null) {
            headersMap.put(WebServiceConstants.X2_SESSIONID, DataStore.getSessionID(context));
        }

        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, BuildConfig.URL+WebServiceConstants.ORDER_INVOICE + orderID,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(Request<byte[]> request, byte[] responseObject, Response<byte[]> response) {
                        try {
                            if (response!=null) {

                                File file = new File(CommonUtility.getFileDirectory().getAbsolutePath()+"/"+orderID+"_Invoice.pdf");
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                                writeToFile(response.result,file.getAbsolutePath());
                                hideProgressBar();
                                addToBackStack((BaseActivity) getActivity(), PdfViewFragment.newInstance(file.getAbsolutePath()), PdfViewFragment.getTagName());

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }

                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(Request request, VolleyError error) {
                hideProgressBar();
                showError(getString(R.string.errorMessage),MESSAGETYPE.SNACK_BAR);
                error.printStackTrace();

            }
        }, null,headersMap);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
        mRequestQueue.add(request);

    }
    public void writeToFile(byte[] data, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

}
