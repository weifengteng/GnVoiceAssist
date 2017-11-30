package com.gionee.gnvoiceassist;

import android.app.Application;

import com.baidu.duer.dcs.framework.internalapi.DcsConfig;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.directiveListener.telecontroller.TeleControllerListener;
import com.gionee.gnvoiceassist.util.Constants;
import com.squareup.leakcanary.LeakCanary;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by twf on 2017/8/4.
 */

public class GnVoiceAssistApplication extends Application{
    public static final String TAG = GnVoiceAssistApplication.class.getSimpleName();
    private static volatile GnVoiceAssistApplication instance = null;
    private static Map<String, List<String>> customDirectiveCmdMap;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate1111");
        instance = this;
        LeakCanary.install(this);
//        String clientId = "83kW99iEz0jpGp9hrX981ezGcTaxNzk0";
//        String clientSecret = "UTjgedIE5CRZM3CWj2cApLKajeZWotvf";
//        DcsSDK.getInstance().initSDK(clientId, clientSecret, this);
    }

    public static GnVoiceAssistApplication getInstance() {
        return instance;
    }

    public static Map<String, List<String>> getCustomDirectiveCmdMap() throws IOException{
        if(customDirectiveCmdMap == null || customDirectiveCmdMap.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = TeleControllerListener.class.getResourceAsStream("/assets/customDirectiveCmd.json");
            customDirectiveCmdMap = objectMapper.readValue(inputStream, Map.class);
        }
        return customDirectiveCmdMap;
    }

    //全局控制
    public static final int ASR_MODE = DcsConfig.ASR_MODE_OFFLINE_PRIORITY;
    public static final int TTS_MODE = Constants.TTS_MODE_OFFLINE;

}
