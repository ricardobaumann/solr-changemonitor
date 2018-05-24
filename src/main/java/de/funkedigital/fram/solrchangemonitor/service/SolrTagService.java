package de.funkedigital.fram.solrchangemonitor.service;

import de.funkedigital.fram.solrchangemonitor.repo.SolrRepo;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SolrTagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrTagService.class);

    private final SolrRepo solrRepo;
    private final ContentUnitGateway contentUnitGateway;
    private List<Map<String, Object>> tagCache;

    SolrTagService(SolrRepo solrRepo,
                   ContentUnitGateway contentUnitGateway) {
        this.solrRepo = solrRepo;
        this.contentUnitGateway = contentUnitGateway;
    }

    @Scheduled(fixedDelay = 120_000L)
    void reloadFromRepo() throws IOException, SolrServerException {
        tagCache = solrRepo.getTagObjects();
        LOGGER.info("Tags reloaded: [{}]", tagCache.size());
        tagCache.forEach(contentUnitGateway::generateTags);
    }

    public List<Map<String, Object>> getTags() {
        return tagCache;
    }
}
