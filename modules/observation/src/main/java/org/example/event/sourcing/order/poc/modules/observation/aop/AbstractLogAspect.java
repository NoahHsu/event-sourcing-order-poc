package org.example.event.sourcing.order.poc.modules.observation.aop;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLogAspect {

    public Object logInfoAround(ProceedingJoinPoint joinPoint) throws Throwable {

        LogInfo logInfo = getLogInfo(joinPoint);
        Logger log = LoggerFactory.getLogger(logInfo.declaringType);
        logBefore(logInfo, log);

        Object object = joinPoint.proceed();

        logAfter(logInfo, log);
        return object;
    }

    private static LogInfo getLogInfo(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        Class declaringType = signature.getDeclaringType();
        String className = declaringType.getSimpleName();
        String annotatedMethodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        return new LogInfo(declaringType, className, annotatedMethodName, args);
    }

    private static void logBefore(LogInfo logInfo, Logger log) {
        log.info("[{}.{}] start ({})", logInfo.className, logInfo.annotatedMethodName, logInfo.args);
    }

    private static void logAfter(LogInfo logInfo, Logger log) {
        log.info("[{}.{}] end", logInfo.className, logInfo.annotatedMethodName);
    }

    public void logBefore(ProceedingJoinPoint joinPoint) {
        LogInfo logInfo = getLogInfo(joinPoint);
        Logger log = LoggerFactory.getLogger(logInfo.declaringType);
        logBefore(logInfo, log);
    }

    public void logAfter(ProceedingJoinPoint joinPoint) {
        LogInfo logInfo = getLogInfo(joinPoint);
        Logger log = LoggerFactory.getLogger(logInfo.declaringType);
        logAfter(logInfo, log);
    }
    private record LogInfo(
            @NotNull
            Class declaringType,
            @NotNull
            String className,
            @NotNull
            String annotatedMethodName,
            @Nullable
            Object[] args) {
    }

}
