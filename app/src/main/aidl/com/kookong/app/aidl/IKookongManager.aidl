package com.kookong.app.aidl;
import com.kookong.app.aidl.ManagerCallback;

interface IKookongManager {
    boolean execVoiceCommand(in String voiceText, in int from,in ManagerCallback callback);
    boolean execVoiceCommandWithSpecifyDevice(in int deviceId, in String command,in ManagerCallback callback);
    String getDeviceList();
    String getCustomACStateList();
    boolean openRemote(in int deviceId, in int from);
}