package com.broker.stream;

import com.broker.channel.ChannelService;
import com.model.Message;
import com.model.Record;
import com.model.RecordFactory;
import com.model.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
public class StreamImpl implements Stream {
    private final Map<String, Message> messages = new ConcurrentHashMap<>();
    private final ChannelService channelService;
    private final RecordFactory recordFactory;
    private static final Logger logger = LoggerFactory.getLogger(StreamImpl.class);

    public StreamImpl(ChannelService channelService, RecordFactory recordFactory) {
        this.channelService = channelService;
        this.recordFactory = recordFactory;
    }

    @Override
    public void stream() {
        boolean exit = false;

        while (!exit) {
            Record record = channelService.pollFromProducerQueue();
            if (record != null) {
                if (record.getType().equals(RecordType.RECEIVED)) {
                    streamReceivedRecord(record);
                }
            }
            else if(!messages.isEmpty()){
                streamRecordsToOrphaned();
                exit = true;
            }

        }
    }

    private synchronized boolean checkExistMessage(Message message) {
        String messageId = message.getId();
        if (messages.remove(messageId)!=null) {
            return true;
        }
        messages.put(messageId, message);
        return false;
    }

    private void streamReceivedRecord(Record record) {
        Message payload = record.getPayload();
        if (checkExistMessage(payload)) {
            channelService.emitIntoConsumerQueue(recordFactory.joined(payload));
        }
    }

    private void streamRecordsToOrphaned() {
        for (Message message : messages.values()) {
            channelService.emitIntoConsumerQueue(recordFactory.orphaned(message));
            messages.remove(message);
            logger.info("Emitting orphaned {}", message);
        }
    }
}
