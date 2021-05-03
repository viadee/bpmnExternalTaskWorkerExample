package de.viadee.bpm.demo.retry.model;

import org.camunda.bpm.client.task.ExternalTask;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetryBehaviour {
    private static final String RETRY_CYCLE_REGEX = "^(?:[Rr](?<times>\\d+)/)(?<interval>(?:[Pp](?:\\d+[Yy])?)(?:\\d+[Mm])?(?:\\d+[Dd])?(?:[Tt](?!$)(?:\\d+[Hh])?(?:\\d+[Mm])?(?:\\d+[Ss])?)?)$";
    private static final Pattern RETRY_CYCLE_PATTERN = Pattern.compile(RETRY_CYCLE_REGEX);

    private final Integer retries;
    private final Matcher matcher;

    public RetryBehaviour(final ExternalTask externalTask) {
        retries = externalTask.getRetries();
        matcher = RETRY_CYCLE_PATTERN.matcher(externalTask.getExtensionProperty("RETRY_CONFIG"));
    }

    public static RetryBehaviour of(final ExternalTask externalTask) {
        return new RetryBehaviour(externalTask);
    }


    public Integer getRemainingRetries() {
        if (retries == null && matcher.matches()) {
            String times = matcher.group("times");
            return Integer.parseInt(times);

        } else {
            return retries - 1; // TODO: Fehlerverhalten
        }
    }


    public Long getNextTimeout() {
        if (getRemainingRetries() == 0) {
            return 0L;
        }

        if (matcher.matches()) {
            String interval = matcher.group("interval");
            return Duration.parse(interval).getSeconds() * 1000L;

        } else {
            return 60000L; // TODO: Fallback
        }
    }
}
