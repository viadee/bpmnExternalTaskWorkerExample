package de.viadee.bpm.demo.ExternalTaskWorker;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableProcessApplication("EtwTest")
@EnableScheduling
public class HutHolenProzessApp {

    private final RuntimeService runtimeService;

    public HutHolenProzessApp(final RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public static void main(final String... args) {
        SpringApplication.run(HutHolenProzessApp.class, args);
    }

    @Scheduled(initialDelay = 20L * 1000L, fixedDelay = 20L * 1000L) // every 20 seconds after 20 seconds delay
    public void startDelegateProcess() {
        this.runtimeService.startProcessInstanceByKey("hutProzessJavaDelegate");
    }

    @Scheduled(initialDelay = 30L * 1000L, fixedDelay = 20L * 1000L) // every 20 seconds after 30 seconds delay
    public void startWorkerProcess() {
        this.runtimeService.startProcessInstanceByKey("hutProzessExternalTaskWorker");
    }
}
