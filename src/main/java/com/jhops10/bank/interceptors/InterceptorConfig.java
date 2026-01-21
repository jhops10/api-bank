package com.jhops10.bank.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuditInterceptors auditInterceptors;

    public InterceptorConfig(AuditInterceptors auditInterceptors) {
        this.auditInterceptors = auditInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(auditInterceptors)
                .order(0);
    }
}
