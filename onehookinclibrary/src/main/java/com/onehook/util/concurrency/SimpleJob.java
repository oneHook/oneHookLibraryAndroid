
package com.onehook.util.concurrency;

/**
 * The SimpleJob that leave implementations of onResume onDrop and onError empty
 * so user only need to implement run and onFinish()
 * 
 * @author EagleDiao
 */
public abstract class SimpleJob implements Job {

    @Override
    public void onStart() {

    }

    @Override
    public void onDrop() {

    }

    @Override
    public void onError() {

    }

}
