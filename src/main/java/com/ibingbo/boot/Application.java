package com.ibingbo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by bing on 17/5/27.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //        SpringApplication.run(Application.class, "--server.port=8081");

    }
}
