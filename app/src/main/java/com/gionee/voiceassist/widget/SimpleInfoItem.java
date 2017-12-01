package com.gionee.voiceassist.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.voiceassist.R;

public class SimpleInfoItem extends BaseItem {
	public static final String TAG = SimpleInfoItem.class.getSimpleName();
    private static final String GN_BROWSER_PACKAGE_NAME = "com.android.browser";
    private String mInfo;
    private int mBgType;
	private String mQuestion;
	private String mUrl;
    private Context mContext;
    private View mCachedView;
    
    /******************************* 构造函数 & Override *******************************/
    public SimpleInfoItem(Context ctx, String info) {
    	LogUtil.d(TAG, "SimpleInfoItem info = " + info);
        mContext = ctx;
        mInfo = info;
    }
    
    public SimpleInfoItem(Context ctx, int infoId) {
    	this(ctx, ctx.getString(infoId));
    }
    
	public SimpleInfoItem(Context ctx, String question, String url) {
		LogUtil.d(TAG, "SimpleInfoItem question = " + question + " , url = " + url);
		mContext = ctx;
		mQuestion = question;
		mUrl = url;
	}
    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        if(mCachedView == null) {
            mCachedView = inflater.inflate(R.layout.simple_host_info, parent, false);
            bindView();
        }
        return mCachedView;
    }

    @Override
    public void bindView() {
        TextView tv = (TextView)mCachedView.findViewById(R.id.info);
		if (mUrl == null) {
			LogUtil.e(TAG, "SimpleInfoItem bindView mUrl == null");
			tv.setText(mInfo);
			return;
		}

		tv.append(mContext.getString(R.string.unknown_info));
		tv.append(Html.fromHtml("<a href=\"\">" + mQuestion + "</a>"));
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doAction();
			}
		});
	}

    
    @Override
    public void onClick() {
    	if(mUrl != null){
			doAction();   		
    	}
    }
    
    /*********************************** 对外接口 ***********************************/
    public void setItemBgType(int type ) {
		LogUtil.d(TAG, "SimpleInfoItem setItemBgType type = " + type );
    	mBgType = type;
    }

    /*********************************** 私有函数 ***********************************/
    private void doAction() {
    	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
    	intent.addCategory(Intent.CATEGORY_BROWSABLE);
    	intent.setPackage(GN_BROWSER_PACKAGE_NAME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
//    	Utils.startActivity(mContext, intent);
		LogUtil.d(TAG, "SimpleInfoItem doAction mUrl = " + mUrl);
    }
}
