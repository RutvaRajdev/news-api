package com.fhf.it.news.api.service;

import com.fhf.it.news.api.model.ResponseWrapper;

import java.util.Date;

public interface PublicNewsAPIService {
    ResponseWrapper getAllArticles(String q,
                                   String searchIn,
                                   String sources,
                                   String domains,
                                   String excludeDomains,
                                   Date from,
                                   Date to,
                                   String language,
                                   String sortBy,
                                   Integer pageSize,
                                   Integer page);
}
