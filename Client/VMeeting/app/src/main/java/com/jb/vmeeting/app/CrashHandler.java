package com.jb.vmeeting.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.tools.L;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CrashHandler
 * Created by Jianbin on 2015/12/7.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "VCCrashLog" + File.separator;
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private static final class InstanceHolder {
        private static final CrashHandler sCrashHandler = new CrashHandler();
    }

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return InstanceHolder.sCrashHandler;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            // 导出异常信息到SDCard
            dumpExceptionToSDCard(throwable);
            // 上传到服务器
            uploadExceptionToServer();
        } catch (IOException e) {
            L.e(e);
        }

        if (AppConstant.DEBUG) {
            L.e(throwable);
            // 测试环境
            if (mDefaultCrashHandler != null) {
                mDefaultCrashHandler.uncaughtException(thread, throwable);
//            } else {
//                Process.killProcess(Process.myPid());
            }
        } else {
            // 正式环境
            //TODO restart automatically
        }
    }

    private void dumpExceptionToSDCard(Throwable throwable) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // SD卡不存在或无法使用
            L.w("sdcard unmounted, skip dump exception.");
            return;
        }

        PackageManager pm = mContext.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", mContext.getPackageName()));
        if (!permission) {
            L.e("no permission to write into storage!");
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyy-MM-dd HH,mm,ss", Locale.CHINA).format(new Date(current));
        File file = new File(dir.getAbsolutePath() + File.separator + FILE_NAME + time + FILE_NAME_SUFFIX);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                L.e("create crash file failed! " + e.getMessage());
                return;
            }
        }

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            throwable.printStackTrace(pw);
            pw.close();
            L.i("dump success."+throwable.getMessage());
        } catch (Exception e) {
            L.e("dump crash info failed: " + e.getMessage());
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        // App 版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        // Android 版本号
        pw.print("OS Version: ");
        pw.print("Android");
        pw.print(Build.VERSION.RELEASE);
        pw.print('_');
        pw.println(Build.VERSION.SDK_INT);

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        // CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer() {
        //TODO Upload Exception Message To Web Server
    }
}
