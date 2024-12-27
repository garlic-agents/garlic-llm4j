package com.garlic.agents.llm.utils;

import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtilTest {

    public static final Logger log = LoggerFactory.getLogger(HttpUtilTest.class);

    @Test
    public void testGet() {
        Response response = HttpUtil.request("http://www.baidu.com");
        log.info("response: {}", response);
    }

}
