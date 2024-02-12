package com.partseazy.android.ui.fragments.product;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.product.ProductDetailAdapter;
import com.partseazy.android.ui.model.productdialog.ProductDetailItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 3/1/17.
 */

public class ProductDetailCardFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.scrollable)
    protected RecyclerView mRecyclerView;

    private ProductDetailAdapter productDetailAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ProductDetailItem> detailsItemList = new ArrayList<>();
    ;
    public static final String ITEM_LIST = "ITEM_LIST";


    public static ProductDetailCardFragment newInstance(List<ProductDetailItem> detailsItem) {
        ProductDetailCardFragment fragment = new ProductDetailCardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEM_LIST, (ArrayList<? extends Parcelable>) detailsItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsItemList = getArguments().getParcelableArrayList(ITEM_LIST);
        CustomLogger.d("detail List size :" + detailsItemList.size());

    }


    public static String getTagName() {
        return ProductDetailCardFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_detail_card;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.product_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCrossNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (detailsItemList != null && detailsItemList.size() > 0) {
            productDetailAdapter = new ProductDetailAdapter(getContext(), detailsItemList,true);
            mRecyclerView.setAdapter(productDetailAdapter);
        }
        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem sendItem = menu.findItem(R.id.action_send);
        MenuItem favItem = menu.findItem(R.id.action_favourite);
        cartItem.setVisible(false);
        searchItem.setVisible(false);
        sendItem.setVisible(false);
        favItem.setVisible(false);
    }

}
