package com;

import com.exception.MalformedMessageException;
import com.model.Message;
import com.model.RecordType;
import com.producer.RestControllerProducer;
import com.producer.sourceA.MessageA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
class RestControllerProducerATest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    private RestControllerProducer<MessageA> producer;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        producer = new RestControllerProducer<MessageA>("url", "host", "port", MessageA.class) {

            @Override
            protected Message adapt(MessageA messageA) {
                if (messageA.getStatus().equals(Constant.STATUS_COMPLETED))
                    return Message.doneMessage();

                return Message.newMessage(messageA.getId());
            }
        };
        producer.setRestTemplate(restTemplate);
    }
    @Test
    void testProduceMessage_ValidMessage() {

        MessageA validMessage = new MessageA();
        validMessage.setStatus(RecordType.RECEIVED.getValue());
        validMessage.setId("123");

        when(restTemplate.getForObject(anyString(), eq(MessageA.class), any(), any())).thenReturn(validMessage);

        Message result = producer.produceMessage();

        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testProduceMessage_DoneMessage() {
        MessageA doneMessage = new MessageA();
        doneMessage.setStatus(Constant.STATUS_COMPLETED);

        when(restTemplate.getForObject(anyString(), eq(MessageA.class), any(), any())).thenReturn(doneMessage);

        Message result = producer.produceMessage();

        assertNotNull(result);
        assertTrue(result.isLast());
    }
    @Test
    void testProduceMessage_RestClientException() {
        when(restTemplate.getForObject(anyString(), eq(MessageA.class), any(), any())).thenThrow(new RuntimeException());

        assertThrows(MalformedMessageException.class, producer::produceMessage);
    }

    @Test
    void testProduceMessage_Exception() {
        when(restTemplate.getForObject(anyString(), eq(MessageA.class), any(), any())).thenThrow(new RuntimeException());

        assertThrows(MalformedMessageException.class, producer::produceMessage);
    }
}


