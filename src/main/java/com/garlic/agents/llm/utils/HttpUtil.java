package com.garlic.agents.llm.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

/**
 * http request util
 *
 * @author MoChenYa
 * @date 2024/12/27
 * @since 1.0
 */
public class HttpUtil {

    private HttpUtil() {
    }

    private static class LazyHolder {
        static final OkHttpClient CLIENT = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static Response request(String url) {
        OkHttpClient client = getClient();
        Call call = client.newCall(new Request.Builder().url(url).build());
        try {
            return call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getClient() {
        return LazyHolder.CLIENT;
    }
}
