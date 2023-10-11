package com.consumer;

import com.consumer.model.SinkMessage;
import com.consumer.model.SinkResponse;
import com.Constant;
import com.broker.channel.ChannelService;
import com.exception.WrongSinkMessageException;
import com.interceptors.LogInterceptor;
import com.model.Record;
import com.model.RecordType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
public class RestControllerConsumer implements Consumer {
    private final ChannelService channelService;
    private final String host;
    private final String port;
    private final RestTemplate restTemplate;
    AtomicBoolean isActive = new AtomicBoolean(true);

    public RestControllerConsumer(ChannelService channelService, String host, String port) {
        this.channelService = channelService;
        this.host = host;
        this.port = port;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(Collections.singletonList(new LogInterceptor()));
    }

    @Override
    public void consume() {
        while (isActive.get()) {
            Record record = channelService.pollFromConsumerQueue();
            if (record != null && (record.getType().equals(RecordType.ORPHANED) || record.getType().equals(RecordType.JOINED)))
                save(fromEvent(record));

        }
    }

    private SinkMessage fromEvent(Record record) {
        RecordType type = record.getType();
        String id = record.getPayload().getId();
        if (RecordType.ORPHANED.equals(type)) {
            return SinkMessage.orphanedFor(id);
        } else {
            return SinkMessage.joinedFor(id);
        }
    }

    public void save(SinkMessage sinkMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<SinkResponse> stringResponseEntity = restTemplate
                .postForEntity(Constant.URL_SINK_A, new HttpEntity<>(sinkMessage, headers), SinkResponse.class, host, port);

        if (!HttpStatus.OK.equals(stringResponseEntity.getStatusCode())) {
            throw new WrongSinkMessageException(sinkMessage.getId());
        }
    }
}
