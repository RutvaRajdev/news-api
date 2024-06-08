package com.fhf.it.news.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper {
    private int totalResults;

    private List<Article> articles;
}
