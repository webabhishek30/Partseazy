package com.partseazy.android.ui.fragments.consumer_finance;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.fragments.product.WebViewFragment;
import com.partseazy.android.ui.model.consumer_finance.Token;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.DOWNLOAD_SERVICE;

public class EpayWebViewFragment extends WebViewFragment {
    private String DOWNLOAD_URL;

    public static EpayWebViewFragment newInstance(String title, String url) {
        EpayWebViewFragment fragment = new EpayWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOOLBAR_TITLE, title);
        bundle.putString(URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar.setVisibility(View.GONE);

        // TODO : Make a hidden webview which opens the login page and auto submits the form, then open the retailer page
//        getEpayToken();
        // Till then directly open the login page
        showError();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private void getEpayToken() {
        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.GET_EPAY_TOKEN.ordinal(),
                WebServiceConstants.GET_EPAY_TOKEN, null, null, this, this, false);
    }

    private void openRetailerPage(String token) {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webUrl = "https://merchant.epaylater.in/epaylater/merchant/check-token";
        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Authorization", "Bearer " + token);
        webView.loadUrl(webUrl, extraHeaders);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());
    }

    @Override
    public void showError() {
        webUrl = "https://merchant.epaylater.in/";
        webView.loadUrl(webUrl);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DOWNLOAD_URL = url;
                if (PermissionUtil.hasPermissions(context, PermissionUtil.STORAGE_PERMISSION)) {
                    downloadFile();
                } else {
                    PermissionUtil.requestStoragePermission((BaseActivity) getActivity());
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.GET_EPAY_TOKEN.ordinal()) {
            showError();
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.GET_EPAY_TOKEN.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), Token.class, new OnGsonParseCompleteListener<Token>() {
                @Override
                public void onParseComplete(Token data) {
                    hideProgressDialog();
                    if (getActivity() != null && isAdded()) {
                        if (data.token != null && !"".equals(data.token)) {
                            openRetailerPage(data.token);
                        } else {
                            showError();
                        }
                    }
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("Exception ", exception);
                }
            });
        }

        return true;
    }

    private void downloadFile() {
        try {
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(DOWNLOAD_URL));

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "EpayLater Report");
            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(context, "Downloading File", //To notify the Client that the file is being downloaded
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            CustomLogger.e("Exception", e);
        }
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);
        if (eventObject.id == EventConstant.STORAGE_PERMISSION) {
            boolean granted = (boolean) eventObject.objects[0];
            if (granted) {
                downloadFile();
            }
        }
    }
}
