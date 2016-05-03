package com.jb.vmeeting.mvp.model.apiservice;

import com.jb.vmeeting.mvp.model.entity.Result;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jianbin on 2016/4/26.
 */
public interface FileService {
    @POST("file/upload")
    Call<Result<String>> upload(@Body MultipartBody file);
}
