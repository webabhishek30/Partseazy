package com.partseazy.android.ui.fragments.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Naveen Kumar on 29/1/17.
 */
public class PdfViewFragment extends BaseFragment {

    @BindView(R.id.pdfView)
    protected PDFView pdfView;
    @BindView(R.id.iv_share_whatsapp)
    protected ImageView shareFileWhatsappFAB;
    @BindView(R.id.iv_share)
    protected ImageView shareFileFAB;


    String url ="";
    public static PdfViewFragment newInstance(String url) {
        PdfViewFragment fragment = new PdfViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pdfURL",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static String getTagName() {
        return PdfViewFragment.class.getSimpleName();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_pdf_viewer;
    }

    @Override
    protected String getFragmentTitle() {
        return "Customer Invoice Copy";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBackNavigation();
        toolbar.setTitle( "Customer Invoice Copy");

        url= getArguments().getString("pdfURL");

        pdfView.fromFile(new File(url))
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .load();


        shareFileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(url);

                if(fileWithinMyDir.exists()) {
                    Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", fileWithinMyDir);

                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, pdfUri);

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing Invoice");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Order Invoice");
                  //  intentShareFile.setPackage("com.whatsapp");

                    context.startActivity(Intent.createChooser(intentShareFile, "Customer Invoice Copy"));
                }
            }
        });
        shareFileWhatsappFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(url);

                try {
                    if(fileWithinMyDir.exists()) {
                        Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", fileWithinMyDir);

                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, pdfUri);

                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing Invoice");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Order Invoice");
                        intentShareFile.setPackage("com.whatsapp");

                        context.startActivity(Intent.createChooser(intentShareFile, "Customer Invoice Copy"));
                    }
                }
                catch (Exception e)
                {
                    showError(getString(R.string.errorMessage),MESSAGETYPE.SNACK_BAR);

                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }




}
