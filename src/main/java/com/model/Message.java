package com.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
@Getter
@Setter
@AllArgsConstructor
public class Message {
    private final String id;
    private final boolean wait;
    private final boolean last;

    public static Message newMessage(String id) {
        return new Message(id,false,false);
    }
    public static Message doneMessage() {
        return new Message(null,false,true);
    }

    public static Message waitMessage() {
        return new Message(null,true,false);
    }
}
