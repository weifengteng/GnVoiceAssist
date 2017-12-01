/**
 * 描述：本地联系人数据
 */
package com.gionee.voiceassist.basefunction.contact.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String CONTACTS_TABLE = "contactslist";
	public static final String CONTACTS_NAME = "contactsname";
    private static final String DATABASE_NAME = "voice.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CONTACTS_ID = "_id";
    private static final String CREATE_CONTACTS_TB = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE + "("
            + CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTACTS_NAME + " TEXT)";

    private DatabaseHelper mDbHelp;
    private SQLiteDatabase mDb;

    /********************************** 对外接口 *********************************/
    /**
     * 描述：构造函数
     * @param cxt
     */
    public DBAdapter(Context cxt) {
        mDbHelp = new DatabaseHelper(cxt);
    }
    
    /**
     * 描述：打开本地数据库
     * @return
     */
    public synchronized SQLiteDatabase open() {
    	if (null == mDb) {
    		mDb = mDbHelp.getWritableDatabase();
    	}
    	
    	return mDb;
    }
    
    /**
     * 描述：关闭本地数据库
     */
    public synchronized void close() {
    	if (null != mDbHelp) {
    		mDbHelp.close();
    	}
    }
    /********************************** 内部类 *********************************/
    /*
     * 描述：本地联系人数据库Helper
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context cxt) {
            super(cxt, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CONTACTS_TB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}