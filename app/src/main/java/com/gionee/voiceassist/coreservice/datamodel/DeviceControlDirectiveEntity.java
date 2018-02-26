package com.gionee.voiceassist.coreservice.datamodel;

import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;

/**
 * Created by liyingheng on 1/26/18.
 */

public class DeviceControlDirectiveEntity extends DirectiveEntity {

    private String functionName;

    private boolean state;

    public DeviceControlDirectiveEntity(String functionName, boolean state) {
        setType(Type.DEVICE_CONTROL);
        this.functionName = functionName;
        this.state = state;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
