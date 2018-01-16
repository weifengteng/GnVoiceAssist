package com.gionee.voiceassist.view.viewitem;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.util.ContactProcessor;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleContactInfoItem extends BaseItem implements OnClickListener {

	public static final String TAG = SimpleContactInfoItem.class.getSimpleName();
	private static final String[] PROJECTION = new String[]{Phone.NUMBER, Phone.TYPE, Data.DATA3};
	public static final int TYPE_DIAL = 0;
	public static final int TYPE_SMS = 1;
	public static final int TYPE_ALL = 2;
	public static final int TYPE_RADIO = 3;

	private static final int INDEX_NUM = 0;
	private static final int INDEX_TYPE = 1;
	private static final int INDEX_LABEL = 2;
	private static final String SMSTO = "smsto";

	private HashMap<String, ArrayList<String>> mPhoneMap = new HashMap<String, ArrayList<String>>();
	private String[] mPhoneType;
	private String mInfo;
	private String mName;
	private String mNumber;
	private String mContent;
	private View mCachedView;
	private Context mContext;
	private RadioGroup mRg;
	private LinearLayout mPanel;
	private boolean mIsViewClick;
	private int mRawContactId;
	private int mType;

	/******************************* 构造函数 & Override *******************************/
	public SimpleContactInfoItem(Context ctx, int contactId, String name) {
		init(ctx, contactId, name, TYPE_ALL);
		loadPhoneInfo();
	}

	public SimpleContactInfoItem(Context ctx, int contactId, String name, int type) {
		init(ctx, contactId, name, type);
		if (type == TYPE_DIAL) {
			HashMap<String, ArrayList<String>> tempPhoneMap = ContactProcessor.getContactProcessor().getNumberByName(name);
			if (tempPhoneMap != null && tempPhoneMap.size() > 0) {
				mPhoneMap = tempPhoneMap;
			}
		} else if (type == TYPE_ALL) {
			HashMap<String, ArrayList<String>> tempPhoneMap = ContactProcessor.getContactProcessor().getNumberById(contactId);
			if (tempPhoneMap != null && tempPhoneMap.size() > 0) {
				mPhoneMap = tempPhoneMap;
			}
		} else {
			loadPhoneInfo();
		}
	}

	public SimpleContactInfoItem(Context ctx, String name, HashMap<String, ArrayList<String>> numberMap, int type) {
		init(ctx, 0, name, type);
		if (numberMap != null) {
			mPhoneMap = numberMap;
		}
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
		if (mCachedView == null) {
			mCachedView = inflater.inflate(R.layout.contact_detail_info_parent, parent, false);
			mCachedView.getBackground().setLevel(mType);
			bindView();
		}
		return mCachedView;
	}

	public View getView() {
		if (mCachedView == null) {
			mCachedView = View.inflate(mContext, R.layout.contact_detail_info_parent, null);
			bindView();
		}
		return mCachedView;
	}


	@Override
	public void bindView() {
		initView();
		fillPhoneList();
	}

	@Override
	public void onClick() {

	}

	@Override
	public void onClick(View v) {
		if (!mIsViewClick) {
			switch (v.getId()) {
				case R.id.dial:
					doClickDial((String) v.getTag());
					break;
				case R.id.sms:
					doClickSms((String) v.getTag());
					break;
				case R.id.custom_panel:
					onClickPanel();
					break;
			}
		}
	}

	@Override
	public void setItemBgType(int type) {
	}

	/*********************************** 对外接口 ***********************************/
	public int getConfirmId() {
		return mRg.getCheckedRadioButtonId();
	}

	public void setContent(String content) {
		LogUtil.d(TAG, "SimpleContactInfoItem setContent content = " + content);
		mContent = content;
	}

	public HashMap<String, String> getPhoneLabelMap() {
		HashMap<String, String> cmdMap = new HashMap<String, String>();
		for (String label : mPhoneMap.keySet()) {
			ArrayList<String> numberList = mPhoneMap.get(label);
			if (numberList.size() > 1) {
				for (int i = 0; i < numberList.size(); ++i) {
					cmdMap.put(mContext.getString(R.string.label_cmd_format, label, i + 1), numberList.get(i));
				}
			} else {
				cmdMap.put(label, numberList.get(0));
			}
		}
		LogUtil.d(TAG, "SimpleContactInfoItem getPhoneLabelMap cmdMap = " + cmdMap);
		return cmdMap;
	}

	public void SkipOudside() {
	}

	/*********************************** 私有函数 ***********************************/
	private void init(Context ctx, int contactId, String name, int type) {
		LogUtil.d(TAG, "SimpleContactInfoItem init contactId = " + contactId + " , name = " + name + " , type =" + type);
		mContext = ctx;
		mRawContactId = contactId;
		mName = name;
		mType = type;
		mPhoneType = mContext.getResources().getStringArray(R.array.phone_type);
	}

	private void initView() {
		TextView title = (TextView) mCachedView.findViewById(R.id.title);
		if (TextUtils.isEmpty(mName)) {
			title.setVisibility(View.GONE);
		} else {
			title.setText(mName);
		}
		mPanel = (LinearLayout) mCachedView.findViewById(R.id.custom_panel);
		mPanel.setOnClickListener(this);
		mRg = (RadioGroup) mCachedView.findViewById(R.id.contacts_group);
		if (mType == TYPE_RADIO) {
			mRg.setVisibility(View.VISIBLE);
		}
	}

	private void loadPhoneInfo() {
		Cursor cs = mContext.getContentResolver().query(Data.CONTENT_URI,
				PROJECTION, Data.RAW_CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "=?",
				new String[]{String.valueOf(mRawContactId), Phone.CONTENT_ITEM_TYPE}, null);

		if (cs == null) {
			return;
		}

		mPhoneMap.clear();
		try {
			while (cs.moveToNext()) {
				int phoneType = cs.getInt(INDEX_TYPE);
				String label = (phoneType == Phone.TYPE_CUSTOM) ? cs.getString(INDEX_LABEL) : getDefPhoneLabel(phoneType);
				if (mPhoneMap.containsKey(label)) {
					mPhoneMap.get(label).add(cs.getString(INDEX_NUM));
				} else {
					if (TextUtils.isEmpty(label)) {
						continue;
					}

					ArrayList<String> numberList = new ArrayList<String>();
					numberList.add(cs.getString(INDEX_NUM));
					mPhoneMap.put(label, numberList);
					LogUtil.d(TAG, "SimpleContactInfoItem loadPhoneInfo label = " + label + " , numberList = " + numberList);
				}
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "SimpleContactInfoItem loadPhoneInfo e1 = " + e);
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
				LogUtil.e(TAG, "SimpleContactInfoItem loadPhoneInfo e2 = " + e);
			}
		}
	}

	private void doGoToContact(long contactId) {
		LogUtil.d(TAG, "SimpleContactInfoItem doGoToContact contactId = " + contactId);
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Intent viewContactIntent = new Intent(Intent.ACTION_VIEW, contactUri);
		viewContactIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Utils.startActivity(mContext, viewContactIntent);
		mContext.startActivity(viewContactIntent);
	}

	private void doClickDial(String tel) {
		LogUtil.d(TAG, "SimpleContactInfoItem doClickDial tel = " + tel);
		mIsViewClick = true;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.fromParts("tel", tel, null));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	Utils.startActivity(mContext, intent);
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mContext.startActivity(intent);
		SkipOudside();
    }
    
    private void doClickSms(String addr) {
    	mIsViewClick = true;
    	Uri msgUri = Uri.fromParts(SMSTO, addr, null);
    	Intent intent = new Intent(Intent.ACTION_SENDTO, msgUri);
//    	intent.setComponent(new ComponentName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity"));
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	 if (!TextUtils.isEmpty(mContent)) {
             intent.putExtra("sms_body", mContent);
         }
//        Utils.startActivity(mContext, intent);
		mContext.startActivity(intent);
        SkipOudside();
        LogUtil.d(TAG, "SimpleContactInfoItem doClickSms addr = " + addr + ", mContent = " + mContent);
    }
    
    private String getDefPhoneLabel(int phoneType) {
    	String label = null;
        switch(phoneType) {
            case Phone.TYPE_MOBILE:
                label = mContext.getString(R.string.phone_label_mobile);
                break;
            case Phone.TYPE_HOME:
                label = mContext.getString(R.string.phone_label_home);
                break;
            case Phone.TYPE_WORK:
                label = mContext.getString(R.string.phone_label_work);
                break;
            case Phone.TYPE_FAX_WORK:
            	label = mContext.getString(R.string.phone_label_work_fax);
            	break;
            case Phone.TYPE_OTHER:
                label = mContext.getString(R.string.phone_label_other);
                break;
            default:
                break;
        }
        LogUtil.d(TAG, "SimpleContactInfoItem getDefPhoneLabel label = " + label + ", phoneType = " + phoneType);
        return label;
    }
    
    private void fillPhoneList() {
		LogUtil.d(TAG, "SimpleContactInfoItem ------111111111111111-----");
    	for(String type : mPhoneType) {
        	if(mPhoneMap.containsKey(type)) {
        		fillPhoneList(mPhoneMap.get(type), type);
        	}
        }
    }
    
    private void fillPhoneList(ArrayList<String> numberList, String label) {
    	LogUtil.d(TAG, "SimpleContactInfoItem fillPhoneList numberList = " + numberList + " , label = " + label);
    	if(numberList.size() > 1) {
    		for(int i=0; i<numberList.size(); ++i) {
    			setView(numberList, label, i);
    		}
    	} else {
			setView(numberList, label, 0);
    	}
    }
    
    private void setDialView(View child, String number) {
    	ImageButton dialBtn = (ImageButton) child.findViewById(R.id.dial);
    	dialBtn.setTag(number);
    	dialBtn.setOnClickListener(this);
    	dialBtn.setVisibility(View.VISIBLE);
    }

    private void setSmsView(View child, String number) {
    	ImageButton smsBtn = (ImageButton) child.findViewById(R.id.sms);
    	smsBtn.setTag(number);
    	smsBtn.setOnClickListener(this);
    	smsBtn.setVisibility(View.VISIBLE);
    }
    
    private void setRadioView(String info, int index) {
    	RadioButton smsRBtn = new RadioButton(mContext);
		smsRBtn.setId(index);
		smsRBtn.setText(info);
		smsRBtn.setVisibility(View.VISIBLE);
    	if (index == 0) {
    		smsRBtn.setChecked(true);
    	}
    	mRg.addView(smsRBtn);
    }

    private void setView(ArrayList<String> numberList, String label, int index) {
    	//mPanel.addView(View.inflate(mContext, R.layout.list_item_divider, null));
		View child = View.inflate(mContext, R.layout.contact_detail_info_child, null);
		String number = numberList.get(index);
		String info = (index == 0) ? mContext.getString(R.string.label_format, label, number)
				: mContext.getString(R.string.label_with_seq_format, label, index + 1, number);
		LogUtil.d(TAG, "SimpleContactInfoItem ------111111111111111-----info = " + info);
		((TextView) child.findViewById(R.id.info)).setText(info);
		switch(mType) {
			case TYPE_DIAL:
				setDialView(child, number);
				break;
			case TYPE_SMS:
				setSmsView(child, number);
				break;
			case TYPE_RADIO:
				setRadioView(info, index);
				break;
			default:
				setDialView(child, number);
				setSmsView(child, number);
				break;
		}
		
		if(mType != TYPE_RADIO) {
        	mPanel.addView(child);
        }
		LogUtil.d(TAG, "SimpleContactInfoItem setView number = " + number + " , info = " + info + " , index = " + index);
    }

	private void onClickPanel(){
		LogUtil.e(TAG, "SimpleContactInfoItem onClick ------------------------- ");
		if(mRawContactId == -1) {
			return;
		}

		Cursor cs = mContext.getContentResolver().query(ContentUris.withAppendedId(RawContacts.CONTENT_URI, mRawContactId),
				new String[] { RawContacts.CONTACT_ID }, null, null, null);

		if(cs == null) {
			return;
		}

		try {
			if (cs.moveToFirst()) {
				long contactId = cs.getLong(0);
				if(contactId > 0){
					doGoToContact(contactId);
					SkipOudside();
				} else {
					Toast.makeText(mContext, mContext.getString(R.string.rsp_contact_alert_no_contact), Toast.LENGTH_SHORT).show();
				}
			}
		} catch(Exception e) {
			LogUtil.e(TAG, "SimpleContactInfoItem loadPhoneInfo e1 = " + e);
		} finally {
			try {
				cs.close();
			} catch(Exception e) {
				LogUtil.e(TAG, "SimpleContactInfoItem loadPhoneInfo e2 = " + e);
			}
		}
	}
}
