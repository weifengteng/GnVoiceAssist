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
package com.gionee.gnvoiceassist.sdk.module.phonecall;

import android.text.TextUtils;

import com.baidu.duer.dcs.devicemodule.phonecall.ApiConstants;
import com.baidu.duer.dcs.devicemodule.phonecall.message.CandidateCallee;
import com.baidu.duer.dcs.devicemodule.phonecall.message.CandidateCalleeNumber;
import com.baidu.duer.dcs.devicemodule.phonecall.message.ContactInfo;
import com.baidu.duer.dcs.devicemodule.phonecall.message.PhonecallByNamePayload;
import com.baidu.duer.dcs.devicemodule.phonecall.message.PhonecallByNumberPayload;
import com.baidu.duer.dcs.devicemodule.phonecall.message.SelectCalleePayload;
import com.baidu.duer.dcs.devicemodule.system.HandleDirectiveException;
import com.baidu.duer.dcs.framework.BaseDeviceModule;
import com.baidu.duer.dcs.framework.IMessageSender;
import com.baidu.duer.dcs.framework.message.ClientContext;
import com.baidu.duer.dcs.framework.message.Directive;
import com.baidu.duer.dcs.framework.message.Header;
import com.baidu.duer.dcs.framework.message.Payload;
import com.baidu.duer.dcs.systeminterface.IPhoneCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by caoyushu01 on 17/7/6.
 */

public class PhoneCallDeviceModule extends BaseDeviceModule {
    private IPhoneCall mPhoneCallImpl;
    private IPhoneCallDirectiveListener iPhoneCallDirectiveListener;

    public PhoneCallDeviceModule (IPhoneCall phoneCallImpl, IMessageSender messageSender) {
        super(ApiConstants.NAMESPACE, messageSender);
        this.mPhoneCallImpl = phoneCallImpl;
    }

    /**
     * 端能力声明接口
     *
     * @return
     */
    @Override
    public ClientContext clientContext () {
        String namespace = ApiConstants.NAMESPACE;
        String name = ApiConstants.Events.PhoneState.NAME;
        Header header = new Header(namespace, name);
        // 当前电话服务需传递空payload
        Payload payload = new Payload();
        return new ClientContext(header, payload);
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        HashMap<String, Class<?>> map = new HashMap<>();
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.phonecall.ApiConstants.Directives.PhonecallByName.NAME, com.gionee.gnvoiceassist.sdk.module.phonecall.message.PhonecallByNamePayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.phonecall.ApiConstants.Directives.SelectCallee.NAME, com.gionee.gnvoiceassist.sdk.module.phonecall.message.SelectCalleePayload.class);
        map.put(getNameSpace() + com.gionee.gnvoiceassist.sdk.module.phonecall.ApiConstants.Directives.PhonecallByNumber.NAME, com.gionee.gnvoiceassist.sdk.module.phonecall.message.PhonecallByNumberPayload.class);
        return map;
    }

    @Override
    public void handleDirective (Directive directive) throws HandleDirectiveException {
        String headerName = directive.getName();
        List<ContactInfo> contactInfos = null;
        if (ApiConstants.Directives.PhonecallByName.NAME.equals(headerName)) {
            contactInfos = assembleContactInfoByName(directive.getPayload());
        } else if (ApiConstants.Directives.SelectCallee.NAME.equals(headerName)) {
            contactInfos = assembleContactInfoByNumber(directive.getPayload());
        } else if (ApiConstants.Directives.PhonecallByNumber.NAME.equals(headerName)) {
            contactInfos = assembleContactInfoBySingleNumber(directive.getPayload());

        } else {
            String message = "phone cannot handle the directive";
            throw (new HandleDirectiveException(
                    HandleDirectiveException.ExceptionType.UNSUPPORTED_OPERATION, message));
        }
        if (iPhoneCallDirectiveListener != null) {
            iPhoneCallDirectiveListener.phoneCallDirectiveReceived(contactInfos, directive);
        }
    }

    /**
     * 根据directive的返回结果装填联系人信息
     *
     * @param payload
     */
    private List<ContactInfo> assembleContactInfoByName (Payload payload) {
        List<ContactInfo> infos = new ArrayList<>();
        if (payload instanceof PhonecallByNamePayload) {
            // 获取推荐的联系人名单
            List<CandidateCallee> recommendNames = ((PhonecallByNamePayload) payload).getCandidateCallees();
            // 获取sim卡
            String simCard = ((PhonecallByNamePayload) payload).getUseSimIndex();
            // 获取运营商信息
            String carrierInfo = ((PhonecallByNamePayload) payload).getUseCarrier();

            // 获取运营商信息
            for (int i = 0; i < recommendNames.size(); i++) {
                List<ContactInfo> oneTmpInfo =
                        mPhoneCallImpl.getPhoneContactsByName(recommendNames.get(i), simCard, carrierInfo);
                infos.addAll(oneTmpInfo);
            }
        }
        return infos;
    }

    /**
     * 根据directive的返回结果装填电话号码的信息
     *
     * @param payload
     */
    private List<ContactInfo> assembleContactInfoByNumber (Payload payload) {
        List<ContactInfo> infos = new ArrayList<>();
        if (payload instanceof SelectCalleePayload) {
            List<CandidateCalleeNumber> numbers = ((SelectCalleePayload) payload).getCandidateCallees();
            String simIndex = ((SelectCalleePayload) payload).getUseSimIndex();
            String carrierInfo = ((SelectCalleePayload) payload).getUseCarrier();

            for (int i = 0; i < numbers.size(); i++) {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setType(ContactInfo.TYPE_NUMBER);
                contactInfo.setName(numbers.get(i).getDisplayName());
                // 与联系人不同，numberList中只有一个numberInfo,为服务端返回
                List<ContactInfo.NumberInfo> numberList = new ArrayList<>();
                ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
                numberInfo.setPhoneNumber(numbers.get(i).getPhoneNumber());
                numberList.add(numberInfo);
                contactInfo.setPhoneNumbersList(numberList);
                if (!TextUtils.isEmpty(simIndex)) {
                    contactInfo.setSimIndex(simIndex);
                }
                if (!TextUtils.isEmpty(carrierInfo)) {
                    contactInfo.setCarrierOprator(carrierInfo);
                }

                infos.add(contactInfo);
            }
        }
        return infos;
    }

    /**
     * 根据directive的返回结果装填电话号码的信息
     *
     * @param payload
     */
    private List<ContactInfo> assembleContactInfoBySingleNumber (Payload payload) {
        List<ContactInfo> infos = new ArrayList<>();
        if (payload instanceof PhonecallByNumberPayload) {
            CandidateCalleeNumber callee = ((PhonecallByNumberPayload) payload).getCallee();
            String simIndex = ((PhonecallByNumberPayload) payload).getUseSimIndex();
            String carrierInfo = ((PhonecallByNumberPayload) payload).getUseCarrier();
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setType(ContactInfo.TYPE_NUMBER);
            contactInfo.setName(callee.getDisplayName());
            // 与联系人不同，numberList中只有一个numberInfo,为服务端返回
            List<ContactInfo.NumberInfo> numberList = new ArrayList<>();
            ContactInfo.NumberInfo numberInfo = new ContactInfo.NumberInfo();
            numberInfo.setPhoneNumber(callee.getPhoneNumber());
            numberList.add(numberInfo);
            contactInfo.setPhoneNumbersList(numberList);
            if (!TextUtils.isEmpty(simIndex)) {
                contactInfo.setSimIndex(simIndex);
            }
            if (!TextUtils.isEmpty(carrierInfo)) {
                contactInfo.setCarrierOprator(carrierInfo);
            }

            infos.add(contactInfo);
        }
        return infos;
    }

    @Override
    public void release () {
        iPhoneCallDirectiveListener = null;
    }

    public void addPhoneCallDirectiveListener (IPhoneCallDirectiveListener listener) {
        iPhoneCallDirectiveListener = listener;
    }


    // 电话指令监听器，用于Android应用层监听电话指令的到来
    public interface IPhoneCallDirectiveListener {
        void phoneCallDirectiveReceived (List<ContactInfo> contactInfos, Directive directive);
    }
}
