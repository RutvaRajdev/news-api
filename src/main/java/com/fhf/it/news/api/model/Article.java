package com.fhf.it.news.api.model;

import lombok.Value;

import java.util.Date;

@Value
public class Article {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
}
