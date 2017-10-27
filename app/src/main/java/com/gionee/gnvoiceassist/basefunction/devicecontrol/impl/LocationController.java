package com.gionee.gnvoiceassist.basefunction.devicecontrol.impl;

import android.content.Context;

import com.gionee.gnvoiceassist.basefunction.devicecontrol.sysinterface.ILocation;

/**
 * Created by liyingheng on 10/24/17.
 */

public class LocationController extends BaseController implements ILocation {
    public LocationController(Context ctx) {
        super(ctx);
    }

    @Override
    public void setLocationEnabled(boolean enabled) {

    }
}
