package com.fhf.it.news.api.client;

import com.fhf.it.news.api.model.EverythingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "public-news-api", url = "${api.newsApiOrg.url}")
public interface PublicNewsAPIClient {

    @RequestMapping(value = "/everything", method = RequestMethod.GET)
    public EverythingResponse getEverything(@RequestHeader("Authorization") String authorization);
}
