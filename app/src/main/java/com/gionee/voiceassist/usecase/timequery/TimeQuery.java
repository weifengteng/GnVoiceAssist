package com.gionee.voiceassist.usecase.timequery;

import android.content.Context;
import android.text.format.Time;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tengweifeng on 9/21/17.
 */

public class TimeQuery extends BasePresenter {
    public static final String TAG = TimeQuery.class.getSimpleName();
    private static final String GN_DATE_FORMAT = "yyyy年MM月dd日";
    private Context mAppCtx;

    public TimeQuery(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    public void queryNowTime() {
        String str = new SimpleDateFormat(GN_DATE_FORMAT).format(new Date(System.currentTimeMillis()));
        Time t = new Time();
        t.setToNow();

        StringBuilder sb = new StringBuilder("[n1][y1]" + str);
        sb.insert(sb.indexOf("年") + 1, " [n2]");
        sb.append(String.format(mAppCtx.getResources().getString(R.string.gethours), String.valueOf(t.hour)));
        sb.append(String.format(mAppCtx.getResources().getString(R.string.getminutes), String.valueOf(t.minute)));
		String info = str/* + "年"*/ + String.format(mAppCtx.getResources().getString(R.string.gethours), String.valueOf(t.hour)) +
				String.format(mAppCtx.getResources().getString(R.string.getminutes), String.valueOf(t.minute));
//        String info = String.format(mAppCtx.getResources().getString(R.string.gethours), String.valueOf(t.hour)) +
//                String.format(mAppCtx.getResources().getString(R.string.getminutes), String.valueOf(t.minute));
        String viewInfo = String.format(mAppCtx.getResources().getString(R.string.gethours), String.valueOf(t.hour)) +
                String.format(mAppCtx.getResources().getString(R.string.getminutes), String.valueOf(t.minute));
        LogUtil.d(TAG, "FocusTime procXmlResult info = " + info);
//        TxtSpeakManager.getInstance().playTTS(info);
//        screenRender.renderAnswerInScreen(viewInfo);
        playAndRenderText(viewInfo, true);
    }

    @Override
    public void onDestroy() {
        // TODO:
        mAppCtx = null;
    }
}
