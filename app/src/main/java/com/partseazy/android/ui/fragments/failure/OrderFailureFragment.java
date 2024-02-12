package com.partseazy.android.ui.fragments.failure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by gaurav on 13/02/17.
 */

public class OrderFailureFragment extends BaseFragment {


    private static final String TRANSACTION_CANCELLED_BY_USER = "_TRANSACTION_CANCELLED_BY_USER";

    @BindView(R.id.order_failed_title)
    protected TextView orderFailedTitleTV;
    @BindView(R.id.order_failed_sub_title)
    protected TextView orderFailedSubTitleTV;

    public static OrderFailureFragment newInstance(boolean cancelledByuser) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TRANSACTION_CANCELLED_BY_USER, cancelledByuser);

        OrderFailureFragment fragment = new OrderFailureFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_failure;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        if (getArguments().getBoolean(TRANSACTION_CANCELLED_BY_USER)) {
            orderFailedSubTitleTV.setVisibility(View.GONE);
            orderFailedTitleTV.setText(getString(R.string.transaction_cancelled));
        } else {
            orderFailedTitleTV.setText(getString(R.string.order_failed_text));
            orderFailedSubTitleTV.setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.btn_view_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToMall(getActivity());
            }
        });
    }


    @Override
    protected String getFragmentTitle() {
        return getString(R.string.orderFailedTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        hideToolbarMenu();
        hideFavMenu();
    }


    @Override
    public boolean onBackPressed() {
        popToMall(getActivity());
        return false;
    }

}