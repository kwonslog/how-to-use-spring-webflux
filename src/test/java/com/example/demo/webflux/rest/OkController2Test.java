package com.example.demo.webflux.rest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

/*
 * 아래 test() 와 test2() 메소드의 테스트 결과가 나오기 까지 걸린 시간은
 *   test  : 1.9s - WebTestClient 사용.
 *   test2 : 131ms - StepVerifier 사용.
 *
 * WebTestClient 를 사용할 경우 실제 http 통신이 발생하기 때문에 시간이 더 오래 걸린다.
 * 빠르게 기능에 대한 검증만 필요하다면 StepVerifier 를 사용하는 것이 더 좋을 것 같다.
 */
@Slf4j
public class OkController2Test {

  @Test
  @DisplayName("OkController2 의 리턴값 OK 체크 - type1")
  public void test() {
    //OkController2 를 생성할때 testServcie 가 꼭 필요하다.
    WebTestClient client = WebTestClient.bindToController(new OkController2(new TestService())).build();

    client.get().uri("/ok2").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("OK");
  }

  /*
   * test1() 메소드와 동일한 기능을 한다.
   * 차이점은 StepVerifier 를 사용했다는 것이다.
   */
  @Test
  @DisplayName("OkController2 의 리턴값 OK 체크 - type2")
  public void test2() {
    StepVerifier
      .create(new OkController2(new TestService()).getData())
      .expectNextMatches(result -> {
        log.debug("Response Body: " + result);
        return result.equals("OK");
      })
      .verifyComplete();
  }
}
