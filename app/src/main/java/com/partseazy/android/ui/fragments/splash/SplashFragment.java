package com.partseazy.android.ui.fragments.splash;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.ui.activity.HomeActivity;
import com.partseazy.android.ui.fragments.auth.BaseAuthFragment;
import com.partseazy.android.utility.CommonUtility;

import butterknife.BindView;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class SplashFragment extends BaseAuthFragment {

    @BindView(R.id.welcomebgIV)
    protected ImageView splashBGIV;
    @BindView((R.id.welcomeLogo))
    protected ImageView splashBGLogo;
    @BindView(R.id.welcomeFL)
    protected FrameLayout welcomeFL;

    private static final int RightToLeft = 1;
    private static final int LeftToRight = 2;
    private static final int DURATION = 6000;

    private ValueAnimator mCurrentAnimator;
    private final Matrix mMatrix = new Matrix();
    private float mScaleFactor;
    private int mDirection = RightToLeft;
    private RectF mDisplayRect = new RectF();


    public static SplashFragment newInstance() {
        Bundle bundle = new Bundle();
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getContext().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        splashBGIV.setImageBitmap(CommonUtility.decodeImage(getActivity(), R.drawable.welcome_bg));
        splashBGIV.post(new Runnable() {
            @Override
            public void run() {
                splashBGIV.setScaleType(ImageView.ScaleType.MATRIX);
                mScaleFactor = (float) splashBGIV.getHeight() / (float) splashBGIV.getDrawable().getIntrinsicHeight();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                splashBGIV.setImageMatrix(mMatrix);
                animate();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.splash_layout;
    }

    @Override
    protected String getFragmentTitle() {
        return SplashFragment.class.getSimpleName();
    }

    public static String getTagName() {
        return SplashFragment.class.getSimpleName();
    }


    private void animate() {
        updateDisplayRect();
        if (mDirection == RightToLeft) {
            animate(mDisplayRect.left, mDisplayRect.left - (mDisplayRect.right - splashBGIV.getWidth()));
        } else {
            animate(mDisplayRect.left, 0.0f);
        }
    }

    private void animate(float from, float to) {
        mCurrentAnimator = ValueAnimator.ofFloat(from, to);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                mMatrix.reset();
                mMatrix.postScale(mScaleFactor, mScaleFactor);
                mMatrix.postTranslate(value, 0);

                splashBGIV.setImageMatrix(mMatrix);

            }
        });

        mCurrentAnimator.setDuration(DURATION);
        mCurrentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDirection == RightToLeft)
                    mDirection = LeftToRight;
                else
                    mDirection = RightToLeft;

                animate();
            }
        });
        mCurrentAnimator.start();
    }

    private void updateDisplayRect() {
        mDisplayRect.set(0, 0, splashBGIV.getDrawable().getIntrinsicWidth(), splashBGIV.getDrawable().getIntrinsicHeight());
        mMatrix.mapRect(mDisplayRect);
    }


    @Override
    protected void replaceLaunchScreen(final BaseFragment fragment, final String tagName) {

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SplashFragment.super.replaceLaunchScreen(fragment, tagName);
//            }
//        }, AppConstants.SPLASH_DURATION_MS);

        //Check if feature is enabled

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.push_notfication)) {
            if (getActivity() != null && ((HomeActivity) getActivity()).isPushNotification()) {

                if (((HomeActivity) getActivity()).launchFragmentFromPushNotif(false)) {
                    return;
                }
            }
        }


        super.replaceLaunchScreen(fragment, tagName);
    }
}
