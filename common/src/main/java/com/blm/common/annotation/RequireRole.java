package com.blm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限检查注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    
    /**
     * 需要的角色列表
     */
    String[] value() default {};
    
    /**
     * 是否需要拥有所有角色 (AND)，false表示拥有任一角色即可 (OR)
     */
    boolean requireAll() default false;
    
    /**
     * 错误信息
     */
    String message() default "权限不足";
}
