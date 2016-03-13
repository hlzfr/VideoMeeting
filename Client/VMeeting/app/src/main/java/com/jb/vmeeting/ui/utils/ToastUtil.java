package com.jb.vmeeting.ui.utils;

import android.widget.Toast;

import com.jb.vmeeting.app.App;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class ToastUtil {

    public static void toast(CharSequence msg) {
        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int msgId) {
        toast(App.getInstance().getText(msgId));
    }

    public static void toast(CharSequence msg, int duration) {
        Toast.makeText(App.getInstance(), msg, duration).show();
    }

    public static void toast(int msgId, int duration) {
        toast(App.getInstance().getText(msgId), duration);
    }
}
