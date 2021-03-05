package com.magiplatform.dorahack.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version V1.0
 * @Description: TODO
 * @class: Swagger2Config
 * @Package com.magiplatform.dorahack.configuration
 * @date 2020/9/1 13:55
 */
@EnableSwagger2
@EnableKnife4j
@Configuration
public class Swagger2Config {


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public Docket web_1_0() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
//                .globalOperationParameters(headParamDefault())
                .groupName(SwaggerApiVersionConstant.WEB_1_0)
                .select()
                .apis(input -> {
                    SwaggerApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(SwaggerApiVersion.class);
                    return apiVersion != null && Arrays.asList(apiVersion.group()).contains(SwaggerApiVersionConstant.WEB_1_0);
                })
                .paths(PathSelectors.any())
                .build();
    }


    private List<Parameter> headParamDefault() {
        List<Parameter> pars = new ArrayList<>();

//        ParameterBuilder ticketPar = new ParameterBuilder();
//        ticketPar.name("token").description("user token")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(true).build(); //header中的ticket参数非必填，传空也可以
//        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数
        return pars;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Restfull API for Dorahack")
                .description("Michael Ran's dorahack restfull API")
                .termsOfServiceUrl("---")
                .version("1.0")
                .build();
    }
}
