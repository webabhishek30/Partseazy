package com.partseazy.android.ui.fragments.customer_management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.utility.CommonUtility;

import butterknife.BindView;

/**
 * Created by shubhang on 19/02/18.
 */

public class MarketingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.callUsBT)
    protected Button callUsBT;

    public static MarketingFragment newInstance() {
        Bundle bundle = new Bundle();
        MarketingFragment fragment = new MarketingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_marketing;
    }

    public static String getTagName() {
        return MarketingFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        callUsBT.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == callUsBT.getId()) {
            CommonUtility.dialPhoneNumber(getContext(), StaticMap.CC_PHONE);
        }
    }
}
