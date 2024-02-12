package com.partseazy.android.ui.adapters.deals.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.model.deal.ContactModel;
import com.partseazy.android.ui.widget.CircularCheckedTextView;
import com.partseazy.android.ui.widget.CircularImageView;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 28/4/17.
 */

public class MobileContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private List<ContactModel> contactList;
    private LayoutInflater mInflater;

    public MobileContactAdapter(Context context, List<ContactModel> contactList)
    {
        this.context = context;
        this.contactList = contactList;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactVH(mInflater.inflate(R.layout.row_mobile_contact_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ContactVH contactVH = (ContactVH)holder;
        final ContactModel contactModel = contactList.get(position);

        contactVH.circularCB.setChecked(contactModel.isSelected);

        contactVH.circularCB.setText(CommonUtility.getNameAbbrevation(contactModel.contactName));
        contactVH.circularCB.setTag(contactModel);
        contactVH.nameTV.setText(contactModel.contactName);

        contactVH.circularB2C2IV.setDefaultImageResId(R.drawable.b2c2_logo_red);

        if(contactModel.isPartsEazyContact==0)
        {
            contactVH.circularB2C2IV.setVisibility(View.GONE);
        }else{
            contactVH.circularB2C2IV.setVisibility(View.VISIBLE);
        }

        contactVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(position);
                PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_SELECTED_CONTACT,contactModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }



    public void updateList(int position) {

        ContactModel contactModel = contactList.remove(position);
        if (contactModel.isSelected) {
            contactModel.isSelected = false;
        } else {
            contactModel.isSelected = true;
        }
        contactList.add(position, contactModel);
        notifyItemChanged(position);
    }


    public class ContactVH extends BaseViewHolder {

        @BindView(R.id.circularCB)
        protected CircularCheckedTextView circularCB;

        @BindView(R.id.nameTV)
        protected TextView nameTV;

        @BindView(R.id.circularB2C2IV)
        protected CircularImageView circularB2C2IV;

        public ContactVH(View itemView) {
            super(itemView);
        }
    }
}
