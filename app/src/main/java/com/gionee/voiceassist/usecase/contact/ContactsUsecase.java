package com.gionee.voiceassist.usecase.contact;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.coreservice.datamodel.ContactsDirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.T;
import com.gionee.voiceassist.view.viewitem.SimpleContactInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengweifeng on 9/14/17.
 *
 * 处理联系人查找功能，并调用系统相应模块实现：
 * 查找联系人、新建联系人操作。
 */

public class ContactsUsecase extends BaseUsecase {
    public static final String TAG = ContactsUsecase.class.getSimpleName();
    private Context mAppCtx;

    public ContactsUsecase(IBaseFunction baseFunction) {
//        super(baseFunction);
        this.mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    public void searchContact(List<String> nameList) {
        T.showShort("SearchContact : " + nameList.toString());
        for(String name : nameList) {
            ArrayList<String> numberList = getPhoneNumberByName(name);
            LogUtil.d(TAG, "**Action Perform** SearchContact :" + numberList.toString());
        }
        showContactInfo(nameList);
    }

    public void createContact(String name, String phoneNumber) {
        T.showShort("创建联系人： name = " + name + " phoneNumber= " + phoneNumber);
        try {
            addContact(name, phoneNumber);
            playAndRenderText("正在创建联系人" + name + ";电话" + phoneNumber);
            LogUtil.d(TAG, "**Action Perform** CreateContact:" + " name = " + name + "; phonenumber = " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加联系人，使用事务
    public void addContact(String name, String phoneNumber) throws Exception {
        if(phoneNumber == null) {
            phoneNumber = "";
        }
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = mAppCtx.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
                .withValue("account_name", null)
                .build();
        operations.add(op1);

        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", name)
                .build();
        operations.add(op2);

        ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data1", phoneNumber)
                .withValue("data2", "2")
                .build();
        operations.add(op3);

        ContentProviderOperation op4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data1", "")
                .withValue("data2", "2")
                .build();
        operations.add(op4);

        resolver.applyBatch("com.android.contacts", operations);
    }

    /**
     * 通过输入获取电话号码
     */
    public ArrayList<String> getPhoneNumberByName (String name) {
        //使用ContentResolver查找联系人数据
        ArrayList<String> numberList = new ArrayList<>();
        Cursor cursor = mAppCtx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //遍历查询结果，找到所需号码
        while (cursor.moveToNext()) {
            //获取联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的名字
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(contactName)) {
                //使用ContentResolver查找联系人的电话号码
                Cursor phone = mAppCtx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phone.moveToNext()) {
                    String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    numberList.add(phoneNumber);
                }
            }
        }
        return numberList;
    }

    private void showSimpleItemAndPlayTts(String info) {
//        screenRender.renderAnswerInScreen(info);
//        playAndRenderText(info, true);
    }

    private void putResultListContent(List<SimpleContactInfoItem> resultList, List<String> names) {
        ContactProcessor cProcessor = ContactProcessor.getContactProcessor();
        for (String name : names) {
            name = name.replaceAll(" ", "");
            for (int id : (List<Integer>) cProcessor.getIdListByTrimName(name)) {
                SimpleContactInfoItem scii = new SimpleContactInfoItem(mAppCtx, id, name, SimpleContactInfoItem.TYPE_ALL) {
                    @Override
                    public void SkipOudside() {
//                        stopTtsAndReco();
//                        mFCB.stopPresentFocus();
                    }
                };
                resultList.add(scii);
            }
        }
    }

    /**
     * 显示联系人信息
     * @param names 联系人结果列表
     */
    private void showContactInfo(List<String> names) {

        List<SimpleContactInfoItem> resultList = new ArrayList<>();
        putResultListContent(resultList, names);

        if(resultList.isEmpty()) {
            LogUtil.e(TAG, "FocusContacts showContactInfo resultList.size() < 1");
            // no contact found in list
            playAndRenderText(mAppCtx.getString(R.string.rsp_no_contact_with_request));
            return;
        }

        // has contact in list
        String hostTip = names.size() > 1 ? mAppCtx.getString(R.string.rsp_multiple_same_name_found) : mAppCtx.getString(R.string.rsp_contact_detail, names.get(0));
        //TODO 显示找到的联系人
//        screenRender.renderAnswerInScreen(hostTip);

        LogUtil.d(TAG, "FocusContacts showContactInfo names = " + names.get(0) + ", hostTip = " + hostTip);
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        ContactsDirectiveEntity contactsPayload = (ContactsDirectiveEntity) payload;
        ContactsDirectiveEntity.ContactsAction action = contactsPayload.getAction();
        switch (action) {
            case CREATE:
                fireCreateContacts(contactsPayload.getContactsName(), contactsPayload.getNumber());
                break;
            case SEARCH:
                fireSearchContacts(contactsPayload.getCandidateName());
                break;
        }
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "contacts";
    }

    private void fireCreateContacts(String name, String phonenum) {
        createContact(name, phonenum);
    }

    private void fireSearchContacts(List<String> candidateName) {
        searchContact(candidateName);
    }
}
