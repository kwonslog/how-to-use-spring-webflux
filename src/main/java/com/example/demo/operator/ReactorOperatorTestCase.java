package com.example.demo.operator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ReactorOperatorTestCase {

  private static Map<String, Consumer<String>> methodMap = new HashMap<>();

  static {
    methodMap.put("zip", notUse -> zip());
    methodMap.put("and", notUse -> and());
  }

  public static void main(String[] args) {
    selector("and");
  }

  private static void selector(String method) {
    methodMap.get(method).accept("notUse");
  }

  private static void zip() {
    Flux
      .zip(
        //2개의 Flux 를 하나로 묶어 준다.
        //데이터의 개수가 다를 경우 적은쪽에 맞춰서 처리 된다.
        Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(300L)),
        Flux
          .just(10, 20, 30, 40, 50, 60, 70)
          .delayElements(Duration.ofMillis(500L)),
        (n1, n2) -> n1 * n2
      )
      .subscribe(tuple2 -> log.info("# onNext: {}", tuple2));

    try {
      Thread.sleep(25000L);
    } catch (InterruptedException e) {
      log.error("zip error", e);
    }
  }

  private static void and() {
    //예제를 실행해 보면 onComplete 가 호출되고 끝이난다.
    //upstream 에서 뭔가 수행하고 and() 를 사용해서 마지막 작업을 할때 사용하는 식??
    Mono
      .just("Task 1")
      .delayElement(Duration.ofSeconds(1))
      .doOnNext(data -> log.info("# Mono doOnNext: {}", data))
      .and(
        Flux
          .just("Task 2", "Task 3")
          .delayElements(Duration.ofMillis(600))
          .doOnNext(data -> log.info("# FluxdoOnNext: {}", data))
      )
      .subscribe(
        data -> log.info("# onNext: {]}", data),
        error -> log.error("# onError: {}", error),
        () -> log.info("# onComplete")
      );

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      log.error("and error", e);
    }
  }

  private static void test() throws Exception {
    log.debug("test");
  }
}
