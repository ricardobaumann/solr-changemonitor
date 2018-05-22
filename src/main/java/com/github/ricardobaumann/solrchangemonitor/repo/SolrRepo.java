package com.github.ricardobaumann.solrchangemonitor.repo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SolrRepo {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private final SolrClient solrClient;

    private Function<SolrDocument, Map<String, Object>> toMap = entries -> entries
            .getFieldNames()
            .stream()
            .collect(Collectors.toMap(Function.identity(), entries::getFieldValue));

    SolrRepo(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public List<Map<String, Object>> getChangesSince(Date date) throws SolrServerException, IOException {

        SolrQuery query = new SolrQuery();
        //2018-05-05T11:23:34Z
        query.set("q", String.format("lastmodifieddate: [%s TO *] contenttype: news", DATE_FORMAT.format(date)));
        query.set("indent", true);
        query.set("fl", "objectid,lastmodifieddate,contenttype,source,sourceid");
        query.set("sort", "lastmodifieddate desc");
        query.set("wt", "json");

        QueryResponse response = solrClient.query(query);

        return response.getResults().stream()
                .map(toMap)
                .collect(Collectors.toList());
    }

}
