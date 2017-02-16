/**
 * 
 */
package cn.salesuite.saf.utils;

import rx.Observable;

/**
 * 使用方法:
 * 		String s = null;
 * 		Optional.ofNullable(s).orElse("default")); // 如果s为null,则显示default,否则显示s的值
 * @author Tony Shen
 *
 */
public class Optional<T> {

    Observable<T> obs;

    public Optional(Observable<T> obs) {
        this.obs = obs;
    }

    public static <T> Optional<T> of(T value) {
        if (value == null) {
            throw new NullPointerException();
        } else {
            return new Optional<T>(Observable.just(value));
        }
    }

    public static <T> Optional<T> ofNullable(T value) {
        if (value == null) {
            return new Optional<T>(Observable.<T>empty());
        } else {
            return new Optional<T>(Observable.just(value));
        }
    }

    public T get() {
        return obs.toBlocking().single();
    }

    public T orElse(T defaultValue) {
        return obs.defaultIfEmpty(defaultValue).toBlocking().single();
    }
}
