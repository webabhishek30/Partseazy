package com.partseazy.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageUtility;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.adapters.search.SearchAutoSuggestAdapter;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;

import java.util.List;

/**
 * Created by Naveen Kumar on 7/2/17.
 */

public class SearchCommon {
    private EditText edtToolSearch;
    private TextView txtEmpty, goTV;
    private ImageView crossIV, backIV;
    private Dialog toolbarSearchDialog;
    private RecyclerView recyclerView;
    private LinearLayout autoSuggestLL;
    private SearchAutoSuggestAdapter searchAdapter;
    protected Context context;


    public SearchCommon(Context context) {
        this.context = context;
    }


    public void setSearchBox() {
        initViews();
        handleAllCLicksEvents();
    }

    private void initViews() {
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.view_toolbar_search, null);
        edtToolSearch = (EditText) view.findViewById(R.id.searchET);
        txtEmpty = (TextView) view.findViewById(R.id.txt_empty);
        backIV = (ImageView) view.findViewById(R.id.backBtnIV);
        goTV = (TextView) view.findViewById(R.id.goTV);
        crossIV = (ImageView) view.findViewById(R.id.crossIV);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        autoSuggestLL = (LinearLayout) view.findViewById(R.id.autoSuggestLL);


        toolbarSearchDialog = new Dialog(context, R.style.MyDialogTheme);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(true);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();
        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        List<String> autoSuggestList = DataStore.getRecentAutoSuggestSearch(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        searchAdapter = new SearchAutoSuggestAdapter(context, autoSuggestList, toolbarSearchDialog);
        recyclerView.setAdapter(searchAdapter);

        if (autoSuggestList != null && autoSuggestList.size() > 0) {
            edtToolSearch.setText(autoSuggestList.get(0));
            edtToolSearch.setSelection(edtToolSearch.getText().length());
            crossIV.setVisibility(View.VISIBLE);
        }
    }

    public void handleAllCLicksEvents() {
        autoSuggestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtToolSearch.setText("");
                crossIV.setVisibility(View.GONE);
            }
        });


        goTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edtToolSearch.getText().toString();
                MoengageUtility.trackEvent(query,context);

                if (query != null && !query.equals("")) {
                    DataStore.addItemInAutoSuggestList(context, query);
                    BaseFragment.addToBackStack((BaseActivity) context, CatalogueFragment.newInstance(query, true,false), CatalogueFragment.getTagName());
                    toolbarSearchDialog.dismiss();


                }

            }
        });


        edtToolSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                goTV.setVisibility(View.VISIBLE);
                crossIV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    txtEmpty.setVisibility(View.GONE);
                    crossIV.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                CustomLogger.d(" search afterTextChanged");

            }
        });


        edtToolSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.KEYCODE_SEARCH) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String query = edtToolSearch.getText().toString();
                    if (query != null && !query.equals("")) {
                        DataStore.addItemInAutoSuggestList(context, query);
                        BaseFragment.addToBackStack((BaseActivity) context, CatalogueFragment.newInstance(query,true,false), CatalogueFragment.getTagName());
                        toolbarSearchDialog.dismiss();
                    }
                    return true;

                }
                return false;
            }
        });


    }

}
