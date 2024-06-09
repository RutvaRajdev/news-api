package com.fhf.it.news.api.client;

import com.fhf.it.news.api.model.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(name = "public-news-api", url = "${api.newsApiOrg.url}")
public interface PublicNewsAPIClient {

    @RequestMapping(value = "/everything", method = RequestMethod.GET)
    public ResponseWrapper getEverything(
            @RequestParam String q,
            @RequestParam String searchIn,
            @RequestParam String sources,
            @RequestParam String domains,
            @RequestParam String excludeDomains,
            @RequestParam Date from,
            @RequestParam Date to,
            @RequestParam String language,
            @RequestParam String sortBy,
            @RequestParam Integer pageSize,
            @RequestParam Integer page,
            @RequestHeader("Authorization") String authorization);

    @RequestMapping(value = "/top-headlines", method = RequestMethod.GET)
    public ResponseWrapper getTopHeadlines(
            @RequestParam String country,
            @RequestParam String category,
            @RequestParam String sources,
            @RequestParam String q,
            @RequestParam Integer pageSize,
            @RequestParam Integer page,
            @RequestHeader("Authorization") String authorization);
}
