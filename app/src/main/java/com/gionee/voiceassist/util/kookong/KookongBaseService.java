package com.gionee.voiceassist.util.kookong;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.util.Constants;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;
import com.kookong.app.aidl.IKookongManager;

/**
 * Created by twf on 2017/8/16.
 */

public abstract class KookongBaseService {
    protected IKookongManager mIKookongManager;
    private final Context mContext = GnVoiceAssistApplication.getInstance();
    protected String[] mDeviceLists;
    protected String[] mCustomACStateLists;

    protected KookongBaseService() {}

    public void execute() {
        boolean isKookongInstalled = Utils.isAvilible(mContext, Constants.KOOKONG_PACKAGE);
        if(isKookongInstalled) {
            Utils.bindKooKongService(mServiceConnection, mContext);
        } else {
            LogUtil.d("DCSF-Kookong", "isKookongInstalled false");
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("DCSF-Kookong", "onServiceConnected");
            mIKookongManager = IKookongManager.Stub.asInterface(service);
            if(mIKookongManager != null) {
                try {

                    updateCustomData();

                    execVoiceCommand();
                } finally {
                    if(Utils.isServiceRunning(mContext, Constants.KOOKONG_CLASSNAME)) {
                        mContext.unbindService(mServiceConnection);
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("DCSF-Kookong", "onServiceDisconnected");
            mIKookongManager = null;
        }
    };

    abstract void updateCustomData();

    abstract void execVoiceCommand();

    public String[] getDeviceLists() {
        return mDeviceLists;
    }

    public String[] getCustomACStateLists() {
        return mCustomACStateLists;
    }

    public static class SimpleKookongService extends KookongBaseService {

        @Override
        void updateCustomData() {

        }

        @Override
        void execVoiceCommand() {

        }
    }
}
