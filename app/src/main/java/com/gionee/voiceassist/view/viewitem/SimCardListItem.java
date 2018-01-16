package com.gionee.voiceassist.view.viewitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gionee.voiceassist.R;

public class SimCardListItem extends BaseItem implements OnClickListener {

	private Context mContext;
	private View mCachedView = null;
	private SelectType mType;
	
	public static enum SelectType {
		TELEPHONE,
		MESSAGE
	}
	
    public SimCardListItem(Context ctx , SelectType type) {
        mContext = ctx;
        mType = type;
    }
    
	@Override
	public View getView(LayoutInflater inflater, ViewGroup parent) {
        if(mCachedView == null) {
            mCachedView = inflater.inflate(R.layout.simcardlist_layout, parent, false);
            bindView();
        }
        return mCachedView;
	}

	public View getView() {
        if(mCachedView == null) {
            mCachedView = View.inflate(mContext,R.layout.simcardlist_layout,null);
            bindView();
        }
        return mCachedView;
	}


	@Override
	public void bindView() {
		TextView simA = (TextView) mCachedView.findViewById(R.id.simcard_1);
		TextView simB = (TextView) mCachedView.findViewById(R.id.simcard_2);
		switch (mType) {
			case TELEPHONE:
				simA.setText(mContext.getString(R.string.sima_call));
				simB.setText(mContext.getString(R.string.simb_call));
				break;
			case MESSAGE:
			default:
				simA.setText(mContext.getString(R.string.sima_send));
				simB.setText(mContext.getString(R.string.simb_send));
				break;
		}
		simA.setTag("卡1");
		simB.setTag("卡2");
		simA.setOnClickListener(this);
		simB.setOnClickListener(this);
	}

	@Override
	public void onClick() {
	}

	@Override
	public void setItemBgType(int type) {
	}

	@Override
	public void onClick(View arg0) {
	}
}
