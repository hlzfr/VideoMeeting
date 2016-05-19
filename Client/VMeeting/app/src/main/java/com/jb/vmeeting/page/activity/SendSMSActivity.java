package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.ChooseUserItem;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.page.adapter.recyclerview.UserListAdapter;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 群发短信界面
 * Created by Jianbin on 2016/5/16.
 */
public class SendSMSActivity extends BaseActivity {

    Room mRoom;

    EditText edtContent;
    RecyclerView rvReceiver;
    CheckBox cbSelectAll;

    UserListAdapter mAdapter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_sms);
        setupToolBar();
        setToolBarCanBack();

        edtContent = findView(R.id.edt_sms_content);
        rvReceiver = findView(R.id.rv_sms_receiver);
        cbSelectAll = findView(R.id.cb_sms_all);

        mAdapter = new UserListAdapter(this);
        mAdapter.setNotifyOnChange(false);

        rvReceiver.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReceiver.setAdapter(mAdapter);
    }

    @Override
    protected void setupListener() {
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int size = mAdapter.getCount();
                for (int i = 0; i < size; i++) {
                    mAdapter.getItem(i).setIsChoose(isChecked);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if (intent != null && bundle != null) {
            mRoom = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
        }
        mAdapter.addAll(ChooseUserItem.toChooseUserList(mRoom.getParticipator()));
        cbSelectAll.setChecked(true);
    }

    public void sendSMS(View view) {
        String content = edtContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.toast("发送消息不能为空");
            return;
        }

        if (mAdapter != null) {
            List<ChooseUserItem> selectedItems = mAdapter.getSelectItem();
            if (selectedItems.size() == 0) {
                ToastUtil.toast("至少选择一个成员");
                return;
            }
            List<String> phones = new ArrayList<>(selectedItems.size());
            String phone;

            for (int i = 0; i < selectedItems.size(); i++) {
                phone = selectedItems.get(i).getUser().getPhoneNumber();
                if (!TextUtils.isEmpty(phone)) {
                    phones.add(phone);
                }
            }
            SystemUtils.sendSMS(this, content, phones);
            return;
        } else {
            ToastUtil.toast("至少选择一个成员");
            return;
        }
    }
}
