package com.example.controllers;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.MessageChannels;
// import org.springframework.integration.support.MessageBuilder;
// import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * @author haoran
 */
@RestController
@RequestMapping("/sse")
public class MessageController implements ApplicationListener {
    private final SubscribableChannel subscribableChannel = MessageChannels.publishSubscribe().get();

    @GetMapping(value = "/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getMessage() {
        return Flux.create(stringFluxSink -> {
            MessageHandler messageHandler = message -> stringFluxSink.next(String.class.cast(message.getPayload()));
            // 用户断开的时候取消订阅
            stringFluxSink.onCancel(() -> subscribableChannel.unsubscribe(messageHandler));
            // 订阅消息
            subscribableChannel.subscribe(messageHandler);
        }, FluxSink.OverflowStrategy.LATEST);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        subscribableChannel.send(new GenericMessage<>(event.getSource()));
    }

    @PostMapping("/publish")
    public void publish(@RequestParam String message) {

        subscribableChannel.send(new GenericMessage<>(message));

    }
}