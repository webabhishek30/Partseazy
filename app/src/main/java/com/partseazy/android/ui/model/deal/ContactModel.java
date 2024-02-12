package com.partseazy.android.ui.model.deal;

import com.applozic.mobicommons.people.contact.Contact;

/**
 * Created by naveen on 28/4/17.
 */

public class ContactModel {
    public String contactName;
    public String contactPhoneNumber;
    public boolean isSelected;
    public Integer isPartsEazyContact=0;

    public ContactModel(){}
    public ContactModel(String contactName,String contactPhoneNumber)
    {
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
    }
}
