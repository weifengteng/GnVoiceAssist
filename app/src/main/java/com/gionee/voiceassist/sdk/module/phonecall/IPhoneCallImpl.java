package com.gionee.voiceassist.sdk.module.phonecall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.baidu.duer.dcs.util.SystemServiceManager;
import com.gionee.voiceassist.sdk.module.phonecall.message.CandidateCallee;
import com.gionee.voiceassist.sdk.module.phonecall.message.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 11/10/17.
 */

public class IPhoneCallImpl {
    public boolean callPhone(Context context, String phoneNumber)
    {
        if ((!TextUtils.isEmpty(phoneNumber)) &&
                ((context instanceof Activity)))
        {
            Uri uri = Uri.parse("tel:" + phoneNumber);
            Intent intent = new Intent("android.intent.action.CALL", uri);
            try
            {
                context.startActivity(intent);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<ContactInfo> getPhoneContactsByName(CandidateCallee recommendName, String simIndex, String carrierOperator)
    {
        Context context = SystemServiceManager.getAppContext();
        ArrayList<ContactInfo> contactsInfos = new ArrayList();
        String[] projection = { "_id", "display_name", "has_phone_number" };

        String selection = "display_name like '%" + recommendName.contactName + "%'";

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
                        ContactInfo contactInfo = convertNameToContactInfo(context, id, name);
                        contactInfo.setType(ContactInfo.TYPE_NAME);
                        if (!TextUtils.isEmpty(simIndex)) {
                            contactInfo.setSimIndex(simIndex);
                        }
                        if (!TextUtils.isEmpty(carrierOperator)) {
                            contactInfo.setCarrierOprator(carrierOperator);
                        }
                        contactsInfos.add(contactInfo);
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
        return contactsInfos;
    }

    private ContactInfo convertNameToContactInfo(Context context, String contactId, String contactName)
    {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUid(contactId);
        contactInfo.setName(contactName);
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
                    ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
                    numberInfo.setPhoneNumber(number);
                    numberInfo.setNumberType(numberTypeString);
                    contactInfo.getPhoneNumbersList().add(numberInfo);
                }
            }
        }
        finally
        {
            if (null != phoneCursor) {
                phoneCursor.close();
            }
        }
        return contactInfo;
    }
}
