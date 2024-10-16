package com.sdm.app;

import com.sdm.app.interceptor.RateLimitInterceptor;
import com.sdm.app.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {


  private final RateLimitInterceptor rateLimitInterceptor;
  private final UserArgumentResolver userArgumentResolver;

  @Autowired
  public WebConfiguration(RateLimitInterceptor rateLimitInterceptor, UserArgumentResolver userArgumentResolver) {
    this.rateLimitInterceptor = rateLimitInterceptor;
    this.userArgumentResolver = userArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(userArgumentResolver);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    WebMvcConfigurer.super.addCorsMappings(registry);
    registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");

  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**");
  }
}
