package com.gionee.voiceassist.directiveListener.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.baidu.duer.dcs.framework.location.Location;
import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/16.
 *
 *
 *
 */

public class LocationHandler extends BaseDirectiveListener implements Location.LocationHandler {
    private double mLongitude;
    private double mLatitude;

    private LocationManager mLocationManager;
    private Context mAppCtx;

    public LocationHandler(IBaseFunction baseFunction) {
        super(baseFunction);
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
        mLocationManager = (LocationManager) mAppCtx.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public double getLongitude() {
        //TODO: 位置信息的获取
//        LocationInterface.LocationInfo locationInfo = DcsSDK.getInstance().getBDLocation().getLocationInfo();
//        mLongitude = locationInfo.longitude;
//        mLatitude = locationInfo.latitude;
        if (ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 116.404;
        }
        return mLongitude;
    }

    @Override
    public double getLatitude() {
//        DcsSDK.getInstance().getBDLocation().requestLocation(true);
//        DcsSDK.getInstance().getBDLocation().addLocationListener(new LocationInterface.LocationListener() {
//            @Override
//            public void onReceiveLocation(LocationInterface.LocationInfo locationInfo) {
//
//            }
//
//            @Override
//            public void onError(int i) {
//
//            }
//        });
        if (ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mAppCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 39.915;
        }
//        return mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        return mLatitude;
    }

    @Override
    public String getCity() {
//        return null;
        return "北京";
    }

    @Override
    public Location.EGeoCoordinateSystem getGeoCoordinateSystem() {
        // GPS球面坐标
        return Location.EGeoCoordinateSystem.WGS84;
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {

    }
}
