package com.test.saf.script;

/**
 * Created by Tony Shen on 16/7/13.
 */
public interface Invocable {

    Object invokeFunction(String name, Object... args);
}
