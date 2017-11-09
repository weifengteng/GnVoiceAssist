package com.gionee.gnvoiceassist.usecase;

import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;

/**
 * Created by liyingheng on 11/8/17.
 */

public abstract class UseCase {

    protected UsecaseCallback mCallback;

    public void setCallback(UsecaseCallback callback) {
        mCallback = callback;
    }

    public abstract void handleMessage(DirectiveResponseEntity message);

    public abstract String getUseCaseName();

    public void sendResponse(UsecaseResponseEntity response) {
        mCallback.onUsecaseResponse(response);
    }

    public interface UsecaseCallback {

        void onUsecaseResponse(UsecaseResponseEntity response);
    }

}
