package study.springboot.docker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @GetMapping("/sayHi")
    public String sayHi(String name) {
        log.info("hello, {}", name);
        return String.format("hi, %s", name);
    }
}
