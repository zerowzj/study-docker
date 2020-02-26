package study.springboot.mvc.support.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@Slf4j
public class TomcatCfg {

    private static final String PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";

    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        Connector connector = new Connector(PROTOCOL);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        connector.setPort(8090);
        protocol.setMaxThreads(2);
        protocol.setMaxConnections(10);
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(connector);
         return tomcat;
    }
}
