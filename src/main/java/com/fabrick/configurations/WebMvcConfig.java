package com.fabrick.configurations;

import com.fabrick.dao.OperationImpl;
import com.fabrick.interceptors.RestrictAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author fabio.sgroi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ConfigurationBase confBase;

    @Autowired
    OperationImpl operationImpl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RestrictAccessInterceptor(confBase.getWhitelistIp(), operationImpl));
    }
}
