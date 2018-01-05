package com.gionee.voiceassist.coreservice.sdk.module.reminder;

/**
 * Created by liyingheng on 1/5/18.
 */

public class ApiConstants {

    public static final String NAMESPACE = "ai.dueros.device_interface.extensions.alert_nlu";
    public static final String NAME = "RemindInterface";

    public static final class Events {

    }

    public static final class Directives {
        public static final class SetAlert {
            public static final String NAME = SetAlert.class.getSimpleName();
        }
        public static final class ManageAlert {
            public static final String NAME = ManageAlert.class.getSimpleName();
        }
    }
}
