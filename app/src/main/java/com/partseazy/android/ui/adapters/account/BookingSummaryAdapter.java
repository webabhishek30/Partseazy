package com.partseazy.android.ui.adapters.account;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.dealbookings.BookingPrepaidAdapter;
import com.partseazy.android.ui.adapters.deals.booking.BookingSummarySkuAdapter;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.model.deal.bookingorder.summary.BookingPayment;
import com.partseazy.android.ui.model.deal.bookingorder.summary.BookingSummaryResult;
import com.partseazy.android.ui.model.prepaid.PrepaidData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 7/6/17.
 */

public class BookingSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    final public static String TYPE_PRODUCT_INFO = "type_product_info";
    final public static String TYPE_PAYMENT_SUMMARY = "type_payment_summary";
    final public static String TYPE_PAYMENT_OPTION = "type_payment_option";
    final public static String TYPE_ORDER_SHIPPING = "type_order_shipping";
    final public static String TYPE_ORDER_STATUS = "type_order_status";

    private Context context;
    private LayoutInflater mInflater;
    private int mTotalItems = 5;
    private BookingSummaryResult bookingSummaryResult;
    private ImageLoader imageLoader;
    private BookingSummarySkuAdapter skuAdapter;
    private BookingPrepaidAdapter bookingPrepaidAdapter;
    private String PREPAID_KEY = "PREPAID";
    private List<String> bookingList;

    public BookingSummaryAdapter(Context context, BookingSummaryResult bookingSummaryResult, List<String> bookingList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.bookingSummaryResult = bookingSummaryResult;
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        this.bookingList = bookingList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_ORDER_PRODUCT_INFO:
                return new DealItemVH(mInflater.inflate(R.layout.row_deal_product_item, parent, false));
            case VIEW_ORDER_PAYMENT_SUMMARY:
                return new PaymentSummaryVH(mInflater.inflate(R.layout.row_deal_order_summary, parent, false));
            case VIEW_BOOKING_PAYMENT_STATUS:
                return new PaymentStatusVH(mInflater.inflate(R.layout.row_booking_payment_status, parent, false));
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
            if (bookingSummaryResult.tradeBooking.shippingMethod != null && bookingSummaryResult.tradeBooking.shippingMethod.equals(DealConstants.DROP_SHIP)) {
                shippingAddressVH.addressTitleTV.setText(context.getString(R.string.delivery_address));
                shippingAddressVH.nameTV.setText(bookingSummaryResult.deliveryAddress.name);
                shippingAddressVH.addressTV.setText(bookingSummaryResult.deliveryAddress.street);
                shippingAddressVH.cityTV.setText(bookingSummaryResult.deliveryAddress.city + "," + bookingSummaryResult.deliveryAddress.state + "-" + bookingSummaryResult.deliveryAddress.pincode);
            } else {
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
            if (bookingSummaryResult.tradeBooking.amount.courierFee > 0) {
                dealItemVH.shippingTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(bookingSummaryResult.tradeBooking.amount.courierFee)));
                dealItemVH.shippingRL.setVisibility(View.VISIBLE);
            } else {
                dealItemVH.shippingRL.setVisibility(View.GONE);
            }
            dealItemVH.totalTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(bookingSummaryResult.tradeBooking.amount.payable)));
//
            String formatedURL = bookingSummaryResult.trade.images.get(0).src;
            formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, dealItemVH.productIV, CommonUtility.IMGTYPE.QUARTERIMG);
            CustomLogger.d("formattedUrl ::" + formatedURL);
            CommonUtility.setImageSRC(imageLoader, dealItemVH.productIV, formatedURL);

            dealItemVH.skuRV.setLayoutManager(new LinearLayoutManager(context));
            skuAdapter = new BookingSummarySkuAdapter(context, bookingSummaryResult.skus);
            dealItemVH.skuRV.setAdapter(skuAdapter);
            dealItemVH.skuRV.setNestedScrollingEnabled(false);
        }

        if (holder instanceof PaymentStatusVH) {
            int itemPosition = position;
            PaymentStatusVH paymentStatusVH = (PaymentStatusVH) holder;
            BookingPayment bookingPayment = bookingSummaryResult.bookingPayment;


            if (bookingPayment.ptin != null && bookingPayment.machineState != null && bookingPayment.machineState.equals("success")) {
                paymentStatusVH.paymentLL.setVisibility(View.GONE);
                paymentStatusVH.statusTV.setText(context.getString(R.string.payment_success));
                paymentStatusVH.paymentStatusMsgTV.setVisibility(View.VISIBLE);
                if (position == 0) {
                    paymentStatusVH.statusIV.setImageResource(R.drawable.check_green_circle);
                    paymentStatusVH.statusIV.setVisibility(View.VISIBLE);
                } else {
                    paymentStatusVH.statusIV.setVisibility(View.GONE);
                }

            } else {
                paymentStatusVH.paymentLL.setVisibility(View.VISIBLE);
                paymentStatusVH.statusTV.setVisibility(View.GONE);
                if (position == 0) {
                    paymentStatusVH.makePaymentTV.setVisibility(View.GONE);
                    paymentStatusVH.statusIV.setImageResource(R.drawable.cancel_icon);
                    paymentStatusVH.statusIV.setVisibility(View.VISIBLE);
                    paymentStatusVH.paymentStatusMsgTV.setVisibility(View.VISIBLE);
                    paymentStatusVH.paymentStatusMsgTV.setText(context.getString(R.string.payment_failed_msg));

                } else {
                    paymentStatusVH.statusIV.setVisibility(View.GONE);
                    paymentStatusVH.paymentStatusMsgTV.setVisibility(View.GONE);
                    paymentStatusVH.makePaymentTV.setVisibility(View.VISIBLE);
                }

                paymentStatusVH.statusTV.setText(context.getString(R.string.payment_failed));

                List<PrepaidData> dataList = CommonUtility.getPrepaidOptionList();
                dataList.get(0).setChooseOption(1);
                if (bookingPrepaidAdapter == null)
                    bookingPrepaidAdapter = new BookingPrepaidAdapter(context, dataList, PREPAID_KEY, true);
                paymentStatusVH.prepaidRV.setLayoutManager(new LinearLayoutManager(context));
                paymentStatusVH.prepaidRV.setAdapter(bookingPrepaidAdapter);

                paymentStatusVH.paymentBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_BOOKING_PAYMENT_EVENT);

                    }
                });

            }


        }

        if (holder instanceof PaymentSummaryVH) {
            PaymentSummaryVH paymentSummaryVH = (PaymentSummaryVH) holder;
            paymentSummaryVH.bookingIdTV.setText(context.getString(R.string.booking_id, bookingSummaryResult.tradeBooking.bkin));
            paymentSummaryVH.bookingDateTV.setText(context.getString(R.string.booking_on_date, CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(bookingSummaryResult.tradeBooking.createdAt)));
            paymentSummaryVH.userNameTV.setText(context.getString(R.string.buyer_name) + " " + bookingSummaryResult.deliveryAddress.name + ", " + bookingSummaryResult.deliveryAddress.city);

            if (bookingSummaryResult.tradeBooking.shippingMethod != null) {
                String bookingMethod = null;
                if (bookingSummaryResult.tradeBooking.shippingMethod.equals(DealConstants.DROP_SHIP)) {
                    bookingMethod = context.getString(R.string.drop_ship_msg);
                } else {
                    bookingMethod = context.getString(R.string.buyer_pickup_msg);
                }
                paymentSummaryVH.shippingMethodTV.setText(context.getString(R.string.shipping_method_str, bookingMethod));
            } else {
                paymentSummaryVH.shippingMethodTV.setText(context.getString(R.string.shipping_method_str, context.getString(R.string.buyer_pickup_msg)));

            }


            if (bookingSummaryResult.bookingPayment.ptin != null && bookingSummaryResult.bookingPayment.machineState != null && bookingSummaryResult.bookingPayment.machineState.equals("success")) {

                paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str,"Prepaid ( "+ bookingSummaryResult.bookingPayment.method+" )"));

            } else {
                if (bookingSummaryResult.tradeBooking.paymentMethod != null) {
                    if (bookingSummaryResult.tradeBooking.paymentMethod.equals(DealConstants.PAYMENT_DIRECT)) {
                        paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str, "Digital Settlement"));
                    } else {
                        paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str, "Cash "));
                    }
                } else {
                    paymentSummaryVH.paymentMethodTV.setText(context.getString(R.string.payment_method_str, context.getString(R.string.not_specified)));
                }
            }
        }

        if (holder instanceof OrderStatusVH) {
            OrderStatusVH orderStatusVH = (OrderStatusVH) holder;
            orderStatusVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return VIEW_ORDER_PAYMENT_SUMMARY.ordinal();
//        }
//        if (position == 1) {
//            return VIEW_ORDER_PRODUCT_INFO.ordinal();
//        }
//        if (position == 2) {
//            return VIEW_BOOKING_PAYMENT_STATUS.ordinal();
//        }
//        if (position == 3) {
//            return VIEW_ORDER_SHIPPING.ordinal();
//        }
//        if (position == 4) {
//            return VIEW_ORDER_STATUS.ordinal();
//        }
//
//        return 0;
//    }

    public int getItemViewType(int position) {
        String bookingType = bookingList.get(position);
        switch (bookingType) {

            case TYPE_PAYMENT_SUMMARY:
                return HolderType.VIEW_ORDER_PAYMENT_SUMMARY.ordinal();

            case TYPE_PRODUCT_INFO:
                return HolderType.VIEW_ORDER_PRODUCT_INFO.ordinal();

            case TYPE_ORDER_SHIPPING:
                return HolderType.VIEW_ORDER_SHIPPING.ordinal();

            case TYPE_PAYMENT_OPTION:
                return HolderType.VIEW_BOOKING_PAYMENT_STATUS.ordinal();

            case TYPE_ORDER_STATUS:
                return HolderType.VIEW_ORDER_STATUS.ordinal();

            default:
                return 0;
        }

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

        @BindView(R.id.cardView)
        protected CardView cardView;

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

    public class PaymentStatusVH extends BaseViewHolder {
        @BindView(R.id.paymentLL)
        protected LinearLayout paymentLL;

        @BindView(R.id.prepaidRV)
        protected RecyclerView prepaidRV;

        @BindView(R.id.paymentBT)
        protected Button paymentBT;

        @BindView(R.id.statusIV)
        protected ImageView statusIV;

        @BindView(R.id.statusTV)
        protected TextView statusTV;

        @BindView(R.id.paymentStatusMsgTV)
        protected TextView paymentStatusMsgTV;

        @BindView(R.id.makePaymentTV)
        protected TextView makePaymentTV;


        public PaymentStatusVH(View view) {
            super(view);
        }
    }
}
