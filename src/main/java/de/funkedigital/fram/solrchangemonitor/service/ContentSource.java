package de.funkedigital.fram.solrchangemonitor.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ContentSource {
    String AUTHORS = "authors";
    String TAGS = "tags";

    @Output
    MessageChannel authors();

    @Output
    MessageChannel tags();
}
