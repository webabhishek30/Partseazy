package com.partseazy.android.ui.adapters.registration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.categoryleveltwo.SubCategory;
import com.partseazy.android.ui.widget.CircularCheckedImageView;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.ui.widget.RectangularCheckedBox;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 16/12/16.
 */

public class SubCategoryPriceSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private final Context mContext;
    private List<SubCategory> subcategoryList;
    private ImageLoader imageLoader;
    private boolean isSelected = false;
    private OnUnSelectAllSubCatListener listener;
    private int position;
    private int l2Id;

    public interface OnUnSelectAllSubCatListener {
        void onUnselectAllSubCat(int l2CatId,int position);

    }


    public SubCategoryPriceSelectionAdapter(Context context, List<SubCategory> subcategoryList,OnUnSelectAllSubCatListener listener,int l2Id,int position) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.subcategoryList = subcategoryList;
        imageLoader = ImageManager.getInstance(context).getImageLoader();
        this.listener = listener;
        this.l2Id = l2Id;
        this.position =position;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryViewHolder(mInflater.inflate(R.layout.row_subcategory_price_range, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        SubCategoryViewHolder subCategoryViewHolder = (SubCategoryViewHolder) viewHolder;
        if (position == subcategoryList.size() - 1)
            subCategoryViewHolder.itemSeparateLine.setVisibility(View.GONE);
        else{
            subCategoryViewHolder.itemSeparateLine.setVisibility(View.VISIBLE);
        }
        SubCategory subCategory = subcategoryList.get(position);
        populateAdapter(subCategoryViewHolder, subCategory);

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
        @BindView(R.id.separateLine)
        public View itemSeparateLine;
        @BindView(R.id.lowCheckBox)
        RectangularCheckedBox lowCheckBox;

        @BindView(R.id.mediumCheckBox)
        RectangularCheckedBox mediumCheckBox;

        @BindView(R.id.highCheckBox)
        RectangularCheckedBox highCheckBox;

        @BindView(R.id.luxuryCheckBox)
        RectangularCheckedBox luxuryCheckBox;


        public SubCategoryViewHolder(View view) {
            super(view);

        }
    }

    public void populateAdapter(final SubCategoryViewHolder subCategoryViewHolder, final SubCategory subCategory) {
        subCategoryViewHolder.subCatNameTV.setText(subCategory.name);
        isSelected = subCategory.isSelected;
        if (isSelected) {
            subCategoryViewHolder.imageView.setChecked(true);
            subCategoryViewHolder.imageView.setCircularImageViewSelected(true);

        } else {
            subCategoryViewHolder.imageView.setChecked(false);
            subCategoryViewHolder.imageView.setCircularImageViewSelected(false);
        }
        initialiseChecks(subCategoryViewHolder, subCategory);
        populatePriceRangeButtons(subCategoryViewHolder, subCategory);
        final CircularImageView circularImageView = subCategoryViewHolder.imageView.getCircularImageView(isSelected);
        if (subCategory.image != null && subCategory.image.src != null) {

            String formatedURL = CommonUtility.getFormattedImageUrl(subCategory.image.src, circularImageView, CommonUtility.IMGTYPE.THUMBNAILIMG);
            CommonUtility.setImageSRC(imageLoader, circularImageView, formatedURL);

        }

        subCategoryViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.imageView.getChecked()) {
                    hideAllItemUnselected(subCategoryViewHolder);
                    subCategoryViewHolder.imageView.setChecked(false);
                    subCategoryViewHolder.imageView.setCircularImageViewSelected(false);
                    subCategory.isSelected = false;
                    unselectAllRange(subCategory);
                    if(isAllUnSelected())
                    {
                        listener.onUnselectAllSubCat(l2Id,position);
                    }
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, false);
                } else {
                    subCategoryViewHolder.imageView.setChecked(true);
                    subCategoryViewHolder.imageView.setCircularImageViewSelected(true);
                    subCategory.isSelected = true;
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

                }

            }
        });


        subCategoryViewHolder.lowCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.lowCheckBox.getChecked()) {
                    subCategoryViewHolder.lowCheckBox.setChecked(false);
                    subCategoryViewHolder.lowCheckBox.setViewSelected(false);
                    subCategory.isLowSelected = false;
                } else {
                    subCategoryViewHolder.lowCheckBox.setChecked(true);
                    subCategoryViewHolder.lowCheckBox.setViewSelected(true);
                    subCategory.isLowSelected = true;
                    subCategory.isSelected = true;
                    showCategoryImageSelected(subCategoryViewHolder);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

            }
        });

        subCategoryViewHolder.mediumCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.mediumCheckBox.getChecked()) {
                    subCategoryViewHolder.mediumCheckBox.setChecked(false);
                    subCategoryViewHolder.mediumCheckBox.setViewSelected(false);
                    subCategory.isMediumSelected = false;
                } else {
                    subCategoryViewHolder.mediumCheckBox.setChecked(true);
                    subCategoryViewHolder.mediumCheckBox.setViewSelected(true);
                    subCategory.isMediumSelected = true;
                    subCategory.isSelected = true;
                    showCategoryImageSelected(subCategoryViewHolder);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

            }
        });

        subCategoryViewHolder.highCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.highCheckBox.getChecked()) {
                    subCategoryViewHolder.highCheckBox.setChecked(false);
                    subCategoryViewHolder.highCheckBox.setViewSelected(false);
                    subCategory.isHighSelected = false;
                } else {
                    subCategoryViewHolder.highCheckBox.setChecked(true);
                    subCategoryViewHolder.highCheckBox.setViewSelected(true);
                    subCategory.isHighSelected = true;
                    subCategory.isSelected = true;
                    showCategoryImageSelected(subCategoryViewHolder);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

            }
        });


        subCategoryViewHolder.highCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.highCheckBox.getChecked()) {
                    subCategoryViewHolder.highCheckBox.setChecked(false);
                    subCategoryViewHolder.highCheckBox.setViewSelected(false);
                    subCategory.isHighSelected = false;
                } else {
                    subCategoryViewHolder.highCheckBox.setChecked(true);
                    subCategoryViewHolder.highCheckBox.setViewSelected(true);
                    subCategory.isHighSelected = true;
                    subCategory.isSelected = true;
                    showCategoryImageSelected(subCategoryViewHolder);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

            }
        });

        subCategoryViewHolder.luxuryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryViewHolder.luxuryCheckBox.getChecked()) {
                    subCategoryViewHolder.luxuryCheckBox.setChecked(false);
                    subCategoryViewHolder.luxuryCheckBox.setViewSelected(false);
                    subCategory.isLuxurySelected = false;
                } else {
                    subCategoryViewHolder.luxuryCheckBox.setChecked(true);
                    subCategoryViewHolder.luxuryCheckBox.setViewSelected(true);
                    subCategory.isLuxurySelected = true;
                    subCategory.isSelected = true;
                    showCategoryImageSelected(subCategoryViewHolder);
                }
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SUBCATEGORY_RANGE_ITEM, subCategory, true);

            }
        });


    }

    public void initialiseChecks(SubCategoryViewHolder viewHolder, SubCategory subModel) {
        if (subModel.isLowSelected) {
            viewHolder.lowCheckBox.setChecked(true);
            viewHolder.lowCheckBox.setViewSelected(true);
        } else {
            ;
            viewHolder.lowCheckBox.setChecked(false);
            viewHolder.lowCheckBox.setViewSelected(false);
        }
        if (subModel.isMediumSelected) {
            viewHolder.mediumCheckBox.setChecked(true);
            viewHolder.mediumCheckBox.setViewSelected(true);
        } else {
            viewHolder.mediumCheckBox.setChecked(false);
            viewHolder.mediumCheckBox.setViewSelected(false);
        }
        if (subModel.isHighSelected) {
            viewHolder.highCheckBox.setChecked(true);
            viewHolder.highCheckBox.setViewSelected(true);
        } else {
            viewHolder.highCheckBox.setChecked(false);
            viewHolder.highCheckBox.setViewSelected(false);
        }
        if (subModel.isLuxurySelected) {
            viewHolder.luxuryCheckBox.setChecked(true);
            viewHolder.luxuryCheckBox.setViewSelected(true);
        } else {
            viewHolder.luxuryCheckBox.setChecked(false);
            viewHolder.luxuryCheckBox.setViewSelected(false);
        }
    }

    public void populatePriceRangeButtons(SubCategoryViewHolder viewHolder, SubCategory category) {
        if (category.info != null) {
            viewHolder.lowCheckBox.setText("Under Rs. " + category.info.segLo);
            viewHolder.mediumCheckBox.setText("Rs. " + category.info.segLo + "-" + category.info.segMd);
            viewHolder.highCheckBox.setText("Rs. " + category.info.segMd + "-" + category.info.segHi);
            viewHolder.luxuryCheckBox.setText("Rs. " + category.info.segHi + " +");
        }
    }

    public void showCategoryImageSelected(SubCategoryViewHolder viewHolder) {
        viewHolder.imageView.setChecked(true);
        viewHolder.imageView.setCircularImageViewSelected(true);
    }


    public void hideAllItemUnselected(SubCategoryViewHolder viewHolder) {
        viewHolder.lowCheckBox.setChecked(false);
        viewHolder.lowCheckBox.setViewSelected(false);
        viewHolder.mediumCheckBox.setChecked(false);
        viewHolder.mediumCheckBox.setViewSelected(false);
        viewHolder.highCheckBox.setChecked(false);
        viewHolder.highCheckBox.setViewSelected(false);
        viewHolder.luxuryCheckBox.setChecked(false);
        viewHolder.luxuryCheckBox.setViewSelected(false);
    }


    public void unselectAllRange(SubCategory submodel) {
        submodel.isLowSelected = false;
        submodel.isHighSelected = false;
        submodel.isMediumSelected = false;
        submodel.isLuxurySelected = false;
    }

    public boolean isAllUnSelected()
    {
        if(subcategoryList!=null && subcategoryList.size()>0)
        {
            for (int i=0;i<subcategoryList.size();i++)
            {
                if(subcategoryList.get(i).isSelected)
                {
                    return false;
                }
            }
        }
        return true;
    }



}
