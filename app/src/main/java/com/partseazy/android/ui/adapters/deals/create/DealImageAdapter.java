package com.partseazy.android.ui.adapters.deals.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.FileData;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;


/**
 * Created by naveen on 9/5/17.
 */

public class DealImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<FileData> itemList;
    private LayoutInflater mInflater;
    private ImageLoader  imageLoader;

    public DealImageAdapter(Context context, List<FileData> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(context);
        this.imageLoader = ImageManager.getInstance(context).getImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

            case VIEW_DEAL_NEW_PIC:
                return new ImageVH(mInflater.inflate(R.layout.row_image_item, parent, false));
            case VIEW_DEAL_NEW_PIC_ICON:
                return new UploadIconVH(mInflater.inflate(R.layout.row_upload_image_item, parent, false));
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

       if(holder instanceof  UploadIconVH)
       {
           UploadIconVH uploadIconVH =(UploadIconVH)holder;

           uploadIconVH.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_UPLOAD_PIC_EVENT);
               }
           });


       }

       if(holder instanceof  ImageVH) {


           final ImageVH imageVH = (ImageVH) holder;
           FileData fileData = itemList.get(position);

           String formatedURL = CommonUtility.getFormattedImageUrl(fileData.src, imageVH.iconIV, CommonUtility.IMGTYPE.THUMBNAILIMG);
           CustomLogger.d("Image url :: " + formatedURL);
           CommonUtility.setImageSRC(imageLoader, imageVH.iconIV, formatedURL);

           imageVH.crossIV.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   PartsEazyEventBus.getInstance().postEvent(EventConstant.DEAL_PRODUCT_IMAGE, position);
               }
           });

       }


    }

    @Override
    public int getItemCount() {
        return itemList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == itemList.size()) {
            return HolderType.VIEW_DEAL_NEW_PIC_ICON.ordinal();
        }
        else {
            return HolderType.VIEW_DEAL_NEW_PIC.ordinal();
        }
    }


    public class ImageVH extends BaseViewHolder {
        @BindView(R.id.iconIV)
        protected NetworkImageView iconIV;

        @BindView(R.id.crossIV)
        protected ImageView crossIV;


        public ImageVH(View itemView) {
            super(itemView);
        }
    }

    public class UploadIconVH extends BaseViewHolder {

        public UploadIconVH(View itemView) {
            super(itemView);
        }
    }



}
