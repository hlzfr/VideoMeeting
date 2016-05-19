package com.jb.vmeeting.page.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.RoomFiles;
import com.jb.vmeeting.mvp.model.helper.ProgressResponseListener;
import com.jb.vmeeting.mvp.presenter.RoomFilesPresenter;
import com.jb.vmeeting.mvp.view.IRoomFilesView;
import com.jb.vmeeting.page.adapter.recyclerview.RoomFileAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.SimpleViewHolder;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.tools.netfile.DownloadManager;
import com.jb.vmeeting.tools.task.TaskExecutor;
import com.jb.vmeeting.utils.FileUtils;

import java.io.File;

/**
 * 查看和编辑房间文件列表
 * Created by Jianbin on 2016/5/18.
 */
public class RoomFilesActivity extends BaseActivity implements IRoomFilesView {

    public static final int MODE_EDIT = 0;
    public static final int MODE_VIEW = 1;

    private int mode = MODE_VIEW;

    Room room;
    RoomFiles roomFiles;
    RoomFilesPresenter presenter;
    RoomFileAdapter adapter;

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_room_files);
        setupToolBar();
        setToolBarCanBack();

        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findView(R.id.swipe_layout);
        swipeRefreshLayout.setEnabled(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        presenter = new RoomFilesPresenter(this);
        bindPresenterLifeTime(presenter);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (room.getOwner().getId().equals(AccountManager.getInstance().getAccountSession().getCurrentUser().getId())) {
            getMenuInflater().inflate(R.menu.menu_room_file, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            if (mode == MODE_VIEW) {
                item.setTitle("查看");
                //点击后切换成编辑
                mode = MODE_EDIT;
                adapter.setMode(mode);
                adapter.notifyDataSetChanged();
            } else {
                item.setTitle("编辑");
                //点击后切换成查看
                mode = MODE_VIEW;
                adapter.setMode(mode);
                adapter.notifyDataSetChanged();
            }
            return true;
        } else if (id == R.id.action_upload) {
            // 选择文件
            PageNavigator.getInstance().toChooseFileForResult(this, 100);
//            FileUtils.showFileChooser(this, 100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d("onActivityResult requestCode：" + requestCode + "; resultCode: " + resultCode);
        if (resultCode == RESULT_OK && requestCode == 100) {
            // 选择文件完毕
            Uri uri = data.getData();
            String filePath =FileUtils.getPath(this, uri);
            if (filePath != null) {
                File file = new File(filePath);
                presenter.uploadFile(file);
            } else {
                ToastUtil.toast("文件不存在");
            }
        }
    }

    private void updateViews(RoomFiles files) {
        adapter.clear();
        if (files.getFiles() != null) {
            adapter.addAll(files.getFiles());
        } else {

        }
    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        room = null;
        if (bundle != null) {
            room = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
            mode = bundle.getInt(IntentConstant.BUNDLE_KEY_ROOM_FILE_TYPE, MODE_VIEW);
        }
        recyclerView.setAdapter(adapter = new RoomFileAdapter(mode));
        adapter.setOnButtonClick(new RoomFileAdapter.OnItemButtonClickListener() {
            @Override
            public void onItemClick(boolean exist, String fileName, int position) {
                if (mode == MODE_VIEW) {
                    if (exist) {
                        // 查看
                        PageNavigator.getInstance().toViewFile(RoomFilesActivity.this, FileUtils.getSavePath(AppConstant.DOWNLOAD_FILE_FOLDER) + File.separator
                                + fileName);
                    } else {
                        presenter.downloadFile(fileName);
                    }
                } else {
                    // 删除
                    roomFiles.getFiles().remove(position);
                    adapter.remove(position);
                    presenter.updateRoomFile(roomFiles);
                }
            }
        });

        if (room != null) {
            presenter.getRoomFiles(room);
        } else {
            ToastUtil.toast("无效房间");
        }
    }

    @Override
    public void onUploadStart(File file) {
        L.d("onUploadStart "+file.getName());
        progressDialog.setTitle("上传 " + file.getName());
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    public void onUploading(final long current, final long total) {
        L.d("onUploading ");
        progressDialog.setProgress((int) (current * 100 / total));
    }

    @Override
    public void onUploadSuccess(String url) {
        roomFiles.getFiles().add(url);
        adapter.add(url);
        presenter.updateRoomFile(roomFiles);
        L.d("onUploadSuccess " + url);
        progressDialog.dismiss();
    }

    @Override
    public void onUploadFailed(int code, String reason) {
        L.d("onUploadSuccess " + reason);
        progressDialog.dismiss();
    }

    @Override
    public void onDownloadStart(String fileName) {
        String[] split = fileName.split("_");
        progressDialog.setTitle("下载 " + split[split.length - 1]);
        progressDialog.setProgress(0);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onDownloading(long current, long total) {
        progressDialog.setProgress((int)(current*100/total));
    }

    @Override
    public void onDownloadSuccess() {
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onDownloadFailed(int code, String reason) {
        progressDialog.dismiss();
    }

    @Override
    public void onGetRoomFiles(RoomFiles files) {
        this.roomFiles = files;
        updateViews(this.roomFiles);
    }
}
