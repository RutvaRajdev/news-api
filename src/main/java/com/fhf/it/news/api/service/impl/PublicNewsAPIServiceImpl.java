package com.fhf.it.news.api.service.impl;

import com.fhf.it.news.api.client.PublicNewsAPIClient;
import com.fhf.it.news.api.model.EverythingResponse;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class PublicNewsAPIServiceImpl implements PublicNewsAPIService {

    @Autowired
    private PublicNewsAPIClient publicNewsAPIClient;

    @Value("${api.newsApiOrg.apiKey}")
    private String encodedApikey;

    /**
     * @return
     */
    @Override
    public EverythingResponse getAllArticles() {
        String authString = getAuthString();

        return publicNewsAPIClient.getEverything(authString);
    }

    private String getAuthString() {
        return "Bearer " + new String(Base64.getDecoder().decode(encodedApikey));
    }
}
