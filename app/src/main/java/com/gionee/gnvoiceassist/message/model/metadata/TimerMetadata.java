package com.gionee.gnvoiceassist.message.model.metadata;

/**
 * Created by liyingheng on 11/17/17.
 */

public class TimerMetadata extends Metadata {

    private long length;

    private String message;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
