package com.ylab.aspect;

import com.ylab.model.Audit;
import com.ylab.repository.AuditDao;
import com.ylab.repository.impl.AuditDaoImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * LoggingAspect class is responsible for logging method calls in the application.
 * <p>
 * This aspect intercepts method calls and logs information such as method name, signature, and arguments.
 *
 * @author razlivinsky
 * @since 16.02.2024
 */
@Aspect
@Component
public class LoggingAspect {
    public final AuditDao auditDao;

    /**
     * Instantiates a new Logging aspect.
     *
     * @param jdbcTemplate the jdbc template used for database operations
     */
    public LoggingAspect(JdbcTemplate jdbcTemplate) {
        this.auditDao = new AuditDaoImpl(jdbcTemplate);
    }

    /**
     * Logs method calls before their execution.
     *
     * @param joinPoint the join point at which the advice is being called
     */
    @Before("execution(* com.ylab.app..*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);

        String methodName = joinPoint.getSignature().getName();
        String signature = joinPoint.getSignature().toString();
        Object[] args = joinPoint.getArgs();

        String result = "Time: " + formattedDateTime + System.lineSeparator()
                + "Method Name: " + methodName + System.lineSeparator()
                + "Signature: " + signature + System.lineSeparator()
                + "Arguments: " + Arrays.toString(args);
        Audit audit = new Audit(result);
        auditDao.sendMessage(audit);
        List<Audit> auditList = auditDao.getMessage();
        System.out.println(auditList);
    }
}