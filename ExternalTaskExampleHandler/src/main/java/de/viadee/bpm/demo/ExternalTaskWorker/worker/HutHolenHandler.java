package de.viadee.bpm.demo.ExternalTaskWorker.worker;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HutHolenHandler implements ExternalTaskHandler {
    private static final Logger LOG = LoggerFactory.getLogger(HutHolenHandler.class);

    public void execute(final ExternalTask externaltask, final ExternalTaskService externalTaskService) {

        LOG.info("Hut wird extern geholt.");
        externalTaskService.complete(externaltask,
                Variables.createVariables().putValue("hut", "Huebscher Hut"));

    }
}
