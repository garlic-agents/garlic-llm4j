package com.garlic.agents.llm.utils;

import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtilTest {

    public static final Logger log = LoggerFactory.getLogger(HttpUtilTest.class);

    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGet() throws IOException {
        // 设置 MockWebServer 的响应
        mockWebServer.enqueue(new MockResponse()
                .setBody("Hello, world!")
                .setResponseCode(200));
        // 获取 MockWebServer 的 URL
        String url = mockWebServer.url("/test").toString();

        Response response = HttpUtil.request(url);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.code());
        log.info("response: {}", response);
        Assertions.assertNotNull(response.body());
        String responseBodyStr = response.body().string();
        Assertions.assertEquals("Hello, world!", responseBodyStr);
        log.info("response body: {}", responseBodyStr);
    }

}
