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
public class Record {
    private final RecordType type;
    private final Message payload;
}
