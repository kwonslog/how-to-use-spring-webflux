package com.example.demo;

import com.example.demo.webflux.basic.sse.SseController2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@Slf4j
public class SseController2Test {

  @Test
  public void testConnectClient() {
    SseController2 controller = new SseController2();
    Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    // 테스트용 클라이언트 생성
    WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    //WebTestClient client = WebTestClient.bindToController(controller).build();

    // "/connect/{clientId}"에 GET 요청 보내기
    client
      .get()
      .uri("/connect/{clientId}", "test-client")
      .accept(MediaType.TEXT_EVENT_STREAM)
      .exchange()
      .expectStatus()
      .isOk()
      .expectHeader()
      .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
      //.contentType(MediaType.TEXT_EVENT_STREAM_VALUE)
      .returnResult(ServerSentEvent.class)
      .getResponseBody()
      .as(StepVerifier::create)
      .expectNextCount(3) // 예상되는 이벤트 수를 검증
      .expectComplete()
      .verify();
  }

  //@Test
  public void testConnectClient2() {
    SseController2 controller = new SseController2();

    // StepVerifier를 사용하여 응답 스트림을 검증
    StepVerifier
      .create(controller.connectClient("test-client"))
      .expectNextMatches(event -> event.data().equals("Test Event 1"))
      .expectNextMatches(event -> event.data().equals("Test Event 2"))
      .expectComplete()
      .verify();
  }
}
