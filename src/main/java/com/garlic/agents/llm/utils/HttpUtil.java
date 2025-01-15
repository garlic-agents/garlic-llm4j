package com.garlic.agents.llm.utils;

import cn.hutool.core.util.ObjUtil;
import com.garlic.agents.llm.enums.ResponseStatus;
import com.garlic.agents.llm.utils.domain.HttpSseResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * http request util
 *
 * @author MoChenYa
 * @since 1.0
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

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

    public static Response request(@NotNull Request request) {
        OkHttpClient client = getClient();
        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void streamRequest(@NotNull Request request, Consumer<HttpSseResponse> callback) {
        try {
            OkHttpClient client = getClient();
            CountDownLatch eventLatch = new CountDownLatch(1);
            RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
                @Override
                public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
                    HttpSseResponse sseResponse = new HttpSseResponse.Builder()
                            .status(ResponseStatus.SUCCESS)
                            .data(data)
                            .build();
                    callback.accept(sseResponse);
                }

                @Override
                public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                    if (ObjUtil.isNotNull(response) && ObjUtil.isNotNull(response.body())) {
                        try {
                            logger.error("stream request onFailure response => {}", response.body().string());
                        } catch (Exception ignore) {
                        }
                    }
                    logger.error("stream request onFailure throwable => ", t);
                    HttpSseResponse sseResponse = new HttpSseResponse.Builder()
                            .status(ResponseStatus.FAILURE)
                            .data(ObjUtil.isNull(t) ? "stream request failed" : t.getMessage())
                            .build();
                    callback.accept(sseResponse);
                    eventLatch.countDown();
                }

                @Override
                public void onClosed(@NotNull EventSource eventSource) {
                    logger.info("stream request closed");
                    eventLatch.countDown();
                    callback.accept(HttpSseResponse.CLOSED);
                }
            });
            realEventSource.connect(client);
            eventLatch.await();
        } catch (Exception exception) {
            logger.error("stream request failed", exception);
            HttpSseResponse sseResponse = new HttpSseResponse.Builder()
                    .status(ResponseStatus.FAILURE)
                    .data(exception.getMessage())
                    .build();
            callback.accept(sseResponse);
        }
    }

    public static OkHttpClient getClient() {
        return LazyHolder.CLIENT;
    }
}
