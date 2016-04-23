package com.jb.vmeeting.page.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.page.activity.LoginActivity;
import com.jb.vmeeting.page.activity.MainActivity;
import com.jb.vmeeting.page.activity.RoomCreateActivity;
import com.jb.vmeeting.page.activity.SignUpActivity;
import com.jb.vmeeting.page.activity.VideoChatActivity;

/**
 * 页面跳转导航
 * Created by Jianbin on 2016/3/13.
 */
public class PageNavigator {

    public static PageNavigator getInstance() {
        return SingleHolder.sPageNavigator;
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

    public void toChatActivity(@NonNull Context ctx) {
        toActivity(ctx, VideoChatActivity.class);
    }

    public void toRoomCreateActivity(@NonNull Context ctx) {
        toActivity(ctx, RoomCreateActivity.class);
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
        private static PageNavigator sPageNavigator = new PageNavigator();
    }

}
