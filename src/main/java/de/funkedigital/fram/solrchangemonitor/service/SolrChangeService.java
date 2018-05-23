package de.funkedigital.fram.solrchangemonitor.service;

import de.funkedigital.fram.solrchangemonitor.model.ChangeBatch;
import de.funkedigital.fram.solrchangemonitor.repo.ChangeBatchRepo;
import de.funkedigital.fram.solrchangemonitor.repo.SolrRepo;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SolrChangeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrChangeService.class);

    private final SolrRepo solrRepo;
    private final ChangeBatchRepo changeBatchRepo;
    private final ContentUnitGateway contentUnitGateway;

    public SolrChangeService(SolrRepo solrRepo,
                             ChangeBatchRepo changeBatchRepo,
                             ContentUnitGateway contentUnitGateway) {
        this.solrRepo = solrRepo;
        this.changeBatchRepo = changeBatchRepo;
        this.contentUnitGateway = contentUnitGateway;
    }

    @Scheduled(fixedDelay = 10000)
    public void run() throws IOException, SolrServerException {
        ChangeBatch previousBatch = changeBatchRepo.findFirstByOrderByCreatedAtDesc();
        Date now = new Date();
        Date lastDate = Optional.ofNullable(previousBatch)
                .map(ChangeBatch::getLastModifiedDate)
                .orElse(new Date(LocalDateTime.now().minusSeconds(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

        LOGGER.info("Polling changes from [{}] on", lastDate);

        List<Map<String, Object>> results = solrRepo.getChangesSince(lastDate);
        LOGGER.info("Sending {}", results);
        results.forEach(contentUnitGateway::generate);
        changeBatchRepo.save(new ChangeBatch(now));
    }

    public ChangeBatch resetMillis(Long resetMillis) {
        ChangeBatch changeBatch = new ChangeBatch(new Date(new Date().getTime() - resetMillis));
        return changeBatchRepo.save(changeBatch);
    }
}
