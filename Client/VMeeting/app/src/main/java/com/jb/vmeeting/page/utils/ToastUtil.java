package com.jb.vmeeting.page.utils;

import android.widget.Toast;

import com.jb.vmeeting.app.App;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class ToastUtil {

    private static Toast sToast;

    public static void toast(CharSequence msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(int msgId) {
        toast(App.getInstance().getText(msgId));
    }

    public static void toast(CharSequence msg, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(App.getInstance(), msg, duration);
        } else { // 直接替换文字并显示出来，不需要等待上一个toast消失
            sToast.setDuration(duration);
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void toast(int msgId, int duration) {
        toast(App.getInstance().getText(msgId), duration);
    }
}
