package com.jb.vmeeting.mvp.model.helper;

import android.text.TextUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jianbin on 2016/4/21.
 */
public abstract class SimpleCallback<T> implements Callback<T> {

    public static final int ERR_LOCAL = -1;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        int code = response.code();
        if (body != null) {
            onSuccess(code, body);
        } else {
            String errMsg = response.message();
            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "body is empty";
            }
            onFailed(code, errMsg);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailed(ERR_LOCAL, t.getMessage());
    }

    public abstract void onSuccess(int code, T result);

    public abstract void onFailed(int code, String message);
}
