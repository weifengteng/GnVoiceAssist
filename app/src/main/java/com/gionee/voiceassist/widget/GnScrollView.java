package com.gionee.voiceassist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.widget.VoiceAdapter.DataSetChanged;

public class GnScrollView extends ScrollView implements DataSetChanged, OnClickListener {
	
	private VoiceAdapter mVoiceAdapter;
	private LayoutInflater mInflater;
	private LinearLayout mBox;
	private View mViewTouched;

	/******************************* 构造函数 & Override *******************************/
	public GnScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int N = getChildCount();
		
		for(int i=0; i<N; ++i) {
			View child = getChildAt(i);
			if("scrollself".equals((String)child.getTag())) {
		        if(isOutOfChild(ev, child)) {
		        	continue;
		        } else {
		        	mViewTouched = child;
		        	return false;
		        }
			}
		}

		mViewTouched = null;
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public void addView(int position) {
		BaseItem newItem = (BaseItem)mVoiceAdapter.getItem(position);
		View newView = null;
		
		if(isBubbleBgItem(newItem)) {
			newView = newItem.getView(mInflater, mBox);
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) newView.getLayoutParams();
			int upOffset = getResources().getDimensionPixelSize(R.dimen.bubble_gap_height);
			lp.setMargins(lp.leftMargin, upOffset, lp.rightMargin, lp.bottomMargin);
		} else {
			newView = newItem.getView(mInflater, mBox);
		}
		mBox.addView(newView, mBox.getChildCount()-1);
		newView.setOnClickListener(this);
		newView.setTag(newItem);
	}
	
	@Override
	public void onClick(View v) {
		Object obj = v.getTag();
		if (obj instanceof BaseItem) {
			BaseItem item = (BaseItem) obj;
			item.onClick();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(mViewTouched != null && mViewTouched.dispatchTouchEvent(ev)) {
			return true;
		}

		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mBox = (LinearLayout)getChildAt(0);
	}
	
	@Override
	public void removeView(int position) {
	}

	/*********************************** 对外接口 ***********************************/
	public void setAdapter (ListAdapter adapter) {
		mVoiceAdapter = (VoiceAdapter)adapter;
		mVoiceAdapter.setDataSetChangedLsn(this);
	}
	
	/*********************************** 私有函数 ***********************************/
	private boolean isBubbleBgItem(BaseItem item) {
		return item instanceof SimpleInfoItem;
	}

	private boolean isOutOfChild(MotionEvent ev, View child) {
		int pos[] = new int[2];
		int x = (int)ev.getRawX();
		int y = (int)ev.getRawY();
		child.getLocationOnScreen(pos);
		
		return x < pos[0] || x > pos[0] + child.getWidth() || y < pos[1] || y > pos[1] + child.getHeight();
	}
}
