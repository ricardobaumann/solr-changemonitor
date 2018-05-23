package de.funkedigital.fram.solrchangemonitor.health;

import de.funkedigital.fram.solrchangemonitor.repo.SolrRepo;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author fschaefer
 */
@Component
public class SolrHealthIndicator implements HealthIndicator {

    private SolrRepo solrRepo;

    SolrHealthIndicator(SolrRepo solrRepo) {
        this.solrRepo = solrRepo;
    }

    @Override
    public Health health() {
        try {
            return solrRepo.getChangesSince(new Date()) != null ? Health.up().build() : Health.down(new RuntimeException("Solr result is null")).build();
        } catch (SolrServerException | IOException e) {
            return Health.down(e).build();
        }
    }


}
