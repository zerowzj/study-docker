package study.springboot.docker.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class EnvController {

    @Value("${docker.env.name}")
    private String name;

    @GetMapping("/env")
    public Map<String, Object> sayHi() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("name", name);
        data.put("age", name);
        return data;
    }
}
