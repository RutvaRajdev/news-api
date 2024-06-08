package com.fhf.it.news.api.service;

import com.fhf.it.news.api.model.EverythingResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

public interface PublicNewsAPIService {
    EverythingResponse getAllArticles(String q,
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
