/*
 * 2015-03-26 Refactoring by eyb
 */
package com.gionee.gnvoiceassist.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.basefunction.contact.db.DataBaseAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ContactProcessor {
    public static final String TAG = ContactProcessor.class.getSimpleName();
	private static final Pattern mWhiteSpace = Pattern.compile("\\s");
	private static final int GET_NAME_COUNT_BY_FIRST_NAME = 15;
	private static final String SPECIAL_LETTER_STR = "[`~!@#$%^&*+=|{}\\[\\].<>/?~！@#￥%……&*——+|{}【】] ";
	private static ContactProcessor mInstance;
    @SuppressLint("UseSparseArrays")
	private Map<Integer, String> mTrimDataMap = new HashMap<Integer, String>();
    private ArrayList<String> mRawNameList = new ArrayList<String>();
    private HashMap<String, HashMap<String, ArrayList<String>>> mContactNumMap = new HashMap<String, HashMap<String, ArrayList<String>>>();
    private Context mCtx;
    private Object mLock = new Object();

    /******************************* 构造函数 & Override *******************************/
    private ContactProcessor() {
        mCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    public static synchronized ContactProcessor getContactProcessor() {
        if (mInstance == null) {
            mInstance = new ContactProcessor();
        }
        return mInstance;

    }
    
    /*********************************** 对外接口 ***********************************/
    public List<String> getRawContactNameList() {
        synchronized (mLock) {
            return mRawNameList;
        }
    }

    public List<Integer> getIdListByTrimName(String name) {
        if (TextUtils.isEmpty(name)) {
            LogUtil.e(TAG, "ContactProcessor getIdListByTrimName name is null");
            return null;
        }

        ArrayList<Integer> ids = new ArrayList<Integer>();
        synchronized (mLock) {
        	//原来：42、36、56、37、23、70、43、75、49、44   475
        	//现在：24、52、37、29、27、12、31、48、29、25   314
        	//可以提高运行效率33.9%      运行100次之前为47.5ms    现在为31.4ms
    		for(Map.Entry<Integer, String> entry : mTrimDataMap.entrySet()) {
    			if(name.equals(entry.getValue())) {
    				ids.add(entry.getKey());
    			}
    		}        		
        }
        LogUtil.d(TAG, "ContactProcessor getIdListByTrimName name = " + name + ", ids = " + ids);
        return ids;
    }

    @SuppressLint("UseSparseArrays")
	public void updateContactData() {
        Cursor cs = mCtx.getContentResolver().query(Data.CONTENT_URI, new String[] {Data.RAW_CONTACT_ID, Data.DISPLAY_NAME},
        		Data.MIMETYPE + "=?", new String[] {StructuredName.CONTENT_ITEM_TYPE}, null);

        if(null == cs) {
            LogUtil.e(TAG, "ContactProcessor updateContactData null == cs");
        	return;
        }
        
        Map<Integer, String> lTrimDataTemp = new HashMap<Integer, String>();
        List<String> specialList = new ArrayList<String>();
        ArrayList<String> lRawNameTemp = new ArrayList<String>();
        try {
        	if (cs.getCount() > 0) {
        		String dbName = null;
        		String name = null;
        		Matcher matcher = null;
        		while (cs.moveToNext()) {
        			dbName = cs.getString(1);
        			if (TextUtils.isEmpty(dbName) || isContainsSpecialLetter(dbName) || dbName.length() > 15) {
        				specialList.add(dbName);
        				continue;
        			}
        			matcher = mWhiteSpace.matcher(dbName);
        			name = matcher.find() ? matcher.replaceAll("") : dbName;
        			lTrimDataTemp.put(cs.getInt(0), name);
        			if (lRawNameTemp.contains(dbName)) {
        				continue;
        			}
        			lRawNameTemp.add(dbName);
        		}
        	} else {
        		// if no contact,insert a invalid name else,iflytek engine will occur service error
        		lTrimDataTemp.put(-1, "unknown");
        		lRawNameTemp.add("unknown");
        	}
        } catch (Exception e1) {
            LogUtil.e(TAG, "ContactProcessor updateContactData Exception e1 = " + e1);
        } finally {
        	try {
        		cs.close();        		
        	} catch (Exception e2) {
                LogUtil.e(TAG, "ContactProcessor updateContactData Exception e2 = " + e2);
        	}
        }

        LogUtil.d(TAG, "ContactProcessor updateContactData specialList = " + specialList);
        synchronized (mLock) {
            mTrimDataMap.clear();
            mRawNameList.clear();
            mContactNumMap.clear();
            mTrimDataMap = lTrimDataTemp;
            mRawNameList = lRawNameTemp;
        }
    }

    public ArrayList<String> getNameListByFirstName(String matchName) {
        if (TextUtils.isEmpty(matchName)) {
            LogUtil.e(TAG, "ContactProcessor getNameListByFirstName matchName is null");
            return null;
        }
        ArrayList<String> result = new ArrayList<String>();
        Cursor cs = mCtx.getContentResolver().query(Data.CONTENT_URI, new String[] {Data.DISPLAY_NAME}, Data.MIMETYPE + "=?" + " AND " + Data.DISPLAY_NAME
        		+ "=?", new String[] {StructuredName.CONTENT_ITEM_TYPE, matchName}, Data.TIMES_CONTACTED + " desc");

        if(null == cs) {
            LogUtil.e(TAG, "ContactProcessor getNameListByFirstName null == cs");
        	return result;
        }

        try {
    		int count = GET_NAME_COUNT_BY_FIRST_NAME;
    		String dbName = null;
    		Matcher matcher = null;
    		String name = null;
    		while (count > 0 && cs.moveToNext()) {
    			dbName = cs.getString(0);
    			if (TextUtils.isEmpty(dbName)) {
    				continue;
    			}
    			matcher = mWhiteSpace.matcher(dbName);
    			name = matcher.find() ? matcher.replaceAll("") : dbName;
    			if (result.contains(name)) {
    				continue;
    			}
    			result.add(name);
    			count--;
    		}
        } catch (Exception e1) {
            LogUtil.e(TAG, "ContactProcessor getNameListByFirstName Exception e1 = " + e1);
        } finally {
        	try {
        		cs.close();        		
        	} catch (Exception e2) {
                LogUtil.e(TAG, "ContactProcessor getNameListByFirstName Exception e2 = " + e2);
        	}
        }

        LogUtil.d(TAG, "ContactProcessor getNameListByFirstName result = " + result);
        return result;
    }
    
    public HashMap<String, ArrayList<String>> getNumberByName(String name) {
        synchronized (mLock) {
            if (mContactNumMap.containsKey(name)) {
                return mContactNumMap.get(name);
            }
        }
        Cursor cs = mCtx.getContentResolver().query(Data.CONTENT_URI, new String[] {Phone.NUMBER, Phone.TYPE, Data.DATA3},
        		Data.DISPLAY_NAME + "=?" + " AND " + Data.MIMETYPE + "=?", new String[] {name, Phone.CONTENT_ITEM_TYPE}, null);

        if(null == cs) {
            LogUtil.e(TAG, "ContactProcessor getNumberByName null == cs");
        	return null;
        }
        HashMap<String, ArrayList<String>> phoneMap = new HashMap<String, ArrayList<String>>();

        try {
    		int phoneType;
    		String label = null;
    		while (cs.moveToNext()) {
    			phoneType = cs.getInt(1);
    			label = (phoneType == Phone.TYPE_CUSTOM) ? cs.getString(2) : getDefPhoneLabel(phoneType);
    			if (phoneMap.containsKey(label)) {
    				phoneMap.get(label).add(cs.getString(0));
    			} else {
    				if (TextUtils.isEmpty(label)) {
    					continue;
    				}
    				ArrayList<String> numberList = new ArrayList<String>();
    				numberList.add(cs.getString(0));
    				phoneMap.put(label, numberList);
    			}
    		}
        } catch (Exception e1) {
            LogUtil.e(TAG, "ContactProcessor getNumberByName Exception e1 = " + e1);
        } finally {
        	try {
        		cs.close();        		
        	} catch (Exception e2) {
                LogUtil.e(TAG, "ContactProcessor getNumberByName Exception e2 = " + e2);
        	}
        }
        synchronized (mLock) {
        	if(phoneMap.size() == 0) {
        		return phoneMap;
        	}
            if (mRawNameList.contains(name)) {
                mContactNumMap.put(name, phoneMap);
            }
        }
        LogUtil.d(TAG, "ContactProcessor getNumberByName phoneMap = " + phoneMap);
        return phoneMap;
    }
    
    public HashMap<String, ArrayList<String>> getNumberById(int mRawContactId) {
        HashMap<String, ArrayList<String>> phoneMap = new HashMap<String, ArrayList<String>>();
        Cursor cs = mCtx.getContentResolver().query(Data.CONTENT_URI, new String[] {Phone.NUMBER, Phone.TYPE, Data.DATA3},
                Data.RAW_CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "=?", new String[] {String.valueOf(mRawContactId), Phone.CONTENT_ITEM_TYPE}, null);
        
        if(null == cs) {
            LogUtil.e(TAG, "ContactProcessor getNumberById null == cs");
        	return phoneMap;
        }
        
        try {
    		int phoneType;
    		String label;
    		while (cs.moveToNext()) {
    			phoneType = cs.getInt(1);
    			label = (phoneType == Phone.TYPE_CUSTOM) ? cs.getString(2) : getDefPhoneLabel(phoneType);
        			
    			if (phoneMap.containsKey(label)) {
    				phoneMap.get(label).add(cs.getString(0));
    			} else {
    				if (TextUtils.isEmpty(label)) {
    					continue;
    				}
    				ArrayList<String> numberList = new ArrayList<String>();
    				numberList.add(cs.getString(0));
    				phoneMap.put(label, numberList);
    			}
    		}
        } catch (Exception e1) {
            LogUtil.e(TAG, "ContactProcessor getNumberById Exception e1 = " + e1);
        } finally {
        	try {
        		cs.close();
        	} catch (Exception e2) {
                LogUtil.e(TAG, "ContactProcessor getNumberById Exception e2 = " + e2);
        	}
        }
        LogUtil.d(TAG, "ContactProcessor getNumberById mRawContactId = " + mRawContactId + ", phoneMap = " + phoneMap);
        return phoneMap;
    }

    public String getContactPinYinByName(String name) {
        Cursor cs = mCtx.getContentResolver().query(Data.CONTENT_URI, new String[] {Data.SORT_KEY_PRIMARY, Data.DATA3},
                Data.DISPLAY_NAME + "=?", new String[] { name }, null);
        
        if(null == cs) {
            LogUtil.e(TAG, "ContactProcessor getContactPinYinByName null == cs");
        	return null;
        }
        try {
            if (cs.moveToFirst()) {
                String result = cs.getString(0).toString();
                String regEx = "[^A-Z|^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(result);
                return m.replaceAll("").trim() + "";
            }
        } catch (Exception e1) {
            LogUtil.e(TAG, "ContactProcessor getContactPinYinByName Exception e1 = " + e1);
        } finally {
        	try {
        		cs.close();
        	} catch (Exception e2) {
                LogUtil.e(TAG, "ContactProcessor getContactPinYinByName Exception e2 = " + e2);
        	}
        }
        
        return null;
    }
    
    public boolean needUpdateContacts() {
        boolean needupdate = false;

        updateContactData();
        synchronized (mLock) {
            needupdate = DataBaseAccess.getInstance(mCtx).needUpdate(mRawNameList);
        }
        LogUtil.d(TAG, "ContactProcessor needUpdateContacts needupdate =" + needupdate);

        return needupdate;
    }
    
    public void insertContacts() {
        synchronized (mLock) {
            DataBaseAccess.getInstance(mCtx).insertContacts(mRawNameList);
        }
    }
    
    public void delContacts() {
        DataBaseAccess.getInstance(mCtx).delContacts();
    }
    
    /*********************************** 私有函数 ***********************************/
    private String getDefPhoneLabel(int phoneType) {
        String label = null;
        switch (phoneType) {
            case Phone.TYPE_MOBILE:
                label = mCtx.getString(R.string.phone_label_mobile);
                break;
            case Phone.TYPE_HOME:
                label = mCtx.getString(R.string.phone_label_home);
                break;
            case Phone.TYPE_WORK:
                label = mCtx.getString(R.string.phone_label_work);
                break;
            case Phone.TYPE_FAX_WORK:
                label = mCtx.getString(R.string.phone_label_work_fax);
                break;
            case Phone.TYPE_OTHER:
                label = mCtx.getString(R.string.phone_label_other);
                break;
            default:
                break;
        }
        LogUtil.d(TAG, "ContactProcessor getDefPhoneLabel label =" + label);
        return label;
    }

    private boolean isContainsSpecialLetter(String name) {
    	try {    		
    		Pattern p = Pattern.compile(SPECIAL_LETTER_STR);
    		Matcher m = p.matcher(name);
    		String str = m.replaceAll("").trim();
    		return name.length() != str.length();
    	} catch(PatternSyntaxException e) {
            LogUtil.e(TAG, "ContactProcessor isContainsSpecialLetter PatternSyntaxException e = " + e);
    	}
    	return false;
    }
}
