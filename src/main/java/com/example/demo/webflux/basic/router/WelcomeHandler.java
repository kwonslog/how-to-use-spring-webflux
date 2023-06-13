package com.example.demo.webflux.basic.router;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/*
 * 라우터를 통해 연결 정보를 받아서 실제로 처리하는 역할을 담당.
 */
@Component
public class WelcomeHandler {

  public Mono<ServerResponse> welcome(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome!"));
  }

  public Mono<ServerResponse> welcome2(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome2!"));
  }

  public Mono<ServerResponse> welcome3(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome3!"));
  }

  public Mono<ServerResponse> welcome4(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome4!"));
  }

  public Mono<ServerResponse> welcome5(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome5!"));
  }

  public Mono<ServerResponse> welcome7(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(MediaType.TEXT_PLAIN)
      .body(BodyInserters.fromValue("Welcome7!"));
  }
}
