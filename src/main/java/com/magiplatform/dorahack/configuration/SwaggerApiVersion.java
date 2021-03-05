package com.magiplatform.dorahack.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * swagger-api接口版本管理注解
 * 
 * @author lichen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SwaggerApiVersion {
    /**
     * 接口版本号(对应swagger中的group)
     *
     * @return String[]
     */
    String[] group();

}
