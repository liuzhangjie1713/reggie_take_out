package com.liu.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.liu.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class WebMvcConfig implements WebMvcConfigurer {


    //设置拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册Interceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new UserLoginInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //添加不拦截路径
        registration.excludePathPatterns(
                "/employee/login",
                "/**/*.html",                 //html静态资源
                "/**/*.js",                  //js静态资源
                "/**/*.css",                //css静态资源
                "/backend/images/**",
                "/user/sendMsg",            //移动端短信发送
                "/user/login",             //移动端登录
                "/doc.html",               //swagger
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        );
    }

    //设置静态资源映射

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //添加映射
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    //设置swagger
    @Bean
    public Docket createRestApi() {
        //文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.liu.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("瑞吉外卖")
                .version("1.0")
                .description("瑞吉外卖接口文档")
                .build();
    }



}
