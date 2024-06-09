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
    private static final String PUBLISHED_AT = "publishedAt";

    private static final Logger LOGGER = LogManager.getLogger(CustomServiceController.class);

    @GetMapping(value = "/getByTitle/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getByTitle(@PathVariable String title) {
        LOGGER.warn("Searching for articles with title \"" + title + "\"");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(title, TITLE, null, null, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/getNLatestByKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getNLatestByKeyword(@RequestParam int n, @RequestParam String keyword) {
        LOGGER.warn("Searching for " + n + " latest articles containing keyword " + keyword);

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, null, null, null, null, null, null, null, null, PUBLISHED_AT, n);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/searchByAuthorAndKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> searchByAuthorAndKeywordInTitle(@RequestParam String author, @RequestParam String keyword) {
        LOGGER.warn("Searching for articles containing \"" + keyword + "\" in the title by author " + author);

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, null, null, author, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/searchByKeywordInTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> searchByKeywordInTitle(@RequestParam String keyword) {
        LOGGER.warn("Searching for articles containing \"" + keyword + "\" in the title");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, TITLE, null, null, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/getHeadlinesByCountry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getHeadlinesByCountry(@RequestParam String country) {
        LOGGER.warn("Searching for top headlines for country code " + country);

        ResponseWrapper allArticles = publicNewsAPIService.getTopHeadlines(country, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/getTopHeadlinesByKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getTopHeadlinesByKeyword(@RequestParam String keyword, @RequestParam(required = false) Integer n) {
        LOGGER.warn("Searching the top headlines for keyword " + keyword);

        ResponseWrapper allArticles = publicNewsAPIService.getTopHeadlines(null, null, null, keyword, n);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }
}
