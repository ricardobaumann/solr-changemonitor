package de.funkedigital.fram.solrchangemonitor.repo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolrRepoTest {

    @Mock
    private SolrClient solrClient;

    @InjectMocks
    private SolrRepo solrRepo;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldMapChangesToMap() throws IOException, SolrServerException {
        //Given
        QueryResponse queryResponse = mock(QueryResponse.class);
        Map<String, Object> data = Collections.singletonMap("key", "value");
        SolrDocumentList list = mock(SolrDocumentList.class);
        when(list.stream()).thenReturn(Stream.of(new SolrDocument(data)));
        when(queryResponse.getResults()).thenReturn(list);
        when(solrClient.query(any())).thenReturn(queryResponse);

        //When
        List<Map<String, Object>> results = solrRepo.getChangesSince(new Date());

        //Then
        assertThat(results).containsExactly(data);
    }

    @Test
    public void shouldMapTagsToCacheAndQueue() throws IOException, SolrServerException {
        //Given
        QueryResponse queryResponse = mock(QueryResponse.class);
        Map<String, Object> data = Collections.singletonMap("key", "value");
        SolrDocumentList list = mock(SolrDocumentList.class);
        when(list.stream()).thenReturn(Stream.of(new SolrDocument(data)));
        when(queryResponse.getResults()).thenReturn(list);
        when(solrClient.query(any())).thenReturn(queryResponse);

        //When
        List<Map<String, Object>> results = solrRepo.getTagObjects();

        //Then
        assertThat(results).containsExactly(data);
    }
}