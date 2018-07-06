package com.hunter.tool.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    /**
     * 线程安全执行线程
     * @param run
     */
    public void runThread(Runnable run) {
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        int KEEP_ALIVE_TIME = 1;    //线程池维护线程所允许的空闲时间
        TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;   //线程池维护线程所允许的空闲时间的单位
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();    //线程池使用的缓存队列
        ExecutorService executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
                NUMBER_OF_CORES*2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue
                , new BackgroundThreadFactory(run.toString())
                , new DefaultRejectedExecutionHandler(DefaultRejectedExecutionHandler.DEAFULE));

        //执行任务
        executorService.execute(run);
    }
}
