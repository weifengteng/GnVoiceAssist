package com.gionee.voiceassist.usecase.phonecall;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.custominteraction.message.CustomClientContextHyperUtterace;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.coreservice.datamodel.PhoneCallDirectiveEntity;
import com.gionee.voiceassist.coreservice.sdk.module.phonecall.message.CandidateCalleeNumber;
import com.gionee.voiceassist.coreservice.sdk.module.phonecall.message.ContactInfo;
import com.gionee.voiceassist.customlink.CustomLinkSchema;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author twf
 * @date 2018/1/11
 */

public class PhoneCallUseCaseHelper {

    public List<ContactInfo> assembleContactInfoByCalleeNumber(PhoneCallDirectiveEntity entity) {
        List<ContactInfo> infos = new ArrayList();
        List<CandidateCalleeNumber> candidateCalleeNumbers = entity.getCandidateCalleeNumbers();
        int simIndex = entity.getSimSlot();
        for(CandidateCalleeNumber calleeNumber : candidateCalleeNumbers) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setType(ContactInfo.TYPE_NUMBER);
            contactInfo.setName(calleeNumber.getDisplayName());
            contactInfo.setSimIndex(String.valueOf(simIndex));

            List<ContactInfo.NumberInfo> numberInfoList = new ArrayList<>();
            ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
            numberInfo.setPhoneNumber(calleeNumber.getPhoneNumber());
            numberInfoList.add(numberInfo);

            contactInfo.setPhoneNumbersList(numberInfoList);

            infos.add(contactInfo);
        }
        return infos;
    }

    public List<ContactInfo> assembleContactInfoByName(PhoneCallDirectiveEntity entity) {
        List<ContactInfo> infos = new ArrayList<>();
        List<String> recommendNames = entity.getCandidateNames();
        int simId = entity.getSimSlot();
        for(String name : recommendNames) {
            List<ContactInfo> oneTempInfo = getPhoneContactsByName(name, String.valueOf(simId));
            infos.addAll(oneTempInfo);
        }
        return infos;
    }

    /**
     * 语音选择号码的多轮交互时生成HyperUtterance
     * @param contactInfos
     * @return
     */
    public ArrayList<CustomClientContextHyperUtterace> generateHyperUtteranceOfPhoneNumberChoose(List<ContactInfo> contactInfos) {
        ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();
        int index;

        for (int i = 0; i < contactInfos.size(); i++) {
            ContactInfo contactInfo = contactInfos.get(i);
            List<ContactInfo.NumberInfo> numberInfos = contactInfo.getPhoneNumbersList();
            if (numberInfos != null && numberInfos.size() > 0) {
                for (int j = 0; j < numberInfos.size(); j++) {
                    List<String> utterances = new ArrayList<String>();
                    index = i + j + 1;
                    utterances.add("第" + index + "条");
                    // 开始拼凑phone协议schema
                    String phoneNumber = numberInfos.get(j).getPhoneNumber();
                    String simIndex = contactInfo.getSimIndex();
                    String carrier = contactInfo.getCarrierOprator();
                    String url = CustomLinkSchema.LINK_PHONE +
                            "num=" + phoneNumber;
                    if (!TextUtils.isEmpty(simIndex)) {
                        url += "#" + "sim=" + simIndex;
                    }
                    if (!TextUtils.isEmpty(carrier)) {
                        url += "#" + "carrier=" + carrier;

                    }
                    LogUtil.d(PhoneCallUsecase.class.getSimpleName(), "initiatePhoneContactSelect url= " + url + " index= " + index);
                    CustomClientContextHyperUtterace customClientContextHyperUtterace =
                            new CustomClientContextHyperUtterace(utterances, url);
                    hyperUtterances.add(customClientContextHyperUtterace);
                }
            }
        }
        return hyperUtterances;
    }

    public ArrayList<CustomClientContextHyperUtterace> generateHyperUtteranceOfSimChoose(String phoneNumber) {
        ArrayList<CustomClientContextHyperUtterace> hyperUtterances = new ArrayList<>();
        for(int i=1; i <= 2; i++) {
            List<String> utterances = new ArrayList<>();
            utterances.add("卡" + i);
            utterances.add("sim卡" + i);
            if(i == 1) {
                utterances.add("卡已");
                utterances.add("卡伊");
            }else if(i == 2) {
                utterances.add("卡尔");
                utterances.add("卡而");
            }
            String url = CustomLinkSchema.LINK_PHONE;
            url += "num=" + phoneNumber;
            url += "#sim=" + i;
            CustomClientContextHyperUtterace customClientContextHyperUtterance =
                    new CustomClientContextHyperUtterace(utterances, url);
            hyperUtterances.add(customClientContextHyperUtterance);
        }
        return hyperUtterances;
    }

    private List<ContactInfo> getPhoneContactsByName(String recommendName, String simIndex) {
        Context context = GnVoiceAssistApplication.getInstance().getApplicationContext();
        ArrayList<ContactInfo> contactsInfos = new ArrayList();
        String[] projection = { "_id", "display_name", "has_phone_number" };

        String selection = "display_name like '%" + recommendName + "%'";

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

    private ContactInfo convertNameToContactInfo(Context context, String contactId, String contactName) {
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
        } finally {
            if (null != phoneCursor) {
                phoneCursor.close();
            }
        }
        return contactInfo;
    }
}
