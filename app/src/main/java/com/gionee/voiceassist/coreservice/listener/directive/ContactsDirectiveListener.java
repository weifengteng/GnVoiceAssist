package com.gionee.voiceassist.coreservice.listener.directive;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClicentContextMachineState;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextPayload;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Payload;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.contacts.ContactsDeviceModule;
import com.gionee.voiceassist.coreservice.sdk.module.contacts.message.CreateContactPayload;
import com.gionee.voiceassist.coreservice.sdk.module.contacts.message.SearchContactPayload;
import com.gionee.voiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
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

    public ContactsDirectiveListener(List<DirectiveListenerController.DirectiveCallback> callbacks) {
        super(callbacks);
    }


    @Override
    public void onCreateContact(CreateContactPayload payload) {
        String name = payload.getContactName();
        String phoneNumber = payload.getPhoneNumber();
        LogUtil.d(TAG,"onCreateContact(). payload = " + payload);
//        iBaseFunction.getContactsPresenter().createContact(name, phoneNumber);
        ContactsDirectiveEntity msg = new ContactsDirectiveEntity();
        msg.setCreateContactsInfo(name, phoneNumber);
        sendDirective(msg);

    }

    @Override
    public void onSearchContact(SearchContactPayload payload) {
        LogUtil.d(TAG, "SearchContact(), payload =  " + payload);
        List<String> nameList = payload.getCandidateNames();
        ContactsDirectiveEntity msg = new ContactsDirectiveEntity();
        msg.setSearchContactsInfo(nameList);
        sendDirective(msg);
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


}
