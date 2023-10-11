package com.producer.sourceB;


import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */

@Getter
@Setter
public class MessageB
{
    Id id;
    Done done;
    @Override public String toString()
    {
        return "MessageB{" +
                "id=" + id +
                ", done=" + done +
                '}';
    }
}

