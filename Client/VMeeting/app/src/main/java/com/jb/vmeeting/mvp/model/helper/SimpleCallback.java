package com.jb.vmeeting.mvp.model.helper;

import android.text.TextUtils;

import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.tools.L;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jianbin on 2016/4/21.
 */
public abstract class SimpleCallback<T> implements Callback<Result<T>> {

    public static final int ERR_LOCAL = -1;

    @Override
    public void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
        Result<T> result = response.body();
        int statusCode = response.code();
        if (result != null) {
            if (result.success) {
                L.i(statusCode+" success result " + result.toString());
                onSuccess(statusCode, result);
            } else {
                L.e(statusCode+" failed result " + result.toString());
                onFailed(statusCode, result);
            }
        } else { // result is null
            String errMsg = response.message();
            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "result is empty";
            }
            result = new Result<>();
            result.success = false;
            result.code = ERR_LOCAL;
            result.message = errMsg;
            L.e(statusCode+" failed result " + result.toString());
            onFailed(statusCode, result);
        }
    }

    @Override
    public void onFailure(Call<Result<T>> call, Throwable t) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = ERR_LOCAL;
        result.message = t.getMessage();
        L.e("status code none. failed result " + result.toString());
        onFailed(ERR_LOCAL, result);
    }

    public abstract void onSuccess(int statusCode, Result<T> result);

    public abstract void onFailed(int statusCode, Result<T> result);
}
