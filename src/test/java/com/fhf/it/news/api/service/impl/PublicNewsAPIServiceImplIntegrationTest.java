package com.fhf.it.news.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhf.it.news.api.model.ResponseWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PublicNewsAPIServiceImplIntegrationTest {

    @Autowired
    private PublicNewsAPIServiceImpl publicNewsAPIServiceImpl;

    private static ClientAndServer mockServer;

    public ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws URISyntaxException {
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "encodedApikey", "OWY3MmVmNTkwYjI0NDhmZGIwZWNiYWMzNGNmYzc3Y2I=");
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "everythingPageSize", 100);
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "topHeadlinesPageSize", 100);

        mockServer = startClientAndServer(12345);
        mockHTTPRequests();
    }

    @AfterEach
    public void cleanUp() {
        mockServer.stop();
    }

    @Test
    public void testGetEverything() {

        ResponseWrapper response = publicNewsAPIServiceImpl.getAllArticles("google", "title", null, null, null, null, null, null, null, null, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getTotalResults(), 2);
    }

    @Test
    public void testGetTopHeadlines() {

        ResponseWrapper response = publicNewsAPIServiceImpl.getTopHeadlines("us", null, null, null, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getTotalResults(), 3);
    }

    private void mockHTTPRequests() throws URISyntaxException {
        mockServer.when(request().withMethod("GET").withPath("/v2/everything")).respond(response()
                .withStatusCode(200)
                .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                .withBody(parseJsonFile("data/EverythingResponse.json")));

        mockServer.when(request().withMethod("GET").withPath("/v2/top-headlines")).respond(response()
                .withStatusCode(200)
                .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                .withBody(parseJsonFile("data/HeadlinesResponse.json")));
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
