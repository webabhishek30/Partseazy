package com.partseazy.android.extraContent;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.MultipartRequestMap;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.order.ThankYouOrderFragment;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.android.utility.dialog.SnackbarFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.partseazy.android.utility.PermissionUtil.REQUEST_CAMERA_PERMISSION;

/**
 * Created by Vikas on 6/6/17.
 */

public class OrderPrescriptionFragment extends BaseFragment implements Response.StringListener {
    @BindView(R.id.recycleViewOrderImages)
    RecyclerView recycleViewOrderImages;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.btnUploadImage)
    Button btnUploadImage;
    @BindView(R.id.imgSingleSeletion)
    ImageView imgSingleSeletion;
    @BindView(R.id.imgBackButton)
    ImageView imgBackButton;
    @BindView(R.id.etDes)
    EditText etDes;
    @BindView(R.id.tvConfirmation)
    TextView tvConfirmation;
    @BindView(R.id.llShowAttachedImage)
    LinearLayout llShowAttachedImage;
    @BindView(R.id.rbBulkOrder)
    RadioButton rbBulkOrder;
    @BindView(R.id.rbRequestForQuote)
    RadioButton rbRequestForQuote;
    @BindView(R.id.txtMaterialPres)
    TextView txtMaterialPres;


    public static final int OPEN_GALLERY = 1;
    public static final int OPEN_CAMERA = 2;
    File imgFile = null;

    String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int STORAGE_PERMISSIONS = 1000;
    private static final int STORAGE_CAMERA_PERMISSIONS = 1001;
    private static final String ORDER_IMAGE_NAME = "order_image.jpg";

    ArrayList<Uri> uriList = new ArrayList<>();
    RecycleAdapter recycleAdapter;

    public static OrderPrescriptionFragment newInstance() {
        OrderPrescriptionFragment fragment = new OrderPrescriptionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleViewOrderImages.setLayoutManager(layoutManager);

   /*     recycleAdapter = new RecycleAdapter(uriList);
        recycleViewOrderImages.setAdapter(recycleAdapter);
*/
    }

    public static String getTagName() {
        return OrderPrescriptionFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_prescription;
    }

    @Override
    protected String getFragmentTitle() {
        return "Order Prescription";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDialog();
        //check current time
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (currentHour < 15)
            tvConfirmation.setText("Today");
        else
            tvConfirmation.setText("Tomorrow");


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).onPopBackStack(false);
            }
        });
        return view;
    }

    @OnClick(R.id.btnUploadImage)
    public void submit(View view) {
        String requestFor = rbBulkOrder.getText().toString();
        if (rbRequestForQuote.isChecked())
            requestFor = rbRequestForQuote.getText().toString();
        if (imgFile != null) {
            uploadPicture(imgFile, etDes.getText().toString(), requestFor);
        } else {
            SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_take_a_picture));
        }
    }


    @Override
    public void onStringResponse(Request<String> request, String responseObject, Response<String> response) {

        if (request.getIdentifier() == RequestIdentifier.TAKE_CARD_PIC_ID.ordinal()) {
            hideProgressDialog();
            mCurrentPhotoPath = "";
            etDes.setText("");
            imgFile = null;
            imgSingleSeletion.setImageResource(R.drawable.order_image_placeholder);
            txtMaterialPres.setText(getString(R.string.material_prescription));

            try {
                JSONObject myObject = new JSONObject(responseObject);
                CustomLogger.d("Response image:: " + responseObject);
                showMessage(myObject.getString("message"), MESSAGETYPE.TOAST);
                showThankYouFragment();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Holder> {

        ArrayList<Uri> imgFile;

        public RecycleAdapter(ArrayList<Uri> imgFile) {
            this.imgFile = imgFile;

        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.recycler_view_images_data, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, int i) {
            File file = new File(imgFile.get(i).getPath());
            try {
                InputStream ims = new FileInputStream(file);
                holder.imgSelected.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            holder.txtRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uriList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), uriList.size());
                }
            });

        }

        @Override
        public int getItemCount() {
            return imgFile.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.imgSelected)
            ImageView imgSelected;
            @BindView(R.id.txtRemoveItem)
            TextView txtRemoveItem;

            public Holder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == STORAGE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                showMessage("Permission denied", MESSAGETYPE.SNACK_BAR);
            }
        }
        else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_CAMERA_PERMISSIONS);
                } else {
                    openCamera();
                }
            } else {
                showMessage( "Camera permission denied", MESSAGETYPE.SNACK_BAR);
            }
        }
        else if (requestCode == STORAGE_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                showMessage("Permission denied", MESSAGETYPE.SNACK_BAR);
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == OPEN_GALLERY) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        String selectedImagePath = CommonUtility.getImagePath(getContext(), selectedImageUri);
                        llShowAttachedImage.setVisibility(View.VISIBLE);
                        txtMaterialPres.setText(getString(R.string.material_prescription_attached));
                        imgSingleSeletion.setImageURI(selectedImageUri);
                        if ("".equals(selectedImagePath)) {
                            showError();
                            return;
                        }
                        imgFile = new File(selectedImagePath);
                        imgFile = CommonUtility.saveBitmapToFile(imgFile);

                        if (imgFile != null) {
                        } else {
                            showError();
                        }
                    } else {
                        SnackbarFactory.showSnackbar(getActivity(), getString(R.string.please_upload_image));
                    }


                }

                if (requestCode == OPEN_CAMERA) {
                    //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgFile = new File(CommonUtility.getFileDirectory() + File.separator + ORDER_IMAGE_NAME);
                    imgFile = CommonUtility.saveBitmapToFile(imgFile);
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    if (bitmap != null) {
                        llShowAttachedImage.setVisibility(View.VISIBLE);
                        txtMaterialPres.setText(getString(R.string.material_prescription_attached));
                        imgSingleSeletion.setImageBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                CustomLogger.e(e.toString());
            }
        }
    }


    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(CommonUtility.getFileDirectory() + File.separator + ORDER_IMAGE_NAME);
        CustomLogger.d("File Pathh :: " + file.getAbsolutePath());
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, OPEN_CAMERA);
    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, OPEN_GALLERY);
    }

    private void uploadPicture(final File file, String etDes, String requestFor) {
        //   demoPostData.file = file;
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        showProgressDialog(getString(R.string.upload_photo), false);
        MultipartRequestMap params = new MultipartRequestMap();
        params.put("order_image", file);
        params.put("job_type", "only_order");
        params.put("mobile_no", DataStore.getUserPhoneNumber(context));
        params.put("description", requestFor + " : " + etDes);
        params.put("estimated_order", "");
        params.put("order_no", "pz_" + DataStore.getUserPhoneNumber(context) + "_" + ts);
        params.put("device_imei", "123456");
        params.put("user_id", "7c301a4b-a758-5916-3880-5d3e8ee1ae68");
        params.put("token", "bcc0a9c1-c098-11cf-0319-5d3e8e6a978d");
        RequestParams requestParams = new RequestParams();
        requestParams.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(requestParams.headerMap);
        getNetworkManager().UploadOrderImageMultipartRequest(RequestIdentifier.TAKE_CARD_PIC_ID.ordinal(), WebServiceConstants.ORDER_BY_UPLOAD_IMAGE, params, requestParams, this, this, false);
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_upload_prescription);
        dialog.show();

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        Button btnCamera = dialog.findViewById(R.id.btnCamera);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_CAMERA_PERMISSIONS);
                        } else {
                            openCamera();
                        }
                    }

            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]
                                {Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS);
                    } else {
                        openGallery();
                    }
                }
            }
        });
    }
    public void showThankYouFragment() {
        popToMall(getActivity());
        addToBackStack(getContext(), ThankYouOrderFragment.newInstance(), ThankYouOrderFragment.getTagName());
    }
}
