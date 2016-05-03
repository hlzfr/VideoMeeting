package com.jb.vmeeting.tools.netfile;

import com.jb.vmeeting.mvp.model.apiservice.FileService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.tools.L;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jianbin on 2016/4/26.
 */
public class UploadManager {
    private UploadManager(){}
    private static class SingleHolder {
        private static final UploadManager sSingleHolder = new UploadManager();
    }

    public static UploadManager getInstance() {
        return SingleHolder.sSingleHolder;
    }

    FileService fileService = RetrofitHelper.createService(FileService.class);

    public void upload(String path, SimpleCallback<String> callback) {
        upload(new File(path), callback);
    }

    public void upload(File file, SimpleCallback<String> callback) {
        if (!file.exists()) {
            // TODO throw file not exist error
            L.e("file not exist");
            return;
        }

        MultipartBody requestBody = new MultipartBody.Builder().addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("media/type"), file)).setType(MediaType.parse("multipart/form-data")).build();

        fileService.upload(requestBody).enqueue(callback);
    }
}
