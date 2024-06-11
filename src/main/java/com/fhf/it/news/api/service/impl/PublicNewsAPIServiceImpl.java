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

    @Value("${api.newsApiOrg.endpoint.everything.pageSize}")
    private int everythingPageSize;

    @Value("${api.newsApiOrg.endpoint.topHeadlines.pageSize}")
    private int topHeadlinesPageSize;

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
                                          String authorName,
                                          String domains,
                                          String excludeDomains,
                                          Date from,
                                          Date to,
                                          String language,
                                          String sortBy,
                                          Integer n) {

        LOGGER.info("Making a call to public API Endpoint");

        ResponseWrapper response = publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, everythingPageSize, 1, getAuthString());

        List<Article> allArticles = response.getArticles();

        // If n is provided, restrict the number of articles returned
        if(n != null) {
            LOGGER.warn("Restricting result size to " + n);

            // Filter out bad articles
            allArticles = filterBadArticles(allArticles);

            // If author name is provided, filter by the name
            if(authorName != null) {
                allArticles = filterByAuthor(allArticles, authorName);
            }

            n = n < allArticles.size() ? n : allArticles.size();

            return new ResponseWrapper(n, allArticles.subList(0, n));
        }

        int nResults = response.getTotalResults();

        LOGGER.info("{} results found!", nResults);

        try {
            // If multiple pages are present, iterating through them all
            for(int i = 2; i<=Math.ceil((double) nResults/ everythingPageSize); i++) {
                allArticles.addAll(publicNewsAPIClient.getEverything(q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy, everythingPageSize, i, getAuthString()).getArticles());
            }
        } catch (Exception e) {
            // Free developer plan only supports 1st page
            if(e.getMessage().contains(ERROR_UPGRADE_REQUIRED)) {
                LOGGER.warn("Number of pages > 1, iterating only through page 1 due to API plan limitations");
            }
        }

        // Filter out bad articles
        allArticles = filterBadArticles(allArticles);

        if(authorName != null) {
            // If author name is provided, filter by the name
            allArticles = filterByAuthor(allArticles, authorName);
        }

        return new ResponseWrapper(allArticles.size(), allArticles);
    }

    /**
     * @param country
     * @param category
     * @param sources
     * @param q
     * @return ResponseWrapper
     */
    @Override
    public ResponseWrapper getTopHeadlines(String country, String category, String sources, String q, Integer n) {
        LOGGER.info("Making a call to public API Endpoint");

        ResponseWrapper response = publicNewsAPIClient.getTopHeadlines(country, category, sources, q, topHeadlinesPageSize, 1, getAuthString());

        List<Article> allArticles = response.getArticles();

        // If n is provided, restrict the number of articles returned
        if(n != null) {
            LOGGER.warn("Restricting result size to " + n);

            // Filter out bad articles
            allArticles = filterBadArticles(allArticles);

            n = n < allArticles.size() ? n : allArticles.size();
            
            return new ResponseWrapper(n, allArticles.subList(0, n));
        }

        int nResults = response.getTotalResults();

        LOGGER.info("{} results found!", nResults);

        // If multiple pages are present, iterating through them all
        try {
            for(int i = 2; i<=Math.ceil((double) nResults/ topHeadlinesPageSize); i++) {
                allArticles.addAll(publicNewsAPIClient.getTopHeadlines(country, category, sources, q, topHeadlinesPageSize, i, getAuthString()).getArticles());
            }
        } catch (Exception e) {
            // Free developer plan only supports 1st page
            if(e.getMessage().contains(ERROR_UPGRADE_REQUIRED)) {
                LOGGER.warn("Number of pages > 1, iterating only through page 1 due to API plan limitations");
            }
        }

        // Filter out bad articles
        allArticles = filterBadArticles(allArticles);

        return new ResponseWrapper(allArticles.size(), allArticles);
    }

    private List<Article> filterBadArticles(List<Article> articles) {
        LOGGER.warn("Filtering out bad articles");

        return  articles.stream().filter((article) ->
                (article.getTitle() == null || !article.getTitle().equalsIgnoreCase(REMOVED)) &&
                        (article.getContent() == null || !article.getContent().equalsIgnoreCase(REMOVED)))
                .collect(Collectors.toList());
    }

    private List<Article> filterByAuthor(List<Article> articles, String authorName) {
        LOGGER.warn("Filtering out articles not by author " + authorName);

        return articles.stream().filter(article ->
                article.getAuthor() != null && article.getAuthor().toLowerCase().contains(authorName.toLowerCase()))
                .collect(Collectors.toList());
    }

    private String getAuthString() {
        return "Bearer " + new String(Base64.getDecoder().decode(encodedApikey));
    }
}
