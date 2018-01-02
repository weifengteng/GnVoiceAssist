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
package com.gionee.voiceassist.coreservice.sdk.module.phonecall.message;

import com.baidu.duer.dcs.framework.message.Payload;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caoyushu01 on 17/7/6.
 */

public class SelectCalleePayload extends Payload implements Serializable {
    // 服务端返回的全部响应当前次打电话的电话号/或服务端解析到的名称
    private List<CandidateCalleeNumber> candidateCallees;
    // 使用sim卡1/卡2
    private String useSimIndex;
    // 用户指定的运营商名称,可选字段
    private String useCarrier;

    public List<CandidateCalleeNumber> getCandidateCallees () {
        return candidateCallees;
    }

    public void setCandidateCallees (List<CandidateCalleeNumber> candidateCallees) {
        this.candidateCallees = candidateCallees;
    }

    public String getUseSimIndex() {
        return useSimIndex;
    }

    public void setUseSimIndex(String useSimIndex) {
        this.useSimIndex = useSimIndex;
    }

    public String getUseCarrier () {
        return useCarrier;
    }

    public void setUseCarrier (String useCarrier) {
        this.useCarrier = useCarrier;
    }
}
