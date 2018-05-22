package com.github.ricardobaumann.solrchangemonitor;

import com.github.ricardobaumann.solrchangemonitor.models.ChangeBatch;
import com.github.ricardobaumann.solrchangemonitor.repo.ChangeBatchRepo;
import com.github.ricardobaumann.solrchangemonitor.repo.SolrRepo;

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
    private ContentUnitGateway contentUnitGateway;

    public SolrChangeService(SolrRepo solrRepo,
                             ChangeBatchRepo changeBatchRepo,
                             ContentUnitGateway contentUnitGateway) {
        this.solrRepo = solrRepo;
        this.changeBatchRepo = changeBatchRepo;
        this.contentUnitGateway = contentUnitGateway;
    }

    @Scheduled(fixedDelay = 10000)
    public void run() throws IOException, SolrServerException {
        ChangeBatch previousBatch = changeBatchRepo.findFirstByOrderByLastModifiedDateDesc();
        Date now = new Date();
        Date lastDate = Optional.ofNullable(previousBatch)
                .map(ChangeBatch::getLastModifiedDate)
                .orElse(new Date(LocalDateTime.now().minusSeconds(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

        List<Map<String, Object>> results = solrRepo.getChangesSince(lastDate);
        LOGGER.info("Sending {}", results);
        results.forEach(contentUnitGateway::generate);
        changeBatchRepo.save(new ChangeBatch(now));
    }

}
