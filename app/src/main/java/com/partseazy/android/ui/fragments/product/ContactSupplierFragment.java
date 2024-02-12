package com.partseazy.android.ui.fragments.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;


import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by naveen on 28/12/16.
 */

public class ContactSupplierFragment extends BaseFragment {

    @BindView(R.id.imageRL)
    protected RelativeLayout imageRL;
    @BindView(R.id.crossIV)
    protected ImageView crossIV;
    @BindView(R.id.attachImageIV)
    protected ImageView attachImageIV;
    @BindView(R.id.selectedIMageIV)
    protected ImageView selectedIMageIV;
    @BindView(R.id.messageET)
    protected EditText messageET;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    private static final int SELECT_PICTURE = 1;


    public static ContactSupplierFragment newInstance() {
        ContactSupplierFragment fragment = new ContactSupplierFragment();
        return fragment;
    }

    public static String getTagName() {
        return ContactSupplierFragment.class.getSimpleName();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contact_supplier;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.contact_supplier);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCrossNavigation();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        attachImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        crossIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showAlertDialog(getActivity(), true, getString(R.string.warning), getString(R.string.want_to_delete_image), getString(R.string.YES)
                        , getString(R.string.NO), null, new DialogListener() {
                            @Override
                            public void onPositiveButton(DialogInterface dialog) {
                                imageRL.setVisibility(View.GONE);
                            }
                        });
            }
        });
        return view;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                CustomLogger.d("Image Path : " + selectedImagePath);
                imageRL.setVisibility(View.VISIBLE);
                selectedIMageIV.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        visibleSendMenu();
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem sendItem = menu.findItem(R.id.action_send);
        cartItem.setVisible(false);
        searchItem.setVisible(false);
        sendItem.setVisible(true);
    }
}
