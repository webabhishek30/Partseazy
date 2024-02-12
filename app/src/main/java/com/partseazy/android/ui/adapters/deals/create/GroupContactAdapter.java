package com.partseazy.android.ui.adapters.deals.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.ContactModel;
import com.partseazy.android.ui.widget.CircularCheckedTextView;
import com.partseazy.android.utility.CommonUtility;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 1/5/17.
 */

public class GroupContactAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<ContactModel> groupList;
    private LayoutInflater mInflater;

    public GroupContactAdapter(Context context,List<ContactModel> groupList)
    {
        this.context = context;
        this.groupList = groupList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupVH(mInflater.inflate(R.layout.row_contact_group, parent, false));
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final GroupVH groupVH = (GroupVH)holder;
        ContactModel contactModel = groupList.get(position);

        groupVH.circularCB.setChecked(contactModel.isSelected);
        groupVH.circularCB.setText(CommonUtility.getNameAbbrevation(contactModel.contactName));

        groupVH.groupNameTV.setText(contactModel.contactName);
        groupVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(position);
            }
        });
    }


    public void updateList(int position) {

        ContactModel contactModel = groupList.remove(position);
        if (contactModel.isSelected) {
            contactModel.isSelected = false;
        } else {
            contactModel.isSelected = true;
        }
        groupList.add(position, contactModel);
        notifyItemChanged(position);
    }
    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class GroupVH extends BaseViewHolder {
        @BindView(R.id.groupNameTV)
        protected TextView groupNameTV;

        @BindView(R.id.circularCB)
        protected CircularCheckedTextView circularCB;

        public GroupVH(View itemView) {
            super(itemView);
        }
    }
}
