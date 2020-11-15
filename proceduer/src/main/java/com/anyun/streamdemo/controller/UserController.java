package com.anyun.streamdemo.controller;

import com.anyun.streamdemo.config.SenderSource;
import com.anyun.streamdemo.entity.UserMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(value = "用户管理")
public class UserController {

    @Autowired
    private SenderSource senderSource;

    @PostMapping("/send")
    @ApiOperation(value = "发送消息到my-output")
    public boolean send(@RequestBody UserMessage message) {
        // <1> 创建 Message
        //UserMessage message = UserMessage.builder().id(new Random().nextInt()).build();
        // <2> 创建 Spring Message 对象
        Message<UserMessage> springMessage = MessageBuilder.withPayload(message)
                .setHeader("tag", "tudou1")
                .build();
        // <3> 发送消息
        return senderSource.outputChannel().send(springMessage);
    }

}