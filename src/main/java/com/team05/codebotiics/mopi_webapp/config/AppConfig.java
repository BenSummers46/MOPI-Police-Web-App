package com.team05.codebotiics.mopi_webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class AppConfig {

    /**
     * Handles the implementation of the REST API
     * @return HandlerMappingIntrospector
     */
    @Bean(name= "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector(){
        return new HandlerMappingIntrospector();
    }
}
