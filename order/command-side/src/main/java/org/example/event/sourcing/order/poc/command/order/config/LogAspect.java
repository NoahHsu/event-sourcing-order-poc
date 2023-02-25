package org.example.event.sourcing.order.poc.command.order.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.event.sourcing.order.poc.common.aop.AbstractLogAspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect extends AbstractLogAspect {

    @Override
    @Around("@annotation(org.example.event.sourcing.order.poc.common.annotation.LogInfo)" +
            "|| @within(org.example.event.sourcing.order.poc.common.annotation.LogInfo)")
    public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.logInfo(joinPoint);
    }

}