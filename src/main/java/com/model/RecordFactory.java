package com.model;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class RecordFactory {
    public Record receivedMessage(Message message) {
        return new Record(RecordType.RECEIVED, message);
    }

    public Record orphaned(Message message) {
        return new Record(RecordType.ORPHANED, message);
    }

    public Record joined(Message message) {
        return new Record(RecordType.JOINED, message);
    }

    public Record producerCompleted() {
        return new Record(RecordType.COMPLETED, null);
    }
}
