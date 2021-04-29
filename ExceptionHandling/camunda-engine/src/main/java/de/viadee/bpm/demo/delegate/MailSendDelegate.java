package de.viadee.bpm.demo.delegate;

import de.viadee.bpm.demo.service.MailService;
import de.viadee.bpm.demo.service.RecipientNotFoundException;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MailSendDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(MailSendDelegate.class);

    private final MailService mailService;

    public MailSendDelegate(final MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void execute(final DelegateExecution execution) {

        // E-Mail-Adresse und Inhalt aus Prozesskontext laden
        String recipient = (String) execution.getVariable("recipient");
        String content = (String) execution.getVariable("content");

        try {
            // E-Mail senden
            this.mailService.send(recipient, content);

        } catch (final RecipientNotFoundException ex) {
            log.error("Business-Error: Recipient could not be found: '{}'", ex.getRecipient());
            throw new BpmnError("recipient-not-found");
        }
    }
}
