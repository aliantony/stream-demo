package com.example.consumer.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author wq
 * created on 2020-11-13
 * @version 1.0.0
 * @program stream-demo
 * @description 输入通道
 */
public interface MySinkChannel {
    String INPUT_CHANNEL = "input-channel";

    @Input(INPUT_CHANNEL)
    SubscribableChannel myInputChannel();
}
