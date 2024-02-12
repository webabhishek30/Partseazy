package com.partseazy.android.ui.fragments.deals.create_deal;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.eventbus.EventConstant;
import com.partseazy.android.gson.OnGsonParseCompleteListener;
import com.partseazy.android.network.manager.ImageManager;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.network.request.WebServicePostParams;
import com.partseazy.android.ui.adapters.deals.SkuItemAdapter;
import com.partseazy.android.ui.adapters.deals.create.GroupContactAdapter;
import com.partseazy.android.ui.adapters.deals.create.MobileContactAdapter;
import com.partseazy.android.ui.fragments.deals.DealHomeFragment;
import com.partseazy.android.ui.model.deal.ContactModel;
import com.partseazy.android.ui.model.deal.deal_detail.Deal;
import com.partseazy.android.ui.model.error.APIError;
import com.partseazy.android.utility.CommonUtility;
import com.partseazy.android.utility.PermissionUtil;
import com.partseazy.partseazy_eventbus.EventObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by naveen on 28/4/17.
 */


public class NewDealSendFragment extends NewDealBaseFragment implements View.OnClickListener {


    @BindView(R.id.priceLineView)
    protected View priceLineView;

    @BindView(R.id.priceCheckIV)
    protected ImageView priceCheckIV;

    @BindView(R.id.shippingLineView)
    protected View shippingLineView;

    @BindView(R.id.shippingCheckIV)
    protected ImageView shippingCheckIV;

    @BindView(R.id.sendLineView)
    protected View sendLineView;

    @BindView(R.id.sendCheckIV)
    protected ImageView sendCheckIV;

    @BindView(R.id.selectedTV)
    protected TextView selectedTV;

    @BindView(R.id.contactProgressBar)
    protected ProgressBar contactProgressBar;


    @BindView(R.id.scrollable)
    protected RecyclerView recyclerView;


    @BindView(R.id.dealNameTV)
    protected TextView dealNameTV;

    @BindView(R.id.productIconIV)
    protected NetworkImageView productIconIV;


    @BindView(R.id.skuPriceTV)
    protected TextView skuPriceTV;

    @BindView(R.id.skuMrpTV)
    protected TextView skuMrpTV;

    @BindView(R.id.skuDiscountTV)
    protected TextView skuDiscountTV;

    @BindView(R.id.dealerNameTV)
    protected TextView dealerNameTV;


    @BindView(R.id.groupRV)
    protected RecyclerView groupRV;

    @BindView(R.id.searchRL)
    protected RelativeLayout searchRL;

    @BindView(R.id.headingRL)
    protected RelativeLayout headingRL;

    @BindView(R.id.crossIV)
    protected ImageView crossIV;

    @BindView(R.id.searchIV)
    protected ImageView searchIV;

    @BindView(R.id.searchET)
    protected EditText searchET;

    @BindView(R.id.skuRV)
    protected RecyclerView skuRV;


    @BindView(R.id.singleSkuLL)
    protected LinearLayout singleSkuLL;

    @BindView(R.id.contentLL)
    protected LinearLayout contentLL;

    @BindView(R.id.timerTV)
    protected TextView timerTV;


    @BindView(R.id.continueBT)
    protected Button continueBT;

    @BindView(R.id.shareBT)
    protected Button shareBT;

    @BindView(R.id.dealSwitch)
    protected Switch dealSwitch;

    @BindView(R.id.publicDealCard)
    protected CardView publicDealCard;


    public static final String DEAL_ID = "deal_id";

    private MobileContactAdapter mobileContactAdapter;
    private GroupContactAdapter groupContactAdapter;
    private SkuItemAdapter skuItemAdapter;

    private List<ContactModel> contactList;
    private List<ContactModel> groupList;
    private Map<String, Integer> selectedContactMap;
    private List<String> mobileList;
    private List<String> appContactList;
    private List<String> smsContactList;

    private ImageLoader imageLoader;
    private CountDownTimer countDownTimer;

    private Deal dealDetailHolder;
    private int mDealId;

    public static NewDealSendFragment newInstance() {
        Bundle bundle = new Bundle();
        NewDealSendFragment fragment = new NewDealSendFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewDealSendFragment newInstance(int dealId) {

        Bundle bundle = new Bundle();
        bundle.putInt(DEAL_ID, dealId);
        NewDealSendFragment fragment = new NewDealSendFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealId = getArguments().getInt(DEAL_ID);
        selectedContactMap = new HashMap<>();
        imageLoader = ImageManager.getInstance(context).getImageLoader();

        if (mobileList == null)
            mobileList = new ArrayList<>();
        if (contactList == null)
            contactList = new ArrayList<>();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_send_new_deal;
    }

    @Override
    protected String getFragmentTitle() {
        return getString(R.string.create_deal);
    }

    public static String getTagName() {
        return NewDealSendFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (dealDetailHolder != null)
            setDealView();
        else
            loadDealDetail();

        if (contactList != null && contactList.size() > 0) {
            setContactAdapter();
        } else {

            if (PermissionUtil.hasPermissions(context, PermissionUtil.CONTACTS_PERMISSIONS))
                new ContactAsyncTask().execute();
            else
                PermissionUtil.requestPermission(context);

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showBackNavigation();
        hideFragmentLayout();
        setSendProgress();
        setGroupAdapter();
        handleDealSwitch();
        headingRL.setOnClickListener(this);
        crossIV.setOnClickListener(this);
        searchET.addTextChangedListener(new SearchTextWatcher());
        continueBT.setOnClickListener(this);
        shareBT.setOnClickListener(this);

        publicDealCard.setVisibility(View.VISIBLE);
        return view;

    }

    private void setSendProgress() {
        priceCheckIV.setImageResource(R.drawable.check_green_circle);
        shippingCheckIV.setImageResource(R.drawable.check_green_circle);
        sendCheckIV.setImageResource(R.drawable.check_green_circle);
        priceLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));
        shippingLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));
        sendLineView.setBackgroundColor(getResources().getColor(R.color.green_checkout));

    }

    private void setDealView() {
        dealNameTV.setText(dealDetailHolder.trade.name);

        dealerNameTV.setText(getString(R.string.sold_by, dealDetailHolder.user.name));

        String formatedURL = dealDetailHolder.trade.images.get(0).src;

        formatedURL = CommonUtility.getFormattedImageUrl(formatedURL, productIconIV, CommonUtility.IMGTYPE.QUARTERIMG);
        CommonUtility.setImageSRC(imageLoader, productIconIV, formatedURL);
        startTimer(CommonUtility.convertYYYYMMDDHHmmssZtoMilliSeconds(dealDetailHolder.trade.endingAt));

        if (dealDetailHolder.skus.size() > 1) {
            // setSKUAdapter();
            skuRV.setVisibility(View.VISIBLE);
            singleSkuLL.setVisibility(View.GONE);

        } else {
            skuPriceTV.setText(getString(R.string.rs_str, dealDetailHolder.skus.get(0).price + ""));
            skuMrpTV.setText(getString(R.string.rs_str, dealDetailHolder.skus.get(0).mrp + ""));
            skuDiscountTV.setText(getString(R.string.off_str, dealDetailHolder.skus.get(0).discount));
            singleSkuLL.setVisibility(View.VISIBLE);
            skuRV.setVisibility(View.GONE);

        }

        contentLL.setVisibility(View.VISIBLE);
        continueBT.setVisibility(View.VISIBLE);

    }

    private void hideFragmentLayout() {
        contentLL.setVisibility(View.GONE);
        continueBT.setVisibility(View.GONE);
    }

    private void loadDealDetail() {
        showProgressBar();
        getNetworkManager().GetRequest(RequestIdentifier.GET_DEAL_ID.ordinal(),
                WebServiceConstants.GET_DEAL + mDealId, null, null, this, this, false);

    }

    private void callMapContacts() {

        Map<String, Object> paramMap = WebServicePostParams.getB2C2ContactParams(mobileList);
        getNetworkManager().PostRequest(RequestIdentifier.GET_PARTSEAZY_CONTACT_ID.ordinal(),
                WebServiceConstants.POST_PARTSEAZY_CONTACTS, paramMap, null, this, this, false);
    }

    private void updateDealCall(int isPublic) {
        Map<String, Object> paramMap = WebServicePostParams.updateDeal(isPublic);
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        WebServicePostParams.addResultWrapHeader(params.headerMap);

        getNetworkManager().PutRequest(RequestIdentifier.UPDATE_DEAL.ordinal(),
                WebServiceConstants.UPDATE_DEAL + mDealId, paramMap, params, this, this, false);
    }

    private void callBroadcastDeal() {

        Map<String, Object> paramMap = WebServicePostParams.broadcastDealParams(mDealId, getString(R.string.sms_message_txt, dealDetailHolder.user.name), getString(R.string.sms_message_txt, dealDetailHolder.user.name), appContactList, smsContactList);
        getNetworkManager().PostRequest(RequestIdentifier.BROADCAST_DEAL_ID.ordinal(),
                WebServiceConstants.BROADCAST_DEAL, paramMap, null, this, this, false);
    }

//    private void setSKUAdapter()
//    {
//
//        if(skuItemAdapter==null)
//            skuItemAdapter = new SkuItemAdapter(context,dealDetailHolder.skus,de);
//        skuRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        skuRV.setAdapter(skuItemAdapter);
//        skuRV.setNestedScrollingEnabled(false);
//
//    }


    private void setContactAdapter() {
        if (mobileContactAdapter == null)
            mobileContactAdapter = new MobileContactAdapter(context, contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mobileContactAdapter);
    }

    private void setGroupAdapter() {
        groupList = new ArrayList<>();
        groupList.add(new ContactModel("South Delhi", "South Delhi"));
        groupList.add(new ContactModel("Karol Bagh", "Karol Bagh"));
        groupList.add(new ContactModel("Mobile group", "Mobile group"));
        groupList.add(new ContactModel("South Delhi", "South Delhi"));
        groupList.add(new ContactModel("Karol Bagh", "Karol Bagh"));
        groupList.add(new ContactModel("Mobile group", "Mobile group"));

        if (groupContactAdapter == null)
            groupContactAdapter = new GroupContactAdapter(context, groupList);
        groupRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        groupRV.setAdapter(groupContactAdapter);
        groupRV.setNestedScrollingEnabled(false);
    }


    private void updateContactMap(ContactModel contactModel) {
        if (contactModel.isSelected) {
            selectedContactMap.put(contactModel.contactPhoneNumber, contactModel.isPartsEazyContact);
        } else {
            if (selectedContactMap.containsKey(contactModel.contactPhoneNumber)) {
                selectedContactMap.remove(contactModel.contactPhoneNumber);
            }
        }
        if (selectedContactMap.size() > 0) {
            CustomLogger.d("SelectedContactMap ::" + selectedContactMap.size());
            selectedTV.setText(getString(R.string.selected_contact, selectedContactMap.size() + ""));
            continueBT.setEnabled(true);

        } else {
            selectedTV.setText(getString(R.string.select_contact));
            continueBT.setEnabled(false);
        }
    }


    private void handleDealSwitch() {
        dealSwitch.setChecked(true);
        dealSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    updateDealCall(1);
                } else {
                    updateDealCall(0);
                }

            }
        });
    }


    @Override
    public boolean handleErrorResponse(Request request, VolleyError error, APIError apiError) {

        hideProgressDialog();
        hideProgressBar();
        hideKeyBoard(getView());
//        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_ID.ordinal()) {
        if (apiError != null)
            showError(apiError.issue, MESSAGETYPE.SNACK_BAR);
        else
            showError();
        //  }

        return true;
    }

    @Override
    public boolean handleResponse(final Request<JSONObject> request, JSONObject responseObject, Response<JSONObject> response) {

        hideProgressDialog();
        hideKeyBoard(getView());

        if (request.getIdentifier() == RequestIdentifier.GET_DEAL_ID.ordinal()) {
            hideProgressBar();
            getGsonHelper().parse(responseObject.toString(), Deal.class, new OnGsonParseCompleteListener<Deal>() {
                @Override
                public void onParseComplete(Deal dealData) {
                    dealDetailHolder = dealData;
                    setDealView();
                }

                @Override
                public void onParseFailure(Exception exception) {
                    showError();
                    APIError er = new APIError();
                    er.message = exception.getMessage();
                    CustomLogger.e("Exception ", exception);
                }


            });
        }

        if (request.getIdentifier() == RequestIdentifier.GET_PARTSEAZY_CONTACT_ID.ordinal()) {
            Map<String, Integer> partsEazyContactMap = new Gson().fromJson(responseObject.toString(), new TypeToken<HashMap<String, Integer>>() {
            }.getType());

            for (int i = 0; i < contactList.size(); i++) {
                if (partsEazyContactMap.containsKey(contactList.get(i).contactPhoneNumber)) {
                    int isPresent = partsEazyContactMap.get(contactList.get(i).contactPhoneNumber);
                    contactList.get(i).isPartsEazyContact = isPresent;

                }
            }
            contactProgressBar.setVisibility(View.GONE);
            setContactAdapter();
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (request.getIdentifier() == RequestIdentifier.BROADCAST_DEAL_ID.ordinal()) {

            if (getActivity() != null && isAdded()) {
                showError(getString(R.string.succesfully_deal_created), MESSAGETYPE.TOAST);
            }
            popToHome(getActivity());
            addToBackStack(getContext(), DealHomeFragment.newInstance(true), DealHomeFragment.class.getSimpleName());
        }


        if (request.getIdentifier() == RequestIdentifier.UPDATE_DEAL.ordinal()) {
            CustomLogger.d("succesfully updated");
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headingRL:
                searchRL.setVisibility(View.VISIBLE);
                headingRL.setVisibility(View.GONE);
                searchET.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchET, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.crossIV:
                searchET.setText("");
                searchRL.setVisibility(View.GONE);
                headingRL.setVisibility(View.VISIBLE);
                break;

            case R.id.continueBT:
                showSendMessageDialog();
                break;

            case R.id.shareBT:
                //CommonUtility.shareData(context);
                break;

        }
    }

    @Override
    public void onEvent(EventObject eventObject) {
        super.onEvent(eventObject);

        if (eventObject.id == EventConstant.SEND_SELECTED_CONTACT) {
            ContactModel contactModel = (ContactModel) eventObject.objects[0];
            updateContactMap(contactModel);

        }

    }

    private void setAppSmsContactList() {
        appContactList = new ArrayList<>();
        smsContactList = new ArrayList<>();
        for (Map.Entry<String, Integer> contact : selectedContactMap.entrySet()) {
            if (contact.getValue() == 1)
                appContactList.add(contact.getKey());
            else
                smsContactList.add(contact.getKey());
        }

    }


    private void showSendMessageDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dailog_send_message_option, null);
        Button sendBT = (Button) view.findViewById(R.id.sendBT);
        TextView noTV = (TextView) view.findViewById(R.id.noTV);


        final Dialog dailog = new Dialog(context);
        dailog.setContentView(view);
        dailog.setCancelable(true);
        dailog.show();

        sendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
                setAppSmsContactList();
                callBroadcastDeal();
            }
        });

        noTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog.dismiss();
            }
        });

    }


    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            query = query.toString().toLowerCase();

            final List<ContactModel> filteredList = new ArrayList<>();

            for (int i = 0; i < contactList.size(); i++) {

                final String text = contactList.get(i).contactName.toLowerCase();

                if (text.contains(query)) {

                    filteredList.add(contactList.get(i));
                }
            }
            //  if (filteredList != null && filteredList.size() > 0) {
            mobileContactAdapter = new MobileContactAdapter(getContext(), filteredList);
            recyclerView.setAdapter(mobileContactAdapter);
            mobileContactAdapter.notifyDataSetChanged();
            //  }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    }


    protected class ContactAsyncTask extends AsyncTask<Void, Void, List<ContactModel>> {

        @Override
        protected void onPreExecute() {
            contactProgressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            searchRL.setEnabled(false);
        }

        @Override
        protected List<ContactModel> doInBackground(Void... params) {
            if (mobileList == null)
                mobileList = new ArrayList<>();
            else
                mobileList.clear();
            if (contactList == null)
                contactList = new ArrayList<>();
            else
                contactList.clear();

            contactList.addAll(CommonUtility.getPhoneContactList(context));
            for (ContactModel contactModel : contactList) {
                mobileList.add(contactModel.contactPhoneNumber);
            }

            return contactList;
        }

        @Override
        protected void onPostExecute(List<ContactModel> list) {
            searchRL.setEnabled(true);
            callMapContacts();
        }


    }

    public void startTimer(long endTime) {


        countDownTimer = new CountDownTimer(endTime, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds


            @Override
            public void onTick(long millisUntilFinished) {

                long day = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));


                //  long durationSeconds = leftTimeInMilliseconds / 1000;
                timerTV.setText("");
                //  CustomLogger.d("Time :: " + durationSeconds % 60);
                if (day > 0) {
                    timerTV.setText(context.getString(R.string.ends_in, String.format("%1d days", day)));

                } else {
                    timerTV.setText(context.getString(R.string.ends_in, String.format("%02d:%02d:%02d", hour, min, second)));

                }

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                timerTV.setText("Deal Ended !");
                timerTV.setVisibility(View.VISIBLE);
            }

        }.start();

    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        popToHome(getActivity());
        addToBackStack(getContext(), DealHomeFragment.newInstance(true), DealHomeFragment.class.getSimpleName());
        return false;
    }
}
