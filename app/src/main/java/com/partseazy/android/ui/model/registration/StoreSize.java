package com.partseazy.android.ui.model.registration;

/**
 * Created by naveen on 15/12/16.
 */

public class StoreSize {
    public int id;
    public boolean isSelected;
    public String sizeType;
    public String sizeName;


    public StoreSize (String sizeType,String sizeName)
    {
        this.sizeType = sizeType;
        this.sizeName = sizeName;
    }
    public StoreSize (String sizeType,String sizeName,boolean isSelected)
    {
        this.sizeType = sizeType;
        this.sizeName = sizeName;
        this.isSelected = isSelected;
    }
}

