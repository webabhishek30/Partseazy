package com.partseazy.android.ui.adapters.deals.buy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.model.deal.CheckboxModel;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 19/8/17.
 */

public class DealFilterRightDailog extends DialogFragment implements DialogInterface.OnDismissListener {

    public static final String CATEGORY_LIST= "CATEGORY_LIST";
    public static final String IS_PUBLIC_DEAL= "IS_PUBLIC_DEAL";

    private LinearLayout emptyLayout;


    protected RelativeLayout dialogattributeRL;
    protected RecyclerView recylerView;
    protected DealRightDrawerAdapter dealRightDrawerAdapter;
    private List<CheckboxModel> categoryList = new ArrayList<>();
    private int isPublicDeal;



    public static DealFilterRightDailog newInstance(String categoryListJson,Integer isPublicDeal){

        DealFilterRightDailog dialogFragment = new DealFilterRightDailog();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_LIST, categoryListJson);
        bundle.putInt(IS_PUBLIC_DEAL, isPublicDeal);
        dialogFragment.setArguments(bundle);
        return dialogFragment;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.RightToLeftDialogAnimation;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PartsEazyEventBus.getInstance().addObserver(this);

        String catListJson = getArguments().getString(CATEGORY_LIST);
        Type listType = new TypeToken<List<CheckboxModel>>() {}.getType();
        categoryList = new Gson().fromJson(catListJson, listType);
        isPublicDeal = getArguments().getInt(IS_PUBLIC_DEAL, isPublicDeal);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_deal_filter, new LinearLayout(getActivity()), false);
        emptyLayout = (LinearLayout) view.findViewById(R.id.emptyLL);
        recylerView = (RecyclerView) view.findViewById(R.id.recylerView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyLayout.setBackgroundColor(0X70000000);
                emptyLayout.invalidate();
            }
        }, 0);

        Dialog builder = new Dialog(getActivity(), R.style.MyDialogTheme) {
            @Override
            public void onBackPressed() {
                dissmissDialog();
            }
        };
        builder.setContentView(view);
        onClickEmptySpace();

        setRightDrawerAdapter();
//        ProductAttributeAdapterFactory productAttributeAdapterFactory = new ProductAttributeAdapterFactory(this.getActivity(), pdpData, isCartLaunch, view);

        return builder;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void onClickEmptySpace() {
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDialog();

            }
        });
    }

    public void onEvent(EventObject eventObject) {

        if (eventObject.id == EventConstant.CLOSE_DEAL_FILTER) {
            dissmissDialog();
        }

        if (eventObject.id == EventConstant.APPLY_DEAL_FILTER) {
            dissmissDialog();
        }

        if (eventObject.id == EventConstant.RESET_DEAL_FILTER) {
            dissmissDialog();
        }
    }

    private void dissmissDialog() {

        emptyLayout.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        }, 200);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    private void setRightDrawerAdapter() {

        if (dealRightDrawerAdapter == null)
            dealRightDrawerAdapter = new DealRightDrawerAdapter(getActivity(), categoryList,isPublicDeal);
        recylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerView.setAdapter(dealRightDrawerAdapter);
        recylerView.setNestedScrollingEnabled(false);

    }



}
