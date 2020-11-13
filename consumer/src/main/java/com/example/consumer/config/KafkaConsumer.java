package com.example.consumer.config;

import com.example.consumer.entity.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

/**
 * 1、集群模式下一个组中的消费者各消费一个分区，轮流消费
 * 2、广播模式下一个组中的每个消费者都会消费
 * 目前我们看到的重试方案，是通过 RetryTemplate 来实现客户端级别的消费冲水。
 * 而 RetryTemplate 又是通过 sleep 来实现消费间隔的时候，这样将影响 Consumer 的整体消费速度，
 * 毕竟 sleep 会占用掉线程。
 */
@Component
public class KafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加 @Payload 注解，声明需要进行反序列化成 POJO 对象
     * @param message
     */
    @StreamListener(MySinkChannel.INPUT_CHANNEL)
    public void onMessage(@Payload UserMessage message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        // <X> 注意，此处抛出一个 RuntimeException 异常，模拟消费失败
        //throw new RuntimeException("我就是故意抛出一个异常");
    }

    /**
     * 消费失败局部异常处理，局部处理了全部不会再处理
     *
     * 不过要注意，如果异常处理方法成功，没有重新抛出异常，会认定为该消息被消费成功，所以就不会发到死信队列了噢。
     * @param errorMessage
     */
    @ServiceActivator(inputChannel = "my_output.my-consumer-group.errors")
    public void handleError(ErrorMessage errorMessage) {
        logger.error("[handleError][payload：{}]", errorMessage.getPayload().getMessage());
        logger.error("[handleError][originalMessage：{}]", errorMessage.getOriginalMessage());
        logger.error("[handleError][headers：{}]", errorMessage.getHeaders());
    }

    /**
     * 全局的消费失败处理逻辑，spring-integration实现的
     * @param errorMessage
     */
    @StreamListener(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
    public void globalHandleError(ErrorMessage errorMessage) {
        logger.error("[globalHandleError][payload：{}]", errorMessage.getPayload().getMessage());
        logger.error("[globalHandleError][originalMessage：{}]", errorMessage.getOriginalMessage());
        logger.error("[globalHandleError][headers：{}]", errorMessage.getHeaders());
    }

}