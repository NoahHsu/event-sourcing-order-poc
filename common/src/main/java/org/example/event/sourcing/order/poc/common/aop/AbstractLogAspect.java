package org.example.event.sourcing.order.poc.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractLogAspect {

    public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Class declaringType = signature.getDeclaringType();
        String className = declaringType.getSimpleName();
        String annotatedMethodName = signature.getName();
        Logger log = LoggerFactory.getLogger(declaringType);
        Object[] args = joinPoint.getArgs();

        log.info("[{}.{}] start ({})", className, annotatedMethodName, args);

        Object object = joinPoint.proceed();

        log.info("[{}.{}] end", className, annotatedMethodName);

        return object;
    }

}
