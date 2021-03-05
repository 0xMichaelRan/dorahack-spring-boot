package com.magiplatform.dorahack.configuration;

import com.magiplatform.dorahack.filter.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("*.js").addResourceLocations(
                "/webapp/js/**");
        registry.addResourceHandler("*.css").addResourceLocations(
                "webapp/css/**");
        registry.addResourceHandler("*.img").addResourceLocations(
                "webapp/img/**");
        super.addResourceHandlers(registry);
    }

    /**
     * 在这里添加Bean 来解决拦截类内部bean注入问题
     * @return
     */
    @Bean
    LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(loginInterceptor()).addPathPatterns("/v1/**");
//        super.addInterceptors(registry);
    }
}
