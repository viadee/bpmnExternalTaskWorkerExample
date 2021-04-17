package de.viadee.bpm.demo.ExternalTaskWorker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void send(final String recipient, final String content) throws RecipientNotFoundException {

        if ("404".equals(recipient)) {
            throw new RecipientNotFoundException(recipient);
        }

        if ("500".equals(recipient)) {
            throw new RuntimeException("Some error occurred!");
        }

        log.info("Sende mail an '{}'", recipient);
    }
}
