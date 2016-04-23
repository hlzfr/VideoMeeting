package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;

/**
 * 服务端返回的数据格式外包装
 * Created by Jianbin on 2016/4/23.
 */
public class Result<T> extends BaseEntity {
    @Expose
    public T body;
    @Expose
    public String message;
    @Expose
    public int code;
    @Expose
    public boolean success = false;
}
