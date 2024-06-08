package com.fhf.it.news.api.controller;

import com.fhf.it.news.api.model.EverythingResponse;
import com.fhf.it.news.api.service.PublicNewsAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/service")
public class CustomServiceController {

    @Autowired
    private PublicNewsAPIService publicNewsAPIService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EverythingResponse> getAllArticles() {
        
        EverythingResponse allArticles = publicNewsAPIService.getAllArticles();
        ResponseEntity<EverythingResponse> response = new ResponseEntity<>(allArticles, HttpStatus.OK);
        return response;
    }
}
