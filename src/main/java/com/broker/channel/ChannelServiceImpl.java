package com.broker.channel;

import com.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class ChannelServiceImpl implements ChannelService {
    private final ArrayBlockingQueue<Record> producerQueue;
    private final ArrayBlockingQueue<Record> consumerQueue;

    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    public ChannelServiceImpl(int bufferCapacity) {
        producerQueue = new ArrayBlockingQueue<>(bufferCapacity);
        consumerQueue = new ArrayBlockingQueue<>(bufferCapacity);
    }

    @Override
    public void emitIntoProducerQueue(Record record) {
        if (!this.producerQueue.offer(record)) {
              logger.info("Queue is full!");
        }
    }

    @Override
    public void emitIntoConsumerQueue(Record record) {
        if (!this.consumerQueue.offer(record)) {
               logger.info("Queue is full!");
        }
    }

    @Override
    public Record pollFromProducerQueue() {
        try {
            return producerQueue.poll(500l, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            logger.error("Dispatcher thread interrupted.");
        }
        return null;
    }

    @Override
    public Record pollFromConsumerQueue() {
        try {
            return consumerQueue.poll(500l, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            logger.error("Dispatcher thread interrupted.");
        }
        return null;
    }
}
