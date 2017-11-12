package com.gionee.gnvoiceassist.service;

import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

/**
 * 将消息调度给具体的UseCase进行处理
 */

public class UsecaseHandler {

    private Map<String,UseCase> mUsecaseMap;

    public UsecaseHandler () {
        mUsecaseMap = new HashMap<>();
    }

    /**
     * 发送消息到指定的Usecase
     * @param msg
     */
    public void sendMessage(DirectiveResponseEntity msg) throws Exception {
        String usecaseAlias = msg.getUsecase();
        if (mUsecaseMap.containsKey(usecaseAlias)) {
            mUsecaseMap.get(usecaseAlias).handleMessage(msg);
        } else {
            throw new Exception("消息没有注册对应的UseCase");
        }
    }

    public void registerUsecase(String alias, UseCase useCase, UseCase.UsecaseCallback callback) {
        if (!mUsecaseMap.containsKey(alias)) {
            useCase.setCallback(callback);
            mUsecaseMap.put(alias,useCase);
        }
    }

    public void unregisterUsecase(String alias) {
        if (mUsecaseMap.containsKey(alias)) {
            mUsecaseMap.remove(alias);
        }
    }

    public void unregisterAllUsecase() {
        mUsecaseMap.clear();
    }

    public void release() {
        unregisterAllUsecase();
    }


}
