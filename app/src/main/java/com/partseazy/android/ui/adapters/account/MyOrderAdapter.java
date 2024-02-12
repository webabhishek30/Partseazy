package com.partseazy.android.ui.adapters.account;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.account.OrderSummaryFragment;
import com.partseazy.android.ui.model.myorders.OrderData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 29/1/17.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<OrderData> orderList;
    private LayoutInflater mInflater;
    private SubOrderAdapter subOrderAdapter;


    public MyOrderAdapter(Context context, List<OrderData> orderList) {
        this.orderList = orderList;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(mInflater.inflate(R.layout.row_my_order_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;

        orderViewHolder.orderNumberTV.setText(PartsEazyApplication.getInstance().getString(R.string.order, orderList.get(position).odin));
        orderViewHolder.orderedDateTV.setText(context.getString(R.string.placed_on_date, CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(orderList.get(position).createdAt)));


        orderViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        subOrderAdapter = new SubOrderAdapter(context, orderList.get(position).orderLine);
        orderViewHolder.recyclerView.setAdapter(subOrderAdapter);
        if (orderList.get(position).orderLine.size() > 0)
        {
            String formattedStateString = orderList.get(position).orderLine.get(0).machineState.replace("_", " ");
            if (!formattedStateString.equalsIgnoreCase("New") && !formattedStateString.equalsIgnoreCase("Confirmed"))
                orderViewHolder.txtOrderInvoice.setVisibility(View.VISIBLE);
            else
                orderViewHolder.txtOrderInvoice.setVisibility(View.GONE);


        }
        orderViewHolder.detailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseFragment orderSummaryFragment = OrderSummaryFragment.newInstance(orderList.get(position).odin);
                BaseFragment.addToBackStack((BaseActivity) context, orderSummaryFragment, OrderSummaryFragment.getTagName());

            }
        });
        orderViewHolder.reorderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.REORDER,orderList.get(position).odin);
            }
        });
        orderViewHolder.txtOrderInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.ORDER_INVOICE,orderList.get(position).odin);
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends BaseViewHolder {

        @BindView(R.id.orderNumberTV)
        protected TextView orderNumberTV;
        @BindView(R.id.orderedDateTV)
        protected  TextView orderedDateTV;
        @BindView(R.id.detailTV)
        protected TextView detailTV;
        @BindView(R.id.recyclerView)
        protected RecyclerView recyclerView;
        @BindView(R.id.reorderTV)
        protected TextView reorderTV;
        @BindView(R.id.txtOrderInvoice)
        protected TextView txtOrderInvoice;

        public OrderViewHolder(View view) {
            super(view);

        }
    }
}