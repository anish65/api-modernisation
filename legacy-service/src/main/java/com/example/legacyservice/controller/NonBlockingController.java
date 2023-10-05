package com.example.legacyservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class NonBlockingController {

    @GetMapping("/blocking")
    public String blocking() {
        return "Blocking";
    }

    @GetMapping("/non-blocking")
    public CompletableFuture<String> nonBlocking() {
        CompletableFuture test= CompletableFuture.supplyAsync(() -> {
            log.info("inside method processing");
            return "Non-blocking";
        });
        log.info("outside method processing");
        return test;
    }
}
