package com.jb.vmeeting.mvp.model.apiservice;

import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jianbin on 2016/4/28.
 */
public interface UserService {

    @POST("user/page/{page}/{limit}")
//    @Headers(value = {"dataType:json","Content-Type:application/json;charset=utf-8"})
    Call<Result<Page<User>>> getUserPage(@Path("page")int page, @Path("limit") int limit, @Body List<User> notInclude);
}
