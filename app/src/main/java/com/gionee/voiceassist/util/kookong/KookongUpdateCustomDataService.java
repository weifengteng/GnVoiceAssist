package com.gionee.voiceassist.util.kookong;

import android.os.RemoteException;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.voiceassist.util.Utils;

/**
 * Created by twf on 2017/8/16.
 */

public class KookongUpdateCustomDataService extends KookongBaseService.SimpleKookongService {

    @Override
    protected void updateCustomData() {
        try {
            String deviceList = mIKookongManager.getDeviceList();
            LogUtil.d("DCSF-Kookong", "onResponse deviceList= " + deviceList);
            if(deviceList != null) {
                mDeviceLists = Utils.parseJsonArray(deviceList, "deviceName");
                if(null != mDeviceLists) {
                    for(int i =0;i<mDeviceLists.length;i++){
                        LogUtil.d("Kookong", "devicelist name = "+mDeviceLists[i]);
                    }
                }
            }

            String customACStateList = mIKookongManager.getCustomACStateList();
            LogUtil.d("DCSF-Kookong", "onResponse customACStateList= " + customACStateList);
            if(null != customACStateList) {
                mCustomACStateLists = Utils.parseJsonArray(customACStateList, "name");
                if(mCustomACStateLists!=null){
                    for(int i =0;i<mCustomACStateLists.length;i++){
                        LogUtil.d("DCSF-Kookong", "mCustomACStateLists mode = "+mCustomACStateLists[i]);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
