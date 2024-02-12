package com.partseazy.android.ui.fragments.customer_management;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseActivity;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.map.StaticMap;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.base.BaseViewPagerAdapter;
import com.partseazy.android.ui.model.common.SuccessResponse;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.dialog.DialogListener;
import com.partseazy.android.utility.dialog.DialogUtil;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by shubhang on 16/02/18.
 */

public class BlastSmsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.messageET)
    protected EditText messageET;

    @BindView(R.id.messageTIL)
    protected TextInputLayout messageTIL;

    @BindView(R.id.charactersTV)
    protected TextView charactersTV;

    @BindView(R.id.creditsTV)
    protected TextView creditsTV;

    @BindView(R.id.submitBT)
    protected Button submitBT;

    private int credits = 0;
    private int linkCount = 57;
    private int msgCount = 159;

    private JSONObject jsonObj;

    private Context mContext;

    private BaseViewPagerAdapter pagerAdapter;
    private ArrayList<String> selectedGroups;

    public static BlastSmsFragment newInstance() {
        Bundle bundle = new Bundle();
        BlastSmsFragment fragment = new BlastSmsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_blast_sms;
    }

    public static String getTagName() {
        return BlastSmsFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.send_sms);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        selectedGroups = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        charactersTV.setText(mContext.getString(R.string.no_of_characters_1_s, "0"));
        creditsTV.setText(mContext.getString(R.string.no_of_credits_sms_1_s, "0"));
        submitBT.setOnClickListener(this);

        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                messageTIL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = s.length();
                credits = (int) Math.ceil((float) (count + linkCount) / msgCount);
                charactersTV.setText(mContext.getString(R.string.no_of_characters_1_s, String.valueOf(count)));
                creditsTV.setText(mContext.getString(R.string.no_of_credits_sms_1_s, String.valueOf(credits)));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (getActivity() != null && isAdded()) {
            pagerAdapter = new BaseViewPagerAdapter(getChildFragmentManager(), context);
            pagerAdapter.addFragment(GroupsFragment.newInstance(), "Groups");
            pagerAdapter.addFragment(MarketingFragment.newInstance(), "Marketing");
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(0);

            /*..after you set the adapter you have to check if view is laid out, i did a custom method for it
             * */
            if (ViewCompat.isLaidOut(tabLayout)) {
                setViewPagerListener();
            } else {
                tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        setViewPagerListener();
//                        tabLayout.notify();
                        tabLayout.removeOnLayoutChangeListener(this);
                    }
                });
            }
        }
    }

    private void setViewPagerListener() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getText() == "Marketing") {
                    submitBT.setEnabled(false);
                } else {
                    if (jsonObj != null)
                        submitBT.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);
        if (eventObject.id == EventConstant.GROUP_API_ERROR) {
            submitBT.setEnabled(false);
        }

        if (eventObject.id == EventConstant.GROUPS) {
            jsonObj = (JSONObject) eventObject.objects[0];
            submitBT.setEnabled(true);
        }

        if (eventObject.id == EventConstant.SELECTED_GROUP) {
            String key = (String) eventObject.objects[0];
            boolean selected = (boolean) eventObject.objects[1];
            if (selected) {
                selectedGroups.add(key);
            } else {
                selectedGroups.remove(key);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == submitBT.getId()) {
            boolean err = false;
            String message = messageET.getText().toString();
            if ("".equals(message) || message.length() < 10) {
                err = true;
                messageTIL.setError("Message should be greater than 10 characters");
            }

            if (selectedGroups.size() == 0) {
                err = true;
                PartsEazyEventBus.getInstance().postEvent(EventConstant.GROUP_ERROR);
            }

            if (!err) {
                ArrayList<Integer> userIDs = new ArrayList<>();
                for (String key : selectedGroups) {
                    try {
                        JSONArray array = jsonObj.getJSONArray(key);
                        for (int i = 0; i < array.length(); i++) {
                            userIDs.add(array.getInt(i));
                        }

                        // Submit details to back end
                        showProgressDialog();
                        Map<String, Object> paramMap = WebServicePostParams.postSMS(message, userIDs);
                        getNetworkManager().PostRequest(RequestIdentifier.BLAST_SMS.ordinal(),
                                WebServiceConstants.BLAST_SMS, paramMap, null, this, this, false);

                    } catch (Exception e) {
                        CustomLogger.e("Exception ", e);
                    }
                }
            }
        }
    }

    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.BLAST_SMS.ordinal()) {
            if (apiError != null) {
                if ("insufficient credits".equalsIgnoreCase(apiError.issue)) {
                    DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.insufficient_funds), getString(R.string.call_us)
                            , getString(R.string.cancel), null, new DialogListener() {
                                @Override
                                public void onPositiveButton(DialogInterface dialog) {
                                    CommonUtility.dialPhoneNumber(getContext(), StaticMap.CC_PHONE);
                                }
                            });
                } else {
                    showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
                }
            } else {
                showError(getString(R.string.err_somthin_wrong), MESSAGETYPE.SNACK_BAR);
            }
        }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());
        hideProgressBar();

        if (request.getIdentifier() == RequestIdentifier.BLAST_SMS.ordinal()) {
            getGsonHelper().parse(responseObject.toString(), SuccessResponse.class, new OnGsonParseCompleteListener<SuccessResponse>() {
                        @Override
                        public void onParseComplete(SuccessResponse data) {
                            hideProgressDialog();
                            if (data != null && data.success == 1) {
                                try {
                                    DialogUtil.showAlertDialog(getActivity(), true, null, getString(R.string.blast_acknowledgment_msg), getString(R.string.OK)
                                            , null, null, new DialogListener() {
                                                @Override
                                                public void onPositiveButton(DialogInterface dialog) {
                                                    ((BaseActivity) getActivity()).onPopBackStack(false);
                                                }
                                            });
                                } catch (Exception exception) {
                                    CustomLogger.e("Exception ", exception);
                                }
                            }
                        }

                        @Override
                        public void onParseFailure(Exception exception) {
                            showError();
                            hideProgressDialog();
                            CustomLogger.e("Exception ", exception);
                        }
                    }
            );
        }

        return true;
    }
}
