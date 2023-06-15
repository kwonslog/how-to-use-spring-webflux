package com.example.demo.webflux.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Slf4j
public class OkControllerTest {

  /*
   * 기본형
   */
  @Test
  @DisplayName("OkController 의 리턴값 OK 체크 - type1")
  public void test1() {
    WebTestClient client = WebTestClient.bindToController(new OkController()).build();

    client.get().uri("/ok").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("OK");
  }

  /*
   * test1() 메소드와 동일한 기능을 한다.
   * 차이점은 응답 결과를 받아서 별도 검증 처리를 하는 것.
   */
  @Test
  @DisplayName("OkController 의 리턴값 OK 체크 - type2")
  public void test2() {
    WebTestClient client = WebTestClient.bindToController(new OkController()).build();

    String responseBody = client
      .get()
      .uri("/ok")
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody(String.class)
      .returnResult()
      .getResponseBody();

    log.debug("getResponseBody: " + responseBody);

    assertEquals("OK", responseBody);
  }

  /*
   * test2() 메소드와 동일한 기능을 한다.
   * 차이점은 로그를 출력하는 방식이다.
   */
  @Test
  @DisplayName("OkController 의 리턴값 OK 체크 - type3")
  public void test3() {
    WebTestClient client = WebTestClient.bindToController(new OkController()).build();

    client
      .get()
      .uri("/ok")
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody(String.class)
      .consumeWith(response -> {
        String responseBody = new String(response.getResponseBodyContent());
        log.info("Response Body: " + responseBody);
      })
      .isEqualTo("OK");
  }

  /*
   * test1() 메소드와 동일한 기능을 한다.
   * 차이점은 StepVerifier 를 사용했다는 것이다.
   */
  @Test
  @DisplayName("OkController 의 리턴값 OK 체크 - type4")
  public void test4() {
    StepVerifier
      .create(new OkController().getData())
      .expectNextMatches(result -> {
        log.debug("Response Body: " + result);
        return result.equals("OK");
      })
      .verifyComplete();
  }
}
