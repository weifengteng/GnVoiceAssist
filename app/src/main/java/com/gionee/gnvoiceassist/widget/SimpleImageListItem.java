package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.duer.dcs.devicemodule.screen.message.Image;
import com.gionee.gnvoiceassist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimpleImageListItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleImageListItem.class.getSimpleName();
	private static final int MAX_DISPLAY_ITEM = 5;
	private Context mContext;
	private List<Image> mImageList;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleImageListItem(Context ctx, List<Image> imageList) {
		mContext = ctx;
		mImageList = imageList;
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
		for(Image img : mImageList) {
			View itemView = View.inflate(mContext,R.layout.imagelist_info_item_lyt, null);
			if(img == null) {
				customPanel.addView(itemView);
				continue;
			}

			setImage(img.getSrc(), itemView);
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
