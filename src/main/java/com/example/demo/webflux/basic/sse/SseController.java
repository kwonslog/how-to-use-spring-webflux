package com.example.demo.webflux.basic.sse;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class SseController {

  @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> sse() {
    return Flux.interval(Duration.ofSeconds(1)).map(seq -> "SSE event #" + seq);
  }

  @GetMapping(path = "/sse2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> sse2() {
    log.debug("/sse2 start!");

    return Flux
      .interval(Duration.ofSeconds(1))
      .map(seq ->
        ServerSentEvent
          .<String>builder()
          //이 값으로 목적을 구분하고
          .event("custom event")
          //이 값으로 메세지를 식별하고
          .id(String.valueOf(seq))
          .comment("코멘트?")
          //이 값을 데이터로 사용하고
          .data("SSE Event #" + seq)
          //재연결 요청 주기
          .retry(Duration.ofSeconds(5))
          .build()
      );
  }
}
