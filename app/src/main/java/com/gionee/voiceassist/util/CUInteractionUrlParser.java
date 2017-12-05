package com.gionee.voiceassist.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成/解析自定义多轮交互的URL的实用类
 */

public class CUInteractionUrlParser {


    public static Map<String, String> parsePhonecallUrl(String url) {
        Map<String, String> result = new HashMap<>();
        Matcher phoneNumberMatcher = Pattern.compile("num=([0-9]+)").matcher(url);
        Matcher simIdMatcher = Pattern.compile("sim=([0-1]+)").matcher(url);
        String phoneNumber = phoneNumberMatcher.find() ? phoneNumberMatcher.group(1):"";
        String simId = simIdMatcher.find() ? simIdMatcher.group(1):"";
        result.put("num", phoneNumber);
        result.put("sim", simId);
        return result;
    }

    public static Map<String, String> parseSmsUrl(String url) {
        Map<String, String> result = new HashMap<>();
        Matcher phoneNumberMatcher = Pattern.compile("num=([0-9]+)").matcher(url);
        Matcher contentMatcher = Pattern.compile("msg=(.+?)(#|$)").matcher(url);
        Matcher simIdMatcher = Pattern.compile("sim=([0-1]+)").matcher(url);
        String phoneNumber = phoneNumberMatcher.find() ? phoneNumberMatcher.group(1):"";
        String content = "";
        try {
            content = contentMatcher.find() ? URLDecoder.decode(contentMatcher.group(1), "utf-8"): "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String simId = simIdMatcher.find() ? simIdMatcher.group(1):"";
        result.put("num", phoneNumber);
        result.put("msg", content);
        result.put("sim", simId);
        return result;
    }

    /**
     * 任意多轮交互Url的匹配
     * @param key Url中的字键
     * @return 存放有对应字键的Map
     */
    private static Map<String, String> parseUrl(String url, String[] keys) {
        List<String> keysList = Arrays.asList(keys);
        Map<String, String> result = new HashMap<>();
        for (String key:keysList) {
            String regex = key + "=(.+?)(#|$)";
            Matcher keyMatcher = Pattern.compile(regex).matcher(url);
            String value = keyMatcher.matches() ? keyMatcher.group(1):"";
            result.put(key, value);
        }
        return result;
    }

}
