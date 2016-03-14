package com.jb.vmeeting.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.ui.activity.LoginActivity;
import com.jb.vmeeting.ui.activity.MainActivity;
import com.jb.vmeeting.ui.activity.SignUpActivity;

/**
 * 页面跳转
 * Created by Jianbin on 2016/3/13.
 */
public class PageJumper {

    public static PageJumper getInstance() {
        return SingleHolder.sPageJumper;
    }

    public void toMainActivity(@NonNull Context ctx) {
        toActivity(ctx, MainActivity.class);
    }

    public void toLoginActivity(@NonNull Context ctx) {
        toActivity(ctx, LoginActivity.class);
    }

    public void toSignUpActivity(@NonNull Context ctx) {
        toActivity(ctx, SignUpActivity.class);
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz) {
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        ctx.startActivity(intent);
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(ctx, clazz);
        intent.putExtra(IntentConstant.INTENT_EXTRA_BUNDLE, bundle);
        ctx.startActivity(intent);
    }

    private static final class SingleHolder {
        private static PageJumper sPageJumper = new PageJumper();
    }

}
