package com.partseazy.android.ui.fragments.catalogue;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;


/**
 * Created by naveen on 7/1/17.
 */

public class CatalogueSortDailog extends DialogFragment {
    public static final String IS_BY_SEARCH = "DAILOG_TYPE";
    public static final String SORT_CODE = "SORT_CODE";
    protected String sortCode;
    private boolean isBySearch;

    public static CatalogueSortDailog newInstance(boolean isBySearch,String sortCode) {

        CatalogueSortDailog dialogFragment = new CatalogueSortDailog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_BY_SEARCH, isBySearch);
        bundle.putString(SORT_CODE, sortCode);
        dialogFragment.setArguments(bundle);
        return dialogFragment;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PartsEazyEventBus.getInstance().addObserver(this);
        isBySearch = getArguments().getBoolean(IS_BY_SEARCH);
        sortCode =getArguments().getString(SORT_CODE);
        CustomLogger.d("sortCode ::"+sortCode);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_catalogue_sort, new LinearLayout(getActivity()), false);
        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme);
        Window window = builder.getWindow();
        setWindowMargin(window);
        builder.setContentView(view);

        CatalogueFilterAdapterFactory catalogueFilterAdapterFactory = new CatalogueFilterAdapterFactory(getActivity(), isBySearch,sortCode, view);
        return builder;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    void setWindowMargin(Window window) {
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.y = (int) PartsEazyApplication.getInstance().getResources().getDimension(R.dimen.defaultCatalogFilterHeight); // bottom margin
        window.setAttributes(layoutParams);
    }

    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.SELECT_SORT_FILTER_ID) {
            if(getDialog()!=null) {
                getDialog().dismiss();
            }

        }

    }
}