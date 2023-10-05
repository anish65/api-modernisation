package com.zand.system.common.messaging.kafka.message;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestMessage implements KafkaMessage {
    String message;
}
