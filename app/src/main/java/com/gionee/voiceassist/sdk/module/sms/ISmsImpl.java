package com.gionee.voiceassist.sdk.module.sms;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.baidu.duer.dcs.util.SystemServiceManager;
import com.gionee.voiceassist.sdk.module.sms.message.CandidateRecipient;
import com.gionee.voiceassist.sdk.module.sms.message.SmsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 11/10/17.
 */

public class ISmsImpl {

    public List<SmsInfo> getSmsContactsByName(CandidateRecipient recommendName, String simIndex, String carrierOperator, String messageContent)
    {
        Context context = SystemServiceManager.getAppContext();
        ArrayList<SmsInfo> smsInfos = new ArrayList();
        String[] projection = { "_id", "display_name", "has_phone_number" };

        String selection = "display_name like '%" + recommendName.getContactName() + "%'";

        Cursor nameCursor = null;
        try
        {
            nameCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, selection, null, null);
            if ((nameCursor != null) &&
                    (nameCursor.getCount() > 0)) {
                while (nameCursor.moveToNext())
                {
                    String id = nameCursor.getString(0);

                    String name = nameCursor.getString(1);

                    int isHasPhoneNumber = nameCursor.getInt(2);
                    if (isHasPhoneNumber > 0)
                    {
                        SmsInfo smsInfo = convertNameToContactInfo(context, id, name);
                        smsInfo.setType(SmsInfo.TYPE_NAME);
                        if (!TextUtils.isEmpty(simIndex)) {
                            smsInfo.setSimIndex(simIndex);
                        }
                        if (!TextUtils.isEmpty(carrierOperator)) {
                            smsInfo.setCarrierOprator(carrierOperator);
                        }
                        smsInfo.setMessageContent(messageContent);
                        smsInfos.add(smsInfo);
                    }
                }
            }
        }
        finally
        {
            if (null != nameCursor) {
                nameCursor.close();
            }
        }
        return smsInfos;
    }

    private SmsInfo convertNameToContactInfo(Context context, String contactId, String contactName)
    {
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setUid(contactId);
        smsInfo.setName(contactName);
        String[] projection = { "data1", "data2" };

        String selection = "contact_id=" + contactId;

        Cursor phoneCursor = null;
        try
        {
            phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null, null);
            if ((phoneCursor != null) && (phoneCursor.getCount() > 0)) {
                while (phoneCursor.moveToNext())
                {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex("data1"));

                    int numberType = phoneCursor.getInt(phoneCursor.getColumnIndex("data2"));

                    String numberTypeString = "其他";
                    if (2 == numberType) {
                        numberTypeString = "手机";
                    } else if (1 == numberType) {
                        numberTypeString = "住宅";
                    } else if (3 == numberType) {
                        numberTypeString = "工作";
                    } else {
                        numberTypeString = "其他";
                    }
                    SmsInfo.NumberInfo numberInfo = new SmsInfo.NumberInfo();
                    numberInfo.setPhoneNumber(number);
                    numberInfo.setNumberType(numberTypeString);
                    smsInfo.getPhoneNumbersList().add(numberInfo);
                }
            }
        }
        finally
        {
            if (null != phoneCursor) {
                phoneCursor.close();
            }
        }
        return smsInfo;
    }
}
