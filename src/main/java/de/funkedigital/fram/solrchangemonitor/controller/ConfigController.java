package de.funkedigital.fram.solrchangemonitor.controller;

import de.funkedigital.fram.solrchangemonitor.model.ChangeBatch;
import de.funkedigital.fram.solrchangemonitor.service.SolrContentChangeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {


    private SolrContentChangeService solrContentChangeService;

    ConfigController(SolrContentChangeService solrContentChangeService) {

        this.solrContentChangeService = solrContentChangeService;
    }

    @PostMapping("/changebatch")
    public ChangeBatch post(@RequestBody ConfigDTO configDTO) {
        return solrContentChangeService.resetMillis(configDTO.getResetMillis());
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
