/*
 * *
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gionee.voiceassist.coreservice.sdk.module.sms.message;

import com.baidu.duer.dcs.framework.message.Payload;


/**
 * Created by caoyushu01 on 2017/7/18.
 */

public class SendSmsByNumberPayload extends Payload {

    private String messageContent = "";
    private CandidateRecipientNumber recipient;
    private String useSimIndex = "";
    private String useCarrier = "";

    public SendSmsByNumberPayload () {

    }

    public String getMessageContent () {
        return messageContent;
    }

    public void setMessageContent (String messageContent) {
        this.messageContent = messageContent;
    }

    public CandidateRecipientNumber getRecipient () {
        return recipient;
    }

    public void setRecipient (CandidateRecipientNumber recipient) {
        this.recipient = recipient;
    }

    public String getUseSimIndex () {
        return useSimIndex;
    }

    public void setUseSimIndex (String useSimIndex) {
        this.useSimIndex = useSimIndex;
    }

    public String getUseCarrier () {
        return useCarrier;
    }

    public void setUseCarrier (String useCarrier) {
        this.useCarrier = useCarrier;
    }
}
