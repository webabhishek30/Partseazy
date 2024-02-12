package com.partseazy.android.ui.fragments.deals.promote_deal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.model.deal.promotion.PromotionPostData;
import com.partseazy.android.ui.model.deal.promotion.required.PromotionRequiredResult;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.ui.model.user.UserDetails;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.KeyPadUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by naveen on 28/8/17.
 */

public class PromoteCreditFragment extends BaseFragment implements  View.OnClickListener{


    @BindView(R.id.firstLineView)
    protected View firstLineView;

    @BindView(R.id.secondLineView)
    protected View secondLineView;

    @BindView(R.id.secondCheckIV)
    protected ImageView secondCheckIV;

    @BindView(R.id.thirdCheckIV)
    protected ImageView thirdCheckIV;

    @BindView(R.id.creditMsgTV)
    protected TextView creditMsgTV;

    @BindView(R.id.statusIV)
    protected ImageView statusIV;

    @BindView(R.id.confirmBT)
    protected Button confirmBT;

    @BindView(R.id.callBT)
    protected Button callBT;

    @BindView(R.id.headingTV)
    protected TextView headingTV;

    @BindView(R.id.contentLL)
    protected LinearLayout contentLL;


    public static final String PROMOTION_POST_OBJ = "promotion_post_obj";

    public PromotionPostData promotionPostData;
    public String promotionJson;


    public static PromoteCreditFragment newInstance(String promotionPostObjJson) {
        Bundle bundle = new Bundle();
        bundle.putString(PROMOTION_POST_OBJ, promotionPostObjJson);
        PromoteCreditFragment fragment = new PromoteCreditFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_promote_credit_deals;
    }

    public static String getTagName() {
        return PreviewPromotionFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.promote_your_deal);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promotionJson = getArguments().getString(PROMOTION_POST_OBJ);
        promotionPostData = new Gson().fromJson(promotionJson, PromotionPostData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        KeyPadUtility.hideSoftKeypad(getActivity());
        showBackNavigation();
        callUserDetails(DataStore.getUserID(context));
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProgress();
        contentLL.setVisibility(View.GONE);
        callBT.setOnClickListener(this);
        confirmBT.setOnClickListener(this);
    }


    private void setProgress() {
        secondCheckIV.setImageResource(R.drawable.check_green_circle);
        thirdCheckIV.setImageResource(R.drawable.check_green_circle);
        firstLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
        secondLineView.setBackgroundColor(context.getResources().getColor(R.color.green_checkout));
        headingTV.setVisibility(View.GONE);
    }


    private void setCredit(double userPromotionalCredits,double requiredPromotinalCredit) {

        callBT.setVisibility(View.GONE);
        confirmBT.setVisibility(View.GONE);

        if(userPromotionalCredits < requiredPromotinalCredit)
        {
            creditMsgTV.setText(getString(R.string.credit_unsuccessfull_msg, userPromotionalCredits + "", requiredPromotinalCredit + ""));
            statusIV.setImageResource(R.drawable.cancel_icon);
            callBT.setVisibility(View.VISIBLE);
        }else{
            creditMsgTV.setText(getString(R.string.credit_successfull_msg, userPromotionalCredits + "", requiredPromotinalCredit + ""));
            statusIV.setImageResource(R.drawable.check_green_circle);
            confirmBT.setVisibility(View.VISIBLE);
        }

        contentLL.setVisibility(View.VISIBLE);

    }

    private void callPromotionRequest() {
        showProgressDialog();
        Map<String, Object> paramMap = WebServicePostParams.getPromotionRequestParams(promotionPostData);
        getNetworkManager().PostRequest(RequestIdentifier.PROMOTION_REQUIRED_ID.ordinal(),
                WebServiceConstants.POST_PROMOTION_REQUEST, paramMap, null, this, this, false);

    }


    protected void callUserDetails(String userId) {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_USER_DETAILS.ordinal(),
                WebServiceConstants.USER_DETAILS + userId, null, null, this, this, false);
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_USER_DETAILS.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_REQUIRED_ID.ordinal()) {
            if (apiError != null)
                showError(apiError.message, MESSAGETYPE.SNACK_BAR);
            else
                showError();
        }




        return true;
    }


    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.PROMOTION_REQUIRED_ID.ordinal()) {


            getGsonHelper().parse(responseObject.toString(), PromotionRequiredResult.class, new OnGsonParseCompleteListener<PromotionRequiredResult>() {
                @Override
                public void onParseComplete(PromotionRequiredResult promotionRequiredResult) {

                    if (promotionRequiredResult != null && promotionRequiredResult.clusters != null && promotionRequiredResult.clusters.size() > 0) {

                        if(getActivity() != null && isAdded()) {
                            DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.promotion_acknowledgment_msg), getString(R.string.OK)
                                    , null, null, new DialogListener() {
                                        @Override
                                        public void onPositiveButton(DialogInterface dialog) {
                                            popToHome(getActivity());
                                        }
                                    });


                        }

                    }

                }

                @Override
                public void onParseFailure(Exception exception) {
                    CustomLogger.d("Exception  dataparsing");
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("c ", exception);
                    showError(er.message, MESSAGETYPE.SNACK_BAR);
                }


            });


        }


        if (request.getIdentifier() == RequestIdentifier.GET_USER_DETAILS.ordinal()) {
            hideProgressBar();


            getGsonHelper().parse(responseObject.toString(), UserDetails.class, new OnGsonParseCompleteListener<UserDetails>() {
                        @Override
                        public void onParseComplete(UserDetails data) {
                            if(data!=null && data.promotionCredits!=null)
                            {
                                setCredit(data.promotionCredits,promotionPostData.creditRequired);
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );


        }


        return true;
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == callBT.getId())
        {
            CommonUtility.dialPhoneNumber(context, StaticMap.CC_PHONE);
        }

        if(view.getId() == confirmBT.getId())
        {
            callPromotionRequest();
        }
    }
}
