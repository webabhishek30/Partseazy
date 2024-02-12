package com.partseazy.android.ui.fragments.splash;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.map.FeatureMap;
import com.partseazy.android.map.FeatureMapKeys;
import com.partseazy.android.ui.activity.HomeActivity;
import com.partseazy.android.ui.fragments.auth.BaseAuthFragment;
import com.partseazy.android.ui.fragments.home.HomeFragment;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class NoUISplashScreen extends BaseAuthFragment {

    public static boolean launched = false;

    public static NoUISplashScreen newInstance() {
        Bundle bundle = new Bundle();
        NoUISplashScreen fragment = new NoUISplashScreen();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.nouisplash;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgressBar();
    }

    @Override
    protected String getFragmentTitle() {
        return null;
    }


    public static String getTagName() {
        return NoUISplashScreen.class.getSimpleName();
    }


    @Override
    protected void replaceLaunchScreen(BaseFragment fragment, String tagName) {
        //Check if feature is enabled

        if (FeatureMap.isFeatureEnabled(FeatureMapKeys.deep_linking) && !launched) {

            if (getActivity() != null && ((HomeActivity) getActivity()).checkforEntryPoint() == HomeActivity.ENTRYPOINT.DEEPLINK) {
                if (((HomeActivity) getActivity()).checkforEntryPoint() != HomeActivity.ENTRYPOINT.DEEPLINK) {
                    super.replaceLaunchScreen(HomeFragment.newInstance(), HomeFragment.getTagName());
                } else {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(this);
                    trans.commit();
                    manager.popBackStack();
                    ((HomeActivity) getActivity()).launchFragmentFromDeeplink(false);
                    launched = true;
                }
            }
        }
    }


}
