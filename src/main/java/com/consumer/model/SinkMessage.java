package com.consumer.model;

import static com.model.RecordType.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
@AllArgsConstructor
@Setter
@Getter
public class SinkMessage {
    private final String kind;
    private final String id;

    public static SinkMessage joinedFor(String id) {
        return new SinkMessage(JOINED.getValue(),id);
    }

    public static SinkMessage orphanedFor(String id) {
        return new SinkMessage(ORPHANED.getValue(),id);
    }
}
