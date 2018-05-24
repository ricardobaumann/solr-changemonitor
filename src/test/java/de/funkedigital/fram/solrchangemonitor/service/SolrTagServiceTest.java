package de.funkedigital.fram.solrchangemonitor.service;

import de.funkedigital.fram.solrchangemonitor.repo.SolrRepo;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolrTagServiceTest {

    @Mock
    private SolrRepo solrRepo;

    @Mock
    private ContentUnitGateway contentUnitGateway;

    @InjectMocks
    private SolrTagService solrTagService;

    @Test
    public void shouldMapResultsFromRepoToQueue() throws IOException, SolrServerException {
        //Given
        Map<String, Object> data = Collections.singletonMap("key", "value");
        when(solrRepo.getTagObjects()).thenReturn(Collections.singletonList(data));
        doNothing().when(contentUnitGateway).generateTags(data);

        //When //Then
        solrTagService.reloadFromRepo();
    }
}