package com.gionee.gnvoiceassist.directiveListener.contacts;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.contacts.ContactsDeviceModule;
import com.baidu.duer.dcs.devicemodule.contacts.message.CreateContactPayload;
import com.baidu.duer.dcs.devicemodule.contacts.message.SearchContactPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.gnvoiceassist.service.IDirectiveListenerCallback;

import java.util.List;

/**
 * Created by twf on 2017/8/16.
 */

public class ContactsDirectiveListener extends BaseDirectiveListener implements ContactsDeviceModule.IContactsDirectiveListener {
    public static final String TAG = ContactsDirectiveListener.class.getSimpleName();
    public static final String SEARCHCONTACT = "SearchContact";
    public static final String CREATECONTACT = "CreateContact";

    public ContactsDirectiveListener(IDirectiveListenerCallback callback) {
        super(callback);
    }

    @Override
    public void onContactsDirectiveReceived(Directive directive) {

        String directiveName = directive.getName();
        if(TextUtils.equals(directiveName, SEARCHCONTACT)) {
            SearchContactPayload searchContactPayload = (SearchContactPayload)directive.getPayload();
            List<String> nameList = searchContactPayload.getCandidateNames();
            LogUtil.d(TAG, "DCSF****************SearchContact: " + nameList.toString());
            iBaseFunction.getContactsPresenter().searchContact(nameList);
        } else if(TextUtils.equals(directiveName, CREATECONTACT)) {
            CreateContactPayload createContactPayload = (CreateContactPayload) directive.getPayload();
            String name = createContactPayload.getContactName();
            String phoneNumber = createContactPayload.getPhoneNumber();
            iBaseFunction.getContactsPresenter().createContact(name, phoneNumber);
        }



    }

    @Override
    public void onDestroy() {

    }
}
