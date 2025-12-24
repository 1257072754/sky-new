package com.sky.aspect.annotation;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 自定义注解，用于标识需要自动填充的属性
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillCut() {
    }

    /**
     * 前置通知
     */
    @Before("autoFillCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行数据填充");

        // 获取当前被拦截的方法上的数据库操作类型
        OperationType operationType = getOperationType(joinPoint);

        // 获取当前被拦截的方法参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        // 准备需要填充的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据不同的操作类型进行对应的属性填充
        if (operationType == OperationType.INSERT) {
            // 为新增操作填充数据
            try {
                setFieldValue(entity, "createTime", now);
                setFieldValue(entity, "updateTime", now);
                setFieldValue(entity, "createUser", currentId);
                setFieldValue(entity, "updateUser", currentId);
            } catch (Exception e) {
                log.error("自动填充字段时发生异常", e);
            }
        } else if (operationType == OperationType.UPDATE) {
            // 为更新操作填充数据
            try {
                setFieldValue(entity, "updateTime", now);
                setFieldValue(entity, "updateUser", currentId);
            } catch (Exception e) {
                log.error("自动填充字段时发生异常", e);
            }
        }
    }

    /**
     * 获取操作类型
     *
     * @param joinPoint
     * @return
     */
    private OperationType getOperationType(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        return autoFill.value();
    }

    // 提取的公共方法
    private void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
