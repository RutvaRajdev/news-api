package com.fhf.it.news.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhf.it.news.api.client.PublicNewsAPIClient;
import com.fhf.it.news.api.model.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@ExtendWith(SpringExtension.class)
public class PublicNewsAPIServiceImplTest {

    @Mock
    private PublicNewsAPIClient publicNewsAPIClient;

    @InjectMocks
    private PublicNewsAPIServiceImpl publicNewsAPIServiceImpl;

    public ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "encodedApikey", "OWY3MmVmNTkwYjI0NDhmZGIwZWNiYWMzNGNmYzc3Y2I=");
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "everythingPageSize", 100);
        ReflectionTestUtils.setField(publicNewsAPIServiceImpl, "topHeadlinesPageSize", 100);

    }

    @Test
    public void testGetAllArticlesSuccess() throws IOException {


        Mockito.when(publicNewsAPIClient.getEverything(Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(Date.class),
                Mockito.nullable(Date.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(Integer.class),
                Mockito.nullable(Integer.class),
                Mockito.anyString())).thenReturn(getSampleResponseWrapper("data/EverythingResponse.json"));

        ResponseWrapper response = publicNewsAPIServiceImpl.getAllArticles("google", "title", null, null, null, null, null, null, null, null, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getArticles().size(), 2);
        Assertions.assertEquals(response.getArticles().get(0).getSource().getName(), "Wired");
    }

    @Test
    public void testGetTopHeadlinesSuccess() throws IOException {


        Mockito.when(publicNewsAPIClient.getTopHeadlines(Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(String.class),
                Mockito.nullable(Integer.class),
                Mockito.nullable(Integer.class),
                Mockito.anyString())).thenReturn(getSampleResponseWrapper("data/HeadlinesResponse.json"));

        ResponseWrapper response = publicNewsAPIServiceImpl.getTopHeadlines("us", null, null, null, null);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getArticles().size(), 3);
        Assertions.assertEquals(response.getArticles().get(2).getTitle(), "Darren Waller reveals near-death experience from last season in Giants retirement video - New York Post ");
    }

    private ResponseWrapper getSampleResponseWrapper(String resourceName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());

        ResponseWrapper responseWrapper = objectMapper.readValue(file, new TypeReference<ResponseWrapper>() {});

        return responseWrapper;

    }
}
