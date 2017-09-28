package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.bean.ImageListBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimpleImageListItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleImageListItem.class.getSimpleName();
	private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";
	private static final int MAX_DISPLAY_ITEM = 5;
	private Context mContext;
	private ArrayList<ImageListBean> mBeanList;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleImageListItem(Context ctx, ArrayList<ImageListBean> beanList) {
		LogUtil.d(TAG, "SimpleImageListItem beanList = " + beanList);
		mContext = ctx;
		mBeanList = beanList;
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

		int count = 0;
		for(ImageListBean bean : mBeanList) {
			LogUtil.d(TAG, "SimpleImageListItem bindView ---------------1111------ ");
			View itemView = View.inflate(mContext,R.layout.imagelist_info_item_lyt, null);
			LogUtil.d(TAG, "SimpleImageListItem bindView ---------------2222------ ");
			if(bean == null) {
				customPanel.addView(itemView);
				continue;
			}

			setImage(bean.getSrc(), itemView);
			count++;
			if(count > MAX_DISPLAY_ITEM) {
				break;
			}
	        customPanel.addView(itemView);
		}
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

	private void setImage(String imageUrl, View itemView) {
		ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
		imageView.setVisibility(View.VISIBLE);
		Picasso.with(mContext)
				.load(imageUrl)
				.placeholder(R.drawable.gn_detail_item_icon_phone_normal)
				.resize(640, 960)
				.centerInside()
				.into(imageView);
	}
}
