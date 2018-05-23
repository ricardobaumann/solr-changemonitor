package de.funkedigital.fram.solrchangemonitor.repo;

import de.funkedigital.fram.solrchangemonitor.model.ChangeBatch;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeBatchRepo extends PagingAndSortingRepository<ChangeBatch, Long> {

    ChangeBatch findFirstByOrderByCreatedAtDesc();
}
