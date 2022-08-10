package com.example.demo2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/api")
@RestController
public class OrderController {

    @GetMapping("/orders")
    public String getOrder() {
        return "good order";
    }

    @GetMapping("/orders/{orderId}")
    public Mono<String> getOrderById(String orderId) {
        return Mono.just(orderId);
    }

    @GetMapping("/orders/2")
    public Mono<String> getAsynOrder() {
        return Mono.just("good order")
                .publishOn(Schedulers.boundedElastic())
                .map(s -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "good daay";
                });
    }

    @GetMapping(value = "/orders/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> flux() {
        // Flux<String> result = Flux
        // .fromStream(IntStream.range(1, 5).mapToObj(i -> {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // }
        // return "flux data--" + i;
        // }));
        ArrayList<String> tmp = new ArrayList<String>() {
            {
                add("1");
                add("2");
                add("3");
                add("4");
            }
        };

        String a = new String("hello world");

        // for (int i = 0; i < 10; i++) {
        // System.out.print(i);
        // }

        int[] i = { 1, 2, 3, 4 };

        List<String> a1 = Arrays.asList("a", "b", "c", "d", "e", "f");

        return Flux.fromArray(new String[] { "1", "2", "3", "4", "5" });

    }

}