package com.partseazy.android.ui.adapters.registration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.categorylevelone.Child_;
import com.partseazy.android.ui.widget.CircularCheckedImageView;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 8/12/16.
 */

public class RegisterSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private final Context mContext;
    private List<Child_> subcategoryList;
    private ImageLoader imageLoader;
    private int parentPosition;
    private boolean isChecked;


    public RegisterSubCategoryAdapter(Context context, List<Child_> subcategoryList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.subcategoryList = subcategoryList;
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        CustomLogger.d("calling selection construcot" + subcategoryList.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryViewHolder(mInflater.inflate(R.layout.row_circular_category_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        SubCategoryViewHolder subCategoryViewHolder = (SubCategoryViewHolder) viewHolder;
        Child_ subcategory = subcategoryList.get(position);
        populateAdapter(subCategoryViewHolder, subcategory);

    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    class SubCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.subCatNameTV)
        public TextView subCatNameTV;
        @BindView(R.id.subCategoryIcon)
        public CircularCheckedImageView imageView;

        public SubCategoryViewHolder(View view) {
            super(view);
        }
    }

    public void populateAdapter(final SubCategoryViewHolder subCategoryViewHolder, Child_ subCategory) {
        subCategoryViewHolder.subCatNameTV.setText(subCategory.name);
        isChecked = subCategoryViewHolder.imageView.getChecked();
        final int subCategoryId = subCategory.id;
        final CircularImageView circularImageView = subCategoryViewHolder.imageView.getCircularImageView(isChecked);

        if (subCategory.image != null && subCategory.image.src != null) {

            String formatedURL = CommonUtility.getFormattedImageUrl(subCategory.image.src, circularImageView, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, circularImageView, formatedURL);


        }

        subCategoryViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_ID, subCategoryId);
                if (subCategoryViewHolder.imageView.getChecked()) {
                    subCategoryViewHolder.imageView.setChecked(false);
                    subCategoryViewHolder.imageView.setCircularImageViewSelected(false);
                } else {
                    subCategoryViewHolder.imageView.setChecked(true);
                    subCategoryViewHolder.imageView.setCircularImageViewSelected(true);
                }
            }
        });
    }


}
