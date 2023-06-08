package com.example.demo.reactive.stream.reactor;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
public class AsyncExample2 {

  public static void main(String[] args) {
    String apiUrl = "https://www.daum.net"; // 호출할 API의 URL

    HttpClient client = HttpClient.create(); // Reactor Netty의 HttpClient 생성

    Mono<String> response = client
      .get()
      .uri(apiUrl)
      .responseContent()
      .aggregate()
      //.asString(Charset.forName("UTF-8"));
      .asString(StandardCharsets.UTF_8);

    response.subscribe(
      result -> log.debug("API Response: {}", result),
      error -> log.error("API Error: ", error.getMessage())
    );

    // 프로그램이 종료되지 않도록 유지하기 위해 잠시 대기
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    AsyncExample2.Test1 test11 = new AsyncExample2.Test1();
  }

  class Test1 {}
}

class Test2 {}
