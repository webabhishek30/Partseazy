package com.partseazy.android.ui.adapters.deals.booking;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.deals.DealConstants;
import com.partseazy.android.ui.fragments.shippingaddress.ShippingAddressFragment;
import com.partseazy.android.ui.model.deal.FinalDealSKU;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.shippingaddress.ShippingAddressDetail;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.google.gson.Gson;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 1/6/17.
 */

public class BookingDealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private int mItemType = 4;
    private BookSkuItemAdapter bookSkuItemAdapter;
    private Deal dealHolder;
    private Map<Integer, FinalDealSKU> skuMap;
    private ImageLoader imageLoader;

    private int totalPrice=0;
    private boolean isShippingAdded = true;
    private ShippingAddressDetail addressDetail;

    public BookingDealAdapter(Context context, Deal deal, Map<Integer,FinalDealSKU> skuMap, ShippingAddressDetail addressDetail) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.dealHolder = deal;
        this.skuMap = skuMap;
        this.addressDetail = addressDetail;
        imageLoader = ImageManager.getInstance(context).getImageLoader();

        totalPrice = getTotalSKUPrices();
        if(dealHolder.trade.shipMethods.size()>0 && dealHolder.trade.shipMethods.contains(DealConstants.DROP_SHIP))  {
            isShippingAdded = true;
        }else{
            isShippingAdded= false;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType holderType = HolderType.values()[viewType];

        switch (holderType) {
            case VIEW_BOOKING_PROGRESS:
                return new ProgressVH(layoutInflater.inflate(R.layout.row_booking_deal_progressbar, parent, false));
            case VIEW_BOOKING_ADDRESS:
                return new AddressVH(layoutInflater.inflate(R.layout.row_payment_delivery_address, parent, false));
            case VIEW_BOOKING_SUMMARY:
                return new SummaryVH(layoutInflater.inflate(R.layout.row_booking_summary_item, parent, false));
            case VIEW_BOKKING_SHIPPING:
                return new ShippingVH(layoutInflater.inflate(R.layout.row_book_shipping_item,parent,false));


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProgressVH) {
            ProgressVH progressVH = (ProgressVH) holder;
            setProgress(progressVH);
        }

        if(holder instanceof AddressVH)
        {
            AddressVH addressVH = (AddressVH)holder;

            if(addressDetail!=null) {

                addressVH.delivery_addressTV.setText(context.getString(R.string.delivery_address));
                addressVH.nameTV.setText(addressDetail.name);
                addressVH.addressTV.setText(addressDetail.street);

                addressVH.pincodeTV.setText(addressDetail.city + ", " + addressDetail.state + " - " + addressDetail.pincode);
                addressVH.pincodeTV.setVisibility(View.VISIBLE);

                if (addressDetail.landmark != null) {
                    addressVH.landmarkTV.setVisibility(View.VISIBLE);
                    addressVH.landmarkTV.setText(context.getString(R.string.shipping_landmark, addressDetail.landmark));
                } else
                    addressVH.landmarkTV.setVisibility(View.GONE);

                addressVH.mobileTV.setVisibility(View.GONE);

            }

            addressVH.change_delivery_addressTV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ShippingAddressFragment shippingAddressFragment = ShippingAddressFragment.newInstance(true, new Gson().toJson(dealHolder), new Gson().toJson(skuMap));
                    BaseFragment.removeTopAndAddToBackStack((BaseActivity) context, shippingAddressFragment, shippingAddressFragment.getTagName());
                }
            });


        }

        if(holder instanceof SummaryVH)
        {
            SummaryVH summaryVH = (SummaryVH)holder;

            summaryVH.dealNameTV.setText(dealHolder.trade.name);
            summaryVH.sellerTV.setText(context.getString(R.string.sold_by,dealHolder.user.name));

            summaryVH.shippingTV.setText(context.getString(R.string.rs, CommonUtility.convertionPaiseToRupee(dealHolder.trade.courierFee)));

            summaryVH.totalTV.setText(context.getString(R.string.rs,CommonUtility.convertionPaiseToRupee(totalPrice)));

            if(dealHolder.trade.courierFee>0 && isShippingAdded) {
                int totalSum = totalPrice+dealHolder.trade.courierFee;
                summaryVH.totalTV.setText(context.getString(R.string.rs,CommonUtility.convertionPaiseToRupee(totalSum)));
                summaryVH.shippingRL.setVisibility(View.VISIBLE);

            }else{
                summaryVH.shippingRL.setVisibility(View.GONE);
            }



            String formatedURL = dealHolder.trade.images.get(0).src;

            formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, summaryVH.productIV, CommonUtility.IMGTYPE.QUARTERIMG);
            CustomLogger.d("formattedUrl ::" + formatedURL);
            CommonUtility.setImageSRC(imageLoader, summaryVH.productIV, formatedURL);

            summaryVH.skuRV.setLayoutManager(new LinearLayoutManager(context));
            bookSkuItemAdapter = new BookSkuItemAdapter(context,dealHolder.skus,skuMap);
            summaryVH.skuRV.setAdapter(bookSkuItemAdapter);
            summaryVH.skuRV.setNestedScrollingEnabled(false);
        }

        if(holder instanceof ShippingVH)
        {
            final ShippingVH shippingVH = (ShippingVH)holder;


            shippingVH.shippingChargeTV.setText(context.getString(R.string.shipping_charge_extra,CommonUtility.convertionPaiseToRupee(dealHolder.trade.courierFee)));
            shippingVH.addressTV.setText((dealHolder.address.street + ", " + dealHolder.address.city + ", " + dealHolder.address.state + " - " + dealHolder.address.pincode));

            shippingVH.pickupRB.setEnabled(false);
            shippingVH.deliverRB.setEnabled(false);
            shippingVH.pickupAddressLL.setVisibility(View.GONE);
            shippingVH.shippingChargeTV.setVisibility(View.GONE);



            if(dealHolder.trade.shipMethods.size()>0 && dealHolder.trade.shipMethods.contains(DealConstants.DROP_SHIP))
            {
                shippingVH.deliverRB.setEnabled(true);
                shippingVH.deliverRB.setChecked(true);
                shippingVH.shippingChargeTV.setVisibility(View.VISIBLE);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DROP_SHIP,DealConstants.DROP_SHIP);
                if(dealHolder.trade.shipMethods.size()==2)
                {
                    shippingVH.pickupRB.setEnabled(true);
                }
            }else{
                shippingVH.deliverRB.setEnabled(false);
                shippingVH.pickupRB.setEnabled(true);
                shippingVH.pickupRB.setChecked(true);
                shippingVH.pickupAddressLL.setVisibility(View.VISIBLE);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DROP_SHIP,DealConstants.PICK_UP);

            }


            shippingVH.pickupRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shippingVH.deliverRB.setChecked(!shippingVH.pickupRB.isChecked());
                    if(shippingVH.pickupRB.isChecked())
                    {
                        shippingVH.pickupAddressLL.setVisibility(View.VISIBLE);
                        shippingVH.shippingChargeTV.setVisibility(View.GONE);
                        updateTotalPrice(false);
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_PICK_UP,DealConstants.PICK_UP);
                    }else{
                        shippingVH.pickupAddressLL.setVisibility(View.GONE);
                    }
                }
            });

            shippingVH.deliverRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shippingVH.pickupRB.setChecked(!shippingVH.deliverRB.isChecked());
                    if(shippingVH.deliverRB.isChecked())
                    {
                        updateTotalPrice(true);
                        shippingVH.shippingChargeTV.setVisibility(View.VISIBLE);
                        shippingVH.pickupAddressLL.setVisibility(View.GONE);
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECT_DROP_SHIP,DealConstants.DROP_SHIP);
                    }else{
                        shippingVH.shippingChargeTV.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mItemType;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return HolderType.VIEW_BOOKING_PROGRESS.ordinal();
        }
        if (position == 1) {
            return HolderType.VIEW_BOOKING_ADDRESS.ordinal();
        }
        if(position==2)
        {
            return HolderType.VIEW_BOKKING_SHIPPING.ordinal();
        }
        if(position==3)
        {
            return  HolderType.VIEW_BOOKING_SUMMARY.ordinal();
        }
        return 0;
    }

    public class ProgressVH extends BaseViewHolder {

        @BindView(R.id.firstCheckIV)
        protected ImageView firstCheckIV;

        @BindView(R.id.secondCheckIV)
        protected ImageView secondCheckIV;

        @BindView(R.id.thirdCheckIV)
        protected ImageView thirdCheckIV;

        @BindView(R.id.firstLineView)
        protected View firstLineView;

        @BindView(R.id.secondLineView)
        protected View secondLineView;


        public ProgressVH(View itemView) {
            super(itemView);
        }
    }

    public class AddressVH extends BaseViewHolder {

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

        @BindView(R.id.delivery_addressTV)
        protected TextView delivery_addressTV;



        public AddressVH(View itemView) {
            super(itemView);
        }
    }

    public class SummaryVH extends BaseViewHolder {





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



        public SummaryVH(View view) {
            super(view);
        }
    }

    public class ShippingVH extends BaseViewHolder{

        @BindView(R.id.deliverRB)
        protected RadioButton deliverRB;

        @BindView(R.id.pickupRB)
        protected RadioButton pickupRB;

        @BindView(R.id.pickupAddressLL)
        protected LinearLayout pickupAddressLL;

        @BindView(R.id.shippingChargeTV)
        protected  TextView shippingChargeTV;


        @BindView(R.id.addressTV)
        protected TextView addressTV;


        public ShippingVH(View view)
        {
            super(view);
        }
    }


    private void setProgress(ProgressVH progressVH) {
        progressVH.firstCheckIV.setImageResource(R.drawable.check_green_circle);
        progressVH.secondCheckIV.setImageResource(R.drawable.check_green_circle);
        progressVH.firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
    }




    public int getTotalSKUPrices()
    {
        int total =0;
        for (Map.Entry<Integer, FinalDealSKU> finalSkuMap : skuMap.entrySet()) {
            FinalDealSKU finalSku = finalSkuMap.getValue();
            total+=(finalSku.price*finalSku.selectedQty);
        }
        return total;

    }

    private void updateTotalPrice(boolean addShippingCharge)
    {

        isShippingAdded = addShippingCharge;
        if(addShippingCharge)
        {
            totalPrice = getTotalSKUPrices();
            totalPrice = totalPrice+dealHolder.trade.courierFee;
        }else{
            totalPrice = getTotalSKUPrices();
        }
        notifyItemChanged(3);

    }

    public int getTotalAmount()
    {
        return  totalPrice;
    }


}

