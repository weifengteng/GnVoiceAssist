package com.gionee.gnvoiceassist.usecase;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.message.io.CustomInteractGenerator;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.io.UsecaseResponseGenerator;
import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.AppLaunchMetadata;
import com.gionee.gnvoiceassist.usecase.annotation.CuiQuery;
import com.gionee.gnvoiceassist.usecase.annotation.CuiResult;
import com.gionee.gnvoiceassist.usecase.annotation.DirectiveResult;
import com.gionee.gnvoiceassist.usecase.annotation.Operation;
import com.gionee.gnvoiceassist.util.LogUtil;

import java.util.List;

/**
 * Created by liyingheng on 11/8/17.
 */

public class AppLaunchUseCase extends UseCase {

    private static final String USECASE_ALIAS = "applaunch";

    //初次请求启动应用
    public static final String ACTION_REQUEST_LAUNCH_APP = "request_launch_app";

    //没有安装应用，发起多轮交互，请求用户下载
    public static final String ACTION_QUERY_DOWNLOAD_APP = "query_download_app";
    public static final String ACTION_CUI_DOWNLOAD_APP = "cui_download_app";
    public static final String SUBACTION_CUI_CONFIRM = "confirm";
    public static final String SUBACTION_CUI_CANCEL = "cancel";


    private static final String TAG = AppLaunchUseCase.class.getSimpleName();

    private static final String GIONEE_MARKET_APK = "com.gionee.aora.market.GoMarketSearchResult";
    private static final String GIONEE_MARKET_PACKAGE_NAME = "com.gionee.aora.market";

    private Context mAppCtx;

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        AppLaunchMetadata metadata = MetadataParser.toEntity(message.getMetadata(),AppLaunchMetadata.class);
        String action = message.getAction();
        String subAction = message.getSubAction();
        switch (action) {
            case ACTION_REQUEST_LAUNCH_APP:
                //请求启动应用。此时需要判断App是否能打开？
                requestLaunchApp(metadata);
                break;
            case ACTION_CUI_DOWNLOAD_APP:
                //收到用户下载回复，未知此时下载还是取消，要判断sub_action
                cuiDownloadApp(subAction,metadata);
                break;
        }
    }

    @Override
    public String getUseCaseName() {
        return USECASE_ALIAS;
    }

    @DirectiveResult("request_launch_app")
    private void requestLaunchApp(AppLaunchMetadata metadata) {
        boolean launchSuccess = false;
        if (!TextUtils.isEmpty(metadata.getPackageName())) {
            launchSuccess = launchAppByPackageName(metadata.getPackageName());
        } else if (!TextUtils.isEmpty(metadata.getDeeplink())) {
            launchAppByDeepLink(metadata.getDeeplink());
            launchSuccess = true;
        } else if (!TextUtils.isEmpty(metadata.getAppName())) {
            launchSuccess = launchAppByName(metadata.getAppName());
        } else {
            LogUtil.e(TAG,"打开应用操作没有传递任何信息");
        }
        if (!launchSuccess) {
            queryCuiDownloadApp(metadata);
        }
    }

    @CuiResult("app_download")
    private void cuiDownloadApp(String subAction, AppLaunchMetadata metadata) {
        if (subAction.equals(SUBACTION_CUI_CONFIRM)) {
            if (!TextUtils.isEmpty(metadata.getAppName()))
                launchAppMarketAndDownload(metadata.getAppName());
        } else if (subAction.equals(SUBACTION_CUI_CANCEL)) {
            //DO-nothing
        }
    }

    @Operation("launch_app")
    private boolean launchAppByName(String appName) {
        PackageManager pManager = mAppCtx.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> pkgList = pManager.getInstalledPackages(0);
        boolean isInstalledSuccess = false;
        if (pkgList != null) {
            try {
                for (int idx = 0; idx < pkgList.size(); idx++) {
                    PackageInfo packageInfo = pkgList.get(idx);
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

    @Operation("launch_app")
    private boolean launchAppByPackageName(String packageName) {
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

    @Operation("launch_app")
    private void launchAppByDeepLink(String deepLink) {
        try {
            Uri uri = Uri.parse(deepLink);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mAppCtx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Operation("launch_download")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void launchAppMarketAndDownload(String appName) {
        Intent intent = new Intent(GIONEE_MARKET_APK);
        intent.putExtra("queryString", appName);
        intent.putExtra("TAGID","100");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mAppCtx.startActivity(intent);
    }

    /**
     * 发起请求用户下载的多轮交互
     * @param metadata
     */
    @CuiQuery("app_download")
    private void queryCuiDownloadApp(AppLaunchMetadata metadata) {
        // 多轮交互下载应用，如"没有下载该应用，下载还是取消"
        // 1.要显示"没有下载[应用名]，下载还是取消"这段文字
        // 2.要播报"没有下载该应用，下载还是取消"
        // 3.要发起多轮交互请求。等待用户回复两个选项：下载、取消
        CUIEntity customInteract = new CustomInteractGenerator(ACTION_CUI_DOWNLOAD_APP)
                .addCommand(CustomLinkSchema.LINK_APP_DOWNLOAD + "download_confirm","是","是的","好的","下载")
                .addCommand(CustomLinkSchema.LINK_APP_DOWNLOAD + "download_cancel", "不","不要","取消","不下载")
                .generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(),ACTION_QUERY_DOWNLOAD_APP)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setShouldSpeak(true)
                .setShouldRender(true)
                .setRenderContent(null) //TODO Add render content
                .generateEntity();
        sendResponse(response);
    }
}
