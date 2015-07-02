/*
 * oneHook Inc. All rights reserved. Copyright 2012. 
 */

package com.onehook.util.concurrency;

/**
 * Abstract class Job to represent a background processing job. run function
 * will be called in background thread, make sure put all computation-heavy code
 * in run function.
 */
public interface Job extends Runnable {

    /**
     * Will be called before the actual job starts, will be called in the thread
     * that actually execute the run function.
     */
    public void onStart();

    /**
     * Will be called after the job finishes, will be called in main thread.
     */
    public void onFinish();

    /**
     * Will be called if this job is canceled(dropped), either by executor or by
     * user. will be called in main thread.
     */
    public void onDrop();

    /**
     * Will be called if thie job is terminated due to error.Will be called in
     * main thread.
     */
    public void onError();

}
