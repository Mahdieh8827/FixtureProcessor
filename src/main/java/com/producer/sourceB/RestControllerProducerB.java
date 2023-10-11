package com.producer.sourceB;


import com.Constant;
import com.model.Message;
import com.producer.RestControllerProducer;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class RestControllerProducerB extends RestControllerProducer<MessageB> {
        public RestControllerProducerB(String host, String port) {
                super(Constant.URL_SOURCE_B, host, port, MessageB.class);
        }

        protected Message adapt(MessageB message) {
                if (message.getId() == null)
                        return Message.doneMessage();
                return Message.newMessage(message.getId().getValue());
        }
}
