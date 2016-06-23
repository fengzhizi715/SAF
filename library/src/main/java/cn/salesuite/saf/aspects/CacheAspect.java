package cn.salesuite.saf.aspects;

import android.annotation.TargetApi;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.reflect.Method;

import cn.salesuite.saf.aspects.annotation.Cacheable;
import cn.salesuite.saf.cache.Cache;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * Created by Tony Shen on 16/3/23.
 */
@TargetApi(14)
@Aspect
public class CacheAspect {

    @Around("execution(!synthetic * *(..)) && onCacheMethod()")
    public Object doCacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return cacheMethod(joinPoint);
    }

    @Pointcut("@within(cn.salesuite.saf.aspects.annotation.Cacheable)||@annotation(cn.salesuite.saf.aspects.annotation.Cacheable)")
    public void onCacheMethod() {
    }

    private Object cacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        String key = cacheable.key();
        int expiry = cacheable.expiry();

        Object result = joinPoint.proceed();
        Cache cache = Cache.get(SAFUtils.getContext());
        if (expiry>0) {
            cache.put(key,(Serializable)result,expiry);
        } else {
            cache.put(key,(Serializable)result);
        }

        return result;
    }
}
