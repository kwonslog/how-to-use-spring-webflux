package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ReactorCase1 {

  public static void main(String[] args) {
    case1();
  }

  public static void case1() {
    Flux.just("hello", "world").subscribe(log::debug);
  }

  public static void case2() {}
}
