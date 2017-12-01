/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.voiceassist.sdk.module.applauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.gionee.voiceassist.sdk.module.applauncher.message.AppInfo;
import com.gionee.voiceassist.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by caoyushu01 on 2017/7/20.
 */

public class IAppLauncherImpl implements IAppLauncher {
    private static final String TAG = "IAppLauncherImpl";
    private boolean isUpdatingAppList = false;
    // 需要上报给云端的app列表
    ArrayList<AppInfo> contextAppList = new ArrayList<>();
    final Object mLock = new Object();

    public IAppLauncherImpl(Context context) {
        // 在初始化的时候更新一次应用列表
        updateAppList(context);
    }

    @Override
    public void launchAppByName(Context context, String appName) {
        PackageManager pManager = context.getApplicationContext().getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        if (paklist != null) {
            try {
                for (int idx = 0; idx < paklist.size(); idx++) {
                    PackageInfo packageInfo = paklist.get(idx);
                    String appNameTmp = packageInfo.applicationInfo.loadLabel(pManager).toString().toLowerCase();
                    if (appName.contains(appNameTmp) || appNameTmp.contains(appName)) {
                        String packageName = packageInfo.applicationInfo.packageName;
                        launchAppByPackageName(context, packageName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void launchAppByPackageName(Context context, String packageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveinfoList.size() == 0) {
            LogUtil.e(TAG, "launch app error!");
            return;
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
            context.startActivity(intent);
        }
    }

    @Override
    public List<AppInfo> getAppList() {
        return contextAppList;
    }

    @Override
    public void updateAppList(final Context context) {
        if (isUpdatingAppList) {
            LogUtil.w(TAG, "updateAppList-isAppListUpdateing");
            return;
        }

        final CountDownLatch countDownLock = new CountDownLatch(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isUpdatingAppList = true;
                countDownLock.countDown();

                try {
                    ArrayList<AppInfo> tempAppList = new ArrayList<>();
                    PackageManager pManager = context.getApplicationContext().getPackageManager();
                    // 获取手机内所有应用
                    List<PackageInfo> paklist = pManager.getInstalledPackages(0);
                    if (paklist != null) {
                        try {
                            for (int idx = 0; idx < paklist.size(); idx++) {
                                PackageInfo packageInfo = paklist.get(idx);

                                String appName = packageInfo.applicationInfo.loadLabel(pManager).toString().toLowerCase();
                                String packageName = packageInfo.applicationInfo.packageName;
                                String versionCode = packageInfo.versionCode + "";
                                String versionName = packageInfo.versionName + "";
                                boolean isNeedCache = filterAppNameToList(appName, packageName);
                                if (isNeedCache) {
                                    AppInfo appInfo = new AppInfo(appName, packageName, versionCode, versionName);
                                    tempAppList.add(appInfo);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //复制到applist中
                        synchronized (mLock) {
                            contextAppList.clear();
                            contextAppList.addAll(tempAppList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isUpdatingAppList = false;
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            countDownLock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchAppByDeepLink(Context context, String deepLink) {
        try {
            Uri uri = Uri.parse(deepLink);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加appName过滤准则，并不是全部的AppName都要缓存。只有需要通过context上传给服务端的才需要缓存
     *
     * @param appName
     * @param packageName
     */
    private boolean filterAppNameToList(String appName, String packageName) {
        if (!TextUtils.isEmpty(appName)
                && !TextUtils.isEmpty(packageName)
                && ("百度地图".contains(appName)
                || ("高德地图").equals(appName)
                || appName.contains("百度地图")
                || appName.contains("高德地图"))
                ) {
            return true;
        }
        return false;
    }

}
