/*
 */
package com.safframework.saf.rxjava.eventbus;

/**
 * 线程模式的枚举
 * @author Tony Shen
 */
public enum ThreadMode {

    PostThread,       // 主线程

    BackgroundThread, // 后台线程

    IO               // io密集型线程

}