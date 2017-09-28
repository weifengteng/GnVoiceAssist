package com.gionee.gnvoiceassist.statemachine.scene;

/**
 * Created by twf on 2017/9/1.
 */

public interface IScene {

    boolean isMatch(String userCmd);

    boolean isConfirm(String userCmd);

    boolean isCancel(String userCmd);
}
