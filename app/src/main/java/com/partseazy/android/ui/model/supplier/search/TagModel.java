package com.partseazy.android.ui.model.supplier.search;


import com.partseazy.android.ui.widget.chipView.Chip;

/**
 * Created by naveen on 06/9/17.
 */

public class TagModel implements Chip {

    private String tagValue;
    public String tagKey;
    public int tagId;
    private int mType=1;

    public TagModel(int id, String value) {
        tagId = id;
        tagValue=value;
    }

    public TagModel(String tagKey, String tagValue) {
        this.tagKey = tagKey;
        this.tagValue=tagValue;
    }

    public TagModel(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public String getValue() {
        return tagValue;
    }

    @Override
    public String getKey() {
        return tagKey;
    }

    public int getType() {
        return mType;
    }
}
