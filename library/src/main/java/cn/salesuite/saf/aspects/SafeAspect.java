package cn.salesuite.saf.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import cn.salesuite.saf.log.L;

/**
 * Created by Tony Shen on 16/3/23.
 */
@Aspect
public class SafeAspect {

    @Around("execution(!synthetic * *(..)) && onSafe()")
    public Object doLogMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return safeMethod(joinPoint);
    }

    @Pointcut("@within(cn.salesuite.saf.aspects.annotation.Safe)||@annotation(cn.salesuite.saf.aspects.annotation.Safe)")
    public void onSafe() {
    }

    private Object safeMethod(final ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            L.w(joinPoint.getSignature().toShortString() + (joinPoint.getArgs() != null ? Arrays.deepToString(joinPoint.getArgs()) : ""));
            L.w(getStringFromException(e));
        }
        return result;
    }

    private static String getStringFromException(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
