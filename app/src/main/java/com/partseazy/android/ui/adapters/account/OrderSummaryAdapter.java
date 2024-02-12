package com.partseazy.android.ui.adapters.account;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.partseazy.android.R;
import com.partseazy.android.analytics.MoengageConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.myorders.ordersummary.Amount;
import com.partseazy.android.ui.model.myorders.ordersummary.OrderSummaryResult;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 29/1/17.
 */

public class OrderSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private CartCheckoutBaseData mCartData;
    private OrderSummaryResult orderSummaryDetail;
    private int mCardType = 5;
    private SubOrderSummaryProductAdapter subOrderSummaryProductAdapter;

    public OrderSummaryAdapter(Context context, OrderSummaryResult orderSummaryDetail) {

        this.context = context;
        this.orderSummaryDetail = orderSummaryDetail;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType holderType = HolderType.values()[viewType];
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (holderType) {

            case VIEW_ORDER_SUMMARY_ORDER_CARD:
                return new OrderDetailViewHolder(layoutInflater.inflate(R.layout.row_order_summary_detail_item, parent, false));

            case VIEW_ORDER_SUMMARY_SHIPPING_CARD:
                return new OrderShippingViewHolder(layoutInflater.inflate(R.layout.row_order_summary_shipping_item, parent, false));

            case VIEW_ORDER_SUMMARY_BILLING_CARD:
                return new OrderBillingViewHolder(layoutInflater.inflate(R.layout.row_order_summary_billing_item, parent, false));

            case VIEW_ORDER_PAYMENT_SUMMARY_CARD:
                return new OrderPaymentSummaryViewHolder(layoutInflater.inflate(R.layout.row_order_summary_my_order, parent, false));

            case VIEW_ORDER_PRODUCT_SUMMARY_CARD:
                return new OrderProductSummaryViewHolder(layoutInflater.inflate(R.layout.row_order_smry_product_card_item, parent, false));

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OrderDetailViewHolder) {

            OrderDetailViewHolder orderDetailViewHolder = (OrderDetailViewHolder) holder;
            orderDetailViewHolder.orderNumberTV.setText(context.getString(R.string.order, orderSummaryDetail.odin));
            orderDetailViewHolder.orderedDateTV.setText(context.getString(R.string.placed_on_date, CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(orderSummaryDetail.createdAt)));
        }

        if (holder instanceof OrderShippingViewHolder) {
            OrderShippingViewHolder orderShippingViewHolder = (OrderShippingViewHolder) holder;
            if (orderSummaryDetail.deliveryAddress.name != null) {
                orderShippingViewHolder.userNameTV.setText(orderSummaryDetail.deliveryAddress.name);
            }
            if (orderSummaryDetail.deliveryAddress.mobile != null) {
                orderShippingViewHolder.mobileNumberTV.setText(context.getString(R.string.country_code) + orderSummaryDetail.deliveryAddress.mobile);
            }
            orderShippingViewHolder.addressTV.setText(orderSummaryDetail.deliveryAddress.street + ", " + orderSummaryDetail.deliveryAddress.city + " - " + orderSummaryDetail.deliveryAddress.pincode);

            if (orderSummaryDetail.deliveryAddress.landmark != null) {
                orderShippingViewHolder.landmarkTV.setVisibility(View.VISIBLE);
                orderShippingViewHolder.landmarkTV.setText(context.getString(R.string.shipping_landmark, orderSummaryDetail.deliveryAddress.landmark));
            } else
                orderShippingViewHolder.landmarkTV.setVisibility(View.GONE);
        }

        if (holder instanceof OrderBillingViewHolder) {
            OrderBillingViewHolder orderBillingViewHolder = (OrderBillingViewHolder) holder;

            if (orderSummaryDetail.billingAddress != null) {
                if (orderSummaryDetail.billingAddress.billingName != null) {
                    orderBillingViewHolder.userNameTV.setText(orderSummaryDetail.billingAddress.billingName);
                }
                if (orderSummaryDetail.billingAddress.mobile != null) {
                    orderBillingViewHolder.mobileNumberTV.setText(context.getString(R.string.country_code) + orderSummaryDetail.billingAddress.mobile);
                }

                orderBillingViewHolder.addressTV.setText(orderSummaryDetail.billingAddress.street + ", " + orderSummaryDetail.billingAddress.city + " - " + orderSummaryDetail.billingAddress.pincode);

                if (orderSummaryDetail.billingAddress.landmark != null) {
                    orderBillingViewHolder.landmarkTV.setVisibility(View.VISIBLE);
                    orderBillingViewHolder.landmarkTV.setText(context.getString(R.string.shipping_landmark, orderSummaryDetail.billingAddress.landmark));
                } else {
                    orderBillingViewHolder.landmarkTV.setVisibility(View.GONE);
                }

                if (orderSummaryDetail.billingAddress.gstn != null) {
                    orderBillingViewHolder.gstnTV.setText(context.getString(R.string.gstn_caps_str, orderSummaryDetail.billingAddress.gstn.toUpperCase()));
                    orderBillingViewHolder.gstnTV.setVisibility(View.VISIBLE);
                } else {
                    orderBillingViewHolder.gstnTV.setVisibility(View.GONE);
                }


            } else {
                orderBillingViewHolder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
            }
        }

        if (holder instanceof OrderPaymentSummaryViewHolder) {
            OrderPaymentSummaryViewHolder orderPaymentSummaryViewHolder = (OrderPaymentSummaryViewHolder) holder;
            Amount amount = orderSummaryDetail.amount;

            orderPaymentSummaryViewHolder.shipping_charges_summaryTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.courierFee)));
            orderPaymentSummaryViewHolder.subtotal_order_summaryTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.subTotal)));
            // priceeff replace with subtotal
            // orderPaymentSummaryViewHolder.subtotal_order_summaryTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.priceEff)));
            orderPaymentSummaryViewHolder.total_paymentTV.setVisibility(View.VISIBLE);
            orderPaymentSummaryViewHolder.total_amountTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.grandTotal)));

            int taxSum = 0;

            if (amount.taxCst == 0) {
                taxSum = amount.taxGST + amount.taxCess + amount.taxVat;
            } else {
                taxSum = amount.taxGST + amount.taxCess + amount.taxCst;
            }

            if (taxSum > 0) {
                orderPaymentSummaryViewHolder.taxTV.setText(context.getString(R.string.tax));
                orderPaymentSummaryViewHolder.taxValueTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(taxSum)));
            }

            if (amount.taxRefund != 0) {
                orderPaymentSummaryViewHolder.cstRebateRL.setVisibility(View.VISIBLE);
                orderPaymentSummaryViewHolder.cst_refundAmntTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.taxRefund)));
            }


            int discountPrice = amount.cartRebate + amount.couponRebate;
            if (discountPrice != 0) {
                orderPaymentSummaryViewHolder.discount_textTV.setVisibility(View.VISIBLE);
                orderPaymentSummaryViewHolder.discount_smryTV.setVisibility(View.VISIBLE);
                orderPaymentSummaryViewHolder.discount_smryTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(discountPrice)));
            } else {
                orderPaymentSummaryViewHolder.discount_textTV.setVisibility(View.GONE);
                orderPaymentSummaryViewHolder.discount_smryTV.setVisibility(View.GONE);
            }

            if (amount.codFee == 0) {
                orderPaymentSummaryViewHolder.codRL.setVisibility(View.GONE);
            } else {
                orderPaymentSummaryViewHolder.codRL.setVisibility(View.VISIBLE);
                orderPaymentSummaryViewHolder.codChargeAmtTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.codFee)));
            }

            if (amount.creditFee == 0) {
                orderPaymentSummaryViewHolder.creditRL.setVisibility(View.GONE);
            } else {
                orderPaymentSummaryViewHolder.creditRL.setVisibility(View.VISIBLE);
                orderPaymentSummaryViewHolder.creditChargeAmtTV.setText(context.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(amount.creditFee)));
            }


            //        Moengage order summary /Checkout started logs .
            PayloadBuilder builder = new PayloadBuilder();

            builder//.putAttrInt(MoengageConstant.Events.AFFILIATION, amount)
                    .putAttrString(MoengageConstant.Events.VALUE, CommonUtility.convertionPaiseToRupeeString(orderSummaryDetail.amount.grandTotal))
                    //.putAttrString(MoengageConstant.Events.REVENUE, )
                    .putAttrString(MoengageConstant.Events.SHIPPING, CommonUtility.convertionPaiseToRupeeString(orderSummaryDetail.amount.courierFee))
                    .putAttrInt(MoengageConstant.Events.TAX,taxSum )
                    .putAttrString(MoengageConstant.Events.DISCOUNT, CommonUtility.convertionPaiseToRupeeString(discountPrice))
                    .putAttrInt(MoengageConstant.Events.COUPON, orderSummaryDetail.amount.couponRebate)
                    .putAttrString(MoengageConstant.Events.CURRENCY, "Rs. ")
                    //.putAttrString(MoengageConstant.Events.PRODUCTS, finalCartAttributes.pincode)
                    //.putAttrString(MoengageConstant.Events.TOTAL_QUANTITY, finalCartAttributes.ShippingMethod)
                    .putAttrString(MoengageConstant.Events.AMOUNT, CommonUtility.convertionPaiseToRupeeString(orderSummaryDetail.amount.grandTotal));

            MoEHelper.getInstance(context).trackEvent(MoengageConstant.Events.CHECKOUT_STARTED, builder);
        }

        if (holder instanceof OrderProductSummaryViewHolder) {
            OrderProductSummaryViewHolder orderProductSummaryViewHolder = (OrderProductSummaryViewHolder) holder;
            orderProductSummaryViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            subOrderSummaryProductAdapter = new SubOrderSummaryProductAdapter(context, orderSummaryDetail.orderLine);
            orderProductSummaryViewHolder.recyclerView.setAdapter(subOrderSummaryProductAdapter);


        }


;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_ORDER_SUMMARY_ORDER_CARD.ordinal();

        if (position == 1) {
            return HolderType.VIEW_ORDER_SUMMARY_SHIPPING_CARD.ordinal();
        }
        if (position == 2) {
            return HolderType.VIEW_ORDER_SUMMARY_BILLING_CARD.ordinal();
        }
        if (position == 3) {
            return HolderType.VIEW_ORDER_PAYMENT_SUMMARY_CARD.ordinal();
        }
        if (position == 4) {
            return HolderType.VIEW_ORDER_PRODUCT_SUMMARY_CARD.ordinal();
        }

        return HolderType.VIEW_ORDER_SUMMARY_ORDER_CARD.ordinal();
    }

    @Override
    public int getItemCount() {
        return mCardType;
    }

    class OrderDetailViewHolder extends BaseViewHolder {

        @BindView(R.id.orderNumberTV)
        protected TextView orderNumberTV;
        @BindView(R.id.orderedDateTV)
        protected TextView orderedDateTV;

        public OrderDetailViewHolder(View view) {
            super(view);
        }
    }


    class OrderShippingViewHolder extends BaseViewHolder {

        @BindView(R.id.addressTV)
        protected TextView addressTV;
        @BindView(R.id.landmarkTV)
        protected TextView landmarkTV;
        @BindView(R.id.mobileNumberTV)
        protected TextView mobileNumberTV;
        @BindView(R.id.userNameTV)
        protected TextView userNameTV;


        OrderShippingViewHolder(View itemView) {
            super(itemView);
        }

    }

    class OrderBillingViewHolder extends BaseViewHolder {

        @BindView(R.id.addressTV)
        protected TextView addressTV;
        @BindView(R.id.landmarkTV)
        protected TextView landmarkTV;
        @BindView(R.id.mobileNumberTV)
        protected TextView mobileNumberTV;
        @BindView(R.id.userNameTV)
        protected TextView userNameTV;

        @BindView(R.id.gstnTV)
        protected TextView gstnTV;


        OrderBillingViewHolder(View itemView) {
            super(itemView);
        }

    }


    class OrderPaymentSummaryViewHolder extends BaseViewHolder {

        @BindView(R.id.subtotal_order_summaryTV)
        TextView subtotal_order_summaryTV;
        @BindView(R.id.discount_smryTV)
        TextView discount_smryTV;
        @BindView(R.id.taxValueTV)
        TextView taxValueTV;
        @BindView(R.id.shipping_charges_summaryTV)
        TextView shipping_charges_summaryTV;
        @BindView(R.id.total_amountTV)
        TextView total_amountTV;
        @BindView(R.id.taxTV)
        TextView taxTV;
        @BindView(R.id.tax_cst_refundTV)
        TextView tax_cst_refundTV;
        @BindView(R.id.cst_refundAmntTV)
        TextView cst_refundAmntTV;
        @BindView(R.id.total_paymentTV)
        RelativeLayout total_paymentTV;
        @BindView(R.id.lhs_item_cartsummaryTV1)
        TextView discount_textTV;
        @BindView(R.id.cstRebateRL)
        RelativeLayout cstRebateRL;
        @BindView(R.id.codRL)
        RelativeLayout codRL;
        @BindView(R.id.codChargeAmtTV)
        TextView codChargeAmtTV;
        @BindView(R.id.creditRL)
        RelativeLayout creditRL;
        @BindView(R.id.creditChargeAmtTV)
        TextView creditChargeAmtTV;

        OrderPaymentSummaryViewHolder(View itemView) {
            super(itemView);
        }
    }


    class OrderProductSummaryViewHolder extends BaseViewHolder {

        @BindView(R.id.recyclerView)
        protected RecyclerView recyclerView;


        OrderProductSummaryViewHolder(View itemView) {
            super(itemView);
        }
    }


}