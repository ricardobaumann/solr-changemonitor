package com.github.ricardobaumann.solrchangemonitor;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ContentSource {
    String CHANNEL_NAME = "changemonitor";

    @Output
    MessageChannel changemonitor();
}
