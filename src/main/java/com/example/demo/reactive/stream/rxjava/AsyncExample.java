package com.example.demo.reactive.stream.rxjava;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExample {

  public static void main(String[] args) {
    Observable<String> hello = Observable.just("Hello, ");
    Observable<String> world = Observable.just("World!");

    Observable<String> result = hello
      .map(item -> item + "RxJava ")
      .map(item -> item + "and ")
      .flatMap(item -> world.map(w -> item + w));

    result.subscribe(
      item -> log.debug("성공: {}", item),
      failure -> log.debug("실패: ", failure),
      () -> log.debug("완료")
    );
  }
}
