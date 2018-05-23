package de.funkedigital.fram.solrchangemonitor.service;

import de.funkedigital.fram.solrchangemonitor.model.ChangeBatch;
import de.funkedigital.fram.solrchangemonitor.repo.ChangeBatchRepo;
import de.funkedigital.fram.solrchangemonitor.repo.SolrRepo;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolrChangeServiceTest {

    Map<String, Object> data = Collections.singletonMap("key", "value");
    ArgumentCaptor<Date> dateArgumentCaptor = ArgumentCaptor.forClass(Date.class);
    ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
    @Mock
    private SolrRepo solrRepo;
    @Mock
    private ChangeBatchRepo changeBatchRepo;
    @Mock
    private ContentUnitGateway contentUnitGateway;
    @InjectMocks
    private SolrChangeService solrChangeService;
    private Date lastModifiedDate;
    private List<Map> repoResults;

    @Before
    public void setUp() throws Exception {
        //Given
        when(changeBatchRepo.findFirstByOrderByCreatedAtDesc()).thenReturn(null);
        List<Map<String, Object>> results = Collections.singletonList(data);
        when(solrRepo.getChangesSince(any())).thenReturn(results);
        doNothing().when(contentUnitGateway).generate(any());
    }

    @Test
    public void shouldLoadTheLast10SecsAtFirstTime() throws IOException, SolrServerException {
        run();
        verify(contentUnitGateway).generate(mapArgumentCaptor.capture());
        repoResults = mapArgumentCaptor.getAllValues();
        assertThat(lastModifiedDate).isBeforeOrEqualsTo(new Date());
    }

    @Test
    public void shouldMapRepoResultsToQueue() throws IOException, SolrServerException {
        run();
        verify(contentUnitGateway).generate(mapArgumentCaptor.capture());
        repoResults = mapArgumentCaptor.getAllValues();
        assertThat(repoResults).containsExactlyInAnyOrder(data);
    }

    @Test
    public void shouldLoadFromTheLastTimestampFromRepo() throws IOException, SolrServerException {
        //Given
        Date date = new Date();
        when(changeBatchRepo.findFirstByOrderByCreatedAtDesc()).thenReturn(new ChangeBatch(date));
        List<Map<String, Object>> results = Collections.emptyList();
        when(solrRepo.getChangesSince(any())).thenReturn(results);

        //When
        run();

        //Then
        verify(solrRepo).getChangesSince(dateArgumentCaptor.capture());
        assertThat(dateArgumentCaptor.getValue()).isEqualTo(date);
        verify(contentUnitGateway, never()).generate(any());
    }

    private void run() throws IOException, SolrServerException {
        //When
        solrChangeService.run();

        //Then
        verify(solrRepo).getChangesSince(dateArgumentCaptor.capture());
        lastModifiedDate = dateArgumentCaptor.getValue();
    }
}