package com.itrail.klinikreact.aspect.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    /**
     * Время выполнения метода через АОП
     * @param joinPoint - ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
    @Around( value = "@annotation( com.itrail.klinikreact.aspect.log.ExecuteTimeLog )")
    public Object logExecutionTime( ProceedingJoinPoint proceedingJoinPoint ) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = proceedingJoinPoint.proceed();
        stopWatch.stop();
        MethodSignature methodSignature = ( MethodSignature ) proceedingJoinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Method name: ").append( methodSignature.getMethod().getName()).append(", ");
        for (int i = 0; i < paramNames.length; i++) {
            logMessage.append(paramNames[i]).append(" = ").append( proceedingJoinPoint.getArgs()[i]);
            if (i < paramNames.length - 1) {
                logMessage.append(", "); 
            }
        }
        logMessage.append( " Execution time: " + stopWatch.getTotalTimeMillis() + " ms");
        log.info(logMessage.toString());
        return proceed;
    }
}
