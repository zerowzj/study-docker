package study.springboot.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import study.springboot.docker.support.SpringBootCfg;

public class Startup {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootCfg.class, args);
        context.start();
    }
}
