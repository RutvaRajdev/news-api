package com.fhf.it.news.api.model;

import java.util.List;

public class EverythingResponse {
    private String status;
    private int nArticles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getnArticles() {
        return nArticles;
    }

    public void setnArticles(int nArticles) {
        this.nArticles = nArticles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    private List<Article> articles;
}
