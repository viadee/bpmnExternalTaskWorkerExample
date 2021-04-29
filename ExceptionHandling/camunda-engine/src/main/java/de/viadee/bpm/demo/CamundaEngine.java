package de.viadee.bpm.demo;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication("external-task-process-application")
public class CamundaEngine {

    public static void main(final String... args) {
        SpringApplication.run(CamundaEngine.class, args);
    }

}
