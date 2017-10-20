package com.gionee.gnvoiceassist.basefunction.recordcontrol;

import android.os.Handler;
import android.util.Log;

import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.internalApi.DcsConfig;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.util.Constants;
import com.gionee.gnvoiceassist.util.SoundPlayer;
import com.gionee.gnvoiceassist.util.Utils;
import com.gionee.gnvoiceassist.util.kookong.KookongCustomDataHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by twf on 2017/8/31.
 *
 * 语音识别控制类
 *
 * 用来控制语音识别的开始、结束与取消。
 */

public class RecordController implements IRecordControl {
    public static final String TAG = RecordController.class.getSimpleName();
    public static final int DELAY_SHORT = 50;

    public static final int ASR_TYPE_AUTO = 1;
    public static final int ASR_TYPE_TOUCH = 2;
    public static final int ASR_MODE_ONLINE = 1;
    public static final int ASR_MODE_OFFLINE = 2;
    public static final int ASR_MODE_OFFLINE_PRIORITY = 3;

    @Override
    public void stopRecord() {
        //TODO: 停止录音
//        DcsSDK.getInstance().getAsr().stopRecord();
        DcsSdkImpl.getInstance().getVoiceRequest().endVoiceRequest();
    }

    @Override
    public void cancelRecord() {
        //TODO: 取消录音
//        DcsSDK.getInstance().getAsr().cancelRecord();
        DcsSdkImpl.getInstance().getVoiceRequest().cancelVoiceRequest();
    }

    @Override
    public void onDestroy() {

    }

    public void startRecordOfflinePrior() {
//        startRecord(ASR_MODE_OFFLINE_PRIORITY, getOfflineAsrSlots());
        startRecord(ASR_MODE_OFFLINE_PRIORITY, getOfflineAsrSlots());

    }

    public void startRecordOfflineOnly() {
        startRecord(ASR_MODE_OFFLINE, getOfflineAsrSlots());
    }

    public void startRecordOnline() {
        startRecord(ASR_MODE_ONLINE, null);
    }

    private void startRecord(final int mode, JSONObject jsonObject) {
        // TODO: 当正在录音时，停止上一次录音进度

        final DcsConfig.ASRConfig asrParam = new DcsConfig.ASRConfig();
        asrParam.setAsrMode(mode);
        if(jsonObject != null) {
            asrParam.setOfflineAsrSlots(jsonObject);
        }

        playBell(R.raw.ring_start);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //参数代表是否开启句尾识别
                DcsSdkImpl.getInstance().getVoiceRequest().beginVoiceRequest(true);
            }
        }, DELAY_SHORT);
    }

    private JSONObject getOfflineAsrSlots() {
        long startTimemills = System.currentTimeMillis();
        long endTimemills = 0;
        JSONObject slotJson = new JSONObject();
        try {
            {
                Map<String, String[]> slotMap = KookongCustomDataHelper.getKookongOfflineAsrSlotMap();

                String[] deviceList = slotMap.get(Constants.SLOT_DEVICELIST);
                if(deviceList != null) {
                    JSONArray slotdataArray1 = new JSONArray(deviceList);
                    slotJson.put(Constants.SLOT_DEVICELIST, slotdataArray1);
                }

                String[] customACStateList = slotMap.get(Constants.SLOT_CUSTOMACSTATELIST);
                if(customACStateList != null) {
                    JSONArray slotDataArray2 = new JSONArray(customACStateList);
                    slotJson.put(Constants.SLOT_CUSTOMACSTATELIST, slotDataArray2);
                }

                JSONArray slotdataArray = new JSONArray();
                slotdataArray.put("相机");
                slotdataArray.put("设置");
                slotdataArray.put("相册");
                slotdataArray.put("联系人");
                // 通用识别槽位
                slotJson.put(Constants.SLOT_APPNAME, slotdataArray);
            }
            {
                JSONArray slotdataArray = new JSONArray();
                ArrayList<String> contactNameList = Utils.getAllContacts(GnVoiceAssistApplication.getInstance());
                for(String name : contactNameList) {
                    slotdataArray.put(name);
                    slotdataArray.put("杨锐");
                    slotdataArray.put("曹玉树");
                }
                // 通用识别槽位
                slotJson.put(Constants.SLOT_CONTACTNAME, slotdataArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTimemills = System.currentTimeMillis();
            Log.i("liyh","getOfflineAsrSlots() duration = " + (endTimemills - startTimemills));
            return slotJson;
        }
    }

    private void playBell(final int resId) {
        LogUtil.d(TAG, "GnVoiceService playBell resId=" + resId);
        SoundPlayer soundPlayer = SoundPlayer.getInstance();
        if(null != soundPlayer) {
            soundPlayer.playMusicSound(resId);
        }
    }
}
