package com.gionee.voiceassist.widget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.RenderCardPayload;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.voiceassist.R.id.tel;

public class SimpleListCardItem extends BaseItem implements OnClickListener {
	public static final String TAG = SimpleListCardItem.class.getSimpleName();
	private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";
	private Context mContext;
	private List<RenderCardPayload.ListItem> mListCardItems;
	private View mCachedView;
	private LayoutInflater mInflater;
	private ViewGroup mParent;

	/******************************* 构造函数 & Override *******************************/
	public SimpleListCardItem(Context ctx, List<RenderCardPayload.ListItem> itemList) {
		mContext = ctx;
		mListCardItems = itemList;
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

		for(RenderCardPayload.ListItem item : mListCardItems) {
			View itemView = View.inflate(mContext,R.layout.listcard_info_item_lyt, null);
			if(item == null) {
				customPanel.addView(itemView);
				continue;
			}

			setTitleTextView(item.title, itemView);
//    		setScoreImage(itemView, new String[] { "75" });
    		setContentView(item.content, itemView);
    		setDetailUrlView(item.url, itemView);
			if (item.image != null) {
				setImage(item.image.src, itemView);
			}
			final String url = item.url;
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
	private void telephoneByNum(String tel) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.fromParts("tel", tel, null));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Utils.startActivity(mContext, intent);
	}

    private void setScoreImage(View itemView, String[] score) {
    	ImageView scoreImage = ((ImageView) itemView.findViewById(R.id.score));
		if(score != null) {
			int scoreValue = Integer.parseInt(score[0]);
			LogUtil.d(TAG, "SimpleListCardItem setScoreImage scoreValue = " + scoreValue);
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
			LogUtil.e(TAG, "SimpleListCardItem setScoreImage score == null");
		}
    }

    private void setTitleTextView(String title, View itemView) {
		if(title != null) {
			LogUtil.d(TAG, "SimpleListCardItem setTitleTextView name = " + title);
			((TextView) itemView.findViewById(R.id.title)).setText(title);
		} else {
			LogUtil.d(TAG, "SimpleListCardItem setTitleTextView name == null");
		}
    }

    private void setContentView(String content, View itemView) {

    	if(TextUtils.isEmpty(content)) {
			LogUtil.e(TAG, "SimpleListCardItem setContentView content is empty");
    		return;
    	}

		String[] newTels = new String[]{content};

    	List<String> list = new ArrayList<String>();
		LinearLayout linearLayout = (LinearLayout) itemView.findViewById(tel);
		for(final String tel : newTels) {
			list.add(tel);
			View telView = View.inflate(mContext,R.layout.phone_number_call_item, null);
			TextView contentView = (TextView) telView.findViewById(R.id.number);
//			contentView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			contentView.setText(tel);
			/*contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					telephoneByNum(tel);
				}
			});*/

			linearLayout.addView(telView);
		}
		LogUtil.d(TAG, "SimpleListCardItem setContentView list = " + list);
    }

	private void setImage(String imageSrc, View itemView) {
		if(TextUtils.isEmpty(imageSrc)) {
			LogUtil.e(TAG, "SimpleListCardItem setImage imageSrc is null");
			return;
		}

		ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.image);
		imageButton.setVisibility(View.VISIBLE);
		Picasso.with(mContext)
				.load(imageSrc)
				.placeholder(R.drawable.gn_detail_item_icon_phone_normal)
				.resize(240, 160)
				.onlyScaleDown()
				.into(imageButton);
	}
    
    private void setDetailUrlView(final String detailUrl, View itemView) {
		if(detailUrl != null) {
			LogUtil.d(TAG, "SimpleListCardItem setDetailUrlView addr[0] = " + detailUrl);
			TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
			detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			detailUrlView.setText("更多信息");
			detailUrlView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					openLink(detailUrl);
				}
			});


		} else {
			LogUtil.e(TAG, "SimpleListCardItem setDetailUrlView addr == null");
		}
    }

	private void openLink(String url) {
		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
		GnVoiceAssistApplication.getInstance().startActivity(mIntent);
	}
}
