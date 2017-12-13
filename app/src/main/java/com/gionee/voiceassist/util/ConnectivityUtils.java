package com.gionee.voiceassist.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by liyingheng on 12/13/17.
 */

public class ConnectivityUtils {

    /**
     * 判断设备是否在线
     * @return 设备网络状态。true为在线，false为离线
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
