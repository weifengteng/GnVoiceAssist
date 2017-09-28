package com.gionee.gnvoiceassist.basefunction.recordcontrol;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.baidu.duer.dcs.util.LogUtil;
import com.baidu.duer.sdk.DcsSDK;
import com.baidu.duer.sdk.asr.AsrInterface;
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
 */

public class RecordController implements IRecordControl {
    public static final String TAG = RecordController.class.getSimpleName();
    public static final int DELAY_SHORT = 50;

    @Override
    public void stopRecord() {
        DcsSDK.getInstance().getAsr().stopRecord();
    }

    @Override
    public void cancelRecord() {
        DcsSDK.getInstance().getAsr().cancelRecord();
    }

    @Override
    public void onDestroy() {

    }

    public void startRecordOfflinePrior() {
        startRecord(AsrInterface.AsrMode.ASR_MODE_OFFLINE_PRIORITY, getOfflineAsrSlots());
    }

    public void startRecordOfflineOnly() {
        startRecord(AsrInterface.AsrMode.ASR_MODE_OFFLINE, getOfflineAsrSlots());
    }

    public void startRecordOnline() {
        startRecord(AsrInterface.AsrMode.ASR_MODE_ONLINE, null);
    }

    private void startRecord(AsrInterface.AsrMode mode, JSONObject jsonObject) {
        // Stop Speak
        DcsSDK.getInstance().getSpeak().stopSpeak();

        final AsrInterface.AsrParam asrParam = new AsrInterface.AsrParam();
        asrParam.setAsrMode(mode);
        if(jsonObject != null) {
            asrParam.setOfflineAsrSlots(jsonObject);
        }

        playBell(R.raw.ring_start);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DcsSDK.getInstance().getAsr().startRecord(asrParam);
            }
        }, DELAY_SHORT);
    }

    @SuppressLint("NewApi")
    private JSONObject getOfflineAsrSlots() {
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
