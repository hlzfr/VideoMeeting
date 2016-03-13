package com.jb.vmeeting.network.entity;

import com.google.gson.Gson;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class BaseEntity {
    protected static Gson sGson = new Gson();

    public static  <T>T parse(String obj, Class<T> tClass) {
        return sGson.fromJson(obj, tClass);
    }

    @Override
    public String toString() {
        return sGson.toJson(this);
    }
}
