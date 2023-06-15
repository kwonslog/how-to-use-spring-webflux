package com.example.demo.webflux.rest;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/*
 * 간단한 문자열을 리턴하는 실습.
 *
 */
@RestController
@NoArgsConstructor
public class OkController {

  @GetMapping(path = "/ok")
  public Mono<String> getData() {
    return Mono.just("OK");
  }
}
