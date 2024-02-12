package com.partseazy.android.ui.fragments.customer_management;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.partseazy.android.R;
import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.partseazy_eventbus.PartsEazyEventBus;
import com.partseazy.partseazy_eventbus.EventObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;

/**
 * Created by shubhang on 19/02/18.
 */

public class GroupsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.scrollNSV)
    protected NestedScrollView scrollNSV;

    @BindView(R.id.parentLL)
    protected LinearLayout parent;

    @BindView(R.id.groupErrorTV)
    protected TextView groupErrorTV;

    @BindView(R.id.noGroupLL)
    protected LinearLayout noGroupLL;

    @BindView(R.id.registerCustomerBT)
    protected Button registerCustomerBT;

    private JSONObject jsonObj;
//    private  checkBox;

    private Context mContext;

    private final static String ANNIVERSARY = "anniversaries";
    private final static String BIRTHDAY = "birthdays";

    public static GroupsFragment newInstance() {
        Bundle bundle = new Bundle();
        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_groups;
    }

    public static String getTagName() {
        return GroupsFragment.class.getSimpleName();
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.groups);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGroups();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        registerCustomerBT.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadGroups() {
        showProgressDialog();
        getNetworkManager().GetRequest(RequestIdentifier.GET_GROUPS.ordinal(),
                WebServiceConstants.GET_GROUPS, null, null, this, this, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == registerCustomerBT.getId()) {
            RegisterCustomerFragment registerCustomerFragment = RegisterCustomerFragment.newInstance();
            BaseFragment.addToBackStack(getContext(), registerCustomerFragment, registerCustomerFragment.getTag());
        }
    }

    @Override
    public boolean handleResponse(Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressBar();
        hideProgressDialog();

        if (request.getIdentifier() == RequestIdentifier.GET_GROUPS.ordinal()) {
            try {
                jsonObj = new JSONObject(responseObject.toString());
                createCheckbox(jsonObj, "All");
                if (jsonObj != null) {
                    PartsEazyEventBus.getInstance().postEvent(EventConstant.GROUPS, jsonObj);

                    Iterator<?> keys = jsonObj.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (!key.equalsIgnoreCase("All"))
                            createCheckbox(jsonObj, key);
                    }
                }
                noGroupLL.setVisibility(View.GONE);
                scrollNSV.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {
        hideProgressBar();
        hideProgressDialog();
        if (request.getIdentifier() == RequestIdentifier.GET_GROUPS.ordinal()) {
            if (apiError.issue != null && apiError.issue.toLowerCase().contains("no groups found")) {
                noGroupLL.setVisibility(View.VISIBLE);
                scrollNSV.setVisibility(View.GONE);
            } else {
                showError();
            }
            PartsEazyEventBus.getInstance().postEvent(EventConstant.GROUP_API_ERROR);
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String group = buttonView.getText().toString().split(" \\(")[0];
        if (group.contains("Send to All")) {
            group = "All";

            // Disabling/enabling all other checkboxes
            for (int i = 2; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                child.setEnabled(!isChecked);
            }
        } else if (group.contains("Birthday in next 30 days")) {
            group = BIRTHDAY;
        } else if (group.contains("Anniversary in next 30 days")) {
            group = ANNIVERSARY;
        }
        if (isChecked) {
            groupErrorTV.setVisibility(View.GONE);
            PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECTED_GROUP, group, true);
        } else {
            PartsEazyEventBus.getInstance().postEvent(EventConstant.SELECTED_GROUP, group, false);
        }
    }

    private void createCheckbox(JSONObject jsonObject, String key) {
        try {
            JSONArray array = jsonObject.getJSONArray(key);
            CheckBox checkBox = new CheckBox(mContext);
            if (key.toLowerCase().contains(BIRTHDAY)) {
                key = "Birthday in next 30 days";
            } else if (key.toLowerCase().contains(ANNIVERSARY)) {
                key = "Anniversary in next 30 days";
            } else if (key.toLowerCase().contains("all")) {
                key = "Send to All";
            }
            String checkBoxText = key + " (" + String.valueOf(array.length()) + ")";
            checkBox.setText(checkBoxText);
            checkBox.setOnCheckedChangeListener(this);

            LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                    CommonUtility.convertDpToPixels(250, mContext), ViewGroup.LayoutParams.WRAP_CONTENT);
            checkParams.gravity = Gravity.CENTER;
            checkBox.setLayoutParams(checkParams);
            checkBox.setScaleY(1.1f);
            checkBox.setScaleX(1.1f);
            checkBox.setTextColor(mContext.getResources().getColor(R.color.cb_dark_grey));
            parent.addView(checkBox);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);
        if (eventObject.id == EventConstant.GROUP_ERROR) {
            groupErrorTV.setVisibility(View.VISIBLE);
        }
    }
}
