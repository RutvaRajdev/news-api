package com.fhf.it.news.api.service.impl;

import com.fhf.it.news.api.client.PublicNewsAPIClient;
import com.fhf.it.news.api.model.Article;
import com.fhf.it.news.api.model.ResponseWrapper;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class PublicNewsAPIServiceImpl implements PublicNewsAPIService {

    @Autowired
    private PublicNewsAPIClient publicNewsAPIClient;

    @Value("${api.newsApiOrg.apiKey}")
    private String encodedApikey;

    private static final Logger LOGGER = LogManager.getLogger(PublicNewsAPIServiceImpl.class);

    /**
     * @return
     */
    @Override
    public ResponseWrapper getAllArticles(String q,
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

        LOGGER.warn("Making a call to public API Endpoint");

        ResponseWrapper response = publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, page, getAuthString());

        int nResults = response.getTotalResults();

        LOGGER.info("{} results found!", nResults);

        if(nResults <= 100) {
            return response;
        } else {

            List<Article> allArticles = response.getArticles();
            for(int i=2; i<=Math.ceil((double) nResults/pageSize); i++) {
                allArticles.addAll(publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, i, getAuthString()).getArticles());
            }

            return new ResponseWrapper(nResults, allArticles);
        }
    }

    private String getAuthString() {
        return "Bearer " + new String(Base64.getDecoder().decode(encodedApikey));
    }
}
