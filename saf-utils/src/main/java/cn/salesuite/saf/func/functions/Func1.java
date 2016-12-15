package cn.salesuite.saf.func.functions;

/**
 * Created by Tony Shen on 16/9/9.
 */

public interface Func1<T, R> extends Function {

    R call(T t);
}
