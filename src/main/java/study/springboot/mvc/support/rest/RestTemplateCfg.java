package study.springboot.mvc.support.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Configuration
public class RestTemplateCfg {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 5 * 1000;

    private static final int DEFAULT_READ_TIMEOUT = 5 * 1000;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.setConnectTimeout(Duration.ofMillis(DEFAULT_CONNECTION_TIMEOUT))
                .setReadTimeout(Duration.ofMillis(DEFAULT_READ_TIMEOUT))
                .build();

//        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    //    @Bean
    public RestTemplate restTemplateBySimple() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(DEFAULT_CONNECTION_TIMEOUT);
        factory.setConnectTimeout(DEFAULT_READ_TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    //    @Bean
    public RestTemplate restTemplateByHttpComponents() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(DEFAULT_CONNECTION_TIMEOUT);
        factory.setConnectTimeout(DEFAULT_READ_TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    //    @Bean
    public RestTemplate restTemplateByOkHttp3() {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        factory.setReadTimeout(DEFAULT_CONNECTION_TIMEOUT);
        factory.setConnectTimeout(DEFAULT_READ_TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}
