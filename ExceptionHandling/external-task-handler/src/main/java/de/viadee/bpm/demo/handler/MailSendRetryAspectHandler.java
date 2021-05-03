package de.viadee.bpm.demo.handler;

import de.viadee.bpm.camunda.externaltask.retry.aspect.error.ExternalTaskBusinessError;
import de.viadee.bpm.demo.service.MailService;
import de.viadee.bpm.demo.service.RecipientNotFoundException;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
//@ExternalTaskSubscription("send-mail")
public class MailSendRetryAspectHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(MailSendRetryAspectHandler.class);
    private static final long ONE_MINUTE = 1000L * 60;
    private final MailService mailService;

    public MailSendRetryAspectHandler(final MailService mailService) {
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
            throw new ExternalTaskBusinessError("recipient-not-found");

        }
    }
}
