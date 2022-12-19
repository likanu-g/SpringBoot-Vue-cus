package com.cc.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class JsonUtils {

    /**
     * json字符串转JSONObject
     * @param jsonString json字符串
     * @return JSONObject
     */
    public static JSONObject stringToJSONObject(String jsonString){
        return JSONObject.parseObject(jsonString);
    }

    /**
     * json字符串转JSONObject
     * @param jsonString json字符串
     * @param clazz class类型
     * @return JSONObject
     */
    public static JSONObject stringToJSONObject(String jsonString, Class<?> clazz){
        return (JSONObject) JSONObject.parseObject(jsonString, clazz);
    }

    /**
     * json字符串转json数组
     * @param jsonString json字符串
     * @return JSONArray
     */
    public static JSONArray stringToJSONArray(String jsonString) {
        return JSONArray.parseArray(jsonString);
    }

    /**
     * List转JSONArray
     * @param list list
     * @return JSONArray
     */
    public static JSONArray listToJSONArray(List<?> list) {
        return JSONArray.parseArray(JSON.toJSONString(list));
    }

    /**
     * JSONArray转List
     * @param jsonArray json数组
     * @return list
     */
    public static List<Object> jsonArrayToList(JSONArray jsonArray) {
        return jsonArray.toJavaList(Object.class);
    }

}
