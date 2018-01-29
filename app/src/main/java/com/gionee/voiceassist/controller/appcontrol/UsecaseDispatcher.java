package com.gionee.voiceassist.controller.appcontrol;

import android.os.Handler;
import android.os.Looper;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.usecase.alarm.AlarmUsecase;
import com.gionee.voiceassist.usecase.applaunch.AppLaunchUsecase;
import com.gionee.voiceassist.usecase.customcommand.AlipayPaymentCodeUsecase;
import com.gionee.voiceassist.usecase.customcommand.AlipayScanUsecase;
import com.gionee.voiceassist.usecase.customcommand.PailitaoUsecase;
import com.gionee.voiceassist.usecase.gnremote.GnRemoteUsecase;
import com.gionee.voiceassist.usecase.music.GNMusicUsecase;
import com.gionee.voiceassist.usecase.phonecall.PhoneCallUsecase;
import com.gionee.voiceassist.usecase.quicksetting.QuickSettingUsecase;
import com.gionee.voiceassist.usecase.remind.RemindUsecase;
import com.gionee.voiceassist.usecase.screenrender.ScreenUsecase;
import com.gionee.voiceassist.usecase.smssend.SmsSendUseCase;
import com.gionee.voiceassist.usecase.timing.StopwatchUsecase;
import com.gionee.voiceassist.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 将底层语义解析返回消息、界面反馈消息分发到具体场景的Usecase中。
 */

public class UsecaseDispatcher {

    private static final String TAG = UsecaseDispatcher.class.getSimpleName();
    private Map<String, BaseUsecase> usecaseMap;

    private AlarmUsecase alarmUsecase = new AlarmUsecase();
    private AppLaunchUsecase applaunchUsecase = new AppLaunchUsecase();
    private GNMusicUsecase gnmusicUsecase = new GNMusicUsecase();
    private GnRemoteUsecase gnRemoteUsecase = new GnRemoteUsecase();
    private RemindUsecase reminderUsecase = new RemindUsecase();
    private PhoneCallUsecase phoneCallUsecase = new PhoneCallUsecase();
    private SmsSendUseCase smsSendUseCase = new SmsSendUseCase();
    private ScreenUsecase screenUsecase = new ScreenUsecase();
    private StopwatchUsecase stopwatchUsecase = new StopwatchUsecase();
    private PailitaoUsecase pailitaoUsecase = new PailitaoUsecase();
    private AlipayScanUsecase alipayScanUsecase = new AlipayScanUsecase();
    private AlipayPaymentCodeUsecase alipayPaymentCodeUsecase = new AlipayPaymentCodeUsecase();
    private QuickSettingUsecase quickSettingUsecase = new QuickSettingUsecase();

    public UsecaseDispatcher() {
        usecaseMap = new HashMap<>();
    }

    void initUsecase() {
        //注册默认的Usecase
        installUsecase(alarmUsecase);
        installUsecase(applaunchUsecase);
        installUsecase(gnmusicUsecase);
        installUsecase(gnRemoteUsecase);
        installUsecase(reminderUsecase);
        installUsecase(phoneCallUsecase);
        installUsecase(smsSendUseCase);
        installUsecase(screenUsecase);
        installUsecase(stopwatchUsecase);
        installUsecase(pailitaoUsecase);
        installUsecase(alipayScanUsecase);
        installUsecase(alipayPaymentCodeUsecase);
        installUsecase(quickSettingUsecase);
    }

    void destroyUsecase() {
        uninstallUsecase(alarmUsecase);
        uninstallUsecase(applaunchUsecase);
        uninstallUsecase(gnmusicUsecase);
        uninstallUsecase(gnRemoteUsecase);
        uninstallUsecase(reminderUsecase);
        uninstallUsecase(phoneCallUsecase);
        uninstallUsecase(smsSendUseCase);
        uninstallUsecase(screenUsecase);
        uninstallUsecase(stopwatchUsecase);
        uninstallUsecase(pailitaoUsecase);
        uninstallUsecase(alipayScanUsecase);
        uninstallUsecase(alipayPaymentCodeUsecase);
        uninstallUsecase(quickSettingUsecase);
    }

    public void installUsecase(BaseUsecase usecase) {
        if (usecase != null && !isUsecaseInstalled(usecase.getAlias())) {
            usecaseMap.put(usecase.getAlias(), usecase);
        }
    }

    public void uninstallUsecase(BaseUsecase usecase) {
        if (usecase != null && isUsecaseInstalled(usecase.getAlias())) {
            usecaseMap.remove(usecase.getAlias());
        }
    }

    public boolean isUsecaseInstalled(String usecaseAlias) {
        return usecaseMap.get(usecaseAlias) != null;
    }

    void sendToUsecase(final DirectiveEntity payload, final String usecaseAlias) {
        if (isUsecaseInstalled(usecaseAlias)) {
            usecaseMap.get(usecaseAlias).handleDirective(payload);
        } else {
            LogUtil.e(TAG, "没有对应的 UseCase 分发别名" + usecaseAlias);
        }
    }

    void uiFeedbackToUsecase(final String uri, final String usecaseAlias) {
        if (isUsecaseInstalled(usecaseAlias)) {
            usecaseMap.get(usecaseAlias).handleUiFeedback(uri);
        } else {
            LogUtil.e(TAG, "没有对应的Usecase分发别名，无法分发界面反馈." + usecaseAlias);
        }
    }

}
