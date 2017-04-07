package school.journal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import school.journal.interceptor.AuthInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("school.journal")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/pages/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean
    public AuthInterceptor createAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createAuthInterceptor()).excludePathPatterns("/api/auth/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
//                .allowedOrigins("http://localhost:4200");
//                .allowedMethods("PUT", "DELETE", "HEAD", "OPTIONS", "GET", "POST")
//                .allowedHeaders("Authorization");
    }

}