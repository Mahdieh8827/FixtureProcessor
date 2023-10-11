package com.exception;

/**
 * @author Mahdieh
 * On 08 Oct 2023
 */
public class WrongSinkMessageException extends RuntimeException
{
    public WrongSinkMessageException(String id)
    {
        super("Sink failure for message with id ["+id+"].");
    }
}
