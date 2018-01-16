package com.gionee.voiceassist.view.viewitem;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsListItem extends BaseItem  implements OnClickListener {
    public static final String TAG = ContactsListItem.class.getSimpleName();
	public static ArrayList<String[]> mListInQueue = new ArrayList<String[]>();
	public static int mListCount;
    private Context mContext;
    private View mCachedView;
    private String mContent;
    private String[] mPhoneType;
    private HashMap<String,HashMap<String, ArrayList<String>>> mContactsList;
	private long lastTimeClick;

    /******************************* 构造函数 & Override *******************************/
    public ContactsListItem(Context ctx, HashMap<String,HashMap<String, ArrayList<String>>> list) {
    	LogUtil.d(TAG, "ContactsListItem list = " + list);
    	mContext = ctx;
    	mContactsList = list;
        mListCount = 0;
        mListInQueue.clear();
        mPhoneType = mContext.getResources().getStringArray(R.array.phone_type);
    }
    
	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
        if(mCachedView == null) {
            mCachedView = inflater.inflate(R.layout.contact_list_info_parent, parent, false);
            bindView();
        }
        return mCachedView;
	}


	public View getView() {
        if(mCachedView == null) {
            mCachedView = View.inflate(mContext,R.layout.contact_list_info_parent,null);
            bindView();
        }
        return mCachedView;
	}

	@Override
	public void bindView() {
		LinearLayout panel = (LinearLayout) mCachedView.findViewById(R.id.custom_panel);
		
        for(String name : mContactsList.keySet()){
        	HashMap<String, ArrayList<String>> numberList = mContactsList.get(name);
        	for(String type : numberList.keySet()) {
				int i = 0;
        		for(String number : numberList.get(type)) {
        			if(!TextUtils.isEmpty(number)) {
						if(i==0||i==numberList.size()-1){
							i++;
							panel.addView(getChildView(number, name));
							continue;
						}else {
							panel.addView(View.inflate(mContext, R.layout.list_item_line, null));
							panel.addView(getChildView(number, name));
						}
        			}
					i++;
        		}
        	}
        }
	}
    
	@Override
	public void onClick() {
	}

	@Override
	public void setItemBgType(int type) {
	}

	@Override
	public void onClick(View arg0) {
		if(SystemClock.uptimeMillis() - lastTimeClick < 500) {
			LogUtil.d(TAG, "ContactsListItem onClick doubleclick check return!");
			return;
		} else {
			lastTimeClick = SystemClock.uptimeMillis();
		}
		LogUtil.d(TAG, "ContactsListItem onClick doubleclick check!");
	}

	/*********************************** 对外接口 ***********************************/
	public void setSmsContent(String content){
    	mContent = content;
    }
	
	/*********************************** 私有函数 ***********************************/
	private View getChildView(String number, String name) {
		LogUtil.d(TAG, "ContactsListItem getChildView number = " + number + " ; name = " + name);
		View child = View.inflate(mContext, R.layout.contact_list_info_child, null);
		child.setOnClickListener(this);
		TextView infoLabel = (TextView) child.findViewById(R.id.info);
		infoLabel.setTag(number);
		infoLabel.setText(number);
		TextView infoName = (TextView) child.findViewById(R.id.title);
		infoName.setText(name);
		infoName.setTag(name);
		mListCount ++;
    	((TextView) child.findViewById(R.id.num_id)).setText(mListCount + "");
    	mListInQueue.add(new String[] { name, number });
    	return child;
	}
}
