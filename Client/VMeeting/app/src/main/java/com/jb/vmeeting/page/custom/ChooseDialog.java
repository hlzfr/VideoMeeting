package com.jb.vmeeting.page.custom;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Jianbin on 2016/5/20.
 */
public class ChooseDialog extends AlertDialog {

    protected ChooseDialog(Context context) {
        super(context);
    }

    protected ChooseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ChooseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {}


}
