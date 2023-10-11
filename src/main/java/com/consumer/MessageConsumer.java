package com.consumer;

import lombok.AllArgsConstructor;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
@AllArgsConstructor
public class MessageConsumer implements Runnable{
    private final Consumer consumer;

    @Override
    public void run() {
        consumer.consume();
    }
}
