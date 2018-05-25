package de.funkedigital.fram.solrchangemonitor.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ContentSource {
    String CONTENT = "content";
    String TAGS = "tags";

    @Output
    MessageChannel content();

    @Output
    MessageChannel tags();
}
