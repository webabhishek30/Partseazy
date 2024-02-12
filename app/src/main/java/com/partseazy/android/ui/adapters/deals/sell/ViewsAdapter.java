package com.partseazy.android.ui.adapters.deals.sell;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.supplier.shop.ShopDetailFragment;
import com.partseazy.android.ui.model.deal.views.ViewUser;
import com.partseazy.android.utility.ChatUtility;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by shubhang on 06/11/17.
 */

public class ViewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ViewUser> users;
    private BaseFragment baseFragment;

    private ImageLoader imageLoader;

    public ViewsAdapter(BaseFragment baseFragment, List<ViewUser> users) {
        this.users = users;
        mInflater = LayoutInflater.from(baseFragment.getContext());
        imageLoader = ImageManager.getInstance(baseFragment.getContext()).getImageLoader();
        this.baseFragment = baseFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(mInflater.inflate(R.layout.row_user_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        final ViewUser user = users.get(position);

        userViewHolder.shopNameTV.setText(user.shopName);
        userViewHolder.userNameTV.setText(user.userName);

        String formatedURL = CommonUtility.getFormattedImageUrl(user.shopImage, userViewHolder.shopImageIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
        CommonUtility.setImageSRC(imageLoader, userViewHolder.shopImageIV, formatedURL);

        userViewHolder.chatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseFragment.showProgressDialog();
                ChatUtility chat = new ChatUtility(baseFragment, user.userID, user.userName);
                chat.startChatting();
            }
        });

        if (user.profileAvailable == 1) {
            userViewHolder.profileBTN.setVisibility(View.VISIBLE);
            userViewHolder.profileBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShopDetailFragment shopDetailFragment = ShopDetailFragment.newInstance(user.shopID, user.shopName);
                    BaseFragment.addToBackStack(baseFragment.getContext(), shopDetailFragment, shopDetailFragment.getTag());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends BaseViewHolder {

        @BindView(R.id.shopNameTV)
        protected TextView shopNameTV;

        @BindView(R.id.userNameTV)
        protected TextView userNameTV;

        @BindView(R.id.shopImageIV)
        protected NetworkImageView shopImageIV;

        @BindView(R.id.profileBTN)
        protected Button profileBTN;

        @BindView(R.id.chatBTN)
        protected Button chatBTN;

        public UserViewHolder(View view) {
            super(view);

        }
    }
}
