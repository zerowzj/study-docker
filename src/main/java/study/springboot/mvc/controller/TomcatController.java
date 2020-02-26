package study.springboot.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class TomcatController {

    @Autowired
    private ServerProperties prop;

    @RequestMapping("/tomcat")
    public String demo() {
        log.info("start...");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("end...");

        return "this ia tomcat";
    }
}
