package com.jb.vmeeting.mvp.model.apiservice;

import com.jb.vmeeting.mvp.model.entity.Result;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Jianbin on 2016/4/26.
 */
public interface FileService {
    @POST("file/upload")
    Call<Result<String>> upload(@Body RequestBody file);

    @GET
    @Streaming
    Call<ResponseBody> download(@Url String url);
}
