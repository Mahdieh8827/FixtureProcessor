package com;

import com.exception.MalformedMessageException;
import com.model.Message;
import com.producer.RestControllerProducer;
import com.producer.sourceB.Done;
import com.producer.sourceB.Id;
import com.producer.sourceB.MessageB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
class RestControllerProducerBTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    private RestControllerProducer<MessageB> producer;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        producer = new RestControllerProducer<>("url", "host", "port", MessageB.class) {

            @Override
            protected Message adapt(MessageB messageB) {
                if (messageB.getId() == null)
                    return Message.doneMessage();
                return Message.newMessage(messageB.getId().getValue());
            }
        };
        producer.setRestTemplate(restTemplate);
    }
    @Test
    void testProduceMessage_ValidMessage() {

        MessageB validMessage = new MessageB();
        validMessage.setDone(null);
        Id id = new Id();
        id.setValue("123");
        validMessage.setId(id);

        when(restTemplate.getForObject(anyString(), eq(MessageB.class), any(), any())).thenReturn(validMessage);

        Message result = producer.produceMessage();

        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testProduceMessage_DoneMessage() {
        MessageB doneMessage = new MessageB();
        doneMessage.setDone(new Done());

        when(restTemplate.getForObject(anyString(), eq(MessageB.class), any(), any())).thenReturn(doneMessage);

        Message result = producer.produceMessage();

        assertNotNull(result);
        assertTrue(result.isLast());
    }
    @Test
    void testProduceMessage_RestClientException() {
        when(restTemplate.getForObject(anyString(), eq(MessageB.class), any(), any())).thenThrow(new RuntimeException());

        assertThrows(MalformedMessageException.class, producer::produceMessage);
    }

    @Test
    void testProduceMessage_Exception() {
        when(restTemplate.getForObject(anyString(), eq(MessageB.class), any(), any())).thenThrow(new RuntimeException());

        assertThrows(MalformedMessageException.class, producer::produceMessage);
    }
}
