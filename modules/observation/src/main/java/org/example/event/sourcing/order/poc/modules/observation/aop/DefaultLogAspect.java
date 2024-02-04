package org.example.event.sourcing.order.poc.modules.observation.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DefaultLogAspect extends AbstractLogAspect {

    @Override
    @Around("@annotation(org.example.event.sourcing.order.poc.common.annotation.LogInfo)" +
            "|| @within(org.example.event.sourcing.order.poc.common.annotation.LogInfo)")
    public Object logInfoAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.logInfoAround(joinPoint);
    }

}