package com.model;

/**
 * @author Mahdieh
 * On 07 Oct 2023
 */
public enum RecordType {
    RECEIVED("received"),
    ORPHANED("orphaned"),
    JOINED("joined"),
    COMPLETED("completed");

    private final String value;

    RecordType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
