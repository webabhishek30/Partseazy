package com.partseazy.android.ui.adapters.deals.sell;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.deals.booking.BookingSummarySkuAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.bookingorder.summary.BookingSummaryResult;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import butterknife.BindView;

/**
 * Created by naveen on 16/5/17.
 */

public class DealOrderDetailAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private LayoutInflater mInflater;
    private int mTotalItems = 4;
    private BookingSummaryResult bookingSummaryResult;
    private ImageLoader imageLoader;
    private BookingSummarySkuAdapter skuAdapter;

    public DealOrderDetailAdapter(Context context,BookingSummaryResult bookingSummaryResult) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.bookingSummaryResult = bookingSummaryResult;
        imageLoader = ImageManager.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_ORDER_PRODUCT_INFO:
                return new DealItemVH(mInflater.inflate(R.layout.row_deal_product_item, parent, false));
            case VIEW_ORDER_PAYMENT_SUMMARY:
                return new PaymentSummaryVH(mInflater.inflate(R.layout.row_deal_order_summary, parent, false));
            case VIEW_ORDER_SHIPPING:
                return new ShippingAddressVH(mInflater.inflate(R.layout.row_deal_order_address_card, parent, false));
            case VIEW_ORDER_STATUS:
                return new OrderStatusVH(mInflater.inflate(R.layout.row_deal_order_status_card, parent, false));





        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ShippingAddressVH) {
            ShippingAddressVH shippingAddressVH = (ShippingAddressVH) holder;

            if(bookingSummaryResult.tradeBooking.shippingMethod !=null && bookingSummaryResult.tradeBooking.shippingMethod.equals(DealConstants.DROP_SHIP)) {
                shippingAddressVH.addressTitleTV.setText(context.getString(R.string.delivery_address));
                shippingAddressVH.nameTV.setText(bookingSummaryResult.deliveryAddress.name);
                shippingAddressVH.addressTV.setText(bookingSummaryResult.deliveryAddress.street);
                shippingAddressVH.cityTV.setText(bookingSummaryResult.deliveryAddress.city + "," + bookingSummaryResult.deliveryAddress.state + "-" + bookingSummaryResult.deliveryAddress.pincode);
            }else{
                shippingAddressVH.addressTitleTV.setText(context.getString(R.string.pickup_address));
                shippingAddressVH.nameTV.setText(context.getString(R.string.seller_warehouse_location));
                shippingAddressVH.addressTV.setText(bookingSummaryResult.pickupAddress.street);
                shippingAddressVH.cityTV.setText(bookingSummaryResult.pickupAddress.city + "," + bookingSummaryResult.pickupAddress.state + "-" + bookingSummaryResult.pickupAddress.pincode);

            }
        }

        if (holder instanceof DealItemVH) {
            DealItemVH dealItemVH = (DealItemVH) holder;


            dealItemVH.dealNameTV.setText(bookingSummaryResult.trade.name);
            dealItemVH.sellerTV.setVisibility(View.GONE);
            //dealItemVH.sellerTV.setText(context.getString(R.string.sold_by,bookingSummaryResult.trade.name));
//
            if(bookingSummaryResult.tradeBooking.amount.courierFee>0) {
                dealItemVH.shippingTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(bookingSummaryResult.tradeBooking.amount.courierFee)));
                dealItemVH.shippingRL.setVisibility(View.VISIBLE);
            }else {
                dealItemVH.shippingRL.setVisibility(View.GONE);
            }
            dealItemVH.totalTV.setText(context.getString(R.string.rs,CommonUtility.convertionPaiseToRupee(bookingSummaryResult.tradeBooking.amount.payable)));
//
            String formatedURL = bookingSummaryResult.trade.images.get(0).src;

            formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, dealItemVH.productIV, CommonUtility.IMGTYPE.QUARTERIMG);
            CustomLogger.d("formattedUrl ::" + formatedURL);
            CommonUtility.setImageSRC(imageLoader, dealItemVH.productIV, formatedURL);

            dealItemVH.skuRV.setLayoutManager(new LinearLayoutManager(context));
            skuAdapter = new BookingSummarySkuAdapter(context,bookingSummaryResult.skus);
            dealItemVH.skuRV.setAdapter(skuAdapter);
            dealItemVH.skuRV.setNestedScrollingEnabled(false);
        }

        if(holder instanceof PaymentSummaryVH)
        {
            PaymentSummaryVH paymentSummaryVH = (PaymentSummaryVH)holder;
            paymentSummaryVH.bookingIdTV.setText(context.getString(R.string.booking_id,bookingSummaryResult.tradeBooking.bkin));
            paymentSummaryVH.bookingDateTV.setText(context.getString(R.string.booking_on_date, CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(bookingSummaryResult.tradeBooking.createdAt)));
            paymentSummaryVH.userNameTV.setText(context.getString(R.string.buyer_name)+" "+bookingSummaryResult.deliveryAddress.name+", "+bookingSummaryResult.deliveryAddress.city);
            paymentSummaryVH.shippingMethodTV.setText(context.getString(R.string.shipping_method_str,bookingSummaryResult.tradeBooking.shippingMethod));


            if(bookingSummaryResult.tradeBooking.shippingMethod !=null)
            {
                String bookingMethod =null;
                if(bookingSummaryResult.tradeBooking.shippingMethod.equals(DealConstants.DROP_SHIP))
                {
                    bookingMethod = context.getString(R.string.drop_ship_msg);
                }else{
                    bookingMethod = context.getString(R.string.buyer_pickup_msg);
                }
                paymentSummaryVH.shippingMethodTV.setText(context.getString(R.string.shipping_method_str,bookingMethod));
            }else{
                paymentSummaryVH.shippingMethodTV.setText(context.getString(R.string.shipping_method_str,context.getString(R.string.buyer_pickup_msg)));
            }

            if(bookingSummaryResult.tradeBooking.paymentMethod!=null) {
                if(bookingSummaryResult.tradeBooking.paymentMethod.equals(DealConstants.PAYMENT_DIRECT)) {
                    paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str,"Digital Settlement"));
                }else{
                    paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str,"Cash " ));
                }
            }else{
                paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str, context.getString(R.string.not_specified)));
            }

        }

        if(holder instanceof OrderStatusVH)
        {
            OrderStatusVH orderStatusVH = (OrderStatusVH) holder;
            orderStatusVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }


    }

    @Override
    public int getItemCount() {
        return mTotalItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HolderType.VIEW_ORDER_PRODUCT_INFO.ordinal();
        }
        if (position == 1) {
            return HolderType.VIEW_ORDER_PAYMENT_SUMMARY.ordinal();
        }
        if (position == 2) {
            return HolderType.VIEW_ORDER_SHIPPING.ordinal();
        }
        if(position==3)
        {
            return HolderType.VIEW_ORDER_STATUS.ordinal();
        }
        if(position==4)
        {
            return HolderType.VIEW_ORDER_STATUS.ordinal();
        }

        return 0;
    }


    public class DealItemVH extends BaseViewHolder {



        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;

        @BindView(R.id.productIV)
        protected NetworkImageView productIV;

        @BindView(R.id.sellerTV)
        protected TextView sellerTV;

        @BindView(R.id.shippingTV)
        protected TextView shippingTV;

        @BindView(R.id.totalTV)
        protected TextView totalTV;

        @BindView(R.id.shippingRL)
        protected RelativeLayout shippingRL;

        @BindView(R.id.skuRV)
        protected RecyclerView skuRV;




        public DealItemVH(View itemView) {
            super(itemView);
        }
    }

    public class PaymentSummaryVH extends BaseViewHolder {

        @BindView(R.id.bookingIdTV)
        protected TextView bookingIdTV;

        @BindView(R.id.bookingDateTV)
        protected TextView bookingDateTV;

        @BindView(R.id.userNameTV)
        protected TextView userNameTV;

        @BindView(R.id.shippingMethodTV)
        protected TextView shippingMethodTV;

        @BindView(R.id.paymentMethodTV)
        protected TextView paymentMethodTV;

        public PaymentSummaryVH(View itemView) {
            super(itemView);
        }
    }

    public class ShippingAddressVH extends BaseViewHolder {


        @BindView(R.id.nameTV)
        protected TextView nameTV;

        @BindView(R.id.addressTV)
        protected TextView addressTV;

        @BindView(R.id.cityTV)
        protected TextView cityTV;

        @BindView(R.id.addressTitleTV)
        protected TextView addressTitleTV;


        public ShippingAddressVH(View itemView) {
            super(itemView);
        }
    }

    public class OrderStatusVH extends BaseViewHolder {
        public OrderStatusVH(View view) {
            super(view);
        }
    }

}
