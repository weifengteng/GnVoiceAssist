package com.gionee.voiceassist.usecase.smssend;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.baidu.duer.dcs.util.SystemServiceManager;
import com.gionee.voiceassist.coreservice.datamodel.SmsDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.CandidateRecipient;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.CandidateRecipientNumber;
import com.gionee.voiceassist.coreservice.sdk.module.sms.message.SmsInfo;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by twf on 2018/1/14.
 */

public class SmsSendUseCaseHelper {
    private static String TAG = SmsSendUseCaseHelper.class.getSimpleName();

    public List<SmsInfo> getSmsInfoFromDirectiveEntity(SmsDirectiveEntity entity) {
        List<SmsInfo> smsInfoList = new ArrayList<>();
        switch (entity.getAction()) {
            case BY_NAME:
                smsInfoList = assembleSmsInfoByCandidateRecipient(entity);
                break;
            case BY_NUMBER:
            case SELECT_RECIPIENT:
                smsInfoList = assembleSmsInfoByCandidateRecipientNumber(entity);
                break;
            default:
                break;
        }
        return smsInfoList;
    }

    public ArrayList<CustomClientContextHyperUtterace> getHyperUtteranceListForSmsChooseContact(List<SmsInfo> smsInfos) {
        ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();
        int index;
        for(int i=0; i<smsInfos.size(); i++) {
            SmsInfo smsInfo = smsInfos.get(i);
            List<SmsInfo.NumberInfo> numberInfos = smsInfo.getPhoneNumbersList();
            if(numberInfos != null && numberInfos.size() > 0) {
                for(int j=0; j < numberInfos.size(); j++) {
                    List<String> utterances = new ArrayList<>();
                    index = i + j + 1;
                    utterances.add("第" + index + "个");

                    String phoneNumber = numberInfos.get(j).getPhoneNumber();
                    String simIndex = smsInfo.getSimIndex();
                    String carrier = smsInfo.getCarrierOprator();
                    String msg = "";
                    try {
                        msg = URLEncoder.encode(smsInfo.getMessageContent(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = CustomLinkSchema.LINK_SMS +
                            "num=" + phoneNumber +
                            "#msg=" + msg;

                    if(!TextUtils.isEmpty(simIndex)) {
                        url += "#" + "sim=" + simIndex;
                    }

                    if(!TextUtils.isEmpty(carrier)){
                        url += "#" + "carrier=" + carrier;
                    }
                    LogUtil.d(TAG, "index= " + (i+j+1) + " phoneNumber: " + phoneNumber);
                    CustomClientContextHyperUtterace customClientContextHyperUtterance =
                            new CustomClientContextHyperUtterace(utterances, url);
                    hyperUtterances.add(customClientContextHyperUtterance);
                }
            }
        }

        return hyperUtterances;
    }

    public ArrayList<CustomClientContextHyperUtterace> getHyperUtteraceListForSmsChooseSim(String phoneNumber, String msgContent) {
        ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();
        for(int i=1; i <= 2; i++) {
            List<String> utterances = new ArrayList<>();
            utterances.add("卡" + i);
            utterances.add("sim卡" + i);
            if(i == 1) {
                utterances.add("卡已");
                utterances.add("卡伊");
                utterances.add("开一");
            }else if(i == 2) {
                utterances.add("卡尔");
                utterances.add("卡而");
                utterances.add("开二");
            }

            String url = CustomLinkSchema.LINK_SMS;
            url += "num=" + phoneNumber;
            try {
                url += "#msg=" + URLEncoder.encode(msgContent.trim(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url += "#sim=" + i;
            CustomClientContextHyperUtterace customClientContextHyperUtterance =
                    new CustomClientContextHyperUtterace(utterances, url);
            hyperUtterances.add(customClientContextHyperUtterance);
        }
        return hyperUtterances;
    }

    private List<SmsInfo> assembleSmsInfoByCandidateRecipient(SmsDirectiveEntity entity) {
        List<SmsInfo> infos = new ArrayList<>();
        List<CandidateRecipient> recommendNames = entity.getCandidateRecipients();

        String simCard = entity.getSimSlot();

        String carrierInfo = entity.getUseCarrier();
        for (int i = 0; i < recommendNames.size(); i++)
        {
            List<SmsInfo> oneTmpInfo = getSmsContactsByName(recommendNames.get(i), simCard, carrierInfo, entity.getMessageContent());
            infos.addAll(oneTmpInfo);
        }
        return infos;
    }

    private List<SmsInfo> assembleSmsInfoByCandidateRecipientNumber(SmsDirectiveEntity entity) {
        List<SmsInfo> smsInfoList = new ArrayList<>();
        List<CandidateRecipientNumber> numbers = entity.getCandidateRecipientNumbers();
        String simIndex = entity.getSimSlot();
        String carrierInfo = entity.getUseCarrier();
        for (int i = 0; i < numbers.size(); i++) {
            SmsInfo smsInfo = new SmsInfo();
            smsInfo.setType(SmsInfo.TYPE_NUMBER);
            smsInfo.setName(numbers.get(i).getDisplayName());

            List<SmsInfo.NumberInfo> numberList = new ArrayList();
            SmsInfo.NumberInfo numberInfo = new SmsInfo.NumberInfo();
            numberInfo.setPhoneNumber(numbers.get(i).getPhoneNumber());
            numberList.add(numberInfo);
            smsInfo.setPhoneNumbersList(numberList);
            smsInfo.setMessageContent(entity.getMessageContent());
            if (!TextUtils.isEmpty(simIndex)) {
                smsInfo.setSimIndex(simIndex);
            }
            if (!TextUtils.isEmpty(carrierInfo)) {
                smsInfo.setCarrierOprator(carrierInfo);
            }
            smsInfoList.add(smsInfo);
        }
        return smsInfoList;
    }

    private List<SmsInfo> getSmsContactsByName(CandidateRecipient recommendName, String simIndex, String carrierOperator, String messageContent)
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

    private SmsInfo convertNameToContactInfo(Context context, String contactId, String contactName) {
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setUid(contactId);
        smsInfo.setName(contactName);
        String[] projection = { "data1", "data2" };

        String selection = "contact_id=" + contactId;

        Cursor phoneCursor = null;
        try {
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
