package org.wh.k8sfunctions;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect

@Component
@SuppressWarnings("unused")
public class Prometheus {

    private static final Counter requestCounter = Counter.build()
            .help("Total number of requests")
            .labelNames("endpoint", "method")
            .name("http_requests_total").register();

    private static final Summary totalTime = Summary.build()
            .name("mw_processing_time")
            .help("Stores the time the request spent in the MW")
            .register();

    private static final Summary backendTime = Summary.build()
            .name("backend_processing_time")
            .help("Stores the time the request spent in the backend")
            .register();

    private Summary.Timer mwTimer;
    private Summary.Timer backendTimer;


    @Around("execution(* org.wh.k8sfunctions.Services.*.*(..))")
    private Object beforeControllerMethod(ProceedingJoinPoint pjp) {
        Object proceedResult = null;
        backendTimer = backendTime.startTimer();
        mwTimer = totalTime.startTimer();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        requestCounter.labels(request.getRequestURI(), request.getMethod()).inc();
        try {
            proceedResult = pjp.proceed();
        } catch (Throwable e){
            e.printStackTrace();
        }
        backendTimer.observeDuration();
        mwTimer.observeDuration();
        return proceedResult;
    }
}