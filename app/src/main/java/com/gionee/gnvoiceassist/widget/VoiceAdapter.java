package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class VoiceAdapter extends BaseAdapter {
    private static final int MAX_ITEM_COUNT = 50;
    private ArrayList<BaseItem> mDataList = new ArrayList<BaseItem>();
    private LayoutInflater mInflater;
    private DataSetChanged mDataChangedLsn;
    
    /******************************* 构造函数 & Override *******************************/
    public VoiceAdapter(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position < 0 ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }
    
    /*********************************** 对外接口 ***********************************/
    public void add(BaseItem item) {
    	synchronized (mDataList) {
    		if(mDataList.size() > MAX_ITEM_COUNT) {
    			mDataList.remove(0);
    			if(mDataChangedLsn != null) {
    				mDataChangedLsn.removeView(0);
    			}
    		}
    		
    		mDataList.add(item);
    		if(mDataChangedLsn != null) {
    			mDataChangedLsn.addView(mDataList.size() - 1);
    		}
    	}
    }
    
    public interface DataSetChanged {
    	void addView(int position);
    	void removeView(int position);
    }
    
    
    public void setDataSetChangedLsn(DataSetChanged dsc) {
    	mDataChangedLsn = dsc;
    }
    
    /*********************************** 私有函数 ***********************************/
    private View createViewFromResource(int position, View convertView, ViewGroup parent) {
        return mDataList.get(position).getView(mInflater, parent);
    }
}