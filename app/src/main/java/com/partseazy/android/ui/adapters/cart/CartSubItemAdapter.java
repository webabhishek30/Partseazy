package com.partseazy.android.ui.adapters.cart;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.cart_checkout.ItemsData;
import com.partseazy.android.ui.model.cart_checkout.SubItemData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CAN Solutions on 1/8/2017.
 */

public class CartSubItemAdapter extends RecyclerView.Adapter<CartSubItemAdapter.CartSubItemAdapterVH> {


    private BaseFragment mContext;
    private List<SubItemData> mSubItemDatas;
    private ImageLoader imageLoader;
    private int minQty, maxQty, totalQty = 0;
    private int parentPosition;
    private ItemsData mItemData;
    private RecyclerView mRecyclerView;
    private boolean isSample = false;
    private boolean isThereRowLevelError;

    public CartSubItemAdapter(BaseFragment context, int position, ItemsData itemsData, RecyclerView recyclerView, boolean isSample, int qty) {
        mContext = context;
        this.parentPosition = position;
        this.totalQty = qty;
        this.minQty = itemsData.minQty;
        this.maxQty = itemsData.maxQty;
        this.isSample = isSample;
        mSubItemDatas = itemsData.rows;
        mItemData = itemsData;
        imageLoader = ImageManager.getInstance(mContext.getContext()).getImageLoader();
        mRecyclerView = recyclerView;
    }

    @Override
    public CartSubItemAdapter.CartSubItemAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CartSubItemAdapter.CartSubItemAdapterVH(inflater.inflate(R.layout.row_cart_sub_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final CartSubItemAdapter.CartSubItemAdapterVH holder, final int position) {

        final SubItemData data = mSubItemDatas.get(position);
        holder.item_attributeTV.setText(mContext.getString(R.string.item_name, data.attr));
        //holder.item_priceTV.setText(mContext.getString(R.string.rs, convertionPaiseToRupee(data.figures.priceEff)));
        holder.item_priceTV.setText(Html.fromHtml(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeStringHTML(data.figures.pricing))));
        if (data.image != null) {
//            String imageURL = CommonUtility.getFormattedImageUrl(data.image);
//            holder.sub_item_cartIV.setImageUrl(imageURL, imageLoader);
            String formatedURL = CommonUtility.getFormattedImageUrl(data.image, holder.sub_item_cartIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, holder.sub_item_cartIV, formatedURL);


        }
        holder.item_qtyTV.setText(data.qty + "");
        holder.increase_item_qtyTV.setTag(position);
        holder.decrease_item_qtyTV.setTag(position);
        holder.item_qtyTV.setTag(position);


//        if (data.ok) {
//
//            isThereRowLevelError = false;
//
//            if (data.isThereAnyErrorInSubItem != null && data.isThereAnyErrorInSubItem) {
//                holder.errTV.setVisibility(View.VISIBLE);
//                holder.errTV.setText(data.errortSubItem);
//            } else {
//                holder.errTV.setVisibility(View.GONE);
//            }
//
//            if (data.stock <= data.qty) {
//                String msg = StaticMap.cart_er_maxQty.replace("***", "" + data.stock);
//                holder.errTV.setVisibility(View.VISIBLE);
//                holder.errTV.setText(msg);
//                isThereRowLevelError = true;
//            }
//
//
//        } else {
//
//            holder.errTV.setVisibility(View.VISIBLE);
//
//            if (!data.isActive)
//                holder.errTV.setText(StaticMap.cart_er_isActive);
//            else if (!data.inStock)
//                holder.errTV.setText(StaticMap.cart_er_inStock);
//            else if (!data.isServicable) {
//                holder.errTV.setText(StaticMap.cart_er_isServicable);
//                holder.errTV.setVisibility(View.GONE);
//
//            }
//
//        }


        if (data.ok) {
            holder.errTV.setVisibility(View.GONE);

            if (data.isThereAnyErrorInSubItem != null && data.isThereAnyErrorInSubItem) {
                holder.errTV.setVisibility(View.VISIBLE);
                holder.errTV.setText(data.errortSubItem);
            } else {
                holder.errTV.setVisibility(View.GONE);
            }

        } else {

            if (data.error_type != null && !TextUtils.isEmpty(data.error_type)) {
                holder.errTV.setVisibility(View.VISIBLE);

                if (data.error_type.equals(CartAdapter.ERROR_TYPE.ACTIVE.error)) {
                    holder.errTV.setText(StaticMap.cart_er_isActive);

                } else if (data.error_type.equals(CartAdapter.ERROR_TYPE.SERVICEABLE.error)) {
                    holder.errTV.setText(StaticMap.cart_er_isServicable);

                } else if (data.error_type.equals(CartAdapter.ERROR_TYPE.STOCK.error)) {
                    holder.errTV.setText(StaticMap.cart_er_inStock);

                } else if (data.error_type.equals(CartAdapter.ERROR_TYPE.MOQ.error)) {
                    String msg = StaticMap.cart_er_minQty.replace("***", "" + data.minQty);
                    holder.errTV.setText(msg);
                } else if (data.error_type.equals(CartAdapter.ERROR_TYPE.MAX.error)) {
                    String msg = StaticMap.cart_er_maxQty.replace("***", "" + data.stock);
                    holder.errTV.setText(msg);
                } else {
                    holder.errTV.setText(mContext.getString(R.string.err_somthin_wrong));
                }

            }
        }

        holder.increase_item_qtyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Manual handling of MOQ and MAX onclick

                int quantity = data.qty;


                if (maxQty <= totalQty ) {
                    String msg = StaticMap.cart_er_maxQty.replace("***", "" + maxQty);
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ERROR, parentPosition - 1, msg);
                    return;
                }


                if (data.stock <= data.qty) {
                    String msg = StaticMap.cart_er_maxQty.replace("***", "" + data.stock);
                    data.isThereAnyErrorInSubItem = true;
                    data.errortSubItem = msg;
                    notifyItemChanged(position);

                } else {
                    quantity = quantity + 1;
                    int productId = data.cartItem.productSkuId;
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ITEM_QUANTITY, productId, quantity, false, parentPosition);
                }
            }
        });

        holder.decrease_item_qtyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (minQty >= totalQty) {
                    String msg = StaticMap.cart_er_minQty.replace("***", "" + minQty);
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ERROR, parentPosition - 1, msg);
                    return;
                }

                if (data.qty < data.minQty) {
                    String msg = StaticMap.cart_er_minQty.replace("***", "" + data.minQty);
                    data.isThereAnyErrorInSubItem = true;
                    data.errortSubItem = msg;
                    notifyItemChanged(position);

                } else {
                    int quantity = data.qty;
                    int productId = data.cartItem.productSkuId;
                    if (quantity >= 0) {
                        quantity = quantity - 1;
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.CART_ITEM_QUANTITY, productId, quantity, false, parentPosition);
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSubItemDatas.size();
    }


    class CartSubItemAdapterVH extends BaseViewHolder {

        @BindView(R.id.sub_item_cartIV)
        NetworkImageView sub_item_cartIV;
        @BindView(R.id.item_attributeTV)
        TextView item_attributeTV;
        @BindView(R.id.item_priceTV)
        TextView item_priceTV;
        @BindView(R.id.decrease_item_qtyTV)
        TextView decrease_item_qtyTV;
        @BindView(R.id.increase_item_qtyTV)
        TextView increase_item_qtyTV;
        @BindView(R.id.item_qtyTV)
        TextView item_qtyTV;
        @BindView(R.id.addMinusWidgetRL)
        RelativeLayout addMinusWidgetRL;
        @BindView(R.id.errorcartTV)
        TextView errTV;


        public CartSubItemAdapterVH(View itemView) {
            super(itemView);
        }
    }


//    private void showAlertDialog(int qty, boolean minMax) {
//        String strMsg;
//        if (minMax)
//            strMsg = mContext.getString(R.string.below_qty_msg) + qty + ")";
//        else
//            strMsg = mContext.getString(R.string.above_qty_msg) + qty + ")";
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                mContext.getContext());
//        alertDialogBuilder
//                .setMessage(strMsg)
//                .setCancelable(true)
//                .setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
}
