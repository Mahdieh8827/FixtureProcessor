package com.exception;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public class MalformedMessageException extends RuntimeException
{
    public MalformedMessageException()
    {
        super("Malformed message");
    }
}
