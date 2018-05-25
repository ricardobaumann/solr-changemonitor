package de.funkedigital.fram.solrchangemonitor.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.Map;

@MessagingGateway
public interface ContentUnitGateway {

    @Gateway(requestChannel = ContentSource.CONTENT)
    void generateContent(Map<String, Object> stringObjectMap);

    @Gateway(requestChannel = ContentSource.TAGS)
    void generateTags(Map<String, Object> stringObjectMap);
}
