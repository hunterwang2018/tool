package com.hunter.tool.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {
    public final static int AP = 0;
    public final static int DEAFULE = 1;
    public final static int DOP = 2;
    public final static int CRP = 3;

    private int mType;
    public DefaultRejectedExecutionHandler(int type) {
        this.mType = type;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        switch (mType) {
            case AP:
                AbortPolicy(r, e);
                break;
            case DEAFULE:
                //do nothing
                break;
            case DOP:
                DiscardOldestPolicy(r, e);
                break;
            case CRP:
                CallerRunsPolicy(r, e);
                break;
            default:
                //do nothing
                break;
        }
    }

    /**
     * 该策略是线程池的默认策略。
     * 使用该策略时，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
     * 不做任何处理，直接抛出异常
     */
    private void AbortPolicy(Runnable r, ThreadPoolExecutor e) {
        throw new RejectedExecutionException("Task " + r.toString() +
                " rejected from " +
                e.toString());
    }

    /**
     * 丢弃最老的。也就是说如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列。
     * 因为队列是队尾进，队头出，所以队头元素是最老的，因此每次都是移除对头元素后再尝试入队。
     */
    private void DiscardOldestPolicy(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            //移除队头元素
            e.getQueue().poll();

            //再尝试入队
            e.execute(r);
        }
    }


    /**
     * 如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行。
     */
    private void CallerRunsPolicy(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            //直接执行run方法
            r.run();
        }
    }

}
