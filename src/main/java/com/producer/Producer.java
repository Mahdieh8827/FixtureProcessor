package com.producer;

import com.model.Message;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public interface Producer {
    Message produceMessage() throws InterruptedException;
}
