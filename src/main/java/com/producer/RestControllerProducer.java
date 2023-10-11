package com.producer;

import com.exception.MalformedMessageException;
import com.interceptors.LogInterceptor;
import com.model.Message;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */

public abstract class RestControllerProducer<T> implements Producer {
    private final String urlTemplate;
    private final String host;
    private final String port;
    private final Class<T> sourceClass;
    private RestTemplate restTemplate;

    public RestControllerProducer(String urlTemplate, String host, String port, Class<T> sourceClass) {
        this.urlTemplate = urlTemplate;
        this.host = host;
        this.port = port;
        this.sourceClass = sourceClass;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(Collections.singletonList(new LogInterceptor()));
    }
    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public Message produceMessage() {
        try {
            return innerNextMessage();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 406) {
                return Message.waitMessage();
            }
            throw ex;
        } catch (RestClientException ex) {
            throw new MalformedMessageException();
        } catch (Exception e) {
            throw new MalformedMessageException();
        }
    }

    protected abstract Message adapt(T t);

    private Message innerNextMessage() {
        T message = restTemplate.getForObject(urlTemplate,
                sourceClass,
                host,
                port);
        return adapt(message);
    }
}
