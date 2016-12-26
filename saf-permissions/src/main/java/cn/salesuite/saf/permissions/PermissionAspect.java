package cn.salesuite.saf.permissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import cn.salesuite.saf.log.L;
import cn.salesuite.saf.utils.Preconditions;

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

        if (Preconditions.isBlank(permission) || Preconditions.isBlank(permission.value()))
            return;

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
                    L.d("joinPoint errror", e);
                }
            }
        }, permission.value());
    }
}
