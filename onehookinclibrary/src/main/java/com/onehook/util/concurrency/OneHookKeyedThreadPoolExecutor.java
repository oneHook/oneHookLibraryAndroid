
package com.onehook.util.concurrency;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * OneHookKeyedThreadPoolExecutor is an extension to Android stock
 * ThreadPoolExecutor. With each job you submit to execute, there will always be
 * a String key associated with it, hence this executor support drop a job by
 * key, and if one job with same key already in the waiting queue, it will be
 * dropped automatically and new job will be added it.
 * 
 * @author EagleDiao
 */
public class OneHookKeyedThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * Hosts multiple thread pool executor at a time.
     */
    private static HashMap<String, OneHookKeyedThreadPoolExecutor> sInstanceMap;

    /**
     * Config.
     */
    private OneHookKeyedThreadPoolExecutorConfig mConfig;

    /**
     * Build a new instance of thread pool executor by given key and
     * configurations. If given key already associated with a executor, it will
     * produce the existing executor.
     * 
     * @param key key
     * @param config config
     * @return instance of a executor
     */
    public static OneHookKeyedThreadPoolExecutor buildInstance(final String key,
            final OneHookKeyedThreadPoolExecutorConfig config) {
        /* lazy initialize instance map */
        if (sInstanceMap == null) {
            sInstanceMap = new HashMap<String, OneHookKeyedThreadPoolExecutor>();
        }
        /* check if there is any existing instance */
        if (sInstanceMap.containsKey(key) && sInstanceMap.get(key) != null) {
            return sInstanceMap.get(key);
        }
        /* build new instance */
        final OneHookKeyedThreadPoolExecutor instance = new OneHookKeyedThreadPoolExecutor(
                config.mCorePoolSize, config.mMaximumPoolSize, config.mAliveTime,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>());
        instance.mConfig = config;
        sInstanceMap.put(key, instance);
        return instance;
    }

    /**
     * Produce the instance associated with the given key if it exists, null
     * otherwise.
     * 
     * @param key key
     * @return the executor instance or null if none exists
     */
    public static OneHookKeyedThreadPoolExecutor getInstance(final String key) {
        if (sInstanceMap != null && sInstanceMap.containsKey(key)) {
            return sInstanceMap.get(key);
        } else {
            return null;
        }
    }

    /**
     * Handler to post runnable to main thread.
     */
    private Handler mMainThreadHandler;

    /**
     * Concurrent hash map to host all the waiting jobs.
     */
    private ConcurrentHashMap<Object, Job> mWaitingJobs;

    /**
     * Initialize a new thread pool executor.
     * 
     * @param corePoolSize the number of threads to keep in the pool, even if
     *            they are idle, unless allowCoreThreadTimeOut is set
     * @param maximumPoolSize the maximum number of threads to allow in the pool
     * @param keepAliveTime when the number of threads is greater than core,
     *            this is the maximum time that excess idle threads will wait
     *            for new tasks before terminating.
     * @param unit the time unit for the keepAliveTime
     * @param workQueue the queue to use for holding tasks before they are
     *            executed. This queue will hold only the Runnable taks
     *            submitted by the execute method
     */
    private OneHookKeyedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        mMainThreadHandler = new Handler();
        mWaitingJobs = new ConcurrentHashMap<Object, Job>();
    }

    @Override
    public final void execute(Runnable command) {
        throw new RuntimeException("Use the execute with object key instead");
    }

    /**
     * Execute a given command associated with key.
     * 
     * @param key key object
     * @param command command to run
     */
    public void execute(final Object key, Job command) {
        checkCurrentJobs(key, command);
        super.execute(command);
    }

    /**
     * Drop a job by given key.
     * 
     * @param key
     * @return true if the job is successfully removed, false if it has already
     *         started
     */
    public synchronized boolean dropJob(final Object key) {
        final Job toDrop = mWaitingJobs.remove(key);
        if (toDrop != null) {
            if (remove(toDrop)) {
                toDrop.onDrop();
                return true;
            }
        }
        return false;
    }

    /**
     * Drop all the jobs in the waiting queue. If some of the jobs have already
     * started, they will not be dropped.
     */
    public synchronized void dropAll() {
        for (Object key : mWaitingJobs.keySet()) {
            final Job toDrop = mWaitingJobs.remove(key);
            if (toDrop != null && remove(toDrop)) {
                toDrop.onDrop();
            }
        }
    }

    /**
     * Check current waiting job to see if the key already exist, if it does,
     * previous job associated with that key will be dropped. either way, new
     * job and new key will be put into the waiting queue.
     * 
     * @param key key of new job
     * @param command new job
     */
    private synchronized void checkCurrentJobs(final Object key, Job command) {
        if (mWaitingJobs.containsKey(key)) {
            final Job toCancel = mWaitingJobs.get(key);
            if (toCancel != null) {
                /*
                 * There is no guarantee that this job will be removed. but if
                 * it does get removed, callback will be invoked.
                 */
                if (remove(toCancel)) {
                    toCancel.onDrop();
                }
                mWaitingJobs.remove(key);
            }
        }
        /*
         * Issue the new command to the blocked queue.
         */
        mWaitingJobs.put(key, command);
    }

    /**
     * Called when receive callback from core that a particular job will be
     * executed. We need remove this job from our waiting queue.
     * 
     * @param r runnable
     */
    private synchronized void startJob(Runnable r) {
        for (Object key : mWaitingJobs.keySet()) {
            if (mWaitingJobs.get(key) == r) {
                mWaitingJobs.remove(key);
            }
        }
    }

    @Override
    protected void beforeExecute(final Thread t, final Runnable r) {
        /*
         * Remove job from waiting queue
         */
        startJob(r);
        /*
         * Call back to the job.
         */
        final Job job = (Job)r;
        job.onStart();
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(final Runnable r, final Throwable t) {
        final Job job = (Job)r;
        /*
         * Post runnable to main thread.
         */
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (t != null) {
                    job.onError();
                } else {
                    job.onFinish();
                }
            }
        };
        mMainThreadHandler.post(runnable);
        super.afterExecute(r, t);
    }

    /**
     * Debug tag.
     */
    private static final String DEBUG_TAG = "OneHookKeyedThreadPoolExecutor";

    /**
     * Debug call proxy.
     */
    @SuppressWarnings("unused")
    private void debug(final String line) {
        if (mConfig.isDebug()) {
            Log.d(DEBUG_TAG, line);
        }
    }

}
