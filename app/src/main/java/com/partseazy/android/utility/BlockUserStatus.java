package com.partseazy.android.utility;

/**
 * Created by naveen on 14/7/17.
 */

public enum BlockUserStatus {

    BLOCK_CUSTOMER("b2c-customer");

    private final String status;

    private BlockUserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
