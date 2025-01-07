package com.garlic.agents.llm.utils.domain;

import com.garlic.agents.llm.enums.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * HttpSseResponse
 *
 * @author MoChenYa
 * @since 1.0
 */
public class HttpSseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final HttpSseResponse CLOSED = new HttpSseResponse(ResponseStatus.END, null);

    private ResponseStatus status;

    private String data;

    public HttpSseResponse() {
    }

    public HttpSseResponse(ResponseStatus status, String data) {
        this.status = status;
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static final class Builder {
        private ResponseStatus status;
        private String data;

        public Builder() {
        }

        public Builder status(ResponseStatus status) {
            this.status = status;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public HttpSseResponse build() {
            HttpSseResponse httpSseResponse = new HttpSseResponse();
            httpSseResponse.setStatus(status);
            httpSseResponse.setData(data);
            return httpSseResponse;
        }
    }
}
