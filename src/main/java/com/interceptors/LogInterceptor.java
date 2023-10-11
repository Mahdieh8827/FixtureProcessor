package com.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LogInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    public ClientHttpResponse intercept(final HttpRequest request,
                                        final byte[] body,
                                        final ClientHttpRequestExecution clientHttpRequestExecution)
            throws IOException {
        extractAndLogRequest(request, body);

        ClientHttpResponse response = doChain(clientHttpRequestExecution, request, body);

        return extractAndLogResponse(response);
    }

    protected ClientHttpResponse
    doChain(final ClientHttpRequestExecution execution, final HttpRequest request, final byte[] body)
            throws IOException {
        return execution.execute(request, body);
    }


    protected void extractAndLogRequest(final HttpRequest request, final byte[] body) {
        logger.info("Request as {} to {} is {}", request.getMethod(), request.getURI(), asString(body));
    }

    private String asString(byte[] body) {
        if (body != null) {
            return new String(body);
        }
        return null;
    }

    protected ClientHttpResponse extractAndLogResponse(final ClientHttpResponse response) throws IOException {
        HttpResponseWrapper wrapper = new HttpResponseWrapper(response);
        logger.info("Response status is {} and contains {}", wrapper.getStatusCode(), asString(wrapper.getResponseBody()));
        return wrapper;
    }

}
