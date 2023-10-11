package com.producer;

import com.broker.channel.ChannelService;
import com.model.Message;
import com.model.RecordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */

public class MessageProducer implements Runnable {
    private final Producer producer;
    private final ChannelService channelService;
    private final RecordFactory recordFactory;
    private final long timeToAwait;
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    public MessageProducer(Producer producer,
                           ChannelService channelService,
                           RecordFactory recordFactory,
                           long timeToAwait) {
        this.producer = producer;
        this.channelService = channelService;
        this.recordFactory = recordFactory;
        this.timeToAwait = timeToAwait;
    }

    public void run() {
        boolean exit = false;
        do {
            Message message = nextMessage();
            if (message != null) {
                if (message.isWait()) {
                    await();
                } else if (message.isLast()) {
                    logger.info("Received shutdown.");
                    channelService.emitIntoProducerQueue(recordFactory.producerCompleted());
                    exit = true;
                } else {
                    channelService.emitIntoProducerQueue(recordFactory.receivedMessage(message));
                }
            } else {
                logger.info("Received null message.");
            }

        } while (!exit);
    }

    private void await() {
        try {
            Thread.sleep(timeToAwait);
        } catch (InterruptedException ex) {
            logger.error("InterruptedException, waiting ...");
        }
    }

    protected Message nextMessage() {
        try {
            return producer.produceMessage();
        } catch (Exception ex) {
            logger.error(String.format("Malformed message received with: %s", ex));
            return null;
        }
    }
}
