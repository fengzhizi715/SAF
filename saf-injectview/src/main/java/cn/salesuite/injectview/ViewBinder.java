package cn.salesuite.injectview;

/**
 * Created by Tony Shen on 2016/12/6.
 */

public interface ViewBinder<T> {


    void inject(T host, Object target, Injector.Finder finder);
}
