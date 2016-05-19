package com.jb.vmeeting.mvp.model.helper;

import android.text.TextUtils;

import com.jb.vmeeting.app.constant.NETCODE;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jianbin on 2016/4/21.
 */
public abstract class SimpleCallback<T> implements Callback<Result<T>> {


    @Override
    public void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
        Result<T> result = response.body();
        String url = call.request().url().url().toString();
        int statusCode = response.code();
        if (result != null) {
            result.statusCode = statusCode;
            if (result.success) {
                L.i(url+": "+statusCode+" success result " + result.toString());
                onSuccess(statusCode, result);
            } else {
                L.e(url+": "+statusCode+" failed result " + result.toString());
                onFailed(statusCode, result);
            }
        } else { // result is null
            String errMsg = response.message();
            if (TextUtils.isEmpty(errMsg)) {
                errMsg = "result is empty";
            }
            result = new Result<>();
            result.success = false;
            result.code = NETCODE.RESULT.CODE_RESULT_EMPTY;
            result.statusCode = statusCode;
            result.message = errMsg;
            L.e(url+": "+statusCode+" failed result " + result.toString());
            onFailed(statusCode, result);
        }
        if (result.code == NETCODE.RESULT.CODE_LOGIN_NEED) {
            AccountManager.getInstance().logout();
        }
    }

    @Override
    public void onFailure(Call<Result<T>> call, Throwable t) {
        String url = call.request().url().url().toString();
        Result<T> result = new Result<>();
        result.success = false;
        result.code = NETCODE.RESULT.ERR_LOCAL;
        result.statusCode = NETCODE.STATUS.ERR_LOCAL;
        result.message = t.getMessage();
        L.e(url+": "+"status code none. failed result " + result.toString());
        onFailed(result.statusCode, result);
    }

    public abstract void onSuccess(int statusCode, Result<T> result);

    public abstract void onFailed(int statusCode, Result<T> result);
}
