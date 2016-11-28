package cn.salesuite.saf.permissions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by Tony Shen on 2016/11/28.
 */
@Aspect
public class PermissionAspect {

    @Pointcut("within(android.app.Activity+)")
    public void withinActivity() {
    }

    @Pointcut("execution(@cn.salesuite.saf.permissions.Permission * *(..))")
    public void camera() {
    }

    @Before("camera() && withinActivity()")
    public void cameraAspect(JoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Permission permission = method.getAnnotation(Permission.class);
        String value = permission.value();

        Permissions.askSinglePermissionToActivity(joinPoint, value);
    }
}
