package de.funkedigital.fram.solrchangemonitor.service;

import de.funkedigital.fram.solrchangemonitor.model.ChangeBatch;
import de.funkedigital.fram.solrchangemonitor.repo.ChangeBatchRepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolrContentChangeServiceResetTest {

    @Mock
    private ChangeBatchRepo changeBatchRepo;
    @InjectMocks
    private SolrContentChangeService solrContentChangeService;

    @Test
    public void shouldResetMillis() {
        //Given
        ArgumentCaptor<ChangeBatch> changeBatchArgumentCaptor = ArgumentCaptor.forClass(ChangeBatch.class);
        when(changeBatchRepo.save(Mockito.any())).thenReturn(null);

        //When
        solrContentChangeService.resetMillis(1000L);

        //Then
        verify(changeBatchRepo).save(changeBatchArgumentCaptor.capture());

        assertThat(changeBatchArgumentCaptor.getValue().getLastModifiedDate())
                .isBeforeOrEqualsTo(new Date(new Date().getTime() - 1000L));
    }
}