package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class BaseEntity {
//    @Expose
//    protected String id;
//
//    @Expose
//    protected Date updateDate;
//
//    @Expose
//    protected Date generateDate;

    protected static Gson sGson = new Gson();

    public static  <T>T parse(String obj, Class<T> tClass) {
        return sGson.fromJson(obj, tClass);
    }

    @Override
    public String toString() {
        return sGson.toJson(this);
    }
}
