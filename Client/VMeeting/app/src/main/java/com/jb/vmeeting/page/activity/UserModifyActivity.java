package com.jb.vmeeting.page.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.UserUpdateEvent;
import com.jb.vmeeting.mvp.model.helper.ProgressRequestListener;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.tools.netfile.UploadManager;
import com.jb.vmeeting.utils.FileUtils;
import com.jb.vmeeting.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by Jianbin on 2016/4/26.
 */
public class UserModifyActivity extends BaseActivity {

    TextView tvUserName;
    TextView tvNickName;
    TextView tvPhone;
    ImageView imgAvatar;
    User currentUser;
    boolean hasModified = false; // 是否进行了修改

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AccountManager.getInstance().checkLogin(this, true, false);
        setContentView(R.layout.activity_user_update);

        setupToolBar();
        setToolBarCanBack();

        tvUserName = ViewUtils.find(this, R.id.tv_user_modify_username);
        tvNickName = ViewUtils.find(this, R.id.tv_user_modify_nickname);
        imgAvatar = ViewUtils.find(this, R.id.img_user_modify_avatar);
        tvPhone = ViewUtils.find(this, R.id.tv_user_modify_phone);

        currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();
        updateUserInfoUI(currentUser);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    @Override
    public void onUserUpdate(UserUpdateEvent updateEvent) {
        if (updateEvent.success && updateEvent.user != null) {
            updateUserInfoUI(updateEvent.user);
        }
    }

    public void updateUserInfoUI(User user) {
        tvUserName.setText(String.format(App.getInstance().getString(R.string.text_user_modify_username), user.getUsername()));
        String nickName = user.getNickName();
        if (TextUtils.isEmpty(nickName)) {
            nickName = user.getUsername();
        }
        tvNickName.setText(nickName);
        tvPhone.setText(user.getPhoneNumber());
        if (!TextUtils.isEmpty(user.getAvatar())) {
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url)+user.getAvatar(), imgAvatar);
        }
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {

    }

    public void modifyAvatar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }

    AlertDialog editDialogNickName;
    EditText editTextNickName;
    public void modifyNickName(View view) {
        if (editDialogNickName == null) {
            if (editTextNickName == null) {
                editTextNickName = new EditText(this);
            }
            editTextNickName.setText("");
            editDialogNickName = new AlertDialog.Builder(this)
                    .setTitle("请输入")
//                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(editTextNickName)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newNickName = editTextNickName.getText().toString().trim();
                            if (!TextUtils.isEmpty(newNickName) && !newNickName.equals(currentUser.getNickName())) {
                                currentUser.setNickName(newNickName);
                                hasModified = true;
                                updateUserInfoUI(currentUser);
                            }
                        }
                    })
                    .setNegativeButton("取消", null).create();
        }
        editDialogNickName.show();
    }

    AlertDialog editDialogPhone;
    EditText editTextPhone;
    public void modifyPhoneNumber(View view) {
        if (editDialogPhone == null) {
            if (editTextPhone == null) {
                editTextPhone = new EditText(this);
            }
            editTextPhone.setText("");
            editDialogPhone = new AlertDialog.Builder(this)
                    .setTitle("请输入")
//                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(editTextPhone)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newPhone = editTextPhone.getText().toString().trim();
                            if (!TextUtils.isEmpty(newPhone) && !newPhone.equals(currentUser.getPhoneNumber()) && newPhone.trim().length() == 11) {
                                currentUser.setPhoneNumber(newPhone);
                                hasModified = true;
                                updateUserInfoUI(currentUser);
                            } else {
                                ToastUtil.toast("请输入正确的手机号");
                            }
                        }
                    })
                    .setNegativeButton("取消", null).create();
        }
        editDialogPhone.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            File file = FileUtils.uri2File(this, uri);
            UploadManager.getInstance().upload(file, new SimpleCallback<String>() {
                @Override
                public void onSuccess(int statusCode, Result<String> result) {
                    String url = result.body;
                    currentUser.setAvatar(url);
                    hasModified = true;
                    updateUserInfoUI(currentUser);
                }

                @Override
                public void onFailed(int statusCode, Result<String> result) {
                    ToastUtil.toast(R.string.text_upload_failed);
                }
            }, new ProgressRequestListener() { // 进度提示
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    L.d("bytesWritten "+bytesWritten+";"+"contentLength "+contentLength+"; done "+done);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasModified) {
            AccountManager.getInstance().updateUserInfo(currentUser);
        }
    }
}
