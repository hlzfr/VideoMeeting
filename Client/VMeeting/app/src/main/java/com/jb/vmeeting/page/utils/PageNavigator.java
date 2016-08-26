package com.jb.vmeeting.page.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.page.activity.ChooseParnerActivity;
import com.jb.vmeeting.page.activity.FullScreenImageActivity;
import com.jb.vmeeting.page.activity.LoginActivity;
import com.jb.vmeeting.page.activity.MainActivity;
import com.jb.vmeeting.page.activity.RoomDetailActivity;
import com.jb.vmeeting.page.activity.RoomFilesActivity;
import com.jb.vmeeting.page.activity.RoomUpdateActivity;
import com.jb.vmeeting.page.activity.SendSMSActivity;
import com.jb.vmeeting.page.activity.SignUpActivity;
import com.jb.vmeeting.page.activity.UserModifyActivity;
import com.jb.vmeeting.page.activity.VideoChatActivity;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public void toLoginActivity(@NonNull Context ctx, boolean clear) {
        toActivity(ctx, LoginActivity.class, clear, null);
    }
    public void toUserModifyActivity(@NonNull Context ctx) {
        toActivity(ctx, UserModifyActivity.class);
    }

    public void toChooseParnerActivity(@NonNull Context ctx, List<User> notInclude) {
        Bundle bundle = new Bundle();
        if (notInclude != null && notInclude.size() > 0) {
            L.d("add not include");
            ArrayList<User> notIncludeArray;
            if (!(notInclude instanceof ArrayList)) {
                L.d("!(notInclude instanceof ArrayList)");
                notIncludeArray = new ArrayList<>(notInclude.size());
                notIncludeArray.addAll(notInclude);
            } else {
                notIncludeArray = (ArrayList<User>) notInclude;
            }
            bundle.putSerializable(IntentConstant.BUNDLE_KEY_USER_LIST, notIncludeArray);
        }
        toActivity(ctx, ChooseParnerActivity.class, bundle);
    }

    public void toLoginActivity(@NonNull Context ctx) {
        toActivity(ctx, LoginActivity.class);
    }

    public void toSignUpActivity(@NonNull Context ctx) {
        toActivity(ctx, SignUpActivity.class);
    }

    public void toChatActivity(@NonNull Context ctx, @NonNull Room room) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_ROOM, room);
        toActivity(ctx, VideoChatActivity.class, bundle);
    }

    public void toRoomCreateActivity(@NonNull Context ctx) {
        toActivity(ctx, RoomUpdateActivity.class);
    }

    public void toRoomCreateActivity(@NonNull Context ctx, Room room) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_ROOM, room);
        toActivity(ctx, RoomUpdateActivity.class, bundle);
    }

    public void toRoomDetailActivity(@NonNull Context ctx, @NonNull Room room) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_ROOM, room);
        toActivity(ctx, RoomDetailActivity.class, bundle);
    }

    public void toRoomFilesActivity(@NonNull Context ctx, @NonNull Room room) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_ROOM, room);
        toActivity(ctx, RoomFilesActivity.class, bundle);
    }

    public void toSendSMSActivity(@NonNull Context ctx, @NonNull Room room) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.BUNDLE_KEY_ROOM, room);
        toActivity(ctx, SendSMSActivity.class, bundle);
    }

    public void toChooseFileForResult(@NonNull BaseActivity activity, int requestCode) {
        FileUtils.showFileChooser(activity, requestCode);
    }

    public void toChooseImageForResult(@NonNull BaseActivity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, requestCode);
    }

    public void toViewFile(@NonNull Context ctx, String filePath) {
        Intent intent = FileUtils.openFile(filePath);
        ctx.startActivity(intent);
    }

    public void toFullScreenImageActivity(@NonNull Context ctx, ArrayList<String> imageUrls, int position) {
        L.d("imageUrls " + String.valueOf(imageUrls));
        L.d("position " + position);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IntentConstant.BUNDLE_KEY_IMAGE_URLS, imageUrls);
        bundle.putInt(IntentConstant.BUNDLE_KEY_IMAGE_POSITION, position);
        toActivity(ctx, FullScreenImageActivity.class, bundle);
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz) {
        toActivity(ctx, clazz, null);
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz, Bundle bundle) {
        toActivity(ctx, clazz, new Intent(), bundle);
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz, boolean clear, Bundle bundle) {
        if (!clear) {
            toActivity(ctx, clazz, new Intent(), bundle);
        } else {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            toActivity(ctx, clazz, intent, bundle);
        }
    }

    private void toActivity(@NonNull Context ctx, @NonNull Class clazz, @NonNull Intent intent, Bundle bundle) {
        intent.setClass(ctx, clazz);
        intent.putExtra(IntentConstant.INTENT_EXTRA_BUNDLE, bundle);
        ctx.startActivity(intent);
    }

    private static final class SingleHolder {
        private static PageNavigator sPageNavigator = new PageNavigator();
    }

}
