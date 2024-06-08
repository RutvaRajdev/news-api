package com.fhf.it.news.api.controller;

import com.fhf.it.news.api.model.ResponseWrapper;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/service")
public class CustomServiceController {

    @Autowired
    private PublicNewsAPIService publicNewsAPIService;

    private static final String TITLE = "title";

    private static final Logger LOGGER = LogManager.getLogger(CustomServiceController.class);

    @GetMapping(value = "/getByTitle/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getByTitle(@PathVariable String title) {
        LOGGER.warn("Searching for articles with title \"" + title + "\"");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(title, TITLE, null, null, null, null, null, null, null, 100, 1);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/searchByKeywordInTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> searchByKeywordInTitle(@RequestParam String keyword) {
        LOGGER.warn("Searching for articles containing \"" + keyword + "\" in the title");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles("+"+keyword, TITLE, null, null, null, null, null, null, null, 100, 1);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }
}
