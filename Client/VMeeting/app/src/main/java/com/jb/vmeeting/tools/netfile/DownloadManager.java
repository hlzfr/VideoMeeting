package com.jb.vmeeting.tools.netfile;

import com.jb.vmeeting.mvp.model.apiservice.FileService;
import com.jb.vmeeting.mvp.model.helper.ProgressResponseListener;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.task.TaskExecutor;
import com.jb.vmeeting.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jianbin on 2016/5/18.
 */
public class DownloadManager {

    FileService fileService = RetrofitHelper.createService(FileService.class);

    private DownloadManager(){}
    private static class SingleHolder {
        private static final DownloadManager sSingleHolder = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return SingleHolder.sSingleHolder;
    }

    public void download(final String url, final ProgressResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileService.download(url).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        TaskExecutor.executeTask(new Runnable() {
                            @Override
                            public void run() {
                                String[] urls = url.split("/");
                                boolean success = writeResponseBodyToDisk("VCFiles", urls[urls.length - 1], response.body(), listener);
                                L.d("save success " + success);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        L.e(throwable);
                        if (listener != null) {
                            listener.onResponseFailed(throwable);
                        }
                    }
                });
            }
        }).start();
    }

    public static boolean writeResponseBodyToDisk(String folderPath, String fileName, ResponseBody body, ProgressResponseListener listener) {
        try {
            File futureStudioIconFile = FileUtils.getSaveFile(folderPath, fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    listener.onResponseProgress(fileSizeDownloaded, fileSize, fileSize == fileSizeDownloaded);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                L.e(e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            L.e(e);
            return false;
        }
    }
}
