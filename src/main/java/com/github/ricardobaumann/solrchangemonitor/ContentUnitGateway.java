package com.github.ricardobaumann.solrchangemonitor;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.Map;

@MessagingGateway
public interface ContentUnitGateway {

    @Gateway(requestChannel = ContentSource.CHANNEL_NAME)
    void generate(Map<String, Object> stringObjectMap);
}
