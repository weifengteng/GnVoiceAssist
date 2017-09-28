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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.bean.StandardCardItemBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.id.list;

public class SimpleStandardCardItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleStandardCardItem.class.getSimpleName();
	private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";
	private Context mContext;
	private StandardCardItemBean mStandardCardBean;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleStandardCardItem(Context ctx, StandardCardItemBean beanList) {
		LogUtil.d(TAG, "SimpleStandardCardItem beanList = " + beanList);
		mContext = ctx;
		mStandardCardBean = beanList;
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
			if(mStandardCardBean == null) {
				customPanel.addView(itemView);
				return;
			}

			setNameTextView(mStandardCardBean.getTitle(), itemView);
    		setContentView(mStandardCardBean.getContent(), itemView);
    		setMoreInfoView(mStandardCardBean.getLink().anchorText, mStandardCardBean.getLink().url, itemView);
			setImage(mStandardCardBean.getImage().src, itemView);
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

    private void setScoreImage(View itemView, String[] score) {
    	ImageView scoreImage = ((ImageView) itemView.findViewById(R.id.score));
//		scoreImage.setVisibility(View.INVISIBLE);
		if(score != null) {
			int scoreValue = Integer.parseInt(score[0]);
			LogUtil.d(TAG, "SimpleStandardCardItem setScoreImage scoreValue = " + scoreValue);
			if(scoreValue <= 0) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_0);
			} else if(scoreValue <= 25) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_1);
			} else if(scoreValue <= 50) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_2);
			} else if(scoreValue <= 75) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_3);
			} else if(scoreValue <= 100) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_4);
			} else if(scoreValue == 100) {
				scoreImage.setImageResource(R.drawable.restaurant_score_star_5);
			}
		} else {
			LogUtil.e(TAG, "SimpleStandardCardItem setScoreImage score == null");
		}
    }

    private void setNameTextView(String name, View itemView) {
		if(name != null) {
			LogUtil.d(TAG, "SimpleStandardCardItem setNameTextView name = " + name);
			((TextView) itemView.findViewById(R.id.title)).setText(name);
		} else {
			LogUtil.d(TAG, "SimpleStandardCardItem setNameTextView name == null");
		}
    }
    
    private String[] getNewTels(String[] tels) {
    	ArrayList<String> aList = new ArrayList<String>();
    	
    	for(String tel : tels) {
    		if(!TextUtils.isEmpty(tel.trim())) {
    			aList.add(tel);
    		}
    	}

		LogUtil.d(TAG, "SimpleStandardCardItem getNewTels aList = " + aList);
    	return (String[]) aList.toArray(new String[0]);
    }
    
    private void setContentView(String content, View itemView) {

    	if(TextUtils.isEmpty(content)) {
			LogUtil.e(TAG, "SimpleStandardCardItem setContentView content is empty");
    		return;
    	}

    	/*final String[] newTels = getNewTels(tels);
		LogUtil.d(TAG, "SimpleStandardCardItem setContentView newTels = " + newTels);
    	if(newTels.length == 0) {
			LogUtil.e(TAG, "SimpleStandardCardItem setContentView newTels.length == 0");
    		return;
    	}*/

/*		String[] newTels = new String[]{content};

    	List<String> list = new ArrayList<String>();
		LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.tel);
		for(final String tel : newTels) {
			list.add(tel);
			View telView = View.inflate(mContext,R.layout.phone_number_call_item, null);
			TextView contentView = (TextView) telView.findViewById(R.id.number);
//			contentView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			contentView.setText(tel);
			*//*contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					telephoneByNum(tel);
				}
			});*//*

			linearLayout.addView(telView);
		}*/
		TextView contentView = (TextView)itemView.findViewById(R.id.content);
		contentView.setText(content);

		LogUtil.d(TAG, "SimpleStandardCardItem setContentView list = " + list);
    }

	private void setImage(String imageUrl, View itemView) {
		if(TextUtils.isEmpty(imageUrl)) {

			return;
		}
		ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.image);
		imageButton.setVisibility(View.VISIBLE);
		Picasso.with(mContext)
				.load(imageUrl)
				.placeholder(R.drawable.gn_detail_item_icon_phone_normal)
				.resize(1240, 1563)
				.onlyScaleDown()
				.into(imageButton);


//		imageButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
////				telephoneByNum(newTels[0]);
//			}
//		});
	}
    
    private void setMoreInfoView(final String moreText, final String detailUrl, View itemView) {
		if(detailUrl != null) {
			LogUtil.d(TAG, "SimpleStandardCardItem setMoreInfoView addr[0] = " + detailUrl);
//			((TextView) itemView.findViewById(R.id.addr)).setText(addr);
			TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
			detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			detailUrlView.setText(moreText);
			detailUrlView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					telephoneByNum(tel);
					openWebSite(detailUrl);
				}
			});


		} else {
			LogUtil.e(TAG, "SimpleStandardCardItem setMoreInfoView addr == null");
		}
    }

	private void openWebSite(String url) {
		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
		GnVoiceAssistApplication.getInstance().startActivity(mIntent);
	}
}
