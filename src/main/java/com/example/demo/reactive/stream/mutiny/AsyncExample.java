package com.example.demo.reactive.stream.mutiny;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncExample {

  public static void main(String[] args) {
    Uni<String> hello = Uni.createFrom().item("Hello, ");
    Uni<String> world = Uni.createFrom().item("World!");

    Uni<String> result = hello
      .onItem()
      //Hello, Mutiny
      .transform(item -> item + "Mutiny ")
      .onItem()
      //Hello, Mutiny and
      .transform(item -> item + "and ")
      .onItem()
      //Hello, Mutiny and World!
      .transformToUni(item -> world.map(w -> item + w));

    result
      .subscribe()
      .with(
        item -> log.debug("성공: {}", item),
        failure -> log.debug("실패:", failure)
      );
  }
}
