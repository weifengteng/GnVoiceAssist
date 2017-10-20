package com.gionee.gnvoiceassist.basefunction.contact;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.tts.TxtSpeakManager;
import com.gionee.gnvoiceassist.util.ContactProcessor;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.gionee.gnvoiceassist.util.T;
import com.gionee.gnvoiceassist.widget.SimpleContactInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengweifeng on 9/14/17.
 *
 * 处理联系人查找功能，并调用系统相应模块，实现：
 * 查找联系人、新建联系人操作。
 */

public class ContactsPresenter extends BasePresenter {
    public static final String TAG = ContactsPresenter.class.getSimpleName();
    private Context mAppCtx;

    public ContactsPresenter(IBaseFunction baseFunction) {
        super(baseFunction);
        this.mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void onSpeakFinish(String utterId) {
        super.onSpeakFinish(utterId);
    }

    @Override
    public void onSpeakError(TxtSpeakManager.TxtSpeakResult txtSpeakResult, String s) {

    }

    @Override
    public void onDestroy() {
        mAppCtx = null;
    }

    public void searchContact(List<String> nameList) {
        T.showShort("SearchContact : " + nameList.toString());
        for(String name : nameList) {
            ArrayList<String> numberList = getPhoneNumberByName(name);
            LogUtil.d(TAG, "SearchContact :" + numberList.toString());
        }
        showContactInfo(nameList);
    }

    public void createContact(String name, String phoneNumber) {
        T.showShort("创建联系人： name = " + name + " phoneNumber= " + phoneNumber);
        // TODO:
        try {
            addContact(name, phoneNumber);
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
                    T.showShort("为您查找到联系人号码： " + phoneNumber);
                    LogUtil.d(TAG, "电话：" + phoneNumber);
                    numberList.add(phoneNumber);
                }
            }
        }
        return numberList;
    }

    private void showSimpleItemAndPlayTts(String info) {
        screenRender.renderAnswerInScreen(info);
        playAndRenderText(info, true);
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
     * show contact info for command what is a number
     * @param names result from DuerOS
     */
    private void showContactInfo(List<String> names) {
        /*RecognizerManager.resetFailCount();
        String[] names = xmlResult.getResultObjectData(new String[] { "name" });
        if (names == null) {
            names = xmlResult.getResultObjectData(new String[] { "contact" });
        }

        if(names == null) {
            Log.e("FocusContacts showContactInfo names == null");
            showContactInfoWhenNameIsNull();
            return;
        }*/

        List<SimpleContactInfoItem> resultList = new ArrayList<>();
        putResultListContent(resultList, names);

        if(resultList.size() < 1) {
            LogUtil.e(TAG, "FocusContacts showContactInfo resultList.size() < 1");
            // no contact found in list
            showSimpleItemAndPlayTts(mAppCtx.getString(R.string.rsp_no_contact_with_request));
            return;
        }

        // has contact in list
        String hostTip = names.size() > 1 ? mAppCtx.getString(R.string.rsp_multiple_same_name_found) : mAppCtx.getString(R.string.rsp_contact_detail, names.get(0));
//        screenRender.renderAnswerInScreen(hostTip);

        LogUtil.d(TAG, "FocusContacts showContactInfo names = " + names.get(0) + ", hostTip = " + hostTip);
        for (SimpleContactInfoItem scii : resultList) {
            View view = scii.getView();
            screenRender.renderInfoPanel(view);
        }
    }
}
