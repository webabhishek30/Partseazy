package com.partseazy.android.ui.adapters.customer_management;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.customer_management.Customer;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by shubhang on 15/02/18.
 */

public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Customer> customers;
    private BaseFragment baseFragment;


    public CustomerAdapter(BaseFragment baseFragment, List<Customer> customers) {
        this.customers = customers;
        mInflater = LayoutInflater.from(baseFragment.getContext());
        this.baseFragment = baseFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomerAdapter.CustomerViewHolder(mInflater.inflate(R.layout.row_customer_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomerAdapter.CustomerViewHolder userViewHolder = (CustomerAdapter.CustomerViewHolder) holder;
        final Customer customer = customers.get(position);

        userViewHolder.mobileTV.setText(customer.mobile);

        if (customer.name != null && !"".equals(customer.name)) {
            userViewHolder.nameTV.setText(customer.name);
        }

        if (customer.email != null && !"".equals(customer.email)) {
            userViewHolder.emailTV.setText(customer.email);
        }

        if (customer.dob != null && !"".equals(customer.dob)) {
            userViewHolder.dobTV.setText(CommonUtility.getddmmyyyyDateFromDateCreated(customer.dob));
        }

        if (customer.anniversary != null && !"".equals(customer.anniversary)) {
            userViewHolder.anniversaryTV.setText(CommonUtility.getddmmyyyyDateFromDateCreated(customer.anniversary));
        }

        if (customer.groups != null) {
            userViewHolder.groupsTV.setText(String.valueOf(customer.groups));
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    class CustomerViewHolder extends BaseViewHolder {

        @BindView(R.id.mobileTV)
        protected TextView mobileTV;

        @BindView(R.id.nameTV)
        protected TextView nameTV;

        @BindView(R.id.emailTV)
        protected TextView emailTV;

        @BindView(R.id.dobTV)
        protected TextView dobTV;

        @BindView(R.id.anniversaryTV)
        protected TextView anniversaryTV;

        @BindView(R.id.groupsTV)
        protected TextView groupsTV;

        public CustomerViewHolder(View view) {
            super(view);
        }
    }
}
