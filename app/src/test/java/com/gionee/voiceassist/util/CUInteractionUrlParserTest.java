package com.gionee.voiceassist.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by liyingheng on 12/5/17.
 */
public class CUInteractionUrlParserTest {

    // phone协议：phone://{num=phonenumber}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}"
    // 短信协议：sms://{num=phonenumber}#{msg=messageContent}#{sim=sim_idx(可选字段)}#{carrier=carrier(可选字段)}

    /**
     * 测试打电话Url中只有电话号码的情况
     * @throws Exception
     */
    @Test
    public void parsePhonecallUrlWithNum() throws Exception {
        String inputUrl = "phone://num=1380000000";
        Map<String, String> outputResult = CUInteractionUrlParser.parsePhonecallUrl(inputUrl);
        assertNotNull("key 'num' is null!", outputResult.get("num"));
        assertEquals("1380000000", outputResult.get("num"));
    }

    /**
     * 测试打电话Url中同时有电话号码和Url的情况
     * @throws Exception
     */
    @Test
    public void parsePhonecallUrlWithNumAndSim() throws Exception {
        String inputUrl = "phone://num=1380000000#sim=1";
        Map<String, String> outputResult = CUInteractionUrlParser.parsePhonecallUrl(inputUrl);
        assertNotNull("key 'num' is null!", outputResult.get("num"));
        assertNotNull("key 'sim' is null!", outputResult.get("sim"));
        assertEquals("1380000000", outputResult.get("num"));
        assertEquals("1", outputResult.get("sim"));
    }

    @Test
    public void parseSmsUrlWithNumAndMsg() throws Exception {
        String inputUrl = "sms://num=10010#msg=hellotissatist#sim=1";
        Map<String, String> outputResult = CUInteractionUrlParser.parseSmsUrl(inputUrl);
        assertNotNull("key 'num' is null!", outputResult.get("num"));
        assertNotNull("key 'msg' is null!", outputResult.get("msg"));
        assertEquals("10010", outputResult.get("num"));
        assertEquals("hellotissatist", outputResult.get("msg"));
    }

    @Test
    public void parseSmsUrlWithAllArgs() throws Exception {
        String inputUrl = "sms://num=10010#msg=hellotissatist#sim=1";
        Map<String, String> outputResult = CUInteractionUrlParser.parseSmsUrl(inputUrl);
        assertNotNull("key 'num' is null!", outputResult.get("num"));
        assertNotNull("key 'msg' is null!", outputResult.get("msg"));
        assertNotNull("key 'sim' is null!", outputResult.get("sim"));
        assertEquals("10010", outputResult.get("num"));
        assertEquals("hellotissatist", outputResult.get("msg"));
        assertEquals("1", outputResult.get("sim"));
    }

    @Test
    public void parseSmsUrlWithChineseChar() throws Exception {
        String inputUrl = "sms://num=10010#msg=%E8%BF%99%E4%B8%AA%E6%9C%88%E4%BB%80%E4%B9%88%E6%97%B6%E5%80%99%E6%9C%88%E7%BB%93#sim=1";
        Map<String, String> outputResult = CUInteractionUrlParser.parseSmsUrl(inputUrl);
        assertNotNull("key 'num' is null!", outputResult.get("num"));
        assertNotNull("key 'msg' is null!", outputResult.get("msg"));
        assertNotNull("key 'sim' is null!", outputResult.get("sim"));
        assertEquals("10010", outputResult.get("num"));
        assertEquals("这个月什么时候月结", outputResult.get("msg"));
        assertEquals("1", outputResult.get("sim"));
    }

}