package de.viadee.bpm.demo.ExternalTaskWorker.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("hutHolenDelegate")
public class HutHolenDelegate implements JavaDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(HutHolenDelegate.class);

    public void execute(final DelegateExecution execution) throws Exception {

        LOG.info("Hut wird geholt.");
        execution.setVariable("hut", "Huebscher Hut");

    }
}
