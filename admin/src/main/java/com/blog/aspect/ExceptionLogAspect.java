package com.blog.aspect;

import com.alibaba.fastjson2.JSON;
import com.blog.domain.entity.ExceptionLog;
import com.blog.handler.listener.event.ExceptionLogEvent;
import com.blog.utils.IPUtil;
import com.blog.utils.WebUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author hy
 * @version 1.0
 */
@Aspect
@Component
public class ExceptionLogAspect {

    @Resource
    private ApplicationContext applicationContext;

    @Pointcut("execution(* com.blog.controller..*.*(..))")
    public void exceptionLogPointcut() {
    }

    @AfterThrowing(value = "exceptionLogPointcut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Exception e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes)
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        ExceptionLog exceptionLog = new ExceptionLog();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Operation operation = method.getAnnotation(Operation.class);
        exceptionLog.setOptUri(Objects.requireNonNull(request).getRequestURI());

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        methodName = className + "." + methodName;
        exceptionLog.setOptMethod(methodName);
        exceptionLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        if (joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] instanceof MultipartFile) {
                exceptionLog.setRequestParam("file");
            } else {
                exceptionLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
            }
        }
        if (Objects.nonNull(operation)) {
            exceptionLog.setOptDesc(operation.summary());
        } else {
            exceptionLog.setOptDesc("");
        }
        exceptionLog.setExceptionInfo(WebUtil.getTrace(e));
        String ipAddress = IPUtil.getIpAddress(request);
        exceptionLog.setIpAddress(ipAddress);
        exceptionLog.setIpSource(IPUtil.getIpSource(ipAddress));
        System.err.println("exceptionLog = " + exceptionLog);
        applicationContext.publishEvent(new ExceptionLogEvent(exceptionLog));
    }

}