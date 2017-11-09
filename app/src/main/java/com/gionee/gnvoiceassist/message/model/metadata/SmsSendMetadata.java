package com.gionee.gnvoiceassist.message.model.metadata;

import java.util.List;

/**
 * Created by liyingheng on 11/7/17.
 */

public class SmsSendMetadata extends Metadata {

    private List<ContactsMetadata> destination;

    private String content;

    private String simSlot;

    private String carrier;

    public List<ContactsMetadata> getDestination() {
        return destination;
    }

    public void setDestination(List<ContactsMetadata> destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSimSlot() {
        return simSlot;
    }

    public void setSimSlot(String simSlot) {
        this.simSlot = simSlot;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}
