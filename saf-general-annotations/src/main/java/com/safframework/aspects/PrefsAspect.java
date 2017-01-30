package com.safframework.aspects;

import android.annotation.TargetApi;

import com.safframework.aspects.annotation.Prefs;
import com.safframework.prefs.AppPrefs;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import cn.salesuite.saf.utils.Preconditions;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * Created by Tony Shen on 16/3/28.
 */
@TargetApi(14)
@Aspect
public class PrefsAspect {

    @Around("execution(!synthetic * *(..)) && onPrefsMethod()")
    public Object doPrefsMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return prefsMethod(joinPoint);
    }

    @Pointcut("@within(com.safframework.aspects.annotation.Prefs)||@annotation(com.safframework.aspects.annotation.Prefs)")
    public void onPrefsMethod() {
    }

    private Object prefsMethod(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Prefs prefs = method.getAnnotation(Prefs.class);
        Object result = null;
        if (prefs!=null) {
            String key = prefs.key();

            result = joinPoint.proceed();
            String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();

            if (!"void".equalsIgnoreCase(type)) {
                String className = ((MethodSignature) joinPoint.getSignature()).getReturnType().getCanonicalName();
                AppPrefs appPrefs = AppPrefs.get(SAFUtils.getContext());
                if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                    appPrefs.putInt(key, (Integer) result);
                } else if ("boolean".equals(className) || "java.lang.Boolean".equals(className)) {
                    appPrefs.putBoolean(key,(Boolean) result);
                } else if ("float".equals(className) || "java.lang.Float".equals(className)) {
                    appPrefs.putFloat(key,(Float) result);
                } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
                    appPrefs.putLong(key,(Long) result);
                } else if ("java.lang.String".equals(className)) {
                    appPrefs.putString(key,(String) result);
                } else {
                    appPrefs.putObject(key,result);
                }
            }
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }
}
