package com.gionee.voiceassist.systemctrl;

import com.gionee.voiceassist.systemctrl.iface.IPhonecallCtrl;
import com.gionee.voiceassist.systemctrl.iface.IScreenshotCtrl;
import com.gionee.voiceassist.systemctrl.iface.ISmsCtrl;
import com.gionee.voiceassist.systemctrl.iface.ISwitchCtrl;
import com.gionee.voiceassist.systemctrl.impl.PhonecallCtrlImpl;
import com.gionee.voiceassist.systemctrl.impl.ScreenshotCtrlImpl;
import com.gionee.voiceassist.systemctrl.impl.SmsCtrlImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.AirplanemodeSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.FlashlightSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.GamemodeSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.LocationSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.MobiledataSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.NfcSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.NotdisturbSwitchImpl;
import com.gionee.voiceassist.systemctrl.impl.sysswitch.WifiSwitchImpl;

/**
 * Created by liyingheng on 12/6/17.
 */

public class SystemCtrlManager {

    IPhonecallCtrl phonecallCtrl;
    IScreenshotCtrl screenshotCtrl;
    ISmsCtrl smsCtrl;
    ISwitchCtrl airplaneModeSwitch;
    ISwitchCtrl flashlightSwitch;
    ISwitchCtrl gamemodeSwitch;
    ISwitchCtrl locationSwitch;
    ISwitchCtrl mobileDataSwitch;
    ISwitchCtrl nfcSwitch;
    ISwitchCtrl notDisturbSwitch;
    ISwitchCtrl wifiSwitch;




    public SystemCtrlManager() {

    }

    public IPhonecallCtrl getPhonecallCtrl() {
        if (phonecallCtrl == null) {
            phonecallCtrl = new PhonecallCtrlImpl();
        }
        return phonecallCtrl;
    }

    public IScreenshotCtrl getScreenshotCtrl() {
        if (screenshotCtrl == null) {
            screenshotCtrl = new ScreenshotCtrlImpl();
        }
        return screenshotCtrl;
    }

    public ISmsCtrl getSmsCtrl() {
        if (smsCtrl == null) {
            smsCtrl = new SmsCtrlImpl();
        }
        return smsCtrl;
    }

    public ISwitchCtrl getAirplaneModeSwitch() {
        if (airplaneModeSwitch == null) {
            airplaneModeSwitch = new AirplanemodeSwitchImpl();
        }
        return airplaneModeSwitch;
    }

    public ISwitchCtrl getFlashlightSwitch() {
        if (flashlightSwitch == null) {
            flashlightSwitch = new FlashlightSwitchImpl();
        }
        return flashlightSwitch;
    }

    public ISwitchCtrl getGamemodeSwitch() {
        if (gamemodeSwitch == null) {
            gamemodeSwitch = new GamemodeSwitchImpl();
        }
        return gamemodeSwitch;
    }

    public ISwitchCtrl getLocationSwitch() {
        if (locationSwitch == null) {
            locationSwitch = new LocationSwitchImpl();
        }
        return locationSwitch;
    }

    public ISwitchCtrl getMobileDataSwitch() {
        if (mobileDataSwitch == null) {
            mobileDataSwitch = new MobiledataSwitchImpl();
        }
        return mobileDataSwitch;
    }

    public ISwitchCtrl getNfcSwitch() {
        if (nfcSwitch == null) {
            nfcSwitch = new NfcSwitchImpl();
        }
        return nfcSwitch;
    }

    public ISwitchCtrl getNotDisturbSwitch() {
        if (notDisturbSwitch == null) {
            notDisturbSwitch = new NotdisturbSwitchImpl();
        }
        return notDisturbSwitch;
    }

    public ISwitchCtrl getWifiSwitch() {
        if (notDisturbSwitch == null) {
            wifiSwitch = new WifiSwitchImpl();
        }
        return wifiSwitch;
    }
}
