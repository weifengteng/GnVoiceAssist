/**
 * 描述：语音助手初始化时，用于保存联系人数据至本地数据库，便于后期联系人相关操作，如打电话、发短信、创建联系人等
 */
package com.gionee.voiceassist.basefunction.contact.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.duer.dcs.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DataBaseAccess {
    public static final String TAG = DataBaseAccess.class.getSimpleName();
    private DBAdapter mDbAdapter;
    private SQLiteDatabase mSQLiteDb;
    private static DataBaseAccess sInstance;

    /********************************** 单例 *********************************/
    /**
     * 描述：私有构造函数
     */
    private DataBaseAccess() {
    }
    
    /**
     * 描述：单例获取类对象
     * @param cxt
     * @return
     */
    public static synchronized DataBaseAccess getInstance(Context cxt) {
    	LogUtil.d(TAG, "DataBaseAccess getInstance cxt = " + cxt + ", sInstance = " + sInstance);
        if (null == sInstance) {
            sInstance = new DataBaseAccess();
            sInstance.initDataBaseAccess(cxt);
        }

        return sInstance;
    }

    /******************************* 对外接口 *******************************/
    /**
     * 描述：关闭数据库并重置全局变量
     */
    public void onDestroy() {
    	LogUtil.d(TAG, "DataBaseAccess onDestroy!");
        closeDataBaseAccess();
        resetInstance();
    }

    /**
     * 描述：将传入的联系人集合数据插入数据库
     * @param contacts
     */
    public void insertContacts(List<String> contacts) {
    	LogUtil.d(TAG, "DataBaseAccess insertContacts contacts = " + contacts);
        delContacts();
        insertContacts(getContentValues(contacts));
    }

    
    /**
     * 描述：删除数据库中的联系人信息
     */
    public void delContacts() {
    	if(null == mSQLiteDb) {
    		LogUtil.e(TAG, "DataBaseAccess delContacts null == mSQLiteDb");
    		return;
    	}
    	
        mSQLiteDb.beginTransaction();
        try {
            mSQLiteDb.delete(DBAdapter.CONTACTS_TABLE, null, null);
            mSQLiteDb.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e(TAG, "DataBaseAccess delContacts exception e = " + e);
        } finally {
            mSQLiteDb.endTransaction();
        }
    }
    
    /**
     * 描述：根据联系人是否有改变（本地数据库和系统联系人数据库对比），判定是否需要更新联系人信息
     * @param newContacts：系统联系人数据库存在的所有有效联系人集合
     * @return
     */
    public boolean needUpdate(List<String> newContacts) {
        List<String> oldContacts = getDBContacts();
        int newSize = newContacts.size();
        int oldSize = oldContacts.size();

        if (newSize != oldSize) {
        	LogUtil.e(TAG, "DataBaseAccess needUpdate newSize != oldSize");
            return true;
        }

        return !newContacts.containsAll(oldContacts);
    }

    /******************************* 私有函数 *******************************/
    /*
     * 描述：重置类对象为null
     */
    private static synchronized void resetInstance() {
    	sInstance = null;
    }

    /*
     * 描述：根据传入联系人信息获得对应的ContentValues值，用于传入到本地联系人数据库
     */
    private ArrayList<ContentValues> getContentValues(List<String> contacts) {
    	ArrayList<ContentValues> vals = new ArrayList<ContentValues>();

    	for (String contact : contacts) {
            ContentValues val = new ContentValues();
            val.put(DBAdapter.CONTACTS_NAME, contact);
            vals.add(val);
        }
        
        return vals;
    }
    
    /*
     * 描述：初始化并打开本地联系人数据库
     */
    private void initDataBaseAccess(Context cxt) {
        if (null == mDbAdapter) {
            mDbAdapter = new DBAdapter(cxt);
        }
        if (null == mSQLiteDb) {
            mSQLiteDb = mDbAdapter.open();
        }
    }

    /*
     * 描述：关闭本地联系人数据库，并重置全局变量为null
     */
    private void closeDataBaseAccess() {
        if (null != mDbAdapter) {
            mDbAdapter.close();
            mDbAdapter = null;
        }
        mSQLiteDb = null;
    }

    /*
     * 描述：获取本地联系人数据库信息
     */
    private List<String> getDBContacts() {
        List<String> contacts = new ArrayList<String>();
        Cursor cur = getContactsCursor();
        if (null == cur) {
        	LogUtil.e(TAG, "DataBaseAccess getDBContacts ContactsCursor is null");
        	return contacts;
        }

        try {
            if (cur.moveToFirst()) {
            	LogUtil.d(TAG, "DataBaseAccess getDBContacts cur.moveToFirst() success!");
            	do {
            		String contact = cur.getString(cur.getColumnIndex(DBAdapter.CONTACTS_NAME));
            		contacts.add(contact);
            	} while (cur.moveToNext());
            }
        } catch (Exception e) {
        	LogUtil.e(TAG, "DataBaseAccess getDBContacts Exception e = " + e);
        } finally {
        	try {
        		cur.close();        		
        	} catch(Exception e) {
        		LogUtil.e(TAG, "DataBaseAccess getDBContacts cur.close() fail and e = " + e);
        	}
        }

        return contacts;
    }

    /*
     * 描述：插入联系人至本地联系人数据库
     */
    private void insertContacts(ArrayList<ContentValues> contactsVal) {
        if (null == contactsVal) {
        	LogUtil.e(TAG, "DataBaseAccess insertContacts null == contactsVal");
            return;
        }
        if(null == mSQLiteDb) {
        	LogUtil.e(TAG, "DataBaseAccess insertContacts null == mSQLiteDb");
        	return;
        }
        
        mSQLiteDb.beginTransaction();
        try {
            for (ContentValues data : contactsVal) {
                mSQLiteDb.insert(DBAdapter.CONTACTS_TABLE, null, data);
            }
            mSQLiteDb.setTransactionSuccessful();
        } catch (Exception e) {
        	LogUtil.e(TAG, "DataBaseAccess insertContacts Exception e = " + e);
        } finally {
            mSQLiteDb.endTransaction();
        }
    }

    /*
     * 描述：获取本地联系人数据库cursor
     */
    private Cursor getContactsCursor() {
    	if(null == mSQLiteDb) {
    		LogUtil.e(TAG, "DataBaseAccess getContactsCursor null == mSQLiteDb");
    		return null;
    	}
    	
        Cursor cursor = null;
        try {
            cursor = mSQLiteDb.query(DBAdapter.CONTACTS_TABLE, new String[] {DBAdapter.CONTACTS_NAME},
                    null, null, null, null, null, null);
        } catch (Exception e) {
        	LogUtil.e(TAG, "DataBaseAccess getContactsCursor Exception e = " + e);
        }

        return cursor;
    }
}