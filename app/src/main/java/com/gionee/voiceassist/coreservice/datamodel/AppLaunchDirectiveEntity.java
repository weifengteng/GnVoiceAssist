package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 1/3/18.
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
