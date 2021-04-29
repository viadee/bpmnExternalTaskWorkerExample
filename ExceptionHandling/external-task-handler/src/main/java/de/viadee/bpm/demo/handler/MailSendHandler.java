package de.viadee.bpm.demo.handler;

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
@ExternalTaskSubscription("send-mail")
public class MailSendHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(MailSendHandler.class);

    private final MailService mailService;

    public MailSendHandler(final MailService mailService) {
        this.mailService = mailService;
    }

    public void execute(final ExternalTask externalTask, final ExternalTaskService externalTaskService) {

        // E-Mail-Adresse und Inhalt aus Prozesskontext laden
        String recipient = externalTask.getVariable("recipient");
        String content = externalTask.getVariable("content");

        try {
            // E-Mail senden
            this.mailService.send(recipient, content);

        } catch (final RecipientNotFoundException ex) {
            log.error("Business-Error: Recipient could not be found: '{}'", ex.getRecipient());
            externalTaskService.handleBpmnError(externalTask, "recipient-not-found");
            return;
        }

        // External-Task beenden
        externalTaskService.complete(externalTask);
    }
}
