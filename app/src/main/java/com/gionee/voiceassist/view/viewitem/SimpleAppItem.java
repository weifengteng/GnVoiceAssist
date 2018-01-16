package com.gionee.voiceassist.view.viewitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.util.LogUtil;

public class SimpleAppItem extends BaseItem {
	public static final String TAG = SimpleAppItem.class.getSimpleName();
	private String mTips;
	private Context mContext;
	private View mView;
	private Button mOkButton;
	private Button mFailButton;
	private TextView mInfoPanel;
	private LinearLayout mButtonBar;

	public static enum ViewType {
		INFO_PANEL,
		CONFERMBUTTON,
		CANCEL_BUTTON,
	}

	/******************************* 构造函数 & Override *******************************/
	public SimpleAppItem(Context context) {
		super();
		mContext = context;
	}

	public SimpleAppItem(String tips, Context context) {
		super();
		LogUtil.d(TAG, "SimplePowerResetConfirmItem tips = " + tips);
		mTips = tips;
		mContext = context;
	}

	public SimpleAppItem(int tipsId, Context context) {
		super();
		LogUtil.d(TAG, "SimplePowerResetConfirmItem tipsId = " + tipsId);
		mContext = context;
		mTips = context.getResources().getString(tipsId);
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
		if (null == mView) {
			try{
				mView = inflater.inflate(R.layout.simple_power_layout, parent, false);
				bindView();
			} catch(Exception e) {
				LogUtil.e(TAG, "SimplePowerResetConfirmItem getView Exception : " + e);
			}
		}
		return mView;
	}

	public View getView() {
		if (null == mView) {
			try{
				mView = View.inflate(mContext,R.layout.simple_app_layout,null);
				bindView();
			} catch(Exception e) {
				LogUtil.d(TAG, "SimplePowerResetConfirmItem getView Exception : " + e);
			}
		}
		return mView;
	}

	@Override
	public void bindView() {
		mInfoPanel = (TextView) mView.findViewById(R.id.info_panel);
		mInfoPanel.setText(mTips);
		mOkButton = (Button) mView.findViewById(R.id.confirm);
		mFailButton = (Button) mView.findViewById(R.id.cancle);
		mButtonBar = (LinearLayout) mView.findViewById(R.id.button_bar);
	}

	@Override
	public void onClick() {
	}

	@Override
	public void setItemBgType(int type) {
	}
	
	/*********************************** 对外接口 ***********************************/
	public View getViewByType(ViewType type) {
		switch(type) {
			case CANCEL_BUTTON:
				return mFailButton;
			case CONFERMBUTTON:
				return mOkButton;
			case INFO_PANEL:
			default:
				return mInfoPanel;
		}
	}
	
	public void SetTipsText(String tips) {
		LogUtil.d(TAG, "SimplePowerResetConfirmItem SetTipsText tips = " + tips);
		mTips = tips;
		if(mInfoPanel != null){
			mInfoPanel.setText(tips);
		}
	}

	public void dismissButtonBar(){
		if(mButtonBar != null && mButtonBar.getVisibility() == View.VISIBLE){
			mButtonBar.setVisibility(View.GONE);
		}
	}
}
