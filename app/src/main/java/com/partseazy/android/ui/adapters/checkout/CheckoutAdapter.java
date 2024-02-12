package com.partseazy.android.ui.adapters.checkout;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.base.BaseViewPagerStateAdapter;
import com.partseazy.android.ui.fragments.checkout.CODFragment;
import com.partseazy.android.ui.fragments.checkout.CheckoutFragment;
import com.partseazy.android.ui.fragments.checkout.CreditFragment;
import com.partseazy.android.ui.fragments.shippingaddress.AddressCheckoutTabFragment;
import com.partseazy.android.ui.fragments.shippingaddress.BillingAddressFragment;
import com.partseazy.android.ui.fragments.shippingaddress.ShippingAddressFragment;
import com.partseazy.android.ui.model.cart_checkout.CartCheckoutBaseData;
import com.partseazy.android.ui.model.cart_checkout.CouponData;
import com.partseazy.android.ui.model.cart_checkout.ItemsData;
import com.partseazy.android.ui.model.cart_checkout.ItemsFiguresData;
import com.partseazy.android.ui.model.cart_checkout.PaymentData;
import com.partseazy.android.ui.model.cart_checkout.ShippinMethodData;
import com.partseazy.android.ui.model.cart_checkout.ShippingData;
import com.partseazy.android.ui.model.cart_checkout.SubItemData;
import com.partseazy.android.ui.model.cart_checkout.SummaryData;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by can on 26/12/16.
 */

public class CheckoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private CheckoutFragment baseContext;
    private Context context;

    private ShippingAddressDetail shippingAddDetails;
    private ShippingAddressDetail billingAddDetails;
    private CartCheckoutBaseData mCheckoutData;
    private ImageLoader imageLoader;
    private BaseViewPagerStateAdapter pagerAdapter, addressPagerAdapter;
    private int selectedShippingAddIndex;
    private int selectedBillingAddIndex;
    private CheckoutSubItemAdapter subItemAdapter;
    private ShippingRadioAdapter paymentAdapter;
    private String TYPE_SAMPLE = "sample";


    public CheckoutAdapter(CheckoutFragment context, CartCheckoutBaseData homeData, ShippingAddressDetail shippingAddDetails, int shippingAddIndex, ShippingAddressDetail billingAddDetails, int billingAddIndex) {

        baseContext = context;
        this.context = context.getContext();
        mCheckoutData = homeData;
        this.selectedShippingAddIndex = shippingAddIndex;
        this.selectedBillingAddIndex = billingAddIndex;
        this.shippingAddDetails = shippingAddDetails;
        this.billingAddDetails = billingAddDetails;
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType holderType = HolderType.values()[viewType];
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


        switch (holderType) {
            case VIEW_PAY_PROGRESSBAR:
                return new PayProgressVH(layoutInflater.inflate(R.layout.row_shipping_progressbar, parent, false));
            case VIEW_ADDRESS_PAGER:
                return new AddressOptionVH(layoutInflater.inflate(R.layout.row_address_view_pager, parent, false));
//            case VIEW_PAY_ADDRESS:
//                return new PayAddressVH(layoutInflater.inflate(R.layout.row_payment_delivery_address, parent, false));
//            case VIEW_BILLING_ADDRESS:
//                return new BillingAddressVH(layoutInflater.inflate(R.layout.row_payment_billing_address, parent, false));
            case VIEW_PAY_PRODUCT_DETAIL:
                return new PayProductDetailVH(layoutInflater.inflate(R.layout.row_payment_order_review, parent, false));
            case VIEW_PAY_PROMOTION_CODE:
                return new PayPromoCodeVH(layoutInflater.inflate(R.layout.row_cart_item_promotinal_code, parent, false));
            case VIEW_PAY_SHIPPING_TYPE:
                return new PaySummaryTypeVH(layoutInflater.inflate(R.layout.row_cart_shipping_detail, parent, false));
            case VIEW_PAY_ORDER_DETAIL:
                return new PayOrderDetailVH(layoutInflater.inflate(R.layout.row_cart_order_smry_main, parent, false));
            case VIEW_PAY_OPTION:
                return new PaymentOptionVH(layoutInflater.inflate(R.layout.row_payment_viewpager, parent, false));
            case VIEW_PAY_CALL:
                return new PaymentCallVH(layoutInflater.inflate(R.layout.row_payment_call, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PayProgressVH) {
            PayProgressVH payProgressVH = (PayProgressVH) holder;
            payProgressVH.secondIV.setImageResource(R.drawable.check_green_circle);
            payProgressVH.firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
            payProgressVH.thirdIV.setImageResource(R.drawable.check_green_circle);
            payProgressVH.secondLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));

        }

        if (holder instanceof AddressOptionVH) {
            AddressOptionVH optionVH = (AddressOptionVH) holder;
            setUpAddressViewPager(optionVH.viewPager, optionVH.tabLayout);
        }

//        if(holder instanceof  AddressCardVH)
//        {
//            AddressCardVH addressCardVH = (AddressCardVH)holder;
//            OnBindAddressCard(addressCardVH,shippingAddDetails,true);
//        }
//        if (holder instanceof PayAddressVH) {
//            PayAddressVH addressVH = (PayAddressVH) holder;
//            onBindShippingAddress(addressVH);
//        }

//        if (holder instanceof BillingAddressVH) {
//            BillingAddressVH billingAddressVH = (BillingAddressVH) holder;
//            billingAddressVH.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0));
//            onBindBillingAddress(billingAddressVH);
//
//        }

        if (holder instanceof PayProductDetailVH) {
            bindViewForItemInfo((PayProductDetailVH) holder, position);
        }

        if (holder instanceof PayPromoCodeVH) {
            PayPromoCodeVH promoCodeVH = (PayPromoCodeVH) holder;
            promoCodeVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            onBindPromoCode(promoCodeVH, position);
        }

        if (holder instanceof PaySummaryTypeVH) {
            PaySummaryTypeVH shippingAddressVH = (PaySummaryTypeVH) holder;
            onBindShipping(shippingAddressVH, position);
        }

        if (holder instanceof PayOrderDetailVH) {
            PayOrderDetailVH payOrderDetailVH = (PayOrderDetailVH) holder;
            onBindOrderPriceSummary(payOrderDetailVH, position);
        }

        if (holder instanceof PaymentOptionVH) {
            PaymentOptionVH optionVH = (PaymentOptionVH) holder;
            setupViewPager(optionVH.viewPager, optionVH.tabLayout);
        }

        if (holder instanceof PaymentCallVH) {
            PaymentCallVH callVH = (PaymentCallVH) holder;
            callVH.itemView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            onBindCall(callVH);
        }
    }

    private void onBindCall(PaymentCallVH callVH) {

        callVH.mobileNumberTV.setText(StaticMap.CC_PHONE);
        callVH.callRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.dialPhoneNumber(baseContext.getActivity(), StaticMap.CC_PHONE);
            }
        });
    }


    private void onBindOrderPriceSummary(final PayOrderDetailVH payOrderDetailVH, int position) {

        SummaryData summaryData = mCheckoutData.summary;
        payOrderDetailVH.total_paymentTV.setVisibility(View.VISIBLE);
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
    //    payOrderDetailVH.total_amountTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.sub_total)));

        if (summaryData.taxGST != 0) {
            payOrderDetailVH.taxRL.setVisibility(View.VISIBLE);
            payOrderDetailVH.taxTV.setText(baseContext.getString(R.string.gst));
            payOrderDetailVH.taxValueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.taxGST)));

            if (summaryData.taxCess != 0) {
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

        /*if (summaryData.taxCst == 0) {
            payOrderDetailVH.taxTV.setText(baseContext.getString(R.string.tax));
            payOrderDetailVH.taxValueTV.setText(baseContext.getString(R.string.rs_str, convertionPaiseToRupeeString(summaryData.taxVat)));
        } else {
            payOrderDetailVH.taxTV.setText(baseContext.getString(R.string.cst));
            payOrderDetailVH.taxValueTV.setText(baseContext.getString(R.string.rs_str, convertionPaiseToRupeeString(summaryData.taxCst)));
            if (summaryData.taxRefund != 0) {
                payOrderDetailVH.cstRebateRL.setVisibility(View.VISIBLE);
                payOrderDetailVH.cst_refundAmntTV.setText(baseContext.getString(R.string.rs_str, convertionPaiseToRupeeString(summaryData.taxRefund)));
            }
        }
        */

        if (summaryData.codFee == 0) {
            payOrderDetailVH.codRL.setVisibility(View.GONE);
        } else {
            payOrderDetailVH.codRL.setVisibility(View.VISIBLE);
            payOrderDetailVH.codChargeAmtTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(summaryData.codFee)));
        }


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

    private void onBindBillingAddress(BillingAddressVH billingAddressVH) {

        if (billingAddDetails.billingName != null) {
            billingAddressVH.nameTV.setText(billingAddDetails.billingName);
        } else {
            billingAddressVH.nameTV.setText(billingAddDetails.name);
        }

        if (billingAddDetails.gstn != null && !billingAddDetails.gstn.equals("")) {
            billingAddressVH.gstnTV.setText(context.getString(R.string.gstn_caps_str, billingAddDetails.gstn.toUpperCase()));
            billingAddressVH.gstnTV.setVisibility(View.VISIBLE);
        } else {
            billingAddressVH.gstnTV.setVisibility(View.GONE);
        }


        billingAddressVH.addressTV.setText(billingAddDetails.street + ", " + billingAddDetails.city + ", " + billingAddDetails.state + " - " + billingAddDetails.pincode);

        if (billingAddDetails.landmark != null) {
            billingAddressVH.landmarkTV.setVisibility(View.VISIBLE);
            billingAddressVH.landmarkTV.setText(context.getString(R.string.shipping_landmark, billingAddDetails.landmark));
        } else
            billingAddressVH.landmarkTV.setVisibility(View.GONE);
        billingAddressVH.mobileTV.setText(context.getString(R.string.country_code) + billingAddDetails.mobile);

        billingAddressVH.billingAddressTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                BillingAddressFragment fragment = new BillingAddressFragment();
                bundle.putInt(fragment.SHIPPING_ADD_INDEX, selectedBillingAddIndex);
                bundle.putSerializable(fragment.SHIPPING_ADD, shippingAddDetails);
                fragment.setArguments(bundle);
                BaseFragment baseFragment = fragment;

                BaseFragment.removeTopAndAddToBackStack(baseContext.getContext(), baseFragment, BillingAddressFragment.getTagName());
            }
        });
    }

    private void onBindShippingAddress(PayAddressVH addressVH) {

        addressVH.nameTV.setText(shippingAddDetails.name);
        addressVH.addressTV.setText(shippingAddDetails.street + ", " + shippingAddDetails.city + ", " + shippingAddDetails.state + " - " + shippingAddDetails.pincode);
        if (shippingAddDetails.landmark != null) {
            addressVH.landmarkTV.setVisibility(View.VISIBLE);
            addressVH.landmarkTV.setText(context.getString(R.string.shipping_landmark, shippingAddDetails.landmark));
        } else
            addressVH.landmarkTV.setVisibility(View.GONE);
        addressVH.mobileTV.setText(context.getString(R.string.country_code) + shippingAddDetails.mobile);

        addressVH.change_delivery_addressTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                ShippingAddressFragment fragment = new ShippingAddressFragment();
                bundle.putInt(context.getString(R.string.selectedValue), selectedShippingAddIndex);
                fragment.setArguments(bundle);
                BaseFragment baseFragment = fragment;

                BaseFragment.removeTopAndAddToBackStack(baseContext.getContext(), baseFragment, ShippingAddressFragment.getTagName());
            }
        });
    }

    private void OnBindAddressCard(AddressCardVH addressCardVH, final ShippingAddressDetail shippingAddressDetail, final boolean isShippingCard) {

        addressCardVH.nameTV.setText(shippingAddressDetail.name);
        if (!isShippingCard) {
            if (billingAddDetails.billingName != null) {
                addressCardVH.nameTV.setText(billingAddDetails.billingName);
            } else {
                addressCardVH.nameTV.setText(billingAddDetails.name);
            }

            if (billingAddDetails.gstn != null && !billingAddDetails.gstn.equals("")) {
                addressCardVH.gstnTV.setText(context.getString(R.string.gstn_caps_str, billingAddDetails.gstn.toUpperCase()));
                addressCardVH.gstnTV.setVisibility(View.VISIBLE);
            } else {
                addressCardVH.gstnTV.setVisibility(View.GONE);
            }
        }

        addressCardVH.addressTV.setText(shippingAddressDetail.street + ", " + shippingAddressDetail.city + ", " + shippingAddressDetail.state + " - " + shippingAddressDetail.pincode);
        if (shippingAddressDetail.landmark != null) {
            addressCardVH.landmarkTV.setVisibility(View.VISIBLE);
            addressCardVH.landmarkTV.setText(context.getString(R.string.shipping_landmark, shippingAddressDetail.landmark));
        } else
            addressCardVH.landmarkTV.setVisibility(View.GONE);
        addressCardVH.mobileTV.setText(context.getString(R.string.country_code) + shippingAddressDetail.mobile);

        addressCardVH.changeAddressTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isShippingCard) {
                    Bundle bundle = new Bundle();
                    ShippingAddressFragment fragment = new ShippingAddressFragment();
                    bundle.putInt(context.getString(R.string.selectedValue), selectedShippingAddIndex);
                    fragment.setArguments(bundle);
                    BaseFragment baseFragment = fragment;

                    BaseFragment.removeTopAndAddToBackStack(baseContext.getContext(), baseFragment, ShippingAddressFragment.getTagName());
                } else {
                    Bundle bundle = new Bundle();
                    BillingAddressFragment fragment = new BillingAddressFragment();
                    bundle.putInt(fragment.SHIPPING_ADD_INDEX, selectedBillingAddIndex);
                    bundle.putSerializable(fragment.SHIPPING_ADD, shippingAddressDetail);
                    fragment.setArguments(bundle);
                    BaseFragment baseFragment = fragment;

                    BaseFragment.removeTopAndAddToBackStack(baseContext.getContext(), baseFragment, BillingAddressFragment.getTagName());

                }
            }
        });
    }

    private void onBindPromoCode(final PayPromoCodeVH promoCodeVH, int position) {

        final CouponData couponData = mCheckoutData.couponData;
        if (couponData != null && couponData.applied != null) {
            if (couponData.applied) {
                promoCodeVH.promo_code_cartEV.setText(couponData.coupon);
                promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
            } else if (!couponData.applied && couponData.coupon.length() > 0) {
                promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
                promoCodeVH.promo_code_cartEV.setText(couponData.coupon);
                promoCodeVH.promo_code_cartTIL.setError(context.getString(R.string.invalid_coupon));
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
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_POST_PROMOCODE, couponData.coupon);
                    else
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_POST_PROMOCODE, promocode);

                } else {
                    promoCodeVH.promo_code_cartTIL.setError(context.getString(R.string.no_promocode));
                }
            }
        });


        promoCodeVH.expand_promoCodeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promoCodeVH.promocode_RL.getVisibility() == View.VISIBLE) {
                    promoCodeVH.promocode_RL.setVisibility(View.GONE);
                    promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_plus);
                } else {
                    promoCodeVH.promocode_RL.setVisibility(View.VISIBLE);
                    promoCodeVH.expand_promotinalIV.setImageResource(R.drawable.ic_minus);
                }
            }
        });
    }


    private void onBindShipping(final PaySummaryTypeVH shippingAddressVH, final int position) {

        ShippingData shipping = mCheckoutData.shipping;
        if (shipping != null) {
            final Map<String, String> availableMethodMap = StaticMap.shippingMap;
            final List<ShippinMethodData> shippingMethods = shipping.methods;
            if (paymentAdapter == null)
                paymentAdapter = new ShippingRadioAdapter(context, shippingMethods, shipping.selected, false);
            shippingAddressVH.recyclerView.setAdapter(paymentAdapter);
            shippingAddressVH.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            shippingAddressVH.recyclerView.setHasFixedSize(true);
            shippingAddressVH.recyclerView.setNestedScrollingEnabled(false);
//            shippingAddressVH.shipping_radioGrp.setTag(position);
//            shippingAddressVH.shipping_radioGrp.removeAllViews();
//            if (shippingMethods != null) {
//                for (int i = 0; i < shippingMethods.size(); i++) {
//                    String shippingText = availableMethodMap.get(shippingMethods.get(i).type.toString());
//                    final RadioButton rdbtn = new RadioButton(baseContext.getContext());
//                    rdbtn.setId(i);
//                    rdbtn.setTag(availableMethodMap.containsKey(shippingMethods.get(i).type.toString()));
//                    rdbtn.setText(baseContext.getString(R.string.radio_btn_txt, shippingText, convertionPaiseToRupee(shippingMethods.get(i).charge)));
//                    if (shippingMethods.get(i).type.equals(shipping.selected))
//                        rdbtn.setChecked(true);
//                    shippingAddressVH.shipping_radioGrp.addView(rdbtn);
//
//                    rdbtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int id = rdbtn.getId();
//                            String selectedMethod = shippingMethods.get(id).type;
//                            PartsEazyEventBus.getInstance().postEvent(EventConstant.CHECKOUT_POST_SELECTED_SHIPPING, selectedMethod);
//                        }
//                    });
//
//                }
//            }
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


    private void bindViewForItemInfo(final PayProductDetailVH detailVH, int position) {

        int pos = position - 2;
        ItemsData itemInfo = getItemList().get(pos);
        ItemsFiguresData figures = itemInfo.figures;
        detailVH.order_reviewTV.setTag(pos);
        if (pos == 0) {
            detailVH.order_reviewTV.setVisibility(View.VISIBLE);
        } else {
            detailVH.order_reviewTV.setVisibility(View.GONE);
        }

        if (itemInfo.image != null && itemInfo.image.length() > 0) {
//            String imageURL = CommonUtility.getFormattedImageUrl(itemInfo.image);
//            detailVH.product_cartIV.setImageUrl(imageURL, imageLoader);

            String formatedURL = CommonUtility.getFormattedImageUrl(itemInfo.image, detailVH.product_cartIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, detailVH.product_cartIV, formatedURL);


        }

        if (itemInfo.categoryId == AppConstants.REFURBISH_CAT_ID || itemInfo.categoryId == AppConstants.REFURBISH_CAT_2_ID) {
       //     detailVH.refurbishIcon.setVisibility(View.VISIBLE);
              detailVH.refurbishIcon.setVisibility(View.GONE);

        } else {
            detailVH.refurbishIcon.setVisibility(View.GONE);
        }
        if (itemInfo.name != null) detailVH.product_name_cartTV.setText(itemInfo.name);

        if (itemInfo.type.equals(TYPE_SAMPLE)) {
            detailVH.sampleItemTV.setText(baseContext.getString(R.string.sample_item));
            detailVH.sampleItemTV.setVisibility(View.VISIBLE);
        } else {
            detailVH.sampleItemTV.setVisibility(View.GONE);
        }

        if (itemInfo.delivery != 0 && StaticMap.dispatch_lower_days != null && StaticMap.dispatch_upper_days != null) {
            int minDelieveryDay = itemInfo.delivery + Integer.parseInt(StaticMap.dispatch_lower_days);
            int maxDeliveryDay = itemInfo.delivery + Integer.parseInt(StaticMap.dispatch_upper_days);
            detailVH.deliveryTimeTV.setText(baseContext.getString(R.string.delivery_by, minDelieveryDay, maxDeliveryDay));
            detailVH.deliveryTimeTV.setVisibility(View.VISIBLE);
        } else detailVH.deliveryTimeTV.setVisibility(View.INVISIBLE);

//        if (itemInfo.ok) detailVH.viewErr.setVisibility(View.GONE);
//        else {
//            detailVH.viewErr.setVisibility(View.VISIBLE);
//
//        }

        if (figures != null) {
            // detailVH.discountPriceTv.setText(baseContext.getString(R.string.rs, convertionPaiseToRupee(itemInfo.figures.priceEff)));
            detailVH.discountPriceTv.setText(Html.fromHtml(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeStringHTML(itemInfo.figures.pricing))));
         //   detailVH.effect_amount_valueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(itemInfo.figures.pricing)));
            detailVH.effect_amount_valueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(itemInfo.figures.subTotal)));
            int discountPrice = itemInfo.figures.cartRebate + itemInfo.figures.couponRebate;
            if (discountPrice == 0) {
                detailVH.seller_discountTV.setVisibility(View.GONE);
                detailVH.seller_discount_amountTV.setVisibility(View.GONE);
            } else {
                detailVH.seller_discountTV.setVisibility(View.VISIBLE);
                detailVH.seller_discount_amountTV.setVisibility(View.VISIBLE);
                detailVH.seller_discount_amountTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(discountPrice)));
            }


            if (itemInfo.figures.taxGST > 0) {
                detailVH.taxTV.setText(baseContext.getString(R.string.gst));
                detailVH.taxValueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(itemInfo.figures.taxGST)));
                detailVH.taxTV.setVisibility(View.VISIBLE);
                detailVH.taxValueTV.setVisibility(View.VISIBLE);
                if (itemInfo.figures.taxCess > 0) {
                    detailVH.cessTV.setText(baseContext.getString(R.string.cess));
                    detailVH.cessValueTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(itemInfo.figures.taxCess)));
                    detailVH.cessTV.setVisibility(View.VISIBLE);
                    detailVH.cessValueTV.setVisibility(View.VISIBLE);
                } else {
                    detailVH.cessTV.setVisibility(View.GONE);
                    detailVH.cessValueTV.setVisibility(View.GONE);
                }

            } else {
                detailVH.cessTV.setVisibility(View.GONE);
                detailVH.cessValueTV.setVisibility(View.GONE);
                detailVH.taxTV.setVisibility(View.GONE);
                detailVH.taxValueTV.setVisibility(View.GONE);
                detailVH.cst_refundTV.setVisibility(View.VISIBLE);
                detailVH.cst_refund_amountTV.setVisibility(View.VISIBLE);
                detailVH.cst_refund_amountTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(itemInfo.figures.taxRefund)));
            }
           // int totalPrice = itemInfo.figures.priceEff + itemInfo.figures.taxVat + itemInfo.figures.taxGST + itemInfo.figures.taxCess;
            int totalPrice = itemInfo.figures.subTotal + itemInfo.figures.taxVat + itemInfo.figures.taxGST + itemInfo.figures.taxCess;
            detailVH.total_amountTV.setText(baseContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeString(totalPrice)));
        }
        if (itemInfo.margin == 0)
            detailVH.marginTV.setVisibility(View.GONE);
        else {
            detailVH.marginTV.setText(baseContext.getString(R.string.margin, itemInfo.margin + ""));
            detailVH.marginTV.setVisibility(View.VISIBLE);
        }

        //detailVH.qtyTV.setText(baseContext.getString(R.string.qty, itemInfo.qty + "")); //todo confirmation by designer..

        detailVH.view_detailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailVH.viewDetailLL.getVisibility() == View.VISIBLE) {
                    detailVH.view_detailTV.setText(R.string.expand_view);
                    detailVH.viewDetailLL.setVisibility(View.GONE);
                } else {
                    detailVH.view_detailTV.setText(R.string.collapse_view);
                    detailVH.viewDetailLL.setVisibility(View.VISIBLE);
                }
            }
        });

        List<SubItemData> subItemDatas = itemInfo.rows;
        CustomLogger.d("subItemsRows ::" + subItemDatas.size());
        onBindSubItemAdapter(detailVH, subItemDatas, position);
    }


    private void onBindSubItemAdapter(PayProductDetailVH detailVH, List<SubItemData> subItemDatas, int position) {

        detailVH.sub_itemRV.setLayoutManager(new LinearLayoutManager(baseContext.getContext()));
        detailVH.sub_itemRV.setNestedScrollingEnabled(false);
        if (subItemAdapter == null) {
            CheckoutSubItemAdapter subItemAdapter = new CheckoutSubItemAdapter(baseContext, subItemDatas);
            detailVH.sub_itemRV.setAdapter(subItemAdapter);
        } else {
            subItemAdapter.notifyDataSetChanged();
        }


    }


    private void setUpAddressViewPager(final ViewPager viewPager, final TabLayout tabLayout) {

        boolean isShippingFragment = true;
        addressPagerAdapter = new BaseViewPagerStateAdapter(baseContext.getChildFragmentManager(), context);
        addressPagerAdapter.addFragment(AddressCheckoutTabFragment.newInstance(!isShippingFragment, billingAddDetails, selectedBillingAddIndex), context.getString(R.string.billing_address));
        addressPagerAdapter.addFragment(AddressCheckoutTabFragment.newInstance(isShippingFragment, shippingAddDetails, selectedShippingAddIndex), context.getString(R.string.shipping_address));
        viewPager.setAdapter(addressPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (ViewCompat.isLaidOut(tabLayout)) {
            setViewPagerListener(viewPager, tabLayout);
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    setViewPagerListener(viewPager, tabLayout);
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }
    }


    private void setupViewPager(final ViewPager viewPager, final TabLayout tabLayout) {

        PaymentData payments = mCheckoutData.payment;
        SummaryData summaryData = mCheckoutData.summary;
        int amount = summaryData.grandTotal;


//        if (pagerAdapter == null) {
        pagerAdapter = new BaseViewPagerStateAdapter(baseContext.getChildFragmentManager(), context);
        if (payments.available.contains("cod"))
            pagerAdapter.addFragment(CODFragment.newInstance(payments.cod), context.getString(R.string.cod));
      /*  if (payments.available.contains("prepaid"))
            pagerAdapter.addFragment(PrepaidFragment.newInstance(payments.prepaid), context.getString(R.string.prepaid));*/
        if (payments.available.contains("fin"))
            pagerAdapter.addFragment(CreditFragment.newInstance(payments.credit), context.getString(R.string.credit));
//        }

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (ViewCompat.isLaidOut(tabLayout)) {
            setViewPagerListener(viewPager, tabLayout);
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    setViewPagerListener(viewPager, tabLayout);
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    private void setViewPagerListener(final ViewPager viewPager, TabLayout tabLayout) {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }


    public void updateData(CartCheckoutBaseData baseData) {
        mCheckoutData = baseData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return HolderType.VIEW_PAY_PROGRESSBAR.ordinal();
        }
        if (position == 1) {
            return HolderType.VIEW_ADDRESS_PAGER.ordinal();
        }
//        if (position == 2) {
//            return VIEW_BILLING_ADDRESS.ordinal();
//        }
        if (position == getItemList().size() + 2) {
            return HolderType.VIEW_PAY_PROMOTION_CODE.ordinal();
        }
        if (position == getItemList().size() + 3) {
            return HolderType.VIEW_PAY_SHIPPING_TYPE.ordinal();
        }
        if (position == getItemList().size() + 4) {
            return HolderType.VIEW_PAY_ORDER_DETAIL.ordinal();
        }
        if (position == getItemList().size() + 5) {
            return HolderType.VIEW_PAY_OPTION.ordinal();
        }

        if (position == getItemList().size() + 6) {
            return HolderType.VIEW_PAY_CALL.ordinal();
        }
        return HolderType.VIEW_PAY_PRODUCT_DETAIL.ordinal();
    }

    private List<ItemsData> getItemList() {
        return mCheckoutData.list;
    }

    @Override
    public int getItemCount() {
        return getItemList().size() + 6;
    }

    class PayProgressVH extends BaseViewHolder {

        @BindView(R.id.firstLineView)
        protected View firstLineView;
        @BindView(R.id.secondLineView)
        protected View secondLineView;

        @BindView(R.id.secondIV)
        protected ImageView secondIV;
        @BindView(R.id.thirdIV)
        protected ImageView thirdIV;


        PayProgressVH(View itemView) {
            super(itemView);
        }
    }


    class BillingAddressVH extends BaseViewHolder {

        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.addressTV)
        TextView addressTV;
        @BindView(R.id.landmarkTV)
        TextView landmarkTV;
        @BindView(R.id.pincodeTV)
        TextView pincodeTV;
        @BindView(R.id.mobileTV)
        TextView mobileTV;
        @BindView(R.id.billingAddressTV)
        TextView billingAddressTV;

        @BindView(R.id.gstnTV)
        TextView gstnTV;

        BillingAddressVH(View itemView) {
            super(itemView);
        }
    }

    class AddressCardVH extends BaseViewHolder {

        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.addressTV)
        TextView addressTV;
        @BindView(R.id.landmarkTV)
        TextView landmarkTV;
        @BindView(R.id.pincodeTV)
        TextView pincodeTV;
        @BindView(R.id.mobileTV)
        TextView mobileTV;

        @BindView(R.id.gstnTV)
        TextView gstnTV;

        @BindView(R.id.changeAddressTV)
        TextView changeAddressTV;

        AddressCardVH(View itemView) {
            super(itemView);
        }
    }


    class PayAddressVH extends BaseViewHolder {

        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.addressTV)
        TextView addressTV;
        @BindView(R.id.landmarkTV)
        TextView landmarkTV;
        @BindView(R.id.pincodeTV)
        TextView pincodeTV;
        @BindView(R.id.mobileTV)
        TextView mobileTV;
        @BindView(R.id.change_delivery_addressTV)
        TextView change_delivery_addressTV;

        PayAddressVH(View itemView) {
            super(itemView);
        }
    }

    class PayProductDetailVH extends BaseViewHolder {

        @BindView(R.id.order_reviewTV)
        TextView order_reviewTV;
        @BindView(R.id.view_detailTV)
        TextView view_detailTV;
        @BindView(R.id.viewDetailLL)
        LinearLayout viewDetailLL;
        @BindView(R.id.product_name_cartTV)
        TextView product_name_cartTV;
        @BindView(R.id.discountPriceTv)
        TextView discountPriceTv;
        @BindView(R.id.marginTV)
        TextView marginTV;
        @BindView(R.id.qtyTV)
        TextView qtyTV;
        @BindView(R.id.deliveryTimeTV)
        TextView deliveryTimeTV;
        @BindView(R.id.effect_amount_valueTV)
        TextView effect_amount_valueTV;
        @BindView(R.id.seller_discount_amountTV)
        TextView seller_discount_amountTV;
        @BindView(R.id.seller_discountTV)
        TextView seller_discountTV;
        @BindView(R.id.taxTV)
        TextView taxTV;
        @BindView(R.id.taxValueTV)
        TextView taxValueTV;
        @BindView(R.id.cst_refundTV)
        TextView cst_refundTV;
        @BindView(R.id.cst_refund_amountTV)
        TextView cst_refund_amountTV;
        @BindView(R.id.total_amountTV)
        TextView total_amountTV;
        @BindView(R.id.product_cartIV)
        NetworkImageView product_cartIV;
        @BindView(R.id.sub_itemRV)
        RecyclerView sub_itemRV;
        @BindView(R.id.viewErr)
        LinearLayout viewErr;
        @BindView(R.id.errTV)
        TextView errTV;
        @BindView(R.id.sampleItemTV)
        TextView sampleItemTV;
        @BindView(R.id.refurbishIcon)
        ImageView refurbishIcon;

        @BindView(R.id.cessTV)
        protected TextView cessTV;

        @BindView(R.id.cessValueTV)
        protected TextView cessValueTV;

        PayProductDetailVH(View itemView) {
            super(itemView);
        }
    }

    class PayPromoCodeVH extends BaseViewHolder {

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


        PayPromoCodeVH(View itemView) {
            super(itemView);
        }
    }

    class PaySummaryTypeVH extends BaseViewHolder {

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

        PaySummaryTypeVH(View itemView) {
            super(itemView);
        }
    }

    class PayOrderDetailVH extends BaseViewHolder {

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

        PayOrderDetailVH(View itemView) {
            super(itemView);
        }
    }

    class PaymentOptionVH extends BaseViewHolder {

        @BindView(R.id.viewPager2)
        ViewPager viewPager;
        @BindView(R.id.payment_tabLayout)
        TabLayout tabLayout;

        PaymentOptionVH(View itemView) {
            super(itemView);
        }
    }

    class AddressOptionVH extends BaseViewHolder {

        @BindView(R.id.viewPager)
        ViewPager viewPager;
        @BindView(R.id.tabLayout)
        TabLayout tabLayout;

        AddressOptionVH(View itemView) {
            super(itemView);
        }
    }

    class PaymentCallVH extends BaseViewHolder {

        @BindView(R.id.callRL)
        LinearLayout callRL;
        @BindView(R.id.callIV)
        ImageView callIV;
        @BindView(R.id.mobileNumberTV)
        protected TextView mobileNumberTV;


        PaymentCallVH(View itemView) {
            super(itemView);
        }
    }

}
