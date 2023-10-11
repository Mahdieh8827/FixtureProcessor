package com.broker.stream;


/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
public class MessageStream implements Runnable {
    private final Stream stream;

    public MessageStream(Stream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        stream.stream();
    }
}
