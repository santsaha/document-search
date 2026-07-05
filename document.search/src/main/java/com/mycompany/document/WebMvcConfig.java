package com.mycompany.document;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mycompany.document.config.TenantRateLimitInterceptor;
import com.mycompany.document.config.TenantResolvingInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final TenantResolvingInterceptor tenantInterceptor;
	private final TenantRateLimitInterceptor tenantRateLimitInterceptor;

	public WebMvcConfig(TenantResolvingInterceptor tenantInterceptor,
			TenantRateLimitInterceptor tenantRateLimitInterceptor) {
		this.tenantInterceptor = tenantInterceptor;
		this.tenantRateLimitInterceptor = tenantRateLimitInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(tenantInterceptor).addPathPatterns("/**")
				.excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/error", "/actuator/health", "/health")
				.order(1);

		registry.addInterceptor(tenantRateLimitInterceptor).addPathPatterns("/**")
				.excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/error", "/actuator/health", "/health")
				.order(2);
	}
}
