package com.anyun.streamdemo;

import com.anyun.streamdemo.config.SenderSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(SenderSource.class)
@SpringBootApplication
public class ProceduerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProceduerApplication.class, args);
    }

}
