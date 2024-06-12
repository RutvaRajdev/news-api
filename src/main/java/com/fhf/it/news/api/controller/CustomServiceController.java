package com.fhf.it.news.api.controller;

import com.fhf.it.news.api.model.ErrorResponseWrapper;
import com.fhf.it.news.api.model.ResponseWrapper;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/service")
public class CustomServiceController {

    @Autowired
    private PublicNewsAPIService publicNewsAPIService;

    private static final String TITLE = "title";
    private static final String PUBLISHED_AT = "publishedAt";
    private static final int BAD_REQUEST_CODE = HttpStatus.BAD_REQUEST.value();

    private static final List<String> validCountries = Arrays.asList("ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch",
            "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr",
            "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg",
            "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za");

    private static final Logger LOGGER = LogManager.getLogger(CustomServiceController.class);

    @Cacheable(value = "articles")
    @GetMapping(value = "/getByTitle/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getByTitle(@PathVariable String title) {
        LOGGER.warn("Searching for articles with title \"" + title + "\"");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(title, TITLE, null, null, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @Cacheable(value = "articles")
    @GetMapping(value = "/getNLatestByKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNLatestByKeyword(@RequestParam Integer n, @RequestParam String keyword) {
        if(n != null && (n < 0 || n > 100)) {
            return new ResponseEntity<ErrorResponseWrapper>(new ErrorResponseWrapper("Please provide a value of n between 1 and 100", BAD_REQUEST_CODE), HttpStatus.BAD_REQUEST);

        }

        LOGGER.warn("Searching for " + n + " latest articles containing keyword " + keyword);

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, null, null, null, null, null, null, null, null, PUBLISHED_AT, n);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @Cacheable(value = "articles")
    @GetMapping(value = "/searchByAuthorAndKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> searchByAuthorAndKeywordInTitle(@RequestParam String author, @RequestParam String keyword) {
        LOGGER.warn("Searching for articles containing \"" + keyword + "\" in the title by author " + author);

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, null, null, author, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @Cacheable(value = "articles")
    @GetMapping(value = "/searchByKeywordInTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> searchByKeywordInTitle(@RequestParam String keyword) {
        LOGGER.warn("Searching for articles containing \"" + keyword + "\" in the title");

        ResponseWrapper allArticles = publicNewsAPIService.getAllArticles(keyword, TITLE, null, null, null, null, null, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @Cacheable(value = "headlines")
    @GetMapping(value = "/getHeadlinesByCountry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHeadlinesByCountry(@RequestParam String country) {
        if(!validCountries.contains(country)) {
            return new ResponseEntity<ErrorResponseWrapper>(new ErrorResponseWrapper("Please provide a valid country code", BAD_REQUEST_CODE), HttpStatus.BAD_REQUEST);
        }

        LOGGER.warn("Searching for top headlines for country code " + country);

        ResponseWrapper allArticles = publicNewsAPIService.getTopHeadlines(country, null, null, null, null);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }

    @Cacheable(value = "headlines")
    @GetMapping(value = "/getTopHeadlinesByKeyword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTopHeadlinesByKeyword(@RequestParam String keyword, @RequestParam(required = false) Integer n) {
        if(n != null && (n < 0 || n > 100)) {
            return new ResponseEntity<ErrorResponseWrapper>(new ErrorResponseWrapper("Please provide a value of n between 1 and 100", BAD_REQUEST_CODE), HttpStatus.BAD_REQUEST);

        }

        LOGGER.warn("Searching the top headlines for keyword " + keyword);

        ResponseWrapper allArticles = publicNewsAPIService.getTopHeadlines(null, null, null, keyword, n);
        ResponseEntity<ResponseWrapper> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }
}
