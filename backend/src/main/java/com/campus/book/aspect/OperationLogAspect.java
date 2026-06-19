package com.campus.book.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.campus.book.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SysOperationLogService sysOperationLogService;

    @Around("@annotation(com.campus.book.aspect.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog annotation = signature.getMethod().getAnnotation(OperationLog.class);

        Object result = null;
        Throwable error = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            try {
                Long adminId = null;
                String ip = "127.0.0.1";

                try {
                    adminId = (Long) StpUtil.getLoginId();
                } catch (Exception ignored) {
                }

                try {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest request = attributes.getRequest();
                        ip = request.getRemoteAddr();
                    }
                } catch (Exception ignored) {
                }

                String detail = annotation.action();
                if (error != null) {
                    detail += " - 失败: " + error.getMessage();
                }

                sysOperationLogService.saveLog(
                        adminId,
                        annotation.module(),
                        annotation.action(),
                        null,
                        detail,
                        ip
                );
            } catch (Exception ignored) {
            }
        }
    }
}
