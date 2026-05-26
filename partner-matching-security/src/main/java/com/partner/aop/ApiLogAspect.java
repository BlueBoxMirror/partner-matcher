package com.partner.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.partner.context.UserContext;
import com.partner.entity.ApiCallLog;
import com.partner.service.ApiCallLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiLogAspect {

    private final ApiCallLogService apiCallLogService;
    private final ObjectMapper objectMapper;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Controller *)")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Throwable businessException = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            businessException = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;

            try {
                ApiCallLog logEntry = new ApiCallLog();
                logEntry.setCreateTime(LocalDateTime.now());
                logEntry.setApiName(joinPoint.getSignature().toShortString());

                String params = "";
                try {
                    params = objectMapper.writeValueAsString(joinPoint.getArgs());
                } catch (JsonProcessingException ignored) {}
                logEntry.setRequestParams(params);


                String responseStr = "";
                if (businessException == null && result != null) {
                    try {
                        responseStr = objectMapper.writeValueAsString(result);
                    } catch (JsonProcessingException ignored) {}
                } else if (businessException != null) {
                    responseStr = "exception: " + businessException.getMessage();
                }

                logEntry.setResponseResult(responseStr.length() > 2000 ? responseStr.substring(0, 2000) : responseStr);

                logEntry.setCostTime(cost);
                logEntry.setUserId(UserContext.getUserId()); // 可能为 null

                apiCallLogService.saveLog(logEntry);
            } catch (Exception e) {
                log.error("记录接口日志失败", e);
            }
        }
    }
}