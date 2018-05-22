package com.github.ricardobaumann.solrchangemonitor.repo;

import com.github.ricardobaumann.solrchangemonitor.model.ChangeBatch;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeBatchRepo extends PagingAndSortingRepository<ChangeBatch, Long> {

    ChangeBatch findFirstByOrderByCreatedAtDesc();
}
