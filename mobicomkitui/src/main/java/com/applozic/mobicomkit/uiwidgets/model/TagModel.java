package com.applozic.mobicomkit.uiwidgets.model;

/**
 * Created by naveen on 13/4/17.
 */

public class TagModel {

    public String tagValue;
    public String tagChatText;
    public boolean showDialog;

    public TagModel()
    {

    }

    public TagModel(String tagValue,String tagChatText)
    {
        this.tagValue = tagValue;
        this.tagChatText = tagChatText;
    }
    public TagModel(String tagValue,String tagChatText,boolean showDialog)
    {
        this.tagValue = tagValue;
        this.tagChatText = tagChatText;
        this.showDialog = showDialog;
    }
}
