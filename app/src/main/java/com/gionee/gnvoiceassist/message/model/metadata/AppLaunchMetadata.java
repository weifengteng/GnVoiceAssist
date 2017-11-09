package com.gionee.gnvoiceassist.message.model.metadata;

/**
 * 应用打开操作 元数据解析结构
 */

public class AppLaunchMetadata extends Metadata{

    //应用名称
    private String appName;

    //应用包名
    private String packageName;

    //应用深度链接（如打开地图所用的目的地信息）
    private String deeplink;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }
}
