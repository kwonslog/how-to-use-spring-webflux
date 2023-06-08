package com.example.demo.reactive.stream.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class AsyncExample {

  public static void main(String[] args) {
    Mono<String> hello = Mono.just("Hello, ");
    Mono<String> world = Mono.just("World!");

    Mono<String> result = hello
      .map(item -> item + "Reactor ")
      .flatMap(item -> world.map(w -> item + w));

    result.subscribe(
      item -> log.debug("성공: {}", item),
      failure -> log.debug("실패: ", failure),
      () -> log.debug("Completed")
    );

    //test();
    test2();
  }

  public static void test() {
    Mono<String> str1 = Mono.just("내가 ");
    Mono<String> str2 = Mono.just(" 한번");
    Mono<String> str3 = Mono.just(" 해보는");

    Mono<String> result = str1
      .flatMap(v -> str2.map(w -> v + w))
      .flatMap(v -> str3.map(w -> v + w))
      .flatMap(v -> Mono.just(" 테스트").map(w -> v + w))
      .map(v -> v + "!");

    result.subscribe(str -> log.debug("결과: {}", str));
  }

  public static void test2() {
    Flux<String> str1 = Flux.just("Hello, ", "Reactive ", "World!");
    str1.reduce((a, b) -> a + b).subscribe(str -> log.debug("str1: {}", str));

    Flux<String> str2 = Flux.just("1 ", "2 ", "3! ");
    str2
      .collectList()
      .map(list -> String.join("", list))
      .subscribe(str -> log.debug("str1: {}", str));
  }
}
