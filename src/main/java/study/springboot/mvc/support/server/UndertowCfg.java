package study.springboot.mvc.support.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@Slf4j
public class UndertowCfg {

    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {

        UndertowServletWebServerFactory undertow = new UndertowServletWebServerFactory();
        return undertow;
    }
}
