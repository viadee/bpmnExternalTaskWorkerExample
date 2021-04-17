package de.viadee.bpm.demo.ExternalTaskWorker.service;

public class RecipientNotFoundException extends RuntimeException {

    private final String recipient;

    public RecipientNotFoundException(final String recipient) {
        super("Recipient not found: " + recipient);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}