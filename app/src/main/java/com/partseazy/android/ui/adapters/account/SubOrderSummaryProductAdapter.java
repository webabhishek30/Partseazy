package com.partseazy.android.ui.adapters.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.product.ProductDetailFragment;
import com.partseazy.android.ui.model.myorders.ordersummary.OrderLine;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 30/1/17.
 */

public class SubOrderSummaryProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<OrderLine> orderLineList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;


    public SubOrderSummaryProductAdapter(Context context, List<OrderLine> orderLineList) {
        this.orderLineList = orderLineList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubOrderViewHolder(mInflater.inflate(R.layout.row_order_product_summary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SubOrderViewHolder subOrderViewHolder = (SubOrderViewHolder) holder;
        final OrderLine orderLineData = orderLineList.get(position);


        String formatedURL = CommonUtility.getFormattedImageUrl(orderLineData.productInfo.image, subOrderViewHolder.productIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
        CommonUtility.setImageSRC(imageLoader, subOrderViewHolder.productIV, formatedURL);

        subOrderViewHolder.productNameTV.setText(orderLineData.productInfo.name + " ( " + orderLineData.productInfo.tag + " )");
        subOrderViewHolder.quantityTV.setText(context.getString(R.string.cart_qty, orderLineData.quantity));
        subOrderViewHolder.totalAmountTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(orderLineData.payable)));

        String formattedStateString = orderLineData.machineState.replace("_", " ");
        if (formattedStateString.trim().equalsIgnoreCase("New")) {
            subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.gray));
            subOrderViewHolder.deliveryTV.setText("Pending");
        }
        else {
            if (formattedStateString.trim().equalsIgnoreCase("Accepted")) {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.dot_light_screen3));
            } else if (formattedStateString.trim().equalsIgnoreCase("confirmed")) {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.green_success));
            } else if (formattedStateString.trim().equalsIgnoreCase("cancel")) {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else if (formattedStateString.trim().equalsIgnoreCase("delivered")) {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.blue));
            } else if (formattedStateString.trim().equalsIgnoreCase("return")) {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.orange));
            } else {
                subOrderViewHolder.deliveryTV.setText(formattedStateString);
                subOrderViewHolder.deliveryTV.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }

        }

        if(orderLineData.isSample>0)
        {
            subOrderViewHolder.sampleItemTV.setVisibility(View.VISIBLE);
        }else {
            subOrderViewHolder.sampleItemTV.setVisibility(View.GONE);
        }

        subOrderViewHolder.productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseFragment proFragment = ProductDetailFragment.newInstance(orderLineData.productMasterId, orderLineData.productInfo.name);
                BaseFragment.addToBackStack((BaseActivity) context, proFragment, ProductDetailFragment.getTagName());
            }
        });


    }


    @Override
    public int getItemCount() {
        return orderLineList.size();
    }

    class SubOrderViewHolder extends BaseViewHolder {

        @BindView(R.id.productIV)
        protected NetworkImageView productIV;
        @BindView(R.id.orderLL)
        protected LinearLayout orderLL;
        @BindView(R.id.productNameTV)
        protected TextView productNameTV;
        @BindView(R.id.deliveryTV)
        protected TextView deliveryTV;
        @BindView(R.id.quantityTV)
        protected TextView quantityTV;
        @BindView(R.id.totalAmountTV)
        protected TextView totalAmountTV;
        @BindView(R.id.sampleItemTV)
        protected TextView sampleItemTV;


        public SubOrderViewHolder(View view) {
            super(view);

        }
    }

}
