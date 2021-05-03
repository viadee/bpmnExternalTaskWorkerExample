package de.viadee.bpm.demo.handler;

import de.viadee.bpm.demo.service.MailService;
import de.viadee.bpm.demo.service.RecipientNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
//@ExternalTaskSubscription("send-mail")
public class MailSendManualRetriesHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(MailSendManualRetriesHandler.class);
    private static final long ONE_MINUTE = 1000L * 60;
    private static final int MAX_RETRIES = 5;
    private final MailService mailService;

    public MailSendManualRetriesHandler(final MailService mailService) {
        this.mailService = mailService;
    }

    public void execute(final ExternalTask externalTask, final ExternalTaskService externalTaskService) {
        try {

            // E-Mail-Adresse und Inhalt aus Prozesskontext laden
            String recipient = externalTask.getVariable("recipient");
            String content = externalTask.getVariable("content");

            // E-Mail senden
            this.mailService.send(recipient, content);

            // External-Task beenden
            externalTaskService.complete(externalTask);

        } catch (final RecipientNotFoundException ex) {
            log.error("Business-Error: Recipient could not be found: '{}'", ex.getRecipient());
            externalTaskService.handleBpmnError(externalTask, "recipient-not-found");

        } catch (final Exception exception) {

            Integer retries = this.getRetries(externalTask);
            Long timeout = this.getNextTimeout(retries);

            externalTaskService.handleFailure(
                    externalTask,                               // task
                    exception.getMessage(),                     // message
                    ExceptionUtils.getStackTrace(exception),    // details
                    retries, timeout);                          // retries + timeout
        }
    }


    private Integer getRetries(final ExternalTask task) {
        Integer retries = task.getRetries();
        if (retries == null) {
            return MAX_RETRIES;
        } else {
            return retries - 1;
        }
    }


    private Long getNextTimeout(Integer retries) {
        return ONE_MINUTE * (MAX_RETRIES - retries);
    }
}
