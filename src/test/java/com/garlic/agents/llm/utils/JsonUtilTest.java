package com.garlic.agents.llm.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonUtilTest {

    @Test
    public void checkJsonPathTest() {
        String json = "{\"name\":\"mcy\",\"age\":18}";
        DocumentContext context = JsonPath.parse(json);
        Assertions.assertTrue(JsonUtil.checkJsonPath(context, "$.name"));
        Assertions.assertTrue(JsonUtil.checkJsonPath(context, "$.age"));
        Assertions.assertFalse(JsonUtil.checkJsonPath(context, "$.test"));
    }

    @Test
    public void safeReadTest() {
        String json = "{\"name\":\"mcy\",\"age\":18}";
        DocumentContext context = JsonPath.parse(json);
        Assertions.assertEquals("mcy", JsonUtil.safeRead(context, "$.name"));
        Assertions.assertEquals(18, JsonUtil.safeRead(context, "$.age"));
        Assertions.assertNull(JsonUtil.safeRead(context, "$.test"));
    }
}
