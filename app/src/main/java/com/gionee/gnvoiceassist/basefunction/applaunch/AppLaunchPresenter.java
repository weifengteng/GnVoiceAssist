package com.gionee.gnvoiceassist.basefunction.applaunch;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.basefunction.BasePresenter;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.basefunction.recordcontrol.RecordController;
import com.gionee.gnvoiceassist.directiveListener.customuserinteraction.CustomUserInteractionManager;
import com.gionee.gnvoiceassist.widget.SimpleAppItem;

import java.util.List;

/**
 * Created by tengweifeng on 9/12/17.
 */

public class AppLaunchPresenter extends BasePresenter {
    public static final String TAG = AppLaunchPresenter.class.getSimpleName();
    private static final String GIONEE_MARKET_APK = "com.gionee.aora.market.GoMarketSearchResult";
    private static final String GIONEE_MARKET_PACKAGE_NAME = "com.gionee.aora.market";
    public static final String UTTER_CONFIRM_DOWNLOAD = "utter_confirm_download";
    private RecordController recordController;
    private SimpleAppItem simpleAppItem;
    private Context mAppCtx;
    private String appName;

    public AppLaunchPresenter(IBaseFunction baseFunction) {
        super(baseFunction);
        this.recordController = baseFunction.getRecordController();
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    @Override
    public void onSpeakFinish(String utterId) {
        super.onSpeakFinish(utterId);
        if(TextUtils.equals(utterId, UTTER_CONFIRM_DOWNLOAD)) {
            launchAppMarketAndDownload();
        }
    }

    @Override
    public void onDestroy() {

    }

    public boolean launchAppByName(String appName) {
        this.appName = appName;
        PackageManager pManager = mAppCtx.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        boolean isInstalledSuccess = false;
        if (paklist != null) {
            try {
                for (int idx = 0; idx < paklist.size(); idx++) {
                    PackageInfo packageInfo = paklist.get(idx);
                    String appNameTmp = packageInfo.applicationInfo.loadLabel(pManager).toString().toLowerCase();
                    if (appName.contains(appNameTmp) || appNameTmp.contains(appName)) {
                        String packageName = packageInfo.applicationInfo.packageName;
                        isInstalledSuccess = launchAppByPackageName(packageName);
                        if(isInstalledSuccess) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isInstalledSuccess;
    }

    public boolean launchAppByPackageName(String packageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        boolean isPkgExist = true;
        try {
            packageinfo = mAppCtx.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mAppCtx.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveinfoList.size() == 0) {
            LogUtil.e(TAG, "launch app error!");
            return false;
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String name = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(name, className);
            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mAppCtx.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                isPkgExist = false;
                e.printStackTrace();
            }
        } else {
            isPkgExist = false;
        }
        return isPkgExist;
    }

    public void disappearSelectButton() {
        simpleAppItem.dismissButtonBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void launchAppMarketAndDownload() {
        Intent intent = new Intent(GIONEE_MARKET_APK);
        intent.putExtra("queryString", appName);
        intent.putExtra("TAGID","100");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mAppCtx.startActivity(intent);
    }

    public void cancelDownload() {
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
        disappearSelectButton();
        String text = mAppCtx.getResources().getString(R.string.app_download_cancel, appName);
        playAndRenderText(text, true);
    }

    public void confirmDownload() {
        CustomUserInteractionManager.getInstance().setStopCurrentInteraction(true);
        disappearSelectButton();
        String text = mAppCtx.getResources().getString(R.string.app_download, appName);
        playAndRenderText(text, UTTER_CONFIRM_DOWNLOAD, this, true);
    }

    public void tipInstallOrCancel(){
        String info = mAppCtx.getString(R.string.rsp_app_not_found);
        simpleAppItem = new SimpleAppItem(mAppCtx);
        View view = simpleAppItem.getView();
        simpleAppItem.SetTipsText(info);
        screenRender.renderInfoPanel(view);
        simpleAppItem.getViewByType(SimpleAppItem.ViewType.INFO_PANEL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LogUtil.d(TAG, "FocusPower drawViewOnIdleAndTts INFO_PANEL");
            }
        });

        simpleAppItem.getViewByType(SimpleAppItem.ViewType.CANCEL_BUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LogUtil.d(TAG, "FocusPower drawViewOnIdleAndTts CANCEL_BUTTON");
                recordController.cancelRecord();
                cancelDownload();
            }
        });

        simpleAppItem.getViewByType(SimpleAppItem.ViewType.CONFERMBUTTON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LogUtil.d(TAG, "FocusPower drawViewOnIdleAndTts CONFERMBUTTON");
                recordController.cancelRecord();
                confirmDownload();
            }
        });
    }
}
