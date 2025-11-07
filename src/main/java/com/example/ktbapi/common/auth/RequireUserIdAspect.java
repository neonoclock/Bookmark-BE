package com.example.ktbapi.common.auth;

import com.example.ktbapi.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RequireUserIdAspect {

    private static final String HEADER_NAME = "X-USER-ID";

    private final HttpServletRequest request;

    public RequireUserIdAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(com.example.ktbapi.common.auth.RequireUserId)")
    public Object checkUserId(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();
        RequireUserId ann = method.getAnnotation(RequireUserId.class);

        Object[] args = pjp.getArgs();
        Class<?>[] paramTypes = method.getParameterTypes();

        Long userId = null;

        int idx = ann.paramIndex();
        if (idx >= 0 && idx < args.length && args[idx] instanceof Long) {
            userId = (Long) args[idx];
        }

        if (userId == null) {
            String header = request.getHeader(HEADER_NAME);
            if (header != null && !header.isBlank()) {
                try {
                    userId = Long.valueOf(header.trim());
                } catch (NumberFormatException ignore) {
                }
            }
        }

        if (userId == null) throw new UnauthorizedException();

        int targetIdx = -1;

        String[] paramNames = sig.getParameterNames();
        if (paramNames != null) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (("userId".equals(paramNames[i])) &&
                    (paramTypes[i] == Long.class || paramTypes[i] == long.class)) {
                    targetIdx = i;
                    break;
                }
            }
        }

        if (targetIdx < 0) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == Long.class || paramTypes[i] == long.class) {
                    targetIdx = i;
                    break;
                }
            }
        }

        if (targetIdx >= 0) {
            Object[] newArgs = args.clone();
            newArgs[targetIdx] = userId;
            request.setAttribute("userId", userId);
            return pjp.proceed(newArgs);
        }

        request.setAttribute("userId", userId);
        return pjp.proceed(args);
    }
}
