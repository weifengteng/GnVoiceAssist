package com.gionee.gnvoiceassist.util;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.baidu.duer.dcs.framework.upload.contact.IUpload;
import com.baidu.duer.dcs.framework.upload.contact.UploadImpl;
import com.baidu.duer.dcs.util.ContactsChoiceUtil;
import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gionee.gnvoiceassist.util.Constants.CHANNELNAME;
import static com.gionee.gnvoiceassist.util.Constants.CHANNELNUMBER;
import static com.gionee.gnvoiceassist.util.Constants.HEADER;
import static com.gionee.gnvoiceassist.util.Constants.KOOKONG_CLASSNAME;
import static com.gionee.gnvoiceassist.util.Constants.KOOKONG_PACKAGE;
import static com.gionee.gnvoiceassist.util.Constants.NAME;
import static com.gionee.gnvoiceassist.util.Constants.NAMESPACE;
import static com.gionee.gnvoiceassist.util.Constants.NAMESPACE_CUSTOM_CMD_NORMAL;
import static com.gionee.gnvoiceassist.util.Constants.NAMESPACE_CUSTOM_CMD_TV;
import static com.gionee.gnvoiceassist.util.Constants.PAYLOAD;

/**
 * Created by twf on 2017/8/14.
 */

public class Utils {

    public static void doUserActivity() {
//        DcsSDK.getInstance().getSystemDeviceModule().getProvider().userActivity();
    }


    public static void startActivity(Context cxt, Intent intent) {
        if (null == cxt || null == intent) {
            LogUtil.e(Utils.class, "Utils startActivity param null");
            return;
        }
        LogUtil.e(Utils.class, "Utils startActivity param cxt = " + cxt + ", intent = " + intent);

        try {
            // TODO:
            /*wakeUpSystem(cxt);
            releaseLockScreen(cxt);*/
            cxt.startActivity(intent);
        } catch (Exception e) {
            LogUtil.e(Utils.class, "Utils startActivity Exception : " + e);
        }
    }

    // TODO: 监听联系人数据库变化
    public static void uploadContacts() {
        try{
            String contacts = ContactsChoiceUtil.getAllContacts(GnVoiceAssistApplication.getInstance());
            IUpload contactUploader = new UploadImpl();
            contactUploader.uploadPhoneContacts(GnVoiceAssistApplication.getInstance(), contacts);
            LogUtil.d("DCSF", "DCSF------------- contacts=  " + contacts);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllContacts(Context context) throws JSONException {
        ArrayList<String> contactNameList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id", "display_name"}, (String)null, (String[])null, (String)null);
        if(phoneCursor != null) {
            int column = phoneCursor.getColumnIndex("display_name");

            while(phoneCursor.moveToNext() && column > -1) {
                String displayName = phoneCursor.getString(column);
                if(!TextUtils.isEmpty(displayName)) {
                    contactNameList.add(displayName);
                    LogUtil.d("DCSF", "DCSF--------------------getAllContacts  contactName = " + displayName);
                }
            }
        }
        return contactNameList;
    }

    public static String getCustomDirectiveCmdFromJson(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuffer parseResult = new StringBuffer();
        String name = "";
        String namespace = "";
        Map<String, Object> payloadMap = null;
        try {
            Map<String, Map<String, Object>> maps = objectMapper.readValue(jsonData, Map.class);
            Set<String> key = maps.keySet();
            LogUtil.d("DcsF", "map size :" + maps.size());
            Iterator<String> iterator = key.iterator();
            while (iterator.hasNext()) {
                String field = iterator.next();
                LogUtil.d("DcsF", field + ":" + maps.get(field));
                if(TextUtils.equals(field, HEADER)) {
                    Map<String, Object> headerMap = maps.get(field);
                    name = String.valueOf(headerMap.get(NAME));
                    namespace = String.valueOf(headerMap.get(NAMESPACE));
                } else if(TextUtils.equals(field, PAYLOAD)) {
                    payloadMap = maps.get(field);
                }

                // 其他指令
                if(TextUtils.equals(NAMESPACE_CUSTOM_CMD_NORMAL, namespace)) {
                    Map<String, List<String>> customDirectiveCmdMap = GnVoiceAssistApplication.getCustomDirectiveCmdMap();
                    if(!TextUtils.isEmpty(name)) {
                        List<String> slotList = customDirectiveCmdMap.get(name);
                        if(slotList != null && !slotList.isEmpty()) {
                            for(int i=0; i<slotList.size(); i++) {
                                String value = slotList.get(i);
                                if(payloadMap != null) {
                                    String cmd = String.valueOf(payloadMap.get(value));
                                    if(cmd != null) {
                                        parseResult.append(cmd);
                                    }
                                }
                            }
                        }
                    }
                // 打开电视指令
                } else if(TextUtils.equals(NAMESPACE_CUSTOM_CMD_TV, namespace)){
                    if(payloadMap != null) {
                        String channelName = String.valueOf(payloadMap.get(CHANNELNAME));
                        String channelNumber = String.valueOf(payloadMap.get(CHANNELNUMBER));
                        LogUtil.d("dcsf-Kookong", "channelName = " + channelName + " channelNumber = " + channelNumber);
                        parseResult.append(wrapTvCmd(channelName, channelNumber));
                    }
                }

                LogUtil.d("dcsf-Kookong", "parseResult= " + parseResult.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d("Kookong", "getCustomDirectiveCmdFromJson exception");
        }
        return parseResult.toString();
    }

    private static String wrapTvCmd(String channelName, String channelNumber) {
        StringBuffer wrapResult = new StringBuffer();
        if(!TextUtils.equals("null", channelName) && !TextUtils.isEmpty(channelName)) {
            wrapResult.append("调到").append(channelName);
            return wrapResult.toString();
        }
        wrapResult.append("调到").append(channelNumber).append("频道");
        return wrapResult.toString();
    }

    public static String getValueByKey(String json, String key) {
        Map<String, Map<String, Object>> maps = getHeaderAndPayloadMap(json);
        if(maps != null) {
            Map<String, Object> headerMap = maps.get(Constants.HEADER);
            Map<String, Object> payloadMap = maps.get(Constants.PAYLOAD);
            if(headerMap.containsKey(key)) {
                return String.valueOf(headerMap.get(key));
            }else if(payloadMap.containsKey(key)) {
                return String.valueOf(payloadMap.get(key));
            }
        }
        return null;
    }

    public static Map<String, String> getValuesByKeyList(String json, ArrayList<String> keylist) {
        Map<String, String> valueMap = new HashMap<>();
        Map<String, Map<String, Object>> maps = getHeaderAndPayloadMap(json);
        if(maps != null) {
            Map<String, Object> headerMap = maps.get(Constants.HEADER);
            Map<String, Object> payloadMap = maps.get(Constants.PAYLOAD);
            for(String key : keylist) {
                if(headerMap.containsKey(key)) {
                    valueMap.put(key, String.valueOf(headerMap.get(key)));
                }else if(payloadMap.containsKey(key)) {
                    valueMap.put(key, String.valueOf(headerMap.get(key)));
                }
            }
        }

        return valueMap;
    }

    public static Map<String, String> getControllerFromDeviceControlJson(String deviceCtrlJson) {
        Map<String, String> deviceCtrlCmdMap = new HashMap<>();
        Map<String, String> funcKeyValueMap = getDeviceControlMap();
        Map<String, Map<String, Object>> maps = getHeaderAndPayloadMap(deviceCtrlJson);
        if(maps != null) {
            Map<String, Object> headerMap = maps.get(Constants.HEADER);
            Map<String, Object> payloadMap = maps.get(Constants.PAYLOAD);

            String name = String.valueOf(headerMap.get(NAME));
            String nameSpace = String.valueOf(headerMap.get(NAMESPACE));
            if(TextUtils.equals(nameSpace, Constants.NAMESPACE_DEVICECONTROL)) {
                String funcKeyInPayload = funcKeyValueMap.get(name);
                String[] funcKeyInPaylaodArray = funcKeyInPayload.split(",");
                if(funcKeyInPaylaodArray.length == 1) {
                    String operator = funcKeyInPaylaodArray[0];
                    String state = String.valueOf(payloadMap.get(operator));
                    deviceCtrlCmdMap.put(Constants.FUN_STATE, state);
                    deviceCtrlCmdMap.put(Constants.FUN_OPERATOR, operator);
                } else if(funcKeyInPaylaodArray.length == 2) {
                    String operator = String.valueOf(payloadMap.get(Constants.JSON_KEY_MODE));
                    String state = "";
                    String stateKey = "";
                    if(funcKeyInPaylaodArray[0].equals(Constants.JSON_KEY_MODE)) {
                        stateKey = funcKeyInPaylaodArray[1];
                    } else {
                        stateKey = funcKeyInPaylaodArray[0];
                    }

                    state = String.valueOf(payloadMap.get(stateKey));

                    deviceCtrlCmdMap.put(Constants.FUN_STATE, state);
                    deviceCtrlCmdMap.put(Constants.FUN_OPERATOR, operator);
                }
                LogUtil.d("Dcsf", "***********************getControllerFromDeviceControlJson = " + deviceCtrlCmdMap.values().toString());
            }
        }
        return deviceCtrlCmdMap;
    }

    private static Map<String, String> deviceControlKeyValueMap = new HashMap<>();
    private static Map<String, String> getDeviceControlMap() {
        if(deviceControlKeyValueMap.isEmpty()) {
            deviceControlKeyValueMap.put(Constants.FUN_SETWIFI, Constants.JSON_KEY_WIFI);
            deviceControlKeyValueMap.put(Constants.FUN_SETBLUETOOTH, Constants.JSON_KEY_BLUETOOTH);
            deviceControlKeyValueMap.put(Constants.FUN_SETPHONEMODE, Constants.JSON_KEY_MODE + "," + Constants.JSON_KEY_PHONEMODE);
            deviceControlKeyValueMap.put(Constants.FUN_SETGPS, Constants.JSON_KEY_GPS);
            deviceControlKeyValueMap.put(Constants.FUN_SETCELLULAR, Constants.JSON_KEY_CELLULAR);
            deviceControlKeyValueMap.put(Constants.FUN_SETHOTSPOT, Constants.JSON_KEY_HOTSPOT);
            deviceControlKeyValueMap.put(Constants.FUN_SETNFC, Constants.JSON_KEY_NFC);
        }
        return deviceControlKeyValueMap;
    }

    public static Map<String, Map<String, Object>> getHeaderAndPayloadMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Object>> maps = null;
        try {
            maps = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return maps;
        }
    }

    public static Map<String, Map<String, String>> getHeaderAndPayloadMap2(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> maps = null;
        try {
            maps = objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return maps;
        }
    }

    public static boolean isPkgInstalled(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }

        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return appInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static String[] parseJsonArray(String deviceJson, String name){
        String[] deviceNames = null;
        JSONObject dataJson = null;
        try {
            JSONTokener jsonParser = new JSONTokener(deviceJson);
            JSONArray json = null;
            json = (JSONArray) jsonParser.nextValue();
            deviceNames = new String[json.length()];
            for (int i = 0; i < json.length(); i++) {
                JSONObject deviceName = (JSONObject) json.get(i);
                deviceNames[i] = deviceName.getString(name);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return deviceNames;
    }

    public static String parseJson(String msgJson, String name){
        String msg = null;
        try {
            JSONTokener jsonParser = new JSONTokener(msgJson);
            JSONObject json = (JSONObject) jsonParser.nextValue();
            msg = json.getString("msg");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return msg;
    }

    /**
     *
     * @param json
     * @param keyList object字段对应的 json 字符串中的 key 值
     * @return
     */
    public static Map<String, String> parseOfflineResult(String json, ArrayList<String> keyList) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> offlineResultMap = new HashMap<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            String nluStr = (String) rootObject.get("results_nlu");
            if (nluStr.startsWith("\ufeff")) {
                nluStr = nluStr.substring(1);
            }
            JSONObject nluObject = new JSONObject(nluStr);
            Map<String, Object> map1 = mapper.readValue(nluObject.toString(), Map.class);
            String rawText = String.valueOf(map1.get(Constants.SLOT_RAW_TEXT));
            offlineResultMap.put(Constants.SLOT_RAW_TEXT, rawText);
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)map1.get(Constants.SLOT_RESULTS);
            if(resultList != null && !resultList.isEmpty()) {
                Map<String, Object> resultMap = resultList.get(0);

                if(resultMap != null && !resultMap.isEmpty()) {
                    offlineResultMap.put(Constants.SLOT_DOMAIN, String.valueOf(resultMap.get(Constants.SLOT_DOMAIN)));
                    offlineResultMap.put(Constants.SLOT_INTENT, String.valueOf(resultMap.get(Constants.SLOT_INTENT)));
                    if(keyList == null || keyList.isEmpty()) {
                        return offlineResultMap;
                    }

                    Map<String, Object> objectMap = (Map<String, Object>) resultMap.get("object");
                    LogUtil.d("DCSF", "--------------------parseOfflineResult:  objectMap= " + objectMap.values().toString());
                    if(objectMap != null && !objectMap.isEmpty()) {
                        for(String key : keyList) {
                            offlineResultMap.put(key, (String) objectMap.get(key));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return offlineResultMap;
        }
    }

    public static List<String> getParseOfflineKeyList() {
        List<String> keyList = new ArrayList<>();
        keyList.add(Constants.SLOT_APPNAME);
        keyList.add(Constants.SLOT_CONTACTNAME);
        keyList.add(Constants.SLOT_CONTACTNAME);
        keyList.add(Constants.SLOT_CUSTOMACSTATELIST);
        keyList.add(Constants.SLOT_DEVICELIST);
        keyList.add(Constants.SLOT_OBJECT);
        return keyList;
    }

    public static void bindKooKongService(ServiceConnection mServiceConnection, Context context){
        LogUtil.d("DCSF-Util", "execute");
        Intent intentService = new Intent();
        intentService.setPackage(KOOKONG_PACKAGE);
        intentService.setClassName(KOOKONG_PACKAGE,KOOKONG_CLASSNAME);
        context.bindService(intentService, mServiceConnection, context.BIND_AUTO_CREATE);
    }

    public static boolean isServiceRunning(Context cxt, String className) {
        ActivityManager manager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        String classN = null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            classN = service.service.getClassName().toString();
            if (className.equals(classN)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTaskActivite(Context cxt, String pkgName) {
        ActivityManager manager = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (pkgName.equals(info.topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
