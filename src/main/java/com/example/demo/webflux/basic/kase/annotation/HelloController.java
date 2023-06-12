package com.example.demo.webflux.basic.kase.annotation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
 * 어노테이션 사용
 */
@Slf4j
@RestController
public class HelloController {

  @GetMapping(path = "hello")
  public Mono<String> hello() {
    //content-type: text/html;
    return Mono.just("Hello world!!");
  }

  @GetMapping(path = "hello2")
  public Mono<String> hello2() {
    return Mono.delay(Duration.ofSeconds(5)).then(Mono.just("Hello2 world!!"));
  }

  @GetMapping(path = "hello3")
  public Mono<String> hello3() {
    return Flux
      .range(1, 3)
      .delayElements(Duration.ofSeconds(1))
      .doOnNext(num -> log.debug("doOnNext: {}", num))
      .map(i -> "Data " + i)
      .last();
  }

  @GetMapping(path = "hello4")
  public Flux<String> hello4() {
    return Flux.just("hello1,", "hello2,", "hello3,");
  }

  @GetMapping(path = "hello5")
  public Flux<String> hello5() {
    //content-type : text/html
    return Flux
      .interval(Duration.ofSeconds(1))
      .take(3)
      .map(i -> " Hello " + (i + 1));
  }

  @GetMapping(path = "hello6", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<JsonData> hello6() {
    //JSON 형태로 리턴.(Conte)
    return Flux.just(
      new JsonData(1, "data 1"),
      new JsonData(1, "data 2"),
      new JsonData(1, "data 3")
    );
  }

  @GetMapping(path = "hello7", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> hello7() {
    //SSE(Server-Sent Events) 방식으로 처리 한다.
    return Flux
      .interval(Duration.ofSeconds(1))
      .take(3)
      .map(i -> " Hello " + (i + 1));
  }
}

@JsonSerialize
@AllArgsConstructor
@Getter
class JsonData {

  private int id;
  private String value;
}
