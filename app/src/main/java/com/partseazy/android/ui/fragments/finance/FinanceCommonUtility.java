package com.partseazy.android.ui.fragments.finance;

import com.partseazy.android.base.BaseFragment;
import com.partseazy.android.network.request.RequestIdentifier;
import com.partseazy.android.network.request.RequestParams;
import com.partseazy.android.network.request.WebServiceConstants;

import java.util.HashMap;

/**
 * Created by gaurav on 13/02/17.
 */

public class FinanceCommonUtility {


    public enum FinanceAppStatus {

        NOT_APPLIED("not-applied"),
        REJECTED("rejected"),
        APPROVED("approved"),
        UNDER_PROCESS("under-process");
        private final String status;

        private FinanceAppStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public static void loadFinanceStatus(BaseFragment context) {
        RequestParams params = new RequestParams();
        params.headerMap = new HashMap<>();
        context.getNetworkManager().GetRequest(RequestIdentifier.FINANCE_APP_STATUS_ID.ordinal(),
                WebServiceConstants.GET_FINANCE_APP_STATUS, null, params, context, context, false);
    }

}
