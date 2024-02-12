package com.partseazy.android.ui.adapters.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.checkout.ShippingRadioAdapter;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.cart_checkout.CouponData;
import com.partseazy.android.ui.model.cart_checkout.ItemsData;
import com.partseazy.android.ui.model.cart_checkout.ItemsFiguresData;
import com.partseazy.android.ui.model.cart_checkout.ShippinMethodData;
import com.partseazy.android.ui.model.cart_checkout.ShippingData;
import com.partseazy.android.ui.model.cart_checkout.SubItemData;
import com.partseazy.android.ui.model.cart_checkout.SummaryData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by CAN Solutions on 1/8/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private BaseFragment baseContext;
    private Context context;

    private CartCheckoutBaseData mCartData;
    private ImageLoader imageLoader;
    private CartSubItemAdapter subItemAdapter;
    private AlertDialog alertDialog;
    private String TYPE_SAMPLE = "sample";
    ShippingRadioAdapter paymentAdapter;


    public enum ERROR_TYPE {

        ACTIVE("active"),
        SERVICEABLE("serviceable"),
        STOCK("stock"),
        MOQ("moq"),
        MAX("max");

        public final String error;

        private ERROR_TYPE(String status) {
            this.error = status;
        }

        public String getErrorType() {
            return error;
        }


    }

    public CartAdapter(BaseFragment context, CartCheckoutBaseData data) {

        baseContext = context;
        this.context = context.getContext();
        mCartData = data;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType holderType = HolderType.values()[viewType];
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (holderType) {
            case VIEW_CART_MESSAGE:
                return new CartMessageVH(layoutInflater.inflate(R.layout.row_cart_message, parent, false));
            case VIEW_CART_INFO:
                return new CartProductDetailVH(layoutInflater.inflate(R.layout.row_cart_item_detail, parent, false));
            case VIEW_CART_PROMOCODE:
                return new CartPromoCodeVH(layoutInflater.inflate(R.layout.row_cart_item_promotinal_code, parent, false));
            case VIEW_CART_SHIPPING:
                return new CartShippingEstimatorVH(layoutInflater.inflate(R.layout.row_cart_shipping_detail, parent, false));
            case VIEW_CART_CFORM:
                return new CFormVH(layoutInflater.inflate(R.layout.row_cart_cform, parent, false));
            case VIEW_CART_ORDER_DETAIL:
                return new CartOrderDetailVH(layoutInflater.inflate(R.layout.row_cart_order_smry_main, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CartMessageVH) {
            if (FeatureMap.isFeatureEnabled(FeatureMapKeys.cart_message)) {
                CartMessageVH messageVH = (CartMessageVH) holder;
                onBindViewMsg(messageVH);
            } else {
                ((CartMessageVH) holder).itemView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

            }
        }

        if (holder instanceof CartProductDetailVH) {
            bindViewForItemInfo((CartProductDetailVH) holder, position);
        }

        if (holder instanceof CartPromoCodeVH) {
            CartPromoCodeVH promoCodeVH = (CartPromoCodeVH) holder;
            //TODO hiding this card  (Not in this release)
            if (FeatureMap.isFeatureEnabled(FeatureMapKeys.cart_coupon)) {
                onBindPromoCode(promoCodeVH, position);
            } else {
                promoCodeVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }


        }

        if (holder instanceof CartShippingEstimatorVH) {
            CartShippingEstimatorVH shippingAddressVH = (CartShippingEstimatorVH) holder;
            onBindShipping(shippingAddressVH, position);
        }

        if (holder instanceof CFormVH) {
            final CFormVH cFormVH = (CFormVH) holder;
            onBindCForm(cFormVH, position);
        }

        if (holder instanceof CartOrderDetailVH) {
            CartOrderDetailVH payOrderDetailVH = (CartOrderDetailVH) holder;
            onBindOrderPriceSummary(payOrderDetailVH, position);
        }
    }

    private void onBindViewMsg(CartMessageVH messageVH) {
        StringBuffer msgList = new StringBuffer();
        for (String msg : mCartData.messages) {
            msgList.append("*").append(" ").append(msg).append("\n");
        }
        messageVH.cartMessage.setText(msgList);
    }

    private void onBindCForm(final CFormVH cFormVH, int position) {

        cFormVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        SummaryData summaryData = mCartData.summary;
        if (summaryData.taxRefund == 0) {
            cFormVH.cform_dataCV.setChecked(false);
            cFormVH.cform_RL.setVisibility(View.GONE);
        } else {
            cFormVH.cform_dataCV.setChecked(true);
            cFormVH.cform_RL.setVisibility(View.VISIBLE);
        }

        cFormVH.cform_dataCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cFormVH.cform_dataCV.isChecked())
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_CFORM, 1);
                else
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_CFORM, 0);
            }
        });
        if (cFormVH.cform_RL.getVisibility() == View.VISIBLE) {
            cFormVH.expand_cformIV.setImageResource(R.drawable.ic_minus);
        } else {
            cFormVH.expand_cformIV.setImageResource(R.drawable.ic_plus);
        }
        cFormVH.expand_cformRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cFormVH.cform_RL.getVisibility() == View.VISIBLE) {
                    cFormVH.cform_RL.setVisibility(View.GONE);
                    cFormVH.expand_cformIV.setImageResource(R.drawable.ic_plus);
                } else {
                    cFormVH.cform_RL.setVisibility(View.VISIBLE);
                    cFormVH.expand_cformIV.setImageResource(R.drawable.ic_minus);
                }
            }
        });
    }


    private void onBindOrderPriceSummary(final CartOrderDetailVH payOrderDetailVH, int position) {

        SummaryData summaryData = mCartData.summary;
        payOrderDetailVH.total_paymentTV.setVisibility(View.GONE);

        payOrderDetailVH.subtotal_order_summaryTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.sub_total)));
        int discountPrice = summaryData.cartRebate + summaryData.couponRebate;
        if (discountPrice == 0) {
            payOrderDetailVH.discountRL.setVisibility(View.GONE);
        } else {
            payOrderDetailVH.discountRL.setVisibility(View.VISIBLE);
            payOrderDetailVH.discount_smryTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(discountPrice)));
        }
        payOrderDetailVH.shipping_charges_summaryTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.courierFee)));
        payOrderDetailVH.total_amountTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.grandTotal)));

        if (summaryData.taxGST >0) {
            payOrderDetailVH.taxRL.setVisibility(View.VISIBLE);
            payOrderDetailVH.taxTV.setText(baseContext.getString(R.string.gst));
            payOrderDetailVH.taxValueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.taxGST)));

            if (summaryData.taxCess>0) {
                payOrderDetailVH.cessTV.setText(baseContext.getString(R.string.cess));
                payOrderDetailVH.cessValueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.taxCess)));
                payOrderDetailVH.cessRL.setVisibility(View.VISIBLE);
            } else {

                payOrderDetailVH.cessRL.setVisibility(View.GONE);

            }
        } else {
            payOrderDetailVH.cessRL.setVisibility(View.GONE);
            payOrderDetailVH.taxRL.setVisibility(View.GONE);
        }

        if (summaryData.codFee == 0) {
            payOrderDetailVH.codRL.setVisibility(View.GONE);
        } else {
            payOrderDetailVH.codRL.setVisibility(View.VISIBLE);
            payOrderDetailVH.codChargeAmtTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.codFee)));
        }

        payOrderDetailVH.detailOrderSummaryRL.setVisibility(View.VISIBLE);
        payOrderDetailVH.expandOrderIV.setImageResource(R.drawable.ic_minus);
        payOrderDetailVH.ordersummaryHeaderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (payOrderDetailVH.detailOrderSummaryRL.getVisibility() == View.VISIBLE) {
                    payOrderDetailVH.detailOrderSummaryRL.setVisibility(View.GONE);
                    payOrderDetailVH.expandOrderIV.setImageResource(R.drawable.ic_plus);
                } else {
                    payOrderDetailVH.detailOrderSummaryRL.setVisibility(View.VISIBLE);
                    payOrderDetailVH.expandOrderIV.setImageResource(R.drawable.ic_minus);
                }
            }
        });
    }


    private void onBindPromoCode(final CartPromoCodeVH promoCodeVH, int position) {

        final CouponData couponData = mCartData.couponData;

        if (couponData != null && couponData.applied != null) {
            if (couponData.applied) {
                promoCodeVH.promo_code_cartEV.setText(couponData.coupon);
                promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);

                promoCodeVH.promocode_RL.setVisibility(View.GONE);
                promoCodeVH.couponApplyRL.setVisibility(View.VISIBLE);
                String coupon = mCartData.couponData.coupon + "";
                String couponStr = "<b>" + coupon.toUpperCase() + " </b>" + context.getString(R.string.applied_successfully);
                promoCodeVH.couponApplyTV.setText(Html.fromHtml(couponStr));
                // promoCodeVH.couponApplyTV.setText(context.getString(R.string.applied_successfully, mCartData.couponData.coupon.toUpperCase()));
                int discountPrice = mCartData.summary.cartRebate + mCartData.summary.couponRebate;
                promoCodeVH.discountMsgTV.setText(baseContext.getString(R.string.discounted_msg, CommonUtility.convertionPaiseToRupeeString(discountPrice)));


            } else if (!couponData.applied && couponData.coupon.length() > 0) {
                promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
                promoCodeVH.promo_code_cartEV.setText(couponData.coupon);
                promoCodeVH.promo_code_cartTIL.setError(context.getString(R.string.invalid_coupon));

                promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
            } else {
                promoCodeVH.promocode_RL.setVisibility(View.GONE);
                promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_plus);
            }
        }

        promoCodeVH.applyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promocode = promoCodeVH.promo_code_cartEV.getText().toString().trim();
                if (promocode.length() != 0) {
                    if (couponData.applied)
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.POST_PROMOCODE, promocode);
                    else
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.POST_PROMOCODE, promocode);

                } else {
                    promoCodeVH.promo_code_cartTIL.setError(context.getString(R.string.no_promocode));
                }
            }
        });


        promoCodeVH.removeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeVH.promo_code_cartEV.setText("");
                PartsEazyEventBus.getInstance().postEvent(EventConstant.POST_PROMOCODE, "");

            }
        });

        promoCodeVH.expand_promoCodeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponData.applied) {
                    if (promoCodeVH.couponApplyRL.getVisibility() == View.VISIBLE) {
                        promoCodeVH.couponApplyRL.setVisibility(View.GONE);
                        promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_plus);
                    } else {
                        promoCodeVH.couponApplyRL.setVisibility(View.VISIBLE);
                        promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
                    }

                } else {
                    if (promoCodeVH.promocode_RL.getVisibility() == View.VISIBLE) {
                        promoCodeVH.promocode_RL.setVisibility(View.GONE);
                        promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_plus);
                    } else {
                        promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                        promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
                    }
                }

            }
        });
    }


    private void onBindShipping(final CartShippingEstimatorVH shippingAddressVH, final int position) {

        ShippingData shipping = mCartData.shipping;
        if (shipping != null) {
            final List<ShippinMethodData> shippingMethods = shipping.methods;
            if (paymentAdapter == null)
                paymentAdapter = new ShippingRadioAdapter(context, shippingMethods, shipping.selected, true);
            shippingAddressVH.recyclerView.setAdapter(paymentAdapter);
            shippingAddressVH.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            shippingAddressVH.recyclerView.setHasFixedSize(true);
            shippingAddressVH.recyclerView.setNestedScrollingEnabled(false);

            if ((shipping.selected.equals(StaticMap.EXPRESS_SHIPPING)) ||
                    (shipping.selected).equals(StaticMap.SELF_SHIPPING)) {
                shippingAddressVH.detail_ordersumryRL.setVisibility(View.VISIBLE);
                shippingAddressVH.expandShippingIV.setImageResource(R.drawable.ic_minus);
            } else {
                shippingAddressVH.detail_ordersumryRL.setVisibility(View.GONE);
                shippingAddressVH.expandShippingIV.setImageResource(R.drawable.ic_plus);
            }
        }

        shippingAddressVH.summary_headerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shippingAddressVH.detail_ordersumryRL.getVisibility() == View.VISIBLE) {
                    shippingAddressVH.detail_ordersumryRL.setVisibility(View.GONE);
                    shippingAddressVH.expandShippingIV.setImageResource(R.drawable.ic_plus);
                } else {
                    shippingAddressVH.detail_ordersumryRL.setVisibility(View.VISIBLE);
                    shippingAddressVH.expandShippingIV.setImageResource(R.drawable.ic_minus);
                }
            }
        });
    }


    private void bindViewForItemInfo(final CartProductDetailVH detailVH, int position) {

        int pos = position - 1;


        //Title handling
        if (pos == 0)
            detailVH.order_reviewTV.setVisibility(View.VISIBLE);
        else
            detailVH.order_reviewTV.setVisibility(View.GONE);

        final ItemsData itemInfo = mCartData.list.get(pos);
        final ItemsFiguresData figures = itemInfo.figures;


        if (itemInfo.categoryId == AppConstants.REFURBISH_CAT_ID || itemInfo.categoryId == AppConstants.REFURBISH_CAT_2_ID) {
            //     detailVH.refurbishIcon.setVisibility(View.VISIBLE);
            detailVH.refurbishIcon.setVisibility(View.GONE);
        } else {
            detailVH.refurbishIcon.setVisibility(View.GONE);
        }

        if (itemInfo.image != null && itemInfo.image.length() > 0) {
//            String imageURL = CommonUtility.getFormattedImageUrl(itemInfo.image);
//            detailVH.product_cartIV.setImageUrl(imageURL, imageLoader);

            String formatedURL = CommonUtility.getFormattedImageUrl(itemInfo.image, detailVH.product_cartIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, detailVH.product_cartIV, formatedURL);

        }
        if (itemInfo.name != null) detailVH.product_name_cartTV.setText(itemInfo.name);


        if (itemInfo.ok) {
            detailVH.errTV.setVisibility(View.GONE);
        } else {


            if (itemInfo.error_type != null && !TextUtils.isEmpty(itemInfo.error_type)) {

                detailVH.errTV.setVisibility(View.VISIBLE);


                if (itemInfo.error_type.equals(ERROR_TYPE.ACTIVE.error)) {
                    detailVH.errTV.setText(StaticMap.cart_er_isActive);

                } else if (itemInfo.error_type.equals(ERROR_TYPE.SERVICEABLE.error)) {
                    detailVH.errTV.setText(StaticMap.cart_er_isServicable);

                } else if (itemInfo.error_type.equals(ERROR_TYPE.STOCK.error)) {
                    detailVH.errTV.setText(StaticMap.cart_er_inStock);

                } else if (itemInfo.error_type.equals(ERROR_TYPE.MOQ.error)) {
                    String msg = StaticMap.cart_er_minQty.replace("***", "" + itemInfo.minQty);
                    detailVH.errTV.setText(msg);
                } else if (itemInfo.error_type.equals(ERROR_TYPE.MAX.error)) {
                    String msg = StaticMap.cart_er_maxQty.replace("***", "" + itemInfo.maxQty);
                    detailVH.errTV.setText(msg);
                } else {
                    detailVH.errTV.setText(context.getString(R.string.err_somthin_wrong));
                }
            }
        }

        if (itemInfo.isThereAnyErrorInCart != null && itemInfo.
                isThereAnyErrorInCart) {
            detailVH.errTV.setVisibility(View.VISIBLE);
            detailVH.errTV.setText(itemInfo.errortCart);
        }
        int qty = 0;

        PartsEazyEventBus.getInstance().postEvent(EventConstant.UPDATE_TAB_COUNT_CART_ID, mCartData.cartCount);

        for (SubItemData data : itemInfo.rows) {
            qty += data.qty;
        }

        detailVH.qtyTV.setText(context.getString(R.string.cart_qty, qty));

        if (figures != null)

        {

            detailVH.PriceTv.setText(Html.fromHtml(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeStringHTML(itemInfo.figures.pricing))));
        }

        if (itemInfo.margin == 0)
            detailVH.marginTV.setVisibility(View.GONE);
        else

        {
            detailVH.marginTV.setText(baseContext.getString(R.string.margin, itemInfo.margin + ""));
            detailVH.marginTV.setVisibility(View.VISIBLE);
        }

        if (itemInfo.minQty == 0)
            detailVH.product_minqty_cartTV.setVisibility(View.GONE);
        else

        {
            detailVH.product_minqty_cartTV.setText(baseContext.getString(R.string.moq, itemInfo.minQty + ""));
            detailVH.product_minqty_cartTV.setVisibility(View.VISIBLE);
        }

        if (itemInfo.type.equals(TYPE_SAMPLE)) {
            detailVH.product_minqty_cartTV.setText(baseContext.getString(R.string.sample_item));
            detailVH.product_minqty_cartTV.setVisibility(View.VISIBLE);
        }

        detailVH.removeItemTV.setOnClickListener(new View.OnClickListener()

                                                 {
                                                     @Override
                                                     public void onClick(View view) {
                                                         showAlertDialog(itemInfo.reference);
                                                     }
                                                 }

        );

        onBindSubItemAdapter(detailVH, position, itemInfo, qty);
    }


    private void onBindSubItemAdapter(CartProductDetailVH detailVH, int position, ItemsData itemsData, int qty) {
        detailVH.sub_itemRV.setLayoutManager(new LinearLayoutManager(baseContext.getContext()));
        detailVH.sub_itemRV.setNestedScrollingEnabled(false);
        if (subItemAdapter == null) {
            boolean isSample = false;
            if (itemsData.type.equals(TYPE_SAMPLE)) {
                isSample = true;
            } else {
                isSample = false;
            }
            CartSubItemAdapter subItemAdapter = new CartSubItemAdapter(baseContext, position, itemsData, detailVH.sub_itemRV, isSample, qty);
            detailVH.sub_itemRV.setAdapter(subItemAdapter);
        } else {
            subItemAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HolderType.VIEW_CART_MESSAGE.ordinal();

        if (position == getItemList().size() + 1) {
            return HolderType.VIEW_CART_PROMOCODE.ordinal();
        }
        if (position == getItemList().size() + 2) {
            return HolderType.VIEW_CART_SHIPPING.ordinal();
        }
        if (position == getItemList().size() + 3) {
            return HolderType.VIEW_CART_CFORM.ordinal();
        }
        if (position == getItemList().size() + 4) {
            return HolderType.VIEW_CART_ORDER_DETAIL.ordinal();
        }

        return HolderType.VIEW_CART_INFO.ordinal();
    }

    private ArrayList<ItemsData> getItemList() {
        return (ArrayList<ItemsData>) mCartData.list;
    }

    @Override
    public int getItemCount() {
        return getItemList().size() + 5;
    }


    class CartProductDetailVH extends BaseViewHolder {

        @BindView(R.id.order_reviewTV)
        TextView order_reviewTV;
        @BindView(R.id.product_name_cartTV)
        TextView product_name_cartTV;
        @BindView(R.id.discountPriceTv)
        TextView PriceTv;
        @BindView(R.id.marginTV)
        TextView marginTV;
        @BindView(R.id.product_minqty_cartTV)
        TextView product_minqty_cartTV;
        @BindView(R.id.qtyTV)
        TextView qtyTV;
        @BindView(R.id.product_cartIV)
        NetworkImageView product_cartIV;
        @BindView(R.id.sub_itemRV)
        RecyclerView sub_itemRV;
        @BindView(R.id.errorcartTV)
        TextView errTV;
        @BindView(R.id.removeItemTV)
        TextView removeItemTV;
        @BindView(R.id.refurbishIcon)
        ImageView refurbishIcon;

        CartProductDetailVH(View itemView) {
            super(itemView);
        }

    }

    class CartPromoCodeVH extends BaseViewHolder {

        @BindView(R.id.expand_promoCodeRL)
        RelativeLayout expand_promoCodeRL;
        @BindView(R.id.promocode_RL)
        RelativeLayout promocode_RL;
        @BindView(R.id.expand_promotinalIV)
        ImageView expand_promotinalIV;
        @BindView(R.id.applyTV)
        TextView applyTV;
        @BindView(R.id.promo_code_cartEV)
        EditText promo_code_cartEV;
        @BindView(R.id.promo_code_cartTIL)
        TextInputLayout promo_code_cartTIL;


        @BindView(R.id.couponApplyRL)
        protected RelativeLayout couponApplyRL;

        @BindView(R.id.couponApplyTV)
        protected TextView couponApplyTV;

        @BindView(R.id.removeTV)
        protected TextView removeTV;

        @BindView(R.id.discountMsgTV)
        protected TextView discountMsgTV;


        CartPromoCodeVH(View itemView) {
            super(itemView);
        }
    }

    class CartShippingEstimatorVH extends BaseViewHolder {

        @BindView(R.id.shipping_radioGrp)
        RadioGroup shipping_radioGrp;
        @BindView(R.id.detail_ordersumryRL)
        RelativeLayout detail_ordersumryRL;
        @BindView(R.id.expandShippingIV)
        ImageView expandShippingIV;
        @BindView(R.id.summary_headerRL)
        RelativeLayout summary_headerRL;


        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        CartShippingEstimatorVH(View itemView) {
            super(itemView);
        }
    }

    class CartOrderDetailVH extends BaseViewHolder {

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
        @BindView(R.id.discountRL)
        RelativeLayout discountRL;
        @BindView(R.id.cstRebateRL)
        RelativeLayout cstRebateRL;
        @BindView(R.id.codRL)
        RelativeLayout codRL;
        @BindView(R.id.codChargeAmtTV)
        TextView codChargeAmtTV;
        @BindView(R.id.detailOrderSummaryRL)
        RelativeLayout detailOrderSummaryRL;
        @BindView(R.id.ordersummaryHeaderRL)
        RelativeLayout ordersummaryHeaderRL;
        @BindView(R.id.expandOrderIV)
        ImageView expandOrderIV;

        @BindView(R.id.cessRL)
        protected RelativeLayout cessRL;

        @BindView(R.id.taxRL)
        protected RelativeLayout taxRL;

        @BindView(R.id.cessTV)
        protected TextView cessTV;
        @BindView(R.id.cessValueTV)
        protected TextView cessValueTV;


        CartOrderDetailVH(View itemView) {
            super(itemView);
        }
    }

    class CartMessageVH extends BaseViewHolder {

        @BindView(R.id.cartMessageTV)
        TextView cartMessage;

        public CartMessageVH(View view) {
            super(view);
        }
    }

    class CFormVH extends BaseViewHolder {

        @BindView(R.id.expand_cformRL)
        RelativeLayout expand_cformRL;
        @BindView(R.id.expand_cformIV)
        ImageView expand_cformIV;
        @BindView(R.id.cform_RL)
        RelativeLayout cform_RL;
        @BindView(R.id.cform_dataCV)
        CheckBox cform_dataCV;

        CFormVH(View itemView) {
            super(itemView);
        }
    }

    public void updateCartOnError(int parentPosition, String error) {

        mCartData.list.get(parentPosition).isThereAnyErrorInCart = true;
        mCartData.list.get(parentPosition).errortCart = error;
        notifyDataSetChanged();

    }


    private void showAlertDialog(final String reference) {

        if (alertDialog != null && alertDialog.isShowing())
            return;
        String strMsg = baseContext.getString(R.string.cart_remove_message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                baseContext.getContext());
        alertDialogBuilder
                .setMessage(strMsg)
                .setCancelable(true)
                .setPositiveButton(baseContext.getString(R.string.remove), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ITEM_REMOVED, reference);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(baseContext.getString(R.string.move_wishlist), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ITEM_MOVED_WISHLIST, reference);
                        dialog.cancel();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void updateData(CartCheckoutBaseData cartCheckoutBaseData) {
        this.mCartData = cartCheckoutBaseData;
        notifyDataSetChanged();
    }


}
