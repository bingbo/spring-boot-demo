package com.ibingbo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bing on 17/5/26.
 */
@Controller
@SpringBootApplication
public class SampleController {

    @ResponseBody
    @RequestMapping(value = "/")
    public String index() {
        return "hello,world!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
//        SpringApplication.run(SampleController.class, "--server.port=8081");
    }
}
