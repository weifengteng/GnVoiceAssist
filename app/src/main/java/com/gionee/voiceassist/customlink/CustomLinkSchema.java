package com.gionee.voiceassist.customlink;

/**
 * Created by twf on 2017/8/7.
 */

public class CustomLinkSchema {

    public static final String LINK_CONTACT = "contact://";
    public static final String LINK_PHONE = "phone://";
    // Phone协议的eg:phone://{num=phonenumber}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
    public static final String LINK_SMS = "sms://";
    // Sms协议的eg:sms://{num=phonenumber}#{msg=messageContent}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}
    public static final String LINK_ALERT = "alert://";

    public static final String LINK_APP_DOWNLOAD = "appdownload://";

}
