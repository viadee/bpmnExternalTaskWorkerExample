package de.viadee.bpm.demo.ExternalTaskWorker.config;

import de.viadee.bpm.demo.ExternalTaskWorker.worker.HutHolenHandler;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.ExternalTaskClientBuilder;
import org.camunda.bpm.client.backoff.BackoffStrategy;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.camunda.bpm.client.impl.ExternalTaskClientBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    @Value("${hutholen.external.initTime:500}")
    private long workerInitTime;

    @Value("${hutholen.external.lockFactor:2}")
    private long workerLockFactor;

    @Value("${hutholen.external.lockMaxTime:600}")
    private long workerLockMaxTime;

    @Value("${hutholen.external.processEngine.rest}")
    private String restUrlProcessEngine;

    @Value("${hutholen.external.id:HappyWorker}")
    private String workerId;

    @Value("${hutholen.external.topic:HutBesorgen}")
    private String workerTopic;

    @Value("${hutholen.external.lockDuration:2000}")
    private long lockDuration;

    private ExternalTaskClientBuilder externalTaskClientBuilder() {
        return new ExternalTaskClientBuilderImpl();
    }

    private BackoffStrategy backoffStrategy() {
        return new ExponentialBackoffStrategy(this.workerInitTime, this.workerLockFactor, this.workerLockMaxTime);
    }

    private ExternalTaskClient externalTaskClient() {
        return this.externalTaskClientBuilder()
                .baseUrl(this.restUrlProcessEngine)
                .backoffStrategy(this.backoffStrategy())
                .lockDuration(this.lockDuration)
                .workerId(this.workerId)
                .build();
    }
    
    //@PostConstruct //Use @Scheduled to avoid errors while starting process engine and worker at the same time
    @Scheduled(initialDelay = 10L * 1000L, fixedDelay = Long.MAX_VALUE)
    public void registerHandler() {
        LOG.info("register task-handler to topic '{}'", this.workerTopic);
        this.externalTaskClient()
                .subscribe(this.workerTopic)
                .handler(new HutHolenHandler())
                .open();
    }

}
