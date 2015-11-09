/*
 */
package cn.salesuite.saf.eventbus;

/**
 * 线程模式的枚举
 * @author Tony
 */
public enum ThreadMode {

    PostThread,

    BackgroundThread,

    Async,
    
    ScheduleBackgroundThread
}