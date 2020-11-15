package com.anyun.streamdemo.controller;

import com.anyun.streamdemo.config.SenderSource;
import com.anyun.streamdemo.entity.UserMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/user")
@Api(value = "用户管理")
@Slf4j
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

    @Transactional
    @GetMapping("/send_transaction")
    @ApiOperation(value = "发送事务消息")
    public void sendTransaction() throws InterruptedException {
        // 创建 Message
        int id = new Random().nextInt();
        UserMessage message = UserMessage.builder().id(id).build();
        // 创建 Spring Message 对象
        Message<UserMessage> springMessage = MessageBuilder.withPayload(message)
                .build();
        // 发送消息
        senderSource.outputChannel().send(springMessage);
        log.info("[sendTransaction][发送编号：[{}] 发送成功]", id);
        // <X> 等待
        /**
         * 如果同步发送消息成功后，Consumer 立即消费到该消息，说明未生效。
         * 如果 Consumer 是 10 秒之后，才消费到该消息，说明已生效。
         */
        Thread.sleep(10 * 1000L);
    }

}