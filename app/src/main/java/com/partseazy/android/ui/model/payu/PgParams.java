
package com.partseazy.android.ui.model.payu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PgParams {

    @SerializedName("City")
    @Expose
    public String city;
    @SerializedName("Country")
    @Expose
    public String country;
    @SerializedName("Lastname")
    @Expose
    public String lastname;
    @SerializedName("Pg")
    @Expose
    public String pg;
    @SerializedName("State")
    @Expose
    public String state;
    @SerializedName("Zipcode")
    @Expose
    public String zipcode;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("check_offer_status_hash")
    @Expose
    public String checkOfferStatusHash;
    @SerializedName("delete_user_card_hash")
    @Expose
    public String deleteUserCardHash;
    @SerializedName("drop_category")
    @Expose
    public String dropCategory;
    @SerializedName("edit_user_card_hash")
    @Expose
    public String editUserCardHash;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("enforce_paymethod")
    @Expose
    public String enforcePaymethod;
    @SerializedName("firstname")
    @Expose
    public String firstname;
    @SerializedName("furl")
    @Expose
    public String furl;
    @SerializedName("get_merchant_ibibo_codes_hash")
    @Expose
    public String getMerchantIbiboCodesHash;
    @SerializedName("get_user_cards_hash")
    @Expose
    public String getUserCardsHash;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("payment_hash")
    @Expose
    public String paymentHash;
    @SerializedName("payment_related_details_for_mobile_sdk_hash")
    @Expose
    public String paymentRelatedDetailsForMobileSdkHash;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("productinfo")
    @Expose
    public String productinfo;
    @SerializedName("save_user_card_hash")
    @Expose
    public String saveUserCardHash;
    @SerializedName("surl")
    @Expose
    public String surl;
    @SerializedName("txnid")
    @Expose
    public String txnid;
    @SerializedName("udf1")
    @Expose
    public String udf1;
    @SerializedName("udf2")
    @Expose
    public String udf2;
    @SerializedName("udf3")
    @Expose
    public String udf3;
    @SerializedName("udf4")
    @Expose
    public String udf4;
    @SerializedName("udf5")
    @Expose
    public String udf5;
    @SerializedName("vas_for_mobile_sdk_hash")
    @Expose
    public String vasForMobileSdkHash;

}
