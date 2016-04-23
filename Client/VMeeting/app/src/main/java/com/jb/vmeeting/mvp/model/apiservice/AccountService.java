package com.jb.vmeeting.mvp.model.apiservice;

import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 账号行为管理请求
 * Created by Jianbin on 2016/3/13.
 */
public interface AccountService {

    @FormUrlEncoded
    @POST("account/login")
    Call<Result<User>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("account/register")
    Call<Result<Void>> signUp(@Field("username") String username, @Field("password") String password);

    @GET("account/logout")
    Call<Result<Void>> logout();
}
