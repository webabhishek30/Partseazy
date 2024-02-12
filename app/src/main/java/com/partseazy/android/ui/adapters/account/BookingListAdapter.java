package com.partseazy.android.ui.adapters.account;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Application.PartsEazyApplication;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.account.BookingDetailFragment;
import com.partseazy.android.ui.model.deal.bookingorder.BookingData;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;


/**
 * Created by naveen on 6/6/17.
 */

public class BookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<BookingData> orderList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;



    public BookingListAdapter(Context context, List<BookingData> orderList) {
        this.orderList = orderList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(this.context).getImageLoader();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(mInflater.inflate(R.layout.row_booking_order_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        final BookingData bookingData = orderList.get(position);

        orderViewHolder.orderNumberTV.setText(PartsEazyApplication.getInstance().getString(R.string.booking_id, bookingData.bkin));

        String formatedURL = CommonUtility.getFormattedImageUrl(bookingData.image, orderViewHolder.productIconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
        CommonUtility.setImageSRC(imageLoader, orderViewHolder.productIconIV, formatedURL);
        orderViewHolder.dealNameTV.setText(bookingData.name);

        orderViewHolder.skuPriceTV.setText(context.getString(R.string.rs,CommonUtility.convertionPaiseToRupee(bookingData.price)));

        orderViewHolder.orderedDateTV.setText(context.getString(R.string.booking_on_date, CommonUtility.getEMMMddyyyyHHmmDateFromDateCreated(bookingData.createdAt)));


        orderViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookingDetailFragment bookingDetailFragment = BookingDetailFragment.newInstance(bookingData.bkin, true,false);
                BaseFragment.addToBackStack((BaseActivity) context,bookingDetailFragment,bookingDetailFragment.getTag());

            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends BaseViewHolder {

        @BindView(R.id.cardView)
        protected CardView cardView;

        @BindView(R.id.orderNumberTV)
        protected TextView orderNumberTV;

        @BindView(R.id.detailTV)
        protected TextView detailTV;

        @BindView(R.id.productIconIV)
        protected NetworkImageView productIconIV;

        @BindView(R.id.dealNameTV)
        protected TextView dealNameTV;


        @BindView(R.id.skuPriceTV)
        protected TextView skuPriceTV;

        @BindView(R.id.orderedDateTV)
        protected TextView orderedDateTV;




        public OrderViewHolder(View view) {
            super(view);

        }
    }
}
