package com.github.ricardobaumann.solrchangemonitor.repo;

import com.github.ricardobaumann.solrchangemonitor.models.ChangeBatch;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeBatchRepo extends PagingAndSortingRepository<ChangeBatch, Long> {

    ChangeBatch findFirstByOrderByLastModifiedDateDesc();
}
