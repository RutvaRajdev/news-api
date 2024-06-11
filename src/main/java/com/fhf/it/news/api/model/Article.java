package com.fhf.it.news.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private Date publishedAt;
    private String content;

    @JsonProperty("source")
    public String getSourceName() {
        return source != null ? source.getName() : null;
    }
}
