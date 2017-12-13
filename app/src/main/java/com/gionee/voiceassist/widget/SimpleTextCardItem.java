package com.gionee.voiceassist.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.sdk.module.screen.message.Link;
import com.gionee.voiceassist.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.sdk.module.screen.message.TextCardPayload;
import com.gionee.voiceassist.util.LogUtil;

import static android.R.id.list;

public class SimpleTextCardItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleTextCardItem.class.getSimpleName();
	private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";
	private Context mContext;
	private RenderCardPayload mTextCardPayload;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleTextCardItem(Context ctx, RenderCardPayload textCardPayload) {
		mContext = ctx;
		mTextCardPayload = textCardPayload;
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
		if (mCachedView == null) {
			mInflater = inflater;
			mParent = parent;
			mCachedView = inflater.inflate(R.layout.empty_note_board, parent, false);
			bindView();
		}
		return mCachedView;
	}

	public View getView() {
		LogUtil.d(TAG, "-------------------------333333333333333------------------------- ");
		if (mCachedView == null) {
			mCachedView = View.inflate(mContext, R.layout.empty_note_board, null);
			bindView();
		}
		return mCachedView;
	}

	@Override
	public void bindView() {
		LinearLayout customPanel = (LinearLayout) mCachedView.findViewById(R.id.custom_panel);

			LogUtil.d(TAG, "SimpleTextCardItem bindView ---------------1111------ ");
			View itemView = View.inflate(mContext,R.layout.text_card_info_item_lyt, null);
			LogUtil.d(TAG, "SimpleTextCardItem bindView ---------------2222------ ");
			if(mTextCardPayload == null) {
				customPanel.addView(itemView);
				return;
			}

    		setContentView(mTextCardPayload.content, itemView);
    		setMoreInfoView(mTextCardPayload.link, itemView);
	        customPanel.addView(itemView);
//		customPanel.addView(View.inflate(mContext, R.layout.restaurant_dianping_info, null));
	}

	@Override
	public void onClick() {
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void setItemBgType(int type) {
	}

	/*********************************** 私有函数 ***********************************/

    private void setContentView(String content, View itemView) {

    	if(TextUtils.isEmpty(content)) {
			LogUtil.e(TAG, "SimpleTextCardItem setContentView content is empty");
    		return;
    	}

		TextView contentView = (TextView)itemView.findViewById(R.id.content);
		contentView.setText(content);
		LogUtil.d(TAG, "SimpleTextCardItem setContentView list = " + list);
    }

    private void setMoreInfoView(final RenderCardPayload.LinkStructure link, View itemView) {
		if(link == null) {
			return;
		}

		final String linkUrl = link.url;
		if(linkUrl != null) {
			LogUtil.d(TAG, "SimpleTextCardItem setMoreInfoView addr[0] = " + linkUrl);
			TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
			detailUrlView.setVisibility(View.VISIBLE);
			detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			detailUrlView.setText(link.anchorText);
			detailUrlView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					openWebSite(linkUrl);
				}
			});
		} else {
			LogUtil.e(TAG, "SimpleTextCardItem setMoreInfoView addr == null");
		}
    }

	private void openWebSite(String url) {
		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
		GnVoiceAssistApplication.getInstance().startActivity(mIntent);
	}
}
