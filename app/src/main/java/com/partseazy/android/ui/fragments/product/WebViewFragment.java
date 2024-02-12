package com.partseazy.android.ui.fragments.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by naveen on 4/1/17.
 */

public class WebViewFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.webView)
    protected WebView webView;

    @BindView(R.id.b2c2_progressbar)
    protected ProgressBar progressBar;

    public static final String URL = "url";
    public static final String TOOLBAR_TITLE = "toolbar_title";

    protected String webUrl;
    protected String title;

    // this is common webView fragment so to show the title we have to pass the title
    public static WebViewFragment newInstance(String title, String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOOLBAR_TITLE, title);
        bundle.putString(URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TOOLBAR_TITLE);
        webUrl = getArguments().getString(URL);

    }


    public static String getTagName() {
        return WebViewFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected String getFragmentTitle() {
        return title;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCrossNavigation();
        webView.loadUrl(webUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        showProgressDialog();
        webView.setWebViewClient(new WebClient());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }

    public class WebClient extends WebViewClient {
        public WebClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressDialog();
        }
    }

}