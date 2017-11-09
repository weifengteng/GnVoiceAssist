package com.gionee.gnvoiceassist.message.model.metadata;

/**
 * Created by liyingheng on 11/7/17.
 */

public class PhonecallMetadata extends Metadata{

    //姓名
    private String name;

    //电话号码
    private String[] number;

    //卡槽
    private String simSlot;

    //运营商名称
    private String carrier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNumber() {
        return number;
    }

    public void setNumber(String[] number) {
        this.number = number;
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
