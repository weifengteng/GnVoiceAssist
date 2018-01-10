package com.gionee.voiceassist.controller.appcontrol;

import android.os.Handler;
import android.os.Looper;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.usecase.alarm.AlarmUsecase;
import com.gionee.voiceassist.usecase.applaunch.AppLaunchUsecase;
import com.gionee.voiceassist.usecase.gnremote.GnRemoteUsecase;
import com.gionee.voiceassist.usecase.music.GNMusicUsecase;
import com.gionee.voiceassist.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyingheng on 1/9/18.
 */

public class UsecaseDispatcher {

    private static final String TAG = UsecaseDispatcher.class.getSimpleName();
    private Map<String, BaseUsecase> usecaseMap;

    private AlarmUsecase alarmUsecase = new AlarmUsecase();
    private AppLaunchUsecase applaunchUsecase = new AppLaunchUsecase();
    private GNMusicUsecase gnmusicUsecase = new GNMusicUsecase();
    private GnRemoteUsecase gnRemoteUsecase = new GnRemoteUsecase();

    public UsecaseDispatcher() {
        usecaseMap = new HashMap<>();
    }

    void initUsecase() {
        //注册默认的Usecase
        installUsecase(alarmUsecase);
        installUsecase(applaunchUsecase);
        installUsecase(gnmusicUsecase);
        installUsecase(gnRemoteUsecase);
    }

    void destroyUsecase() {
        uninstallUsecase(alarmUsecase);
        uninstallUsecase(applaunchUsecase);
        uninstallUsecase(gnmusicUsecase);
        uninstallUsecase(gnRemoteUsecase);
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
            LogUtil.e(TAG, "没有对应的Usecase分发。别名" + usecaseAlias);
        }
    }

}
