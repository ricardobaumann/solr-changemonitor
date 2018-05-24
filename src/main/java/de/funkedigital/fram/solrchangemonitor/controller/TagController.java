package de.funkedigital.fram.solrchangemonitor.controller;

import de.funkedigital.fram.solrchangemonitor.service.SolrTagService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TagController {

    private SolrTagService solrTagService;

    TagController(SolrTagService solrTagService) {
        this.solrTagService = solrTagService;
    }

    @GetMapping("/tags")
    public List<Map<String, Object>> get() {
        return solrTagService.getTags();
    }

}
