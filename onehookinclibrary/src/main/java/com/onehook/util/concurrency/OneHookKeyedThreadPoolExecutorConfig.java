
package com.onehook.util.concurrency;

/**
 * OneHookKeyedThreadPoolExecutor configuration class.
 * 
 * @author EagleDiao
 */
public class OneHookKeyedThreadPoolExecutorConfig {

    /**
     * Should print log.
     */
    protected boolean mDebugMode;

    /**
     * The number of threads to keep in the pool, even if they are idle, unless
     * allowCoreThreadTimeOut is set.
     */
    protected int mCorePoolSize;

    /**
     * The maximum number of threads to allow in the pool.
     */
    protected int mMaximumPoolSize;

    /**
     * When the number of threads is greater than core, this is the maximum time
     * that excess idle threads will wait for new tasks before terminating.
     */
    protected long mAliveTime;

    /**
     * Initialize a new Thread pool executor configuration, all fields will be
     * set to default values.
     */
    public OneHookKeyedThreadPoolExecutorConfig() {
        mCorePoolSize = 2;
        mMaximumPoolSize = 5;
        mAliveTime = 2000;
        mDebugMode = false;
    }

    /**
     * @param size the number of threads to keep in the pool, even if they are
     *            idle, unless allowCoreThreadTimeOut is set
     * @return self
     */
    public OneHookKeyedThreadPoolExecutorConfig setCorePoolSize(final int size) {
        mCorePoolSize = size;
        return this;
    }

    /**
     * @param size the maximum number of threads to allow in the pool
     * @return self
     */
    public OneHookKeyedThreadPoolExecutorConfig setMaximumPoolSize(final int size) {
        mMaximumPoolSize = size;
        return this;
    }

    /**
     * @param aliveTime when the number of threads is greater than core,
     *            this is the maximum time that excess idle threads will wait
     *            for new tasks before terminating.
     * @return self
     */
    public OneHookKeyedThreadPoolExecutorConfig setAliveTime(final long aliveTime) {
        mAliveTime = aliveTime;
        return this;
    }

    /**
     * Chained. Set debug mode.
     * 
     * @param enable should allow debug mode
     * @return self
     */
    public OneHookKeyedThreadPoolExecutorConfig debug(final boolean enable) {
        mDebugMode = enable;
        return this;
    }

    /**
     * @return is in debug mode
     */
    public boolean isDebug() {
        return mDebugMode;
    }

}
