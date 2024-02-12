package com.partseazy.android.ui.adapters.checkout;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.adapters.cart.CartAdapter;
import com.partseazy.android.ui.model.cart_checkout.SubItemData;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CAN Solutions on 1/6/2017.
 */

public class CheckoutSubItemAdapter extends RecyclerView.Adapter<CheckoutSubItemAdapter.CheckoutSubItemAdapterVH> {

    private BaseFragment mContext;
    private List<SubItemData> mSubItemDatas;

    private ImageLoader imageLoader;

    public CheckoutSubItemAdapter(BaseFragment context, List<SubItemData> subItemDatas) {
        mContext = context;
        mSubItemDatas = subItemDatas;
        imageLoader = ImageManager.getInstance(mContext.getContext()).getImageLoader();
    }

    @Override
    public CheckoutSubItemAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CheckoutSubItemAdapterVH(inflater.inflate(R.layout.row_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckoutSubItemAdapterVH holder, int position) {
        SubItemData data = mSubItemDatas.get(position);
        holder.item_attributeTV.setText(mContext.getString(R.string.sub_item_name, data.attr, data.qty));
        holder.item_priceTV.setText(Html.fromHtml(mContext.getString(R.string.rs_str, CommonUtility.convertionPaiseToRupeeStringHTML(data.figures.pricing))));
       // holder.item_priceTV.setText(mContext.getString(R.string.rs, convertionPaiseToRupee(data.figures.priceEff)));

//        String imageURL = CommonUtility.getFormattedImageUrl(data.image);
//        holder.sub_item_cartIV.setImageUrl(imageURL, imageLoader);

        String formatedURL = CommonUtility.getFormattedImageUrl(data.image, holder.sub_item_cartIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
        CommonUtility.setImageSRC(imageLoader, holder.sub_item_cartIV, formatedURL);



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
    }

    @Override
    public int getItemCount() {
        return mSubItemDatas.size();
    }

    class CheckoutSubItemAdapterVH extends BaseViewHolder {

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
        @BindView(R.id.errorcartTV)
        TextView errTV;


        public CheckoutSubItemAdapterVH(View itemView) {
            super(itemView);
        }
    }


}
