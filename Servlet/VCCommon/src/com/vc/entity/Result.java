package com.vc.entity;

/**
 * 服务端返回的数据格式外包装
 * Created by Jianbin on 2016/4/23.
 */
public class Result<T> extends BaseEntity {
	public static final int DEFAULT_SUCCESS_CODE = 200;
	public static final int DEFAULT_ERR_CODE = -1;
	
    public T body;
    public String message;
    public int code;
    public boolean success = false;
    
//    public Result(boolean success, T body) {
//    	this.success =success;
//    	if(success) {
//    		this.code = DEFAULT_SUCCESS_CODE;
//    	} else {
//    		this.code = DEFAULT_ERR_CODE;
//    	}
//    	this.message = null;
//    	this.body = body;
//    }
    
    public Result(boolean success, String message, T body) {
    	this.success =success;
    	if(success) {
    		this.code = DEFAULT_SUCCESS_CODE;
    	} else {
    		this.code = DEFAULT_ERR_CODE;
    	}
    	this.message = message;
    	this.body = body;
    }
    
    public Result(boolean success, int code, String message, T body) {
    	this.success =success;
    	this.code = code;
    	this.message = message;
    	this.body = body;
    }
}
