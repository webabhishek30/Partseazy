package com.partseazy.android.extraContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.fragments.catalogue.CatalogueFragment;


public class ViewPager1Fragment extends BaseFragment {
    private EditText mEtSearchHome;
    private Button mBtnSearchHome;


    @Override
    protected int getLayoutResId() {
        return R.layout.view_pager_shop_by_id;
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mEtSearchHome = view.findViewById(R.id.et_search_home);
        mBtnSearchHome = view.findViewById(R.id.btn_search_home);
        mBtnSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mBtnSearchHome.getText().toString().trim();
                if (searchQuery != null) {
                    addToBackStack((BaseActivity) getActivity(), CatalogueFragment.newInstance(searchQuery, true,false), CatalogueFragment.getTagName());
                }
            }
        });

        return view;
    }


}
