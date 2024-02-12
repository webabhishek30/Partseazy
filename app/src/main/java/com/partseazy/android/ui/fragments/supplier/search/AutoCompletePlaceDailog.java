package com.partseazy.android.ui.fragments.supplier.search;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.ui.adapters.suppliers.search.AutoCompletePlaceAdapter;
import com.partseazy.android.ui.adapters.suppliers.search.OnPlaceClickListener;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class AutoCompletePlaceDailog extends DialogFragment implements View.OnClickListener {


    private LinearLayout autoSuggestLL;
    private RecyclerView recyclerView;
    private TextView emptyTV, recentSearchTV;
    private EditText searchET;
    private ImageView backBtnIV, crossIV;
    public AutoCompletePlaceAdapter autoCompletePlaceAdapter;
    private OnPlaceClickListener onPlaceClickListener;
    private List<Prediction> predictionList;
    private PlaceAPI placeAPI;
    private Context context;

    public static AutoCompletePlaceDailog newInstance(OnPlaceClickListener onPlaceClickListener) {

        AutoCompletePlaceDailog dialogFragment = new AutoCompletePlaceDailog();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        dialogFragment.setOnPlaceClickListener(onPlaceClickListener);
        return dialogFragment;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PartsEazyEventBus.getInstance().addObserver(this);
        predictionList = new ArrayList<>();
        placeAPI = new PlaceAPI();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_autocomplete_place, new LinearLayout(getActivity()), false);
        Dialog dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
        //  Window window = dialog.getWindow();
        // setWindowMargin(window);
        initiliseAllViews(view, dialog);
        dialog.setContentView(view);
        return dialog;
    }


    private void initiliseAllViews(View view, Dialog dialog) {
        searchET = (EditText) view.findViewById(R.id.searchET);
        emptyTV = (TextView) view.findViewById(R.id.emptyTV);
        recentSearchTV = (TextView) view.findViewById(R.id.recentSearchTV);
        backBtnIV = (ImageView) view.findViewById(R.id.backBtnIV);
        crossIV = (ImageView) view.findViewById(R.id.crossIV);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        autoSuggestLL = (LinearLayout) view.findViewById(R.id.autoSuggestLL);
        recentSearchTV.setVisibility(View.VISIBLE);
        backBtnIV.setOnClickListener(this);
        searchET.addTextChangedListener(new MyTextWatcher(searchET));
        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchET.setText("");
                crossIV.setVisibility(View.GONE);
            }
        });


        predictionList = DataStore.getRecentLocalitySearchList(context);
        if (predictionList != null && predictionList.size() > 0) {
            recentSearchTV.setVisibility(View.VISIBLE);
        } else {
            recentSearchTV.setVisibility(View.GONE);
        }
        setAdapter(getActivity(), dialog);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtnIV:
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
                break;
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private EditText view;

        public MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            crossIV.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.searchET:
                    recentSearchTV.setVisibility(View.GONE);
                    if (charSequence.length() != 0) {
                        String input = searchET.getText().toString().trim();
                        new AutoSuggestionAsynTask().execute(input);
                    } else {
                        crossIV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchET:
                    break;

            }
        }
    }

    public void setAdapter(Context context, Dialog dialog) {
        if (autoCompletePlaceAdapter == null)
            autoCompletePlaceAdapter = new AutoCompletePlaceAdapter(context, predictionList, dialog);
        autoCompletePlaceAdapter.setOnPlaceClickListener(onPlaceClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(autoCompletePlaceAdapter);

    }

    public void setOnPlaceClickListener(OnPlaceClickListener onPlaceClickListener) {
        this.onPlaceClickListener = onPlaceClickListener;
    }

    class AutoSuggestionAsynTask extends AsyncTask<String, String, List<Prediction>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Prediction> doInBackground(String... input) {
            String placeText = input[0];
            return placeAPI.autoSuggestionPlaces(placeText);

        }

        @Override
        protected void onPostExecute(List<Prediction> itemList) {
            predictionList.clear();
            if (itemList != null && itemList.size() > 0) {
                predictionList.addAll(itemList);
                autoCompletePlaceAdapter.notifyDataSetChanged();
                emptyTV.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                emptyTV.setVisibility(View.VISIBLE);
            }
        }

    }
}
