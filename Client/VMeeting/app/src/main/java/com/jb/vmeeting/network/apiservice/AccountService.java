package com.jb.vmeeting.network.apiservice;

import com.jb.vmeeting.network.entity.User;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Jianbin on 2016/3/13.
 */
public interface AccountService {

    @POST("account/login")
    Call<User> login(String username, String password);

    @POST("account/register")
    Call<Void> register(String username, String password);

    Call<Void> logout();
}
