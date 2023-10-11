package com.interceptors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse delegate;
    private final byte[] responseBody;

    public HttpResponseWrapper(final ClientHttpResponse delegate)
            throws IOException {
        this.delegate = delegate;
        InputStream bodyInputStream = delegate.getBody();
        if (bodyInputStream != null) {
            responseBody = FileCopyUtils.copyToByteArray(bodyInputStream);
        } else {
            responseBody = new byte[0];
        }
    }

    public void close() {
        delegate.close();
    }

    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(responseBody);
    }

    public HttpHeaders getHeaders() {
        return delegate.getHeaders();
    }

    public int getRawStatusCode() throws IOException {
        return delegate.getRawStatusCode();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public HttpStatus getStatusCode() throws IOException {
        return delegate.getStatusCode();
    }


    public String getStatusText() throws IOException {
        return delegate.getStatusText();
    }
}
