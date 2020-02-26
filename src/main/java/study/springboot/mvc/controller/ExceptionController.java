package study.springboot.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @GetMapping("/throwException")
    public void throwException() {
        if (1 == 1) {
            throw new RuntimeException("我是一个异常");
        }
    }
}
