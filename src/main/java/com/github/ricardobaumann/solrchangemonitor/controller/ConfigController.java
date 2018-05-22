package com.github.ricardobaumann.solrchangemonitor.controller;

import com.github.ricardobaumann.solrchangemonitor.model.ChangeBatch;
import com.github.ricardobaumann.solrchangemonitor.service.SolrChangeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {


    private SolrChangeService solrChangeService;

    ConfigController(SolrChangeService solrChangeService) {

        this.solrChangeService = solrChangeService;
    }

    @PostMapping("/changebatch")
    public ChangeBatch post(@RequestBody ConfigDTO configDTO) {
        return solrChangeService.resetMillis(configDTO.getResetMillis());
    }

    public static class ConfigDTO {
        private Long resetMillis;

        public Long getResetMillis() {
            return resetMillis;
        }

        public void setResetMillis(Long resetMillis) {
            this.resetMillis = resetMillis;
        }
    }

}
