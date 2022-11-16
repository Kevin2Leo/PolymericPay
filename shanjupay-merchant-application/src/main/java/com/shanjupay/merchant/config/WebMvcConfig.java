package com.shanjupay.merchant.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Date Created in 6:48 2022/6/24
 * @Author: Chen_zhuo
 * @Modified By
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加静态资源文件，外部可以直接访问地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger‐ui.html")
                .addResourceLocations("classpath:/META‐INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META‐INF/resources/webjars/");
    }
}