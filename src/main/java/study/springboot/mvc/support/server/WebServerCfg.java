package study.springboot.mvc.support.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 当Spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时
 * 会把此bean加载金spring容器中
 */
@Slf4j
@Component
public class WebServerCfg implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        String name = factory.getClass().getName();
        log.info("======>{}", name);
        //使用对应工厂类提供给我们的接口定制化我们的tomcat connector
        TomcatServletWebServerFactory tomcatFactory = (TomcatServletWebServerFactory) factory;
        tomcatFactory.addConnectorCustomizers(new ConnectorCustomizers());
    }

    public class ConnectorCustomizers implements TomcatConnectorCustomizer {

        @Override
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            //定制化keepAliveTimeout，设置30秒内没有请求则服务端自动断开keepalive链接
            protocol.setKeepAliveTimeout(300000);
            //当客户端发送超过10000个请求则自动断开keepalive链接
            protocol.setMaxKeepAliveRequests(10000);

            int maxThreads = protocol.getMaxThreads();
            int minSpareThreads = protocol.getMinSpareThreads();
            int maxConnections = protocol.getMaxConnections();
            int acceptCount = protocol.getAcceptCount();
            log.info("======>");
            log.info("======>maxThreads={}, minSpareThreads={}", maxThreads, minSpareThreads);
            log.info("======>maxConnections={}, acceptCount={}", maxConnections, acceptCount);
            log.info("======>");
        }
    }
}
