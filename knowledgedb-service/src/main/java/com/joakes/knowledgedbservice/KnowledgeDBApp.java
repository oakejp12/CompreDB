package com.joakes.knowledgedbservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableEncryptableProperties
@SpringBootApplication
public class KnowledgeDBApp {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeDBApp.class, args);
    }

}
