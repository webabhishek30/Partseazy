package com.partseazy.android.ui.adapters.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.home.HomeFragment;
import com.partseazy.android.ui.model.home.usershop.L2CategoryData;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 31/1/17.
 */

public class NavL2CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<L2CategoryData> l2CategoryList;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private DrawerL2CallBack mDrawerL2CallBack;


    public NavL2CategoryAdapter(Context context, List<L2CategoryData> l2CategoryList, DrawerL2CallBack drawerL2CallBack) {
        this.l2CategoryList = l2CategoryList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        mDrawerL2CallBack = drawerL2CallBack;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(mInflater.inflate(R.layout.row_nav_l2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        L2CategoryData l2CategoryData = l2CategoryList.get(position);
        categoryViewHolder.l3NameTV.setText(l2CategoryData.name);
//        categoryViewHolder.iconIV.setImageUrl(CommonUtility.getFormattedImageUrl(l2CategoryData.name),imageLoader);
        CustomLogger.d("imageiCon ::"+l2CategoryData.icon);
        String formatedURL = CommonUtility.getFormattedImageUrl(l2CategoryData.imageUrl, categoryViewHolder.iconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
        CommonUtility.setImageSRC(imageLoader, categoryViewHolder.iconIV, formatedURL);

        categoryViewHolder.l3CategoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // BaseFragment fragment = CatalogueFragment.newInstance(l2CategoryList.get(position).l3CategoryDataList.get(0).id);
                BaseFragment fragment = HomeFragment.newInstance(position);
                mDrawerL2CallBack.launchFragmentByL2Nav(fragment, fragment.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return l2CategoryList.size();
    }

    class CategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.l3NameTV)
        protected TextView l3NameTV;
        @BindView(R.id.l3CategoryLL)
        protected LinearLayout l3CategoryLL;
        @BindView(R.id.iconIV)
        protected NetworkImageView iconIV;

        public CategoryViewHolder(View view) {
            super(view);

        }
    }

    public interface DrawerL2CallBack {

        public void launchFragmentByL2Nav(BaseFragment fragment, String tag);

    }
}