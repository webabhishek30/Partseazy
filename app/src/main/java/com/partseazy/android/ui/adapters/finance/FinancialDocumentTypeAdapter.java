package com.partseazy.android.ui.adapters.finance;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;
import com.partseazy.android.ui.fragments.finance.FinancialApplicationFragment;
import com.partseazy.android.ui.model.financialapplication.Document;
import com.partseazy.android.ui.model.financialapplication.DocumentType;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by naveen on 4/1/17.
 */

public class FinancialDocumentTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DocumentType> itemList;
    private LayoutInflater mInflater;
    private final BaseFragment mContext;

    public FinancialDocumentTypeAdapter(BaseFragment context, List<DocumentType> itemList) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        this.itemList = itemList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DocumentTypeViewHolder(mInflater.inflate(R.layout.row_financial_doc_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DocumentTypeViewHolder documentViewHolder = (DocumentTypeViewHolder) holder;
        final ImageView uploadedIV = documentViewHolder.uploadedIV;
        final RelativeLayout progressBarLyt = documentViewHolder.progressBarLyt;
        final String documentType = itemList.get(position).documentName;

        documentViewHolder.headingTV.setText(documentType);
        final RadioGroup radioGroup = (documentViewHolder).radioGroup;
        List<Document> documentList = itemList.get(position).documentList;

        for (Document document : documentList) {
            RadioButton rbn = new RadioButton(mContext.getContext());
            rbn.setId(document.id);
            rbn.setText(document.name);
            radioGroup.addView(rbn);

        }
        documentViewHolder.uploadPicBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int documentId = radioGroup.getCheckedRadioButtonId();
                if (documentId != -1) {
                    if (PermissionUtil.hasPermissions(mContext.getContext(), PermissionUtil.CAMERA_PERMISSIONS)) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_DOCUMENT_ID, documentId, documentType, uploadedIV, progressBarLyt);
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_PICK);
                        galleryIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        mContext.startActivityForResult(galleryIntent, FinancialApplicationFragment.OPEN_GALLERY);
                    } else {
                        PermissionUtil.requestCameraPermission((BaseActivity) mContext.getActivity());
                    }
                } else {
                    SnackbarFactory.showSnackbar(mContext.getActivity(), mContext.getString(R.string.please_select_option));
                }
            }
        });

        documentViewHolder.takePicBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int documentId = radioGroup.getCheckedRadioButtonId();
                if (documentId != -1) {
                    if (PermissionUtil.hasPermissions(mContext.getContext(), PermissionUtil.CAMERA_PERMISSIONS)) {
                        PartsEazyEventBus.getInstance().postEvent(EventConstant.SEND_DOCUMENT_ID, documentId, documentType, uploadedIV, progressBarLyt);
                        // Create intent to Open Mobile Camera
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
                        mContext.startActivityForResult(cameraIntent, FinancialApplicationFragment.OPEN_CAMERA);
                    } else {
                        PermissionUtil.requestCameraPermission((BaseActivity) mContext.getActivity());
                    }
                } else {
                    SnackbarFactory.showSnackbar(mContext.getActivity(), mContext.getString(R.string.please_select_option));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DocumentTypeViewHolder extends BaseViewHolder {
        @BindView(R.id.headingTV)
        public TextView headingTV;
        @BindView(R.id.radioGroup)
        public RadioGroup radioGroup;
        @BindView(R.id.uploadPicBTN)
        public Button uploadPicBTN;
        @BindView(R.id.takePicBTN)
        public Button takePicBTN;
        @BindView(R.id.uploadedIV)
        public ImageView uploadedIV;
        @BindView(R.id.progressBarLyt)
        public RelativeLayout progressBarLyt;

        public DocumentTypeViewHolder(View view) {
            super(view);
        }
    }

}