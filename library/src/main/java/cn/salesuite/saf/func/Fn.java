package cn.salesuite.saf.func;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.func.functions.Action1;
import cn.salesuite.saf.func.functions.Func1;
import cn.salesuite.saf.func.functions.Predicate;
import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 16/9/9.
 */

public class Fn {

    public static <T, R> void forEach(Action1<? super T> func, Iterable<T> iterable) {

        if (Preconditions.isNotBlank(iterable)) {
            for (T t : iterable) {
                func.call(t);
            }
        }
    }

    public static <T, R> List<R> map(Func1<? super T, ? extends R> func, List<T> list) {
        List<R> r = new ArrayList<R>();
        if (Preconditions.isNotBlank(list)) {
            for (T l : list) {
                r.add(func.call(l));
            }
        }

        return r;
    }

    public static <T> T first(Predicate<? super T> p,
                              List<? extends T> list) {

        if (Preconditions.isNotBlank(list)) {
            for (T t : list) {
                if (p.accept(t)) {
                    return t;
                }
            }
        }

        return null;
    }

    public static <T, R>void first(final Func1<? super T, ? extends R> func, Predicate<? super T> p,
                                   List<? extends T> list) {

        if (Preconditions.isNotBlank(list)) {
            for (T t : list) {
                if (p.accept(t)) {
                    func.call(t);
                    return;
                }
            }
        }
    }
}
