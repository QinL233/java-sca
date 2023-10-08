package com.baidu.sz.web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年06月21日 17:43:00
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${header.token}")
    private String token;

    @Value("${header.uuap_login_user}")
    private String uuapLoginUser;

    @Value("${server.controller-packages}")
    private String controller;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("rest_api")
                .select()
                //扫描有@RestController或Controller注解的类
                .apis(RequestHandlerSelectors.basePackage(controller))
                //扫描所有路径
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful API")
                .description("swagger-ui")
                .version("1.0")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey(token, token, "header"));
        apiKeyList.add(new ApiKey(uuapLoginUser, uuapLoginUser, "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContextList = new ArrayList<>();
        List<SecurityReference> securityReferenceList = new ArrayList<>();
        securityReferenceList.add(new SecurityReference(token, new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")}));
        securityReferenceList.add(new SecurityReference(uuapLoginUser, new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")}));
        securityContextList.add(SecurityContext
                .builder()
                .securityReferences(securityReferenceList)
                .forPaths(PathSelectors.any())
                .build()
        );
        return securityContextList;
    }
}
