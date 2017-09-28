package com.gionee.gnvoiceassist.directiveListener.location;

import com.baidu.duer.dcs.framework.location.Location;
import com.baidu.duer.sdk.DcsSDK;
import com.baidu.duer.sdk.location.LocationInterface;
import com.gionee.gnvoiceassist.basefunction.IBaseFunction;
import com.gionee.gnvoiceassist.directiveListener.BaseDirectiveListener;

/**
 * Created by twf on 2017/8/16.
 */

public class LocationHandler extends BaseDirectiveListener implements Location.LocationHandler {
    private double mLongitude;
    private double mLatitude;

    public LocationHandler(IBaseFunction baseFunction) {
        super(baseFunction);
    }

    @Override
    public double getLongitude() {
        DcsSDK.getInstance().getBDLocation().requestLocation(false);
        LocationInterface.LocationInfo locationInfo = DcsSDK.getInstance().getBDLocation().getLocationInfo();
        mLongitude = locationInfo.longitude;
        mLatitude = locationInfo.latitude;
//        return 116.404;
        return mLongitude;
    }

    @Override
    public double getLatitude() {
        DcsSDK.getInstance().getBDLocation().requestLocation(true);
        DcsSDK.getInstance().getBDLocation().addLocationListener(new LocationInterface.LocationListener() {
            @Override
            public void onReceiveLocation(LocationInterface.LocationInfo locationInfo) {

            }

            @Override
            public void onError(int i) {

            }
        });
//        return 39.915;
        return mLatitude;
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
