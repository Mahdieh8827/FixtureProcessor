package com.producer.sourceA;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageA {
    private String status;
    private String id;

    @Override
    public String toString() {
        return "MessageA{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
