package com.fhf.it.news.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class EverythingResponse {
    private int totalResults;

    private List<Article> articles;
}
