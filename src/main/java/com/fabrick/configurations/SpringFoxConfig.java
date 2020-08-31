package com.fabrick.configurations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author fabio.sgroi
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Autowired
    ConfigurationBase confBase;

    @Bean
    public Docket swaggerPersonApi10() {

        Set<String> protocols = new HashSet<>();
        confBase.getProtocols().forEach((protocol) -> {
            protocols.add(protocol);
        });

        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey("apiKey", "Authorization", "header"));

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Version 1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fabrick.controllers.rest.v1"))
                .paths(regex("/v1/*.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Fabrick Test API").description("Documentation Fabrick Test API v1.0").build())
                .host(confBase.getSwaggerHost())
                .protocols(protocols)
                .securitySchemes(schemeList)
                .useDefaultResponseMessages(false);

    }
}
