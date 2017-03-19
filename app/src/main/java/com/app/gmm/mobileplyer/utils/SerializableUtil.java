package com.app.gmm.mobileplyer.utils;

import com.app.gmm.mobileplyer.domain.MediaItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmm on 2017/3/19.
 * 序列化与反序列化工具类
 */

public class SerializableUtil {

    /**
     * 将对象序列化为 json 格式
     * @param object
     * @return
     */
    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> vClass){
        Gson gson = new Gson();
        return gson.fromJson(json, vClass);
    }

    public static <T> T fromJson(String json, Type type){
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
