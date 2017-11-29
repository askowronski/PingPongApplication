package app.API;


import javafx.application.Application;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class ,args);
    }
//    public static void main(String[] args) {
//        SpringApplication.run(applicationClass, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(applicationClass);
//    }
//
//    private static Class<PlayerAPI> applicationClass = PlayerAPI.class;
//
//    @RestController
//    class GreetingController {
//
//        @RequestMapping("/hello")
//        String hello() {
//            return "Hello!";
//        }
//    }
//
//    @RestController
//    class GreetController {
//
//        @RequestMapping("/yo")
//        String hello() {
//            return "Hello!";
//        }
//    }
}