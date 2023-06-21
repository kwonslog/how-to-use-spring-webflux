package com.example.demo.webflux.basic.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SseController3 {

  @GetMapping(path = "/receive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> receive() {
    /*
     * 접속하는 모든 대상에게 welcome 출력
     */
    return Flux.create(sink -> sink.next("welcome!"));
  }
}
