package de.funkedigital.fram.solrchangemonitor.repo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class SolrRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrRepo.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final Integer TAG_BATCH_SIZE = 1000;
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

    public List<Map<String, Object>> getTagObjects() throws IOException, SolrServerException {
        SolrDocumentList firstResponse = getTagObjectsPage(0).getResults();
        long batches = firstResponse.getNumFound();
        long parts = (batches / TAG_BATCH_SIZE) + 1;
        List<Map<String, Object>> results = IntStream.range(1, (int) parts).mapToObj(page -> {
            try {
                return getTagObjectsPage(page * TAG_BATCH_SIZE).getResults().stream().map(toMap).collect(Collectors.toList());
            } catch (IOException | SolrServerException e) {
                LOGGER.warn("Skipping tag page due to {}", e);
                return null;
            }
        }).filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());//Can be changed to a chunk collector in case of too many results

        results.addAll(0, firstResponse.stream().map(toMap).collect(Collectors.toList()));
        return results;
    }

    private QueryResponse getTagObjectsPage(int start) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.set("q", "contenttype: com.escenic.tag");
        query.set("fl", "objectid,title,tag_scheme,tag_scheme_name,tag_parent,tag_alias");
        query.set("sort", "id asc");
        query.set("start", String.valueOf(start));
        query.set("rows", String.valueOf(TAG_BATCH_SIZE));
        query.set("wt", "json");

        return solrClient.query(query);

    }

}
