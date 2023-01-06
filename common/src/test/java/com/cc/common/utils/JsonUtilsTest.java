package com.cc.common.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {

    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String jsonString1;
    private String jsonString2;
    private List list;

    @BeforeTestClass
    public void init() {
        jsonString1 = "[\n" +
                "  {\n" +
                "    \"userId\": \"1\",\n" +
                "    \"userName\": \"admin\",\n" +
                "    \"phone\": \"13112345678\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"userId\": \"2\",\n" +
                "    \"userName\": \"test\",\n" +
                "    \"phone\": \"13133333333\",\n" +
                "  }\n" +
                "]";
        jsonString2 = "  {\n" +
                "    \"userId\": \"1\",\n" +
                "    \"userName\": \"admin\",\n" +
                "    \"phone\": \"13112345678\"\n" +
                "  }";
        jsonObject = new JSONObject();
        jsonObject.put("userId","1");
        jsonObject.put("userName","admin");
        jsonObject.put("phone","13112345678");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("userId","2");
        jsonObject2.put("userName","test");
        jsonObject2.put("phone","13133333333");
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject2);
        list = new ArrayList<User>();
        list.add(new User("1", "admin", "13112345678"));
        list.add(new User("2", "test", "13133333333"));

    }


    class User{
        private String userId;
        private String userName;
        private String phone;

        User(String userId, String userName, String phone){
            this.userId = userId;
            this.userName = userName;
            this.phone = phone;
        }
    }

    @Test
    void testStringToJSONObject() {
        assertEquals(jsonObject,JsonUtils.stringToJSONObject(jsonString2));
    }

    @Test
    void testStringToJSONObject1() {
        assertEquals(jsonObject,JsonUtils.stringToJSONObject(jsonString2,User.class));
    }

    @Test
    void testStringToJSONArray() {
        assertEquals(jsonArray, JsonUtils.stringToJSONArray(jsonString1));
    }

    @Test
    void testListToJSONArray() {
        assertEquals(jsonArray, JsonUtils.listToJSONArray(list));
    }

    @Test
    void testJsonArrayToList(){
        assertEquals(list, JsonUtils.listToJSONArray(jsonArray));
    }
}
