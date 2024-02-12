package com.applozic.mobicomkit.uiwidgets.conversation.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.model.TagModel;

import java.util.List;

/**
 * Created by naveen on 13/4/17.
 */

public class ChatTagsAdapter extends RecyclerView.Adapter<ChatTagsAdapter.TagViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<TagModel> tagList;
    private OnClickTagListener listener;

    public interface OnClickTagListener {

        void onSelectChatTag(TagModel tagModel);
    }



    public ChatTagsAdapter(Context context, List<TagModel> tagList,OnClickTagListener listener)
    {
        this.context = context;
        this.tagList = tagList;
        this.mInflater = LayoutInflater.from(context);
        this.listener  = listener;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewHolder(mInflater.inflate(R.layout.row_tag_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final TagViewHolder holder, int position) {
        final TagModel tagModel = tagList.get(position);
        holder.tagNameTV.setText(tagModel.tagValue);
        holder.tagNameTV.setTag(tagModel);

        holder.tagNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagModel tagModel1 = (TagModel) holder.tagNameTV.getTag();
                listener.onSelectChatTag(tagModel1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }


    public class  TagViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tagNameTV;
        public TagViewHolder(View view)
        {
            super(view);
            tagNameTV = (TextView)view.findViewById(R.id.tagNameTV);
        }

    }


}
