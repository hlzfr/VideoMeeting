package com.jb.vmeeting.page.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.RoomPpt;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.presenter.RoomChatPresenter;
import com.jb.vmeeting.mvp.view.IRoomChatView;
import com.jb.vmeeting.page.adapter.recyclerview.TextMessageAdapter;
import com.jb.vmeeting.page.adapter.viewpager.ImageAdapter;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.custom.PptViewPager;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.WeakHandler;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.tools.task.TaskExecutor;
import com.jb.vmeeting.tools.webrtc.TextMessage;
import com.jb.vmeeting.utils.FileUtils;
import com.jb.vmeeting.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatActivity extends BaseActivity implements IRoomChatView{

    RoomChatPresenter roomChatPresenter;
    private GLSurfaceView mSurfaceView;
    private EditText mEdtChatText;
    private RecyclerView rvChatMesage;
    private ProgressDialog progressDialog;
    private PptViewPager pptViewPager;
//    private Handler mHandler = new Handler();

    private TextMessageAdapter mAdapter;
    private Room room;
    private User currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();
//    private List<String> ppts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                /*WindowManager.LayoutParams.FLAG_FULLSCREEN
                        |*/ WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        mSurfaceView = findView(R.id.sv_chat);
        mEdtChatText = findView(R.id.edt_chat_text);
        rvChatMesage = findView(R.id.rv_chat_text);
        pptViewPager = findView(R.id.vp_chat_ppt);
        pptViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (currentUser != null && room != null && currentUser.equals(room.getOwner())) {
                    roomChatPresenter.movePpt(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pptViewPager.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoChatActivity.this);
                if (currentUser != null && room != null && currentUser.equals(room.getOwner())) {
                    builder.setItems(new String[]{"查看大图", "删除"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                PageNavigator.getInstance().toFullScreenImageActivity(VideoChatActivity.this, (ArrayList<String>) pptViewPager.getPpts(), position);
                            } else if (which == 1) {
                                roomChatPresenter.deletePpt(pptViewPager.getAdapter().getItem(position));
                            }
                        }
                    });
                } else {
                    builder.setItems(new String[]{"查看大图"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                PageNavigator.getInstance().toFullScreenImageActivity(VideoChatActivity.this, (ArrayList<String>) pptViewPager.getPpts(), position);
                            }
                        }
                    });
                }
                builder.create().show();
//                return true;
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        rvChatMesage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        // TODO 设置item加入和删除的动画
        mAdapter = new TextMessageAdapter();
        mAdapter.setNotifyOnChange(false);
        rvChatMesage.setAdapter(mAdapter);

        setupToolBar();
        setToolBarCanBack();

        roomChatPresenter = new RoomChatPresenter(this);
        bindPresenterLifeTime(roomChatPresenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentUser != null && room != null && currentUser.equals(room.getOwner())) {
            getMenuInflater().inflate(R.menu.menu_chat_room_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_room_detail) {
            PageNavigator.getInstance().toRoomDetailActivity(this, room);
            return true;
        } else if (id == R.id.action_room_message) {
            PageNavigator.getInstance().toSendSMSActivity(this, room);
            return true;
        } else if (id == R.id.action_room_upload_ppt) {
            PageNavigator.getInstance().toChooseImageForResult(this, 100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if (bundle != null) {
            room = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
        }
        roomChatPresenter.getPpt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            // 选择文件完毕
            Uri uri = data.getData();
            String filePath = FileUtils.getPath(this, uri);
            if (filePath != null) {
                File file = new File(filePath);
                roomChatPresenter.addPpt(file);
            } else {
                ToastUtil.toast("文件不存在");
            }
        }
    }

    @Override
    public GLSurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    @Override
    public Point getDisplaySize() {
        return ViewUtils.getDisplaySize(this);
    }

    @Override
    public String getRoomName() {
        return room.getId();
    }

    @Override
    public void onReceiveTextMessage(TextMessage message) {
        L.d("onReceiveTextMessage " + message.toString());
        mAdapter.add(0, message);
        mAdapter.notifyItemInserted(0);
        // 5s 后移出最后一个item
        TaskExecutor.scheduleTaskOnUiThread(5000, new Runnable() {
            @Override
            public void run() {
                int pos = mAdapter.getCount() - 1;
                mAdapter.remove(pos);
                mAdapter.notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public void onGetPpts(RoomPpt roomPpt) {
        List<String> ppts = null;
        if (roomPpt != null) {
            ppts = roomPpt.getPpts();
        }
        if (ppts != null && ppts.size() > 0) {
            pptViewPager.addAll(ppts);
            pptViewPager.setVisibility(View.VISIBLE);
        } else {
            pptViewPager.setVisibility(View.GONE);
        }
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void onUploadStart(File file) {
        progressDialog.setTitle("上传 " + file.getName());
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    public void onUploading(long current, long total) {
        progressDialog.setProgress((int) (current * 100 / total));
    }

    @Override
    public void onUploadSuccess(String url) {
        progressDialog.dismiss();
    }

    @Override
    public void onUploadFailed(int code, String reason) {
        progressDialog.dismiss();
    }

    @Override
    public void onPptDelete(String url) {
        pptViewPager.delete(url);
        if (pptViewPager.getPpts().size() <= 0) {
            pptViewPager.setVisibility(View.GONE);
        } else {
            pptViewPager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPptAdd(String url) {
        pptViewPager.add(url);
        pptViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPptMoved(int toPosition) {
        pptViewPager.setCurrentItem(toPosition);
    }

    public void sendTextMessage(View view) {
        boolean sendSuccess = roomChatPresenter.sendTextMessage(mEdtChatText.getText().toString());
        if (sendSuccess) {
            mEdtChatText.setText("");
        }
    }
}
