package com.partseazy.android.ui.model.productdialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naveen on 3/1/17.
 */
//TODO This is dummy class to show product detail on Product Detail Card on PDP screen
public class ProductDetailItem  implements Parcelable  {
    public String itemName;
    public String itemValue;
    public String parentName;
    public boolean showParentName;

    public ProductDetailItem(String itemName,String itemValue)
    {
        this.itemName = itemName;
        this.itemValue = itemValue;
    }
    public ProductDetailItem(String itemName,String itemValue,String parentName)
    {
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.parentName = parentName;
    }
    public ProductDetailItem(String itemName,String itemValue,String parentName,boolean showParentName)
    {
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.parentName = parentName;
        this.showParentName = showParentName;
    }

    protected ProductDetailItem(Parcel in) {
        itemName = in.readString();
        itemValue = in.readString();
        parentName = in.readString();
        showParentName = in.readByte() != 0;
    }

    public static final Creator<ProductDetailItem> CREATOR = new Creator<ProductDetailItem>() {
        @Override
        public ProductDetailItem createFromParcel(Parcel in) {
            return new ProductDetailItem(in);
        }

        @Override
        public ProductDetailItem[] newArray(int size) {
            return new ProductDetailItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeString(itemValue);
        parcel.writeString(parentName);
        parcel.writeByte((byte) (showParentName ? 1 : 0));
    }
}

