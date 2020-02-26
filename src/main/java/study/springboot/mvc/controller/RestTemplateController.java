package study.springboot.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class RestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/restTemplate")
    public void demo() {
        ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
        log.info("==========");
        log.info("{}", factory.getClass().getName());
        log.info("==========");
    }
}
