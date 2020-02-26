package study.springboot.mvc.support.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletCfg {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletCfg.class);

    public FilterRegistrationBean filter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        return registration;
    }


    public ServletRegistrationBean servlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean();
        registration.addUrlMappings("/demo");
        return registration;
    }

    public ServletListenerRegistrationBean listener() {
        ServletListenerRegistrationBean registration = new ServletListenerRegistrationBean();
        registration.setOrder(1);
        return registration;
    }
}
