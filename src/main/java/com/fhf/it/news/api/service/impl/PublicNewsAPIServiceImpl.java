package com.fhf.it.news.api.service.impl;

import com.fhf.it.news.api.client.PublicNewsAPIClient;
import com.fhf.it.news.api.model.EverythingResponse;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

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
    public EverythingResponse getAllArticles(String q,
                                             String searchIn,
                                             String sources,
                                             String domains,
                                             String excludeDomains,
                                             Date from,
                                             Date to,
                                             String language,
                                             String sortBy,
                                             Integer pageSize,
                                             Integer page) {

        return publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, page, getAuthString());
    }

    private String getAuthString() {
        return "Bearer " + new String(Base64.getDecoder().decode(encodedApikey));
    }
}
