package com.broker.channel;

import com.model.Record;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public interface ChannelService {
    void emitIntoProducerQueue(Record record);

    void emitIntoConsumerQueue(Record record);

    Record pollFromProducerQueue();

    Record pollFromConsumerQueue();

}
