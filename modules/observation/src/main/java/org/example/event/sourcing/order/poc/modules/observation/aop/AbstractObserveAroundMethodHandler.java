package org.example.event.sourcing.order.poc.modules.observation.aop;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.aop.ObservedAspect;
import org.aspectj.lang.ProceedingJoinPoint;

public class AbstractObserveAroundMethodHandler extends AbstractLogAspect
        implements ObservationHandler<ObservedAspect.ObservedAspectContext> {

    @Override
    public void onStart(ObservedAspect.ObservedAspectContext context) {
        ProceedingJoinPoint joinPoint = context.getProceedingJoinPoint();
        super.logBefore(joinPoint);
    }

    @Override
    public void onStop(ObservedAspect.ObservedAspectContext context) {
        ProceedingJoinPoint joinPoint = context.getProceedingJoinPoint();
        super.logAfter(joinPoint);
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return context instanceof ObservedAspect.ObservedAspectContext;
    }
}
