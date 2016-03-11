package com.jb.vmeeting.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.tools.L;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 *
 * Created by Jianbin on 2015/12/24.
 */
public class App extends Application {

    private static App sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        initLib();
    }


    private void initLib() {
        // 初始化日志功能, 开启/关闭 日志输出
        L.setLogOpen(AppConstant.LOG_OPEN);

        // 初始化自定义异常捕获
        CrashHandler.getInstance().init(this);

        // 初始化ImageLoader
        // 设置图片显示选项
        DisplayImageOptions displayOp = new DisplayImageOptions.Builder()
                .showImageOnLoading(0)// 图片正在加载时显示的背景
                .cacheInMemory(true)// 缓存在内存中
                .cacheOnDisk(true)// 缓存在磁盘中
                .displayer(new FadeInBitmapDisplayer(300))// 显示渐变动画
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .considerExifParams(true)// 考虑旋转角
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(displayOp)
                .denyCacheImageMultipleSizesInMemory()// 不解析多种尺寸
                .build();

        ImageLoader.getInstance().init(config);
    }

    public static App getInstance() {
        return sApplication;
    }
}
