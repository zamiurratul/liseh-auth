package com.liseh;

import lombok.Data;

@Data
public class GenericKafkaObject {
    private String eventType;
    private String eventName;
    private String content;
}
