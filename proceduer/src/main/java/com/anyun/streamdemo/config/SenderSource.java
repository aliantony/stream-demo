package com.anyun.streamdemo.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface SenderSource {

    @Output("output-channel")
    MessageChannel outputChannel();
}