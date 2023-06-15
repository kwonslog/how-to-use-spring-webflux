package com.example.demo.webflux.rest;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/*
 * OkController 와 동일한 기능을 구현
 *
 * 차이점은 bean으로 등록된 TestService 를 사용하는 것
 *
 */
@RestController
@NoArgsConstructor
@AllArgsConstructor
public class OkController2 {

  private TestService testService;

  @GetMapping(path = "/ok2")
  public Mono<String> getData() {
    return Mono.just(testService.getData());
  }
}
