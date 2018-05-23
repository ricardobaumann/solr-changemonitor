package de.funkedigital.fram.solrchangemonitor;

import de.funkedigital.fram.solrchangemonitor.config.AppProperties;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
@EnableAutoConfiguration(exclude = SolrAutoConfiguration.class)
public class SolrChangemonitorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrChangemonitorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SolrChangemonitorApplication.class, args);
    }

    @Bean
    SolrClient solrClient(AppProperties appProperties) {
        return new HttpSolrClient.Builder(appProperties.getSolrUrl()).build();
    }

    @Bean
    String databaseUrl(@Value("spring.datasource.url") String dbUrl) {
        LOGGER.info(dbUrl);
        return dbUrl;
    }
}
