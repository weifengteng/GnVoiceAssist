package com.gionee.gnvoiceassist.message.model.metadata;

/**
 * Created by liyingheng on 11/9/17.
 */

public class DeviceControlMetadata extends Metadata {

    private String command;

    private boolean state;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
