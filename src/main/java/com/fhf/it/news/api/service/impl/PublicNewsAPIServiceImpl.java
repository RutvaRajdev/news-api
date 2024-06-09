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
import java.util.stream.Collectors;

@Service
public class PublicNewsAPIServiceImpl implements PublicNewsAPIService {

    @Autowired
    private PublicNewsAPIClient publicNewsAPIClient;

    @Value("${api.newsApiOrg.apiKey}")
    private String encodedApikey;

    private static final String REMOVED = "[Removed]";
    private static final String ERROR_UPGRADE_REQUIRED = "[426 Upgrade Required]";

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

        LOGGER.info("Making a call to public API Endpoint");

        ResponseWrapper response = publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, page, getAuthString());

        int nResults = response.getTotalResults();

        LOGGER.info("{} results found!", nResults);

        List<Article> allArticles = response.getArticles();

        try {
            for(int i=2; i<=Math.ceil((double) nResults/pageSize); i++) {
                allArticles.addAll(publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, pageSize, i, getAuthString()).getArticles());
            }
        } catch (Exception e) {
            if(e.getMessage().contains(ERROR_UPGRADE_REQUIRED)) {
                LOGGER.warn("Result size > 100, truncating result size to 100 due to API plan limitations");
            }
        }


        allArticles = filterBadArticles(allArticles);

        return new ResponseWrapper(allArticles.size(), allArticles);
    }

    /**
     * @param country
     * @param category
     * @param sources
     * @param q
     * @param pageSize
     * @param page
     * @return ResponseWrapper
     */
    @Override
    public ResponseWrapper getTopHeadlines(String country, String category, String sources, String q, Integer n, Integer pageSize, Integer page) {
        LOGGER.info("Making a call to public API Endpoint");

        ResponseWrapper response = publicNewsAPIClient.getTopHeadlines(country, category, sources, q, 100, 1, getAuthString());

        List<Article> allArticles = response.getArticles();

        if(n != null) {
            LOGGER.warn("Restricting result size to " + n);
            allArticles = filterBadArticles(allArticles);
            return new ResponseWrapper(n, allArticles.subList(0, n));
        }

        int nResults = response.getTotalResults();

        LOGGER.info("{} results found!", nResults);

        try {
            for(int i=2; i<=Math.ceil((double) nResults/pageSize); i++) {
                allArticles.addAll(publicNewsAPIClient.getTopHeadlines(country, category, sources, q, 100, i, getAuthString()).getArticles());
            }
        } catch (Exception e) {
            if(e.getMessage().contains(ERROR_UPGRADE_REQUIRED)) {
                LOGGER.warn("Result size > 100, truncating result size to 100 due to API plan limitations");
            }
        }


        allArticles = filterBadArticles(allArticles);

        return new ResponseWrapper(allArticles.size(), allArticles);
    }

    private List<Article> filterBadArticles(List<Article> articles) {
        LOGGER.warn("Filtering bad articles");

        return  articles.stream().filter((article) ->
                (article.getTitle() == null || !article.getTitle().equalsIgnoreCase(REMOVED)) &&
                        (article.getContent() == null || !article.getContent().equalsIgnoreCase(REMOVED)))
                .collect(Collectors.toList());
    }

    private String getAuthString() {
        return "Bearer " + new String(Base64.getDecoder().decode(encodedApikey));
    }
}
