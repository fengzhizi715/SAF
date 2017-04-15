package com.safframework.saf.queue;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.Process;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tony on 2017/4/15.
 */

public class Queue {

    private Bundle                                 bundle = null;
    private HandlerThread                          operationHandlerThread = null;
    private Handler                                operationHandlerThreadHandler = null;
    private LinkedBlockingQueue<OperationRunnable> operationQueue = new LinkedBlockingQueue<>();
    private boolean         isRunning = false;
    private String          name = null;
    private int             priority = Process.THREAD_PRIORITY_DEFAULT;

    /**
     * 构造Queue
     * @param name
     */
    public Queue(String name) {
        this(name, Process.THREAD_PRIORITY_DEFAULT);
    }

    /**
     * 基于线程优先级构造Queue
     * @param name
     * @param priority
     */
    public Queue(String name, int priority) {
        this.name = name;
        this.priority = priority;
        bundle = new Bundle();
        operationHandlerThread = new HandlerThread(name, priority);
    }

    /**
     * 启动queue
     */
    public synchronized void start() {

        if(isRunning) return;

        isRunning = true;
        if(operationHandlerThread == null) {
            operationHandlerThread = new HandlerThread(name, priority);
        }
        operationHandlerThread.start();
        operationHandlerThreadHandler = new Handler(operationHandlerThread.getLooper());
        for(OperationRunnable op : operationQueue ){
            op.queueing(operationHandlerThreadHandler);
        }
        operationQueue.clear();
    }

    public boolean isActivated() {
        return isRunning;
    }

    /**
     * 停止queue，并且清空所有操作
     */
    public synchronized void stop() {

        if(!isRunning) return;

        isRunning = false;
        removeAllOperations();
        operationHandlerThread.quit();
        operationHandlerThread = null;
        operationHandlerThreadHandler = null;
        bundle.clear();
    }

    public boolean addOperation(Operation operation) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return false;

            return operationHandlerThreadHandler.post(new OperationRunnable(this, operation));
        } else {
            return operationQueue.add(new OperationRunnable(this, operation));
        }
    }

    public boolean addOperationAtFirst(Operation operation) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return false;

            return operationHandlerThreadHandler.postAtFrontOfQueue(new OperationRunnable(this, operation));
        } else {
            return operationQueue.add(new OperationRunnable(this, operation, OperationRunnable.Type.ATFIRST, null, 0));
        }
    }

    public boolean addOperationAtTime(Operation operation, long uptimeMillis) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return false;

            return operationHandlerThreadHandler.postAtTime(new OperationRunnable(this, operation), uptimeMillis);
        } else {
            return operationQueue.add(new OperationRunnable(this, operation, OperationRunnable.Type.ATTIME, null, uptimeMillis));
        }
    }

    public boolean addOperationAtTime(Operation operation, Object token, long uptimeMillis) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return false;

            return operationHandlerThreadHandler.postAtTime(new OperationRunnable(this, operation), token, uptimeMillis);
        } else {
            return operationQueue.add(new OperationRunnable(this, operation, OperationRunnable.Type.ATTIME_WITH_TOKEN, token, uptimeMillis));
        }
    }

    public boolean addOperationAfterDelay(Operation operation, long delayTimeMillis) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return false;

            return operationHandlerThreadHandler.postDelayed(new OperationRunnable(this, operation), delayTimeMillis);
        } else {
            return operationQueue.add(new OperationRunnable(this, operation, OperationRunnable.Type.DELAY, null, delayTimeMillis));
        }
    }

    public void removeOperation(Operation operation) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return ;
            operationHandlerThreadHandler.removeCallbacks(new OperationRunnable(this, operation));
        } else {
            operationQueue.remove(new OperationRunnable(this, operation));
        }
    }

    public void removeOperation(Operation operation, Object token) {
        if(isRunning) {
            if(operationHandlerThreadHandler == null)
                return;
            operationHandlerThreadHandler.removeCallbacks(new OperationRunnable(this, operation), token);
        } else {
            operationQueue.remove(new OperationRunnable(this, operation, OperationRunnable.Type.NORMAL, token, 0));
        }
    }

    public void removeAllOperations() {
        if(operationHandlerThreadHandler != null) {
            operationHandlerThreadHandler.removeCallbacksAndMessages(null);
        }
        operationQueue.clear();
    }

    public Bundle getBundle() {
        return bundle;
    }
}
