package com.partseazy.android.ui.adapters.registration;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.constants.AppConstants;
import com.partseazy.android.datastore.DataStore;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.ui.adapters.banner.BannerPagerAdapter;
import com.partseazy.android.ui.adapters.base.BaseViewHolder;

import com.partseazy.android.ui.fragments.product.WebViewFragment;
import com.partseazy.android.ui.fragments.registration.RegisterMobileFragment;
import com.partseazy.android.ui.model.registration.banner.Banner;
import com.partseazy.android.ui.widget.AutoScrollPager;
import com.partseazy.android.ui.widget.CircleIndicator;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.HolderType;

import java.util.List;

import butterknife.BindView;
import me.toptas.fancyshowcase.FancyShowCaseView;


/**
 * Created by gaurav on 07/12/16.
 */

public class RegisterMobileFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private final BaseFragment mContext;
    private int mTotalItems = 1;
    private BannerPagerAdapter customPagerAdapter;
    CountDownTimer cTimer = null;


    private boolean isOTPrequestSent;
    private String otpCode;
    private String mobileNumber;
    private SMSNotifier smsNotifier;


    public RegisterMobileFragmentAdapter(RegisterMobileFragment context, SMSNotifier smsNotifier) {
        this.mInflater = LayoutInflater.from(context.getContext());
        this.mContext = context;
        customPagerAdapter = new BannerPagerAdapter(context);
        this.smsNotifier = smsNotifier;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderType whichView = HolderType.values()[viewType];
        switch (whichView) {

       /*     case VIEW_IMAGE_BANNER:
                return new BannerViewHolder(mInflater.inflate(R.layout.view_banner, parent, false));
*/
            case VIEW_REGISTER_MOBILE: {
                return new RegisterMobileViewHolder( mInflater.inflate(R.layout.register_mobile_card_item, parent, false));
            }
            default:
                return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
     /*   if (position == 0) {
            return HolderType.VIEW_IMAGE_BANNER.ordinal();
        }*/
        if (position == 0) {
            return HolderType.VIEW_REGISTER_MOBILE.ordinal();
        }

        return 0;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (position) {
/*
            case 0:
                BannerViewHolder bannerViewHolder = (BannerViewHolder) viewHolder;
                bannerViewHolder.registerMessage.setText(StaticMap.REGISTRATION_MESSAGE);
                bannerViewHolder.registerTitle.setText(StaticMap.REGISTRATION_TITLE);
                bannerViewHolder.viewPager.setAdapter(customPagerAdapter);
                bannerViewHolder.circleIndicator.setViewPager(bannerViewHolder.viewPager);
                bannerViewHolder.viewPager.setClipToPadding(false);
                // set padding manually, the more you set the padding the more you see of prev & next page
                bannerViewHolder.viewPager.setPadding(30, 0, 30, 0);
                // sets a margin b/w individual pages to ensure that there is a gap b/w them

                bannerViewHolder.viewPager.setInterval(3000);
                bannerViewHolder.viewPager.startAutoScroll();
                return;
*/

            case 0:
                final RegisterMobileViewHolder registerMobileViewHolder = (RegisterMobileViewHolder) viewHolder;
                FragmentActivity activity = (FragmentActivity) registerMobileViewHolder.itemView.getContext();
              /*  // viewHolder.itemView.findViewById(R.id.yourItem)
                TapTargetView.showFor(activity,                 // `this` is an Activity
                        TapTarget.forView(registerMobileViewHolder.itemView.findViewById(R.id.otpET), "Enter Otp", "Otp Will help you to validate your mobile number.")
                                // All options below are optional
                                .outerCircleColor(R.color.red)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.red)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                // Specify a custom drawable to draw as the target
                                .targetRadius(40),                  // Specify the target radius (in dp)
                        new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                            @Override
                            public void onTargetClick(TapTargetView view) {
                                super.onTargetClick(view);      // This call is optional
                                Log.e("hello","hello");
                            }
                        });*/

                // handle back event on top back arrow
                registerMobileViewHolder.imgVBackFromOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerMobileViewHolder.otpRL.setVisibility(View.GONE);
                        registerMobileViewHolder.imgVBackFromOtp.setVisibility(View.GONE);
                        registerMobileViewHolder.mobileRL.setVisibility(View.VISIBLE);

                    }
                });
                if (isOTPrequestSent) {
                    CustomLogger.d(otpCode);
                    // CommonUtility.animateLayoutRightToLeft(registerMobileViewHolder.otpRL);
                    registerMobileViewHolder.otpRL.setVisibility(View.VISIBLE);
                    registerMobileViewHolder.imgVBackFromOtp.setVisibility(View.VISIBLE);
                    registerMobileViewHolder.mobileRL.setVisibility(View.GONE);

                    cTimer = new CountDownTimer(AppConstants.OTP_WAIT_TIME, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long timer = millisUntilFinished / 1000;
                            registerMobileViewHolder.timeTV.setText("");
                            if (mContext.getActivity() != null) {
                                registerMobileViewHolder.timeTV.setText(mContext.getString(R.string.otp_not_recieved, "00:" + timer + ""));
                            }
                        }

                        public void onFinish() {
                            registerMobileViewHolder.timeTV.setVisibility(View.GONE);
                            registerMobileViewHolder.resend_otpTV.setVisibility(View.VISIBLE);
                        }

                    }.start();
                    if (otpCode != null) {
                        registerMobileViewHolder.otpET.setText(otpCode);
                        if (registerMobileViewHolder.chkTermAndCondition.isChecked()) {
                            smsNotifier.sendOTPVerification(mobileNumber, otpCode);
                            if (cTimer != null) {
                                cTimer.onFinish();
                            }
                        }
                        else
                        {
                            mContext.showMessage("Please accept out Terms and Conditions.", BaseFragment.MESSAGETYPE.TOAST);
                        }
                    }
                    registerMobileViewHolder.otpMobileTV.setText(mContext.getString(R.string.mobile_number, mobileNumber));
                } else {
                    registerMobileViewHolder.otpRL.setVisibility(View.GONE);
                    registerMobileViewHolder.mobileRL.setVisibility(View.VISIBLE);

                }

                if ((registerMobileViewHolder.mobileET.getText() == null || registerMobileViewHolder.mobileET.getText().length() == 0) && DataStore.getUserPhoneNumber(mContext.getContext()) != null)
                    registerMobileViewHolder.mobileET.setText(DataStore.getUserPhoneNumber(mContext.getContext()));


                /*** for future use to get device phone number

                 if ((registerMobileViewHolder.mobileET.getText() == null || registerMobileViewHolder.mobileET.getText().length() == 0)  && registerMobileViewHolder.mobileET.getText() != null)
                 registerMobileViewHolder.mobileET.setText(CommonUtility.getDevicePhoneNUmber(mContext.getContext()));
                 */

                registerMobileViewHolder.resend_otpTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomLogger.d("Resending the mobile Number ::" + mobileNumber);
                        if (mobileNumber != null) {
                            registerMobileViewHolder.otpET.setText("");
                            otpCode = null;
                            smsNotifier.sendOTPRequest(mobileNumber);
                        }
                    }

                });

                registerMobileViewHolder.continueMobileRegBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mobileNumber = registerMobileViewHolder.mobileET.getText().toString();
                        CustomLogger.d("mobile Number ::" + mobileNumber);

                        if (mobileNumber != null && CommonUtility.isValidMobileNumber(mobileNumber, false, 10, 10)) {
                            smsNotifier.sendOTPRequest(mobileNumber);
                        } else {
                            registerMobileViewHolder.mobileET.setError("Invalid Mobile Number");
                        }

                    }
                });
                registerMobileViewHolder.continueOtpRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otpCode = registerMobileViewHolder.otpET.getText().toString();
                        if (registerMobileViewHolder.chkTermAndCondition.isChecked()) {
                            if (otpCode != null && !TextUtils.isEmpty(otpCode)) {
                                if (cTimer != null) {
                                    cTimer.onFinish();
                                }
                                smsNotifier.sendOTPVerification(mobileNumber, otpCode);
                            } else {
                                mContext.showError("Invalid OTP", BaseFragment.MESSAGETYPE.TOAST);

                            }
                        }
                        else
                        {
                            mContext.showMessage("Please accept out Terms and Conditions.", BaseFragment.MESSAGETYPE.TOAST);

                        }
                    }
                });

                registerMobileViewHolder.txtTermConditionClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFragment fragment = WebViewFragment.newInstance(mContext.getString(R.string.terms_and_condition),  AppConstants.TERMS_AND_CONDITION_URL);
                        BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), fragment, WebViewFragment.getTagName());
                    }
                });
                registerMobileViewHolder.txtPrivacyPolicyClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFragment fragment = WebViewFragment.newInstance(mContext.getString(R.string.privacy_policy),  AppConstants.PRIVACY_POLICY_URL);
                        BaseFragment.addToBackStack((BaseActivity) mContext.getActivity(), fragment, WebViewFragment.getTagName());
                    }
                });


                // handle actionDone event on pressing the Done Key
                registerMobileViewHolder.mobileET.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            mobileNumber = registerMobileViewHolder.mobileET.getText().toString();
                            CustomLogger.d("mobile Number ::" + mobileNumber);
                            if (mobileNumber != null && CommonUtility.isValidMobileNumber(mobileNumber, false, 10, 10)) {
                                smsNotifier.sendOTPRequest(mobileNumber);
                            } else {
                                registerMobileViewHolder.mobileET.setError("Invalid Mobile Number");
                            }

                            return true;
                        }
                        return false;
                    }
                });
                registerMobileViewHolder.otpET.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            otpCode = registerMobileViewHolder.otpET.getText().toString();
                            CustomLogger.d("OTP Code " + otpCode);
                            if (registerMobileViewHolder.chkTermAndCondition.isChecked()) {
                                if (otpCode != null && !TextUtils.isEmpty(otpCode)) {
                                    if (cTimer != null) {
                                        cTimer.onFinish();
                                    }
                                    smsNotifier.sendOTPVerification(mobileNumber, otpCode);
                                } else {
                                    mContext.showError("Invalid OTP", BaseFragment.MESSAGETYPE.TOAST);
                                }
                            }
                            else
                            {
                                mContext.showMessage("Please accept out Terms and Conditions.", BaseFragment.MESSAGETYPE.TOAST);
                            }
                            return true;

                        }

                        return false;
                    }
                });
                registerMobileViewHolder.crossIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cTimer.cancel();
                        BaseFragment fragment = RegisterMobileFragment.newInstance(false);
                        BaseFragment.removeTopAndAddToBackStack((BaseActivity) mContext.getActivity(), fragment, fragment.getTag());
                    }
                });

        }

    }

    class BannerViewHolder extends BaseViewHolder {

        @BindView(R.id.viewpager)
        public AutoScrollPager viewPager;
        @BindView(R.id.indicator)
        public CircleIndicator circleIndicator;


        @BindView(R.id.registerTitle)
        public TextView registerTitle;
        @BindView(R.id.registerMessage)
        public TextView registerMessage;


        public BannerViewHolder(View view) {
            super(view);
        }
    }


    class RegisterMobileViewHolder extends BaseViewHolder {

        @BindView(R.id.continue_register_mobileBT)
        public Button continueMobileRegBT;
        @BindView(R.id.mobileRL)
        public RelativeLayout mobileRL;
        @BindView(R.id.otpRL)
        public RelativeLayout otpRL;
        @BindView(R.id.mobileET)
        public EditText mobileET;
        @BindView(R.id.submitOTPBT)
        public Button continueOtpRL;
        @BindView(R.id.otpET)
        public EditText otpET;
        @BindView(R.id.opt_textTV)
        public TextView otpTextTV;
        @BindView(R.id.opt_mobileTV)
        public TextView otpMobileTV;
        @BindView(R.id.resend_otpTV)
        public TextView resend_otpTV;
        @BindView(R.id.timeTv)
        public TextView timeTV;
        @BindView(R.id.txtTermConditionClick)
        public TextView txtTermConditionClick;
        @BindView(R.id.txtPrivacyPolicyClick)
        public TextView txtPrivacyPolicyClick;
        @BindView(R.id.crossIV)
        public ImageView crossIV;
        @BindView(R.id.imgVBackFromOtp)
        public ImageView imgVBackFromOtp;
        @BindView(R.id.chkTermAndCondition)
        public CheckBox chkTermAndCondition;



        public RegisterMobileViewHolder(View view) {
            super(view);
            otpET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int otpLength= s.toString().length();
                    if (otpLength == 4)
                        continueOtpRL.setEnabled(true);
                    else
                        continueOtpRL.setEnabled(false);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mTotalItems;
    }


    public void updateBannerList(List<Banner.Banner_> items) {
        // customPagerAdapter.update(items);
        //   notifyItemChanged(0);
    }

    public void updateSmsSentStatus(boolean st) {
        isOTPrequestSent = st;
        if (mobileNumber == null)
            mobileNumber = DataStore.getUserPhoneNumber(mContext.getContext());
        notifyItemChanged(0);

    }

    public void updateOTPView(String otpCode) {
        this.otpCode = otpCode;
        CustomLogger.d("otpCode ::" + otpCode);
        notifyItemChanged(0);
    }


    public interface SMSNotifier {

        public void sendOTPRequest(String mobileNumber);

        public void sendOTPVerification(String mobileNumber, String OTP);
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

}
