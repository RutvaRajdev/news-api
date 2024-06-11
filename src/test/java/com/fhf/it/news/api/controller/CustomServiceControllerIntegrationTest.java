package com.fhf.it.news.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhf.it.news.api.model.ErrorResponseWrapper;
import com.fhf.it.news.api.model.ResponseWrapper;
import com.fhf.it.news.api.service.impl.PublicNewsAPIServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CustomServiceControllerIntegrationTest {

    @Autowired
    private CustomServiceController customServiceController;

    private static ClientAndServer mockServer;

    public ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws URISyntaxException {
        mockServer = startClientAndServer(12345);
        mockHTTPRequests();
    }

    @AfterEach
    public void cleanUp() {
        mockServer.stop();
    }

    @Test
    public void testGetByTitle() {

        ResponseWrapper response = customServiceController.getByTitle("Learning to Live With Google's AI Overviews").getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getTotalResults(), 2);
    }

    @Test
    public void testGetTopHeadlinesByKeywordError() {

        ErrorResponseWrapper response = (ErrorResponseWrapper) customServiceController.getTopHeadlinesByKeyword("finance", 200).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getErrorMessage(), "Please provide a value of n between 1 and 100");
        Assertions.assertEquals(response.getErrorCode(), 400);
        Assertions.assertEquals(response.getClass(), ErrorResponseWrapper.class);
    }

    private void mockHTTPRequests() throws URISyntaxException {
        mockServer.when(request().withMethod("GET").withPath("/v2/everything")).respond(response()
                .withStatusCode(200)
                .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                .withBody(parseJsonFile("data/EverythingResponse.json")));
    }

    private String parseJsonFile(String fileName) throws URISyntaxException {
        String content = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
