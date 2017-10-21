package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.sdk.module.screen.message.Image;
import com.gionee.gnvoiceassist.sdk.module.screen.message.Link;
import com.gionee.gnvoiceassist.sdk.module.screen.message.RenderCardPayload;
import com.gionee.gnvoiceassist.sdk.module.screen.message.StandardCardPayload;
import com.squareup.picasso.Picasso;

import static android.R.id.list;

public class SimpleStandardCardItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleStandardCardItem.class.getSimpleName();
	private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";
	private Context mContext;
	private RenderCardPayload mStandardCardPayload;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleStandardCardItem(Context ctx, RenderCardPayload standardCardPayload) {
		mContext = ctx;
		mStandardCardPayload = standardCardPayload;
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

			LogUtil.d(TAG, "SimpleStandardCardItem bindView ---------------1111------ ");
			View itemView = View.inflate(mContext,R.layout.standard_card_info_item_lyt, null);
			LogUtil.d(TAG, "SimpleStandardCardItem bindView ---------------2222------ ");
			if(mStandardCardPayload == null) {
				customPanel.addView(itemView);
				return;
			}

			setNameTextView(mStandardCardPayload.title, itemView);
    		setContentView(mStandardCardPayload.content, itemView);
    		setMoreInfoView(mStandardCardPayload.link, itemView);
			setImage(mStandardCardPayload.image, itemView);
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

    private void setNameTextView(String name, View itemView) {
		if(name != null) {
			LogUtil.d(TAG, "SimpleStandardCardItem setNameTextView name = " + name);
			((TextView) itemView.findViewById(R.id.title)).setText(name);
		} else {
			LogUtil.d(TAG, "SimpleStandardCardItem setNameTextView name == null");
		}
    }

    private void setContentView(String content, View itemView) {

    	if(TextUtils.isEmpty(content)) {
			LogUtil.e(TAG, "SimpleStandardCardItem setContentView content is empty");
    		return;
    	}

		TextView contentView = (TextView)itemView.findViewById(R.id.content);
		contentView.setText(content);

		LogUtil.d(TAG, "SimpleStandardCardItem setContentView list = " + list);
    }

	private void setImage(RenderCardPayload.ImageStructure image, View itemView) {
		if(image == null) {
			LogUtil.d(TAG, "SimpleStandardCardItem setImage image = " + list);
			return;
		}
		ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.image);
		imageButton.setVisibility(View.VISIBLE);
		Picasso.with(mContext)
				.load(image.src)
				.placeholder(R.drawable.gn_detail_item_icon_phone_normal)
				.resize(1240, 1563)
				.onlyScaleDown()
				.into(imageButton);
	}
    
    private void setMoreInfoView(final RenderCardPayload.LinkStructure link, View itemView) {
		if(link != null) {
			TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
			detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			detailUrlView.setText(link.anchorText);
			detailUrlView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					openLink(link.url);
				}
			});
		} else {
			LogUtil.e(TAG, "SimpleStandardCardItem setMoreInfoView link == null");
		}
    }

	private void openLink(String url) {
		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
		GnVoiceAssistApplication.getInstance().startActivity(mIntent);
	}
}
