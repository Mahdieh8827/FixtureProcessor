package com.producer.sourceA;

import com.producer.RestControllerProducer;
import com.Constant;
import com.model.Message;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class RestControllerProducerA extends RestControllerProducer<MessageA> {

    public RestControllerProducerA(String host, String port) {
        super(Constant.URL_SOURCE_A, host, port, MessageA.class);
    }

    @Override
    protected Message adapt(MessageA message) {
        if (message.getStatus().equals(Constant.STATUS_COMPLETED))
            return Message.doneMessage();

        return Message.newMessage(message.getId());
    }

}
