package cn.salesuite.saf.permissions;

import android.Manifest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Tony Shen on 2016/11/28.
 */
@Aspect
public class CameraAspect {

    @Pointcut("within(android.app.Activity+)")
    public void withinActivity() {
    }

    @Pointcut("execution(@cn.salesuite.saf.permissions.Camera * *(..))")
    public void camera() {
    }

    @Before("camera() && withinActivity()")
    public void cameraAspect(JoinPoint joinPoint) throws Throwable {
        Permissions.askSinglePermissionToActivity(joinPoint, Manifest.permission.CAMERA);
    }
}
