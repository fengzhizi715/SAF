package cn.salesuite.saf.aspects;

import com.safframework.log.L;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import cn.salesuite.saf.aspects.annotation.HookMethod;
import cn.salesuite.saf.reflect.Reflect;
import cn.salesuite.saf.reflect.ReflectException;
import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 2016/12/7.
 */
@Aspect
public class HookMethodAspect {

    @Around("execution(!synthetic * *(..)) && onHookMethod()")
    public void doHookMethodd(final ProceedingJoinPoint joinPoint) throws Throwable {
        hookMethod(joinPoint);
    }

    @Pointcut("@within(cn.salesuite.saf.aspects.annotation.HookMethod)||@annotation(cn.salesuite" +
            ".saf.aspects.annotation.HookMethod)")
    public void onHookMethod() {
    }

    private void hookMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        HookMethod hookMethod = method.getAnnotation(HookMethod.class);

        if (Preconditions.isBlank(hookMethod)) return;

        String beforeMethod = hookMethod.beforeMethod();
        String afterMethod = hookMethod.afterMethod();

        if (Preconditions.isNotBlank(beforeMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(beforeMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                L.e("no method "+beforeMethod);
            }
        }

        joinPoint.proceed();

        if (Preconditions.isNotBlank(afterMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(afterMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                L.e("no method "+afterMethod);
            }
        }
    }
}
