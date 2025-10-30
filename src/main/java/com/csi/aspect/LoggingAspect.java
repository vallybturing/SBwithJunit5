package com.csi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Aspect for logging method execution across different layers of the application
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    /**
     * Pointcut for all methods in REST Controllers
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
        // Pointcut definition
    }

    /**
     * Logs execution of REST Controller methods
     * Includes: method name, arguments, execution time, and return value
     */
    @Around("restControllerPointcut()")
    public Object logAroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        // Log method entry
        log.info("→ [CONTROLLER] {}.{}() called with parameters: {}",
                className, methodName, formatArguments(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = null;
        try {
            // Execute the actual method
            result = joinPoint.proceed();

            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            // Log successful execution
            log.info("← [CONTROLLER] {}.{}() completed successfully in {}ms",
                    className, methodName, executionTime);

            // Log return value (be careful with large objects)
            if (result != null) {
                log.debug("   [CONTROLLER] {}.{}() returned: {}",
                        className, methodName, formatResult(result));
            }

            return result;

        } catch (Exception e) {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            // Log exception
            log.error("✗ [CONTROLLER] {}.{}() failed after {}ms with exception: {} - {}",
                    className, methodName, executionTime,
                    e.getClass().getSimpleName(), e.getMessage());

            throw e;
        }
    }

    /**
     * Format method arguments for logging
     */
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        return Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) {
                        return "null";
                    }
                    String argString = arg.toString();
                    // Truncate long strings
                    return argString.length() > 100
                            ? argString.substring(0, 97) + "..."
                            : argString;
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Format return value for logging
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }

        String resultString = result.toString();

        // Truncate long results
        if (resultString.length() > 200) {
            return resultString.substring(0, 197) + "...";
        }

        return resultString;
    }
}