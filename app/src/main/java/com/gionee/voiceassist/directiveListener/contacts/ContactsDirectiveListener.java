package com.gionee.voiceassist.directiveListener.contacts;

import android.text.TextUtils;


import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.voiceassist.sdk.module.contacts.ContactsDeviceModule;
import com.gionee.voiceassist.sdk.module.contacts.message.CreateContactPayload;
import com.gionee.voiceassist.sdk.module.contacts.message.SearchContactPayload;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twf on 2017/8/16.
 */

public class ContactsDirectiveListener extends BaseDirectiveListener implements ContactsDeviceModule.IContactsListener {
    public static final String TAG = ContactsDirectiveListener.class.getSimpleName();
    public static final String SEARCHCONTACT = "SearchContact";
    public static final String CREATECONTACT = "CreateContact";



    public ContactsDirectiveListener(IBaseFunction iBaseFunction) {
        super(iBaseFunction);
    }

    @Override
    public void onCreateContact(CreateContactPayload payload) {
        String name = payload.getContactName();
        String phoneNumber = payload.getPhoneNumber();
        LogUtil.d(TAG,"onCreateContact(). payload = " + payload);
        iBaseFunction.getContactsPresenter().createContact(name, phoneNumber);
    }

    @Override
    public void onSearchContact(SearchContactPayload payload) {
        List<String> nameList = payload.getCandidateNames();
        LogUtil.d(TAG, "SearchContact(), payload =  " + payload);
        iBaseFunction.getContactsPresenter().searchContact(nameList);
    }

    //TODO Deprecated. Should migrate to newer SDK's API
    @Deprecated
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

    private void initiateAddContactsNumber(String name) {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState state) {
                LogUtil.d(TAG, "generateContextPayloadByInteractionState");
                if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                    return new CustomClientContextPayload(null);
                }

                int index;
                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();

                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };
    }

    private void initiateAddContactsName(String number) {
        CustomUserInteractionDeviceModule.PayLoadGenerator generator = new CustomUserInteractionDeviceModule.PayLoadGenerator() {
            @Override
            public Payload generateContextPayloadByInteractionState(CustomClicentContextMachineState state) {
                LogUtil.d(TAG, "generateContextPayloadByInteractionState");
                if(CustomUserInteractionManager.getInstance().shouldStopCurrentInteraction()) {
                    return new CustomClientContextPayload(null);
                }

                int index;
                Payload payload;
                ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();

                payload = new CustomClientContextPayload(false, hyperUtterances);
                return payload;
            }
        };
    }



    @Override
    public void onDestroy() {

    }


}
