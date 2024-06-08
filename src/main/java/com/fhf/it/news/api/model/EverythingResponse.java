package com.fhf.it.news.api.model;

import java.util.List;
import lombok.Value;

@Value
public class EverythingResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;
}
