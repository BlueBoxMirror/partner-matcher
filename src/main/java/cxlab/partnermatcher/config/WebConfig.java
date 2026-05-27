package cxlab.partnermatcher.config;

import cxlab.partnermatcher.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/api/user/**") // 拦截所有/api/user开头的接口
                // 放行不需要token的4个接口，和前端约定的地址完全一致
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/user/emailLogin",
                        "/api/user/sendCode"
                );
    }
}