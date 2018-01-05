package com.gionee.voiceassist.coreservice.datamodel;

/**
 * 打开应用场景payload。
 * 通过取得应用名称、包名或隐式uri（deeplink），打开相应的应用
 */

public class AppLaunchDirectiveEntity extends DirectiveEntity {


    private String appName = "";
    private String packageName = "";
    private String deeplink = "";
    private LaunchType launchType;

    public AppLaunchDirectiveEntity() {
        setType(Type.APPLAUNCH);
    }

    public enum LaunchType {
        BY_NAME,
        BY_DEEPLINK
    }

    /**
     * 获得打开应用的名字
     * @return 应用名称
     */
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获得打开应用的包名
     * @return 应用软件包全名
     */
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 获得deeplink（即隐式intent的uri）
     * @return
     */
    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void setLaunchType(LaunchType launchType) {
        this.launchType = launchType;
    }

    public void setAppLaunchByName(String appName, String packageName) {
        setLaunchType(LaunchType.BY_NAME);
        setAppName(appName);
        setPackageName(packageName);
    }

    public void setAppLaunchByDeeplink(String deeplink) {
        setLaunchType(LaunchType.BY_DEEPLINK);
        setDeeplink(deeplink);
    }

    @Override
    public String toString() {
        return "AppLaunchDirectiveEntity{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", deeplink='" + deeplink + '\'' +
                ", launchType=" + launchType +
                '}';
    }
}
