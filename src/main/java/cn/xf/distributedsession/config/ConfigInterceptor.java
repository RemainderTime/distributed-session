package cn.xf.distributedsession.config;

import cn.xf.distributedsession.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: distributed-session
 * @description:
 * @author: xf
 * @create: 2021-01-25 23:40
 **/
@Configuration
public class ConfigInterceptor implements WebMvcConfigurer {

    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/user/*")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/tokenLogin")
                .excludePathPatterns("/user/jwtLogin");

    }
}
