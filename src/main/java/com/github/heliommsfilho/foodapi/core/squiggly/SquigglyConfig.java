package com.github.heliommsfilho.foodapi.core.squiggly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SquigglyConfig {
    
    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(final ObjectMapper objectMapper) {
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));
        
        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
        filterRegistration.setUrlPatterns(Arrays.asList("/pedidos/*", "/restaurante/*"));
        filterRegistration.setFilter(new SquigglyRequestFilter());
        filterRegistration.setOrder(1);
        
        return filterRegistration;
    }
}
