package com.safframework.permissions;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by Tony Shen on 2016/11/28.
 */
@Aspect
public class PermissionAspect {

    @Around("execution(!synthetic * *(..)) && onPermissionMethod()")
    public void doPermissionMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        permissionMethod(joinPoint);
    }

    @Pointcut("@within(cn.salesuite.saf.permissions.Permission)||@annotation(cn.salesuite.saf.permissions.Permission)")
    public void onPermissionMethod() {
    }

    public void permissionMethod(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Permission permission = method.getAnnotation(Permission.class);

        if (permission==null || permission.value()==null) return;

        PermissionGuardAware permissionGuardAware = (PermissionGuardAware) joinPoint.getTarget();
        PermissionGuard permissionGuard = permissionGuardAware.getPermissionGuard();
        if (permissionGuard.isPermissionResult())
            return;

        permissionGuard.requestPermission(new Runnable() {
            @Override
            public void run() {
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                    Log.d("PermissionAspect","joinPoint errror", e);
                }
            }
        }, permission.value());
    }
}
