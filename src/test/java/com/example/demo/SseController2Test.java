package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@WebFluxTest
@AutoConfigureWebTestClient
public class SseController2Test {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void testConnectClient() {
    //TODO WebTestClient, StepVerifier 사용법을 익히자.
    log.debug("webTestClient: {}", webTestClient);
  }
}
