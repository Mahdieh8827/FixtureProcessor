package com;

import com.broker.channel.ChannelServiceImpl;
import com.broker.stream.MessageStream;
import com.broker.stream.Stream;
import com.broker.stream.StreamImpl;
import com.consumer.MessageConsumer;
import com.consumer.RestControllerConsumer;
import com.model.RecordFactory;
import com.producer.MessageProducer;
import com.producer.Producer;
import com.producer.sourceA.RestControllerProducerA;
import com.producer.sourceB.RestControllerProducerB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class FixtureProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FixtureProcessor.class);

    public static void main(String[] args) {

        RecordFactory recordFactory = new RecordFactory();
        ChannelServiceImpl topicBroker = new ChannelServiceImpl(1000);

        Producer sourceA = new RestControllerProducerA(Constant.HOST, Constant.PORT);
        MessageProducer messageProducerForResourceA = new MessageProducer(sourceA, topicBroker, recordFactory, 500);

        Producer sourceB = new RestControllerProducerB(Constant.HOST, Constant.PORT);
        MessageProducer messageProducerForResourceB = new MessageProducer(sourceB, topicBroker, recordFactory, 500);

        Stream stream = new StreamImpl(topicBroker, recordFactory);
        MessageStream messageStream = new MessageStream(stream);

        RestControllerConsumer consumer = new RestControllerConsumer(topicBroker, Constant.HOST, Constant.PORT);

        MessageConsumer messageConsumer = new MessageConsumer(consumer);

        ExecutorService executorProducer = Executors.newFixedThreadPool(2);
        executorProducer.submit(messageProducerForResourceA);
        executorProducer.submit(messageProducerForResourceB);

        ExecutorService executorStream = Executors.newFixedThreadPool(1);
        executorStream.submit(messageStream);

        ExecutorService executorConsumer = Executors.newFixedThreadPool(1);
        executorConsumer.submit(messageConsumer);

        try {
            executorProducer.shutdown();
            executorProducer.awaitTermination(1, TimeUnit.MINUTES);
            logger.info("Producers completed.");

        } catch (InterruptedException ex) {
            logger.error("Interrupted", ex);
        }

        try {
            executorStream.shutdown();
            executorStream.awaitTermination(1, TimeUnit.MINUTES);
            logger.info("Stream completed.");

        } catch (InterruptedException ex) {
            logger.error("Interrupted", ex);
        }

        try {
            executorConsumer.shutdown();
            executorConsumer.awaitTermination(1, TimeUnit.MINUTES);
            logger.info("Stream completed.");

        } catch (InterruptedException ex) {
            logger.error("Interrupted", ex);
        }
    }
}
