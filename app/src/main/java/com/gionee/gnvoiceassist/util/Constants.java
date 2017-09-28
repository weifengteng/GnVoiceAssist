package com.gionee.gnvoiceassist.util;

/**
 * Created by twf on 2017/8/14.
 */

public class Constants {
    public static final String APP_PACKAGE_NAME = "com.gionee.gnvoiceassist";
    public static final int OUTSIDE_KOOKONG_APP = 0;
    public static final String KOOKONG_PACKAGE = "com.kookong.app.gionee";
    public static final String KOOKONG_CLASSNAME = "com.kookong.app.service.KookongService";

    // namespace
    public static final String NAMESPACE_CUSTOM_CMD_NORMAL = "ai.dueros.device_interface.thirdparty.gionee.voiceassist";
    public static final String NAMESPACE_CUSTOM_CMD_TV = "ai.dueros.device_interface.tv_live";
    public static final String NAMESPACE_DEVICECONTROL = "ai.dueros.device_interface.extensions.device_control";

    // Header json key
    public static final String HEADER = "header";
    public static final String NAME = "name";
    public static final String NAMESPACE = "namespace";
    public static final String MESSAGEID = "messageId";
    public static final String DIALOGREQUESTID = "dialogRequestId";
    public static final String HANDLE_UNKNOWN_UTTERANCE = "HandleUnknownUtterance";

    // Payload tv json key
    public static final String PAYLOAD = "payload";
    public static final String CHANNELNAME = "channelName";
    public static final String CHANNELNUMBER = "channelNumber";
    public static final String TYPE = "type";
    public static final String CONTENT = "content";

    // Payload device control json key in Payload, value type is boolean
    public static final String JSON_KEY_MODE = "mode";
    public static final String JSON_KEY_WIFI = "wifi";
    public static final String JSON_KEY_BLUETOOTH = "bluetooth";
    public static final String JSON_KEY_PHONEMODE = "phoneMode";
    public static final String JSON_KEY_GPS = "gps";
    public static final String JSON_KEY_CELLULAR = "cellular";
    public static final String JSON_KEY_PHONEPOWER = "phonePower";


    // Device control function value
    // Device control function value in Header, key:name
    public static final String FUN_SETWIFI = "SetWifi";
    public static final String FUN_SETBLUETOOTH = "SetBluetooth";
    public static final String FUN_SETPHONEMODE = "SetPhoneMode";
    public static final String FUN_SETGPS = "SetGps";
    public static final String FUN_SETCELLULAR = "SetCellular";
    public static final String FUN_SETPHONEPOWER = "SetPhonePower";

    // Device control function value in Payload
    // PhoneMode  key:mode
    public static final String FUN_NO_DISTURB = "NO_DISTURB";
    public static final String FUN_AIRPLANE_MODE = "AIRPLANE_MODE";
    public static final String FUN_DRIVING_MODE = "DRIVING_MODE";
    public static final String FUN_NIGHT_MODE = "NIGHT_MODE";
    public static final String FUN_POWER_SAVING_MODE = "POWER_SAVING_MODE";
    public static final String FUN_SILENT_MODE = "SILENT_MODE";
    public static final String FUN_EYE_PROTECTION_MODE = "EYE_PROTECTION_MODE";

    // PhonePower key:mode
    public static final String FUN_RESTART = "RESTART";
    public static final String FUN_SHUTDOWN = "SHUTDOWN";


    public static final String FUN_OPERATOR = "func_operator";
    public static final String FUN_STATE = "func_state";







    // RenderCard
    public static final String TEXT_CARD = "TextCard";
    public static final String STANDARD_CARD = "StandardCard";
    public static final String LIST_CARD = "ListCard";
    public static final String IMAGELIST_CARD = "ImageListCard";

    public static final String SLOT_RAW_TEXT = "raw_text";

    public static final String SLOT_RESULTS = "results";
    public static final String SLOT_DOMAIN = "domain";
    public static final String SLOT_INTENT = "intent";
    public static final String SLOT_OBJECT = "object";

    public static final String SLOT_DEVICELIST = "customdevice";
    public static final String SLOT_CUSTOMACSTATELIST = "customacstate";
    public static final String SLOT_APPNAME = "appname";
    public static final String SLOT_CONTACTNAME = "contactname";


    public static final int MSG_SHOW_QUERY = 0x1011;
    public static final int MSG_SHOW_ANSWER = 0x1012;
    public static final int MSG_SHOW_INFO_PANEL = 0x1013;
    public static final int MSG_UPDATE_CONTACTS = 0x1014;
    public static final int MSG_UPDATE_INPUTVOLUME = 0x1015;
}
