package com.jb.vmeeting.tools.task;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutorWrapper
 * Created by Jianbin on 2015/12/20.
 */
public class ThreadPoolExecutorWrapper {
    private ExecutorService mThreadPoolExecutor;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private Handler mMainHandler;

    /**
     *
     * @param activeThreadCount 线程池维护线程的最少数量
     * @param maxThreadCount 线程池维护线程的最大数量
     * @param maxScheduleTaskThread 最多可定时的线程数
     */
    public ThreadPoolExecutorWrapper(int activeThreadCount, int maxThreadCount, int maxScheduleTaskThread) {
        mThreadPoolExecutor = new ThreadPoolExecutor(activeThreadCount, maxThreadCount,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory());

        if (maxScheduleTaskThread > 0) {
            mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(maxScheduleTaskThread);
        }

        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void executeTask(Runnable task) {
        mThreadPoolExecutor.execute(task);
    }

    public <T> Future<T> submitTask(Callable<T> task) {
        return mThreadPoolExecutor.submit(task);
    }

    public void scheduleTask(long delay, Runnable task) {
        mScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long initialDelay, long period, Runnable task) {
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public void scheduleTaskAtFixedRateIncludingTaskRunningTime(long initialDelay, long period, Runnable task) {
        mScheduledThreadPoolExecutor.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public boolean removeScheduledTask(Runnable task) {
        return mScheduledThreadPoolExecutor.remove(task);
    }

    public void scheduleTaskOnUiThread(long delay, Runnable task) {
        mMainHandler.postDelayed(task, delay);
    }

    public void removeScheduledTaskOnUiThread(Runnable task) {
        mMainHandler.removeCallbacks(task);
    }

    public void runTaskOnUiThread(Runnable task) {
        mMainHandler.post(task);
    }

    public void shutdown() {
        if (mThreadPoolExecutor != null) {
            mThreadPoolExecutor.shutdown();
            mThreadPoolExecutor = null;
        }

        if (mScheduledThreadPoolExecutor != null) {
            mScheduledThreadPoolExecutor.shutdown();
            mScheduledThreadPoolExecutor = null;
        }
    }
}
