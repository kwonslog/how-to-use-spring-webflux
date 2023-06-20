package com.example.demo.webflux.basic.sse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/*
 * 이 테스트 클래스는
 * Sinks.one, Sinks.many 에 대한 이해를 위해 작성 되었습니다.
 *
 * 테스트 결과 요약
 * - one() 을 사용하면 데이터는 한번 발생이 되고 종료 됩니다.
 * - many() 를 사용하면 데이터를 여러번 발생 시킬 수 있습니다.
 * - many().multicast()를 사용하면 다수의 구독자에게 동일한 데이터를 전송 할 수 있습니다.
 * - many().unicast()는 1명의 구독자에게만 데이터를 전송 할 수 있습니다.
 */
public class TestSinks {

  @Test
  @DisplayName("Sinks.one 은 데이터를 한번만 발생시킨다. 검증1")
  public void test_SinksOne() {
    // Sinks.One 생성
    Sinks.One<String> sink = Sinks.one();

    // 구독자 생성
    Mono<String> mono = sink.asMono();

    //데이터 설정 및 전송
    sink.tryEmitValue("Hello, World!");

    //이 값은 출력되지 않는다.
    sink.tryEmitValue("Hello, World!!");

    // 데이터 수신
    mono.subscribe(
      value -> System.out.println("Received: " + value),
      error -> System.err.println("Error: " + error),
      () -> System.out.println("Completed")
    );
  }

  @Test
  @DisplayName("Sinks.one 은 데이터를 한번만 발생시킨다. 검증2")
  public void test_SinksOne2() {
    // Sinks.One 생성
    Sinks.One<String> sink = Sinks.one();

    // 구독자 생성
    Mono<String> mono = sink.asMono();

    //데이터 설정 및 전송
    for (int i = 1; i <= 3; i++) {
      //반복문으로 실행하여도 최초 1번만 실행된다.
      sink.tryEmitValue("Hello, World!" + i);
    }

    // 데이터 수신
    mono.subscribe(
      value -> System.out.println("Received: " + value),
      error -> System.err.println("Error: " + error),
      () -> System.out.println("Completed")
    );
  }

  @Test
  @Disabled
  @DisplayName("Sinks.one 은 데이터를 한번만 발생시킨다. 결론")
  public void test_SinksOne3() {
    /*
     * Sinks.one() 으로 생성한 경우 데이터를 emit 하고 나면 종료 된다.
     * 그렇기 때문에 두번째 emit으로 데이터를 전송 할 수 없다.
     *
     * emit 을 여러번 해야 한다면 Sinks.many() 를 사용해야 한다.
     */
  }

  @Test
  @DisplayName("Sinks.many multicast 테스트 ")
  public void test_SinksMany_multicast() {
    Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    Flux<String> flux = sink.asFlux();

    //Sinks.many 를 사용하면 여러개의 메세지를 모두 발생 시킨다.
    sink.tryEmitNext("Hello, World!");
    sink.tryEmitNext("Hello, World!!");
    sink.tryEmitNext("Hello, World!!!");

    // 데이터 수신
    flux.subscribe(
      value -> System.out.println("Received: " + value),
      error -> System.err.println("Error: " + error),
      () -> System.out.println("Completed")
    );
  }

  @Test
  @DisplayName("Sinks.many unicast 테스트")
  public void test_SinksMany_unicast() {
    Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    Flux<String> flux = sink.asFlux();

    //Sinks.many 를 사용하면 여러개의 메세지를 모두 발생 시킨다.
    sink.tryEmitNext("Hello, World!");
    sink.tryEmitNext("Hello, World!!");
    sink.tryEmitNext("Hello, World!!!");

    // 데이터 수신
    flux.subscribe(
      value -> System.out.println("Received: " + value),
      error -> System.err.println("Error: " + error),
      () -> System.out.println("Completed")
    );
  }

  @Test
  @DisplayName("Sinks.many multicast 테스트2")
  public void test_SinksMany_multicast2() {
    Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    Flux<String> flux = sink.asFlux();

    //multicast 의 경우 다수의 구독자에 대해 정확히 데이터를 전송한다.
    flux.subscribe(value -> System.out.println("Subscriber 1: " + value));
    flux.subscribe(value -> System.out.println("Subscriber 2: " + value));

    sink.tryEmitNext("Hello, Multicast Sink!");
    sink.tryEmitNext("Hello, Multicast Sink!!");
  }

  @Test
  @DisplayName("Sinks.many unicast 테스트2")
  public void test_SinksMany_unicast2() {
    Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
    Flux<String> flux = sink.asFlux();

    flux.subscribe(value -> System.out.println("Subscriber 1: " + value));
    /* unicast 의 경우 두번째 구독을 하게 되면 오류가 발생한다.
     * reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException: UnicastProcessor allows only a single Subscriber
     * UnicastProcessor 는 하나의 구독자만 허용하기 때문이다.
     */
    flux.subscribe(value -> System.out.println("Subscriber 2: " + value));

    sink.tryEmitNext("Hello, unicast Sink!");
    sink.tryEmitNext("Hello, unicast Sink!!");
  }

  @Test
  @Disabled
  @DisplayName("Sinks.many multicast 와 unicast 에 대한 결론")
  public void test_SinksMany() {
    /*
     * 구독자가 1명인 경우에는 multicast 와 unicast 모두 데이터를 발생(emit) 시켜
     * 전송하는 것에 문제가 없다.
     *
     * 하지만 구독자가 2명 이상부터는 multicast 를 사용해야 동일한 데이터를 다수의
     * 구독자에게 전송 할 수 있다.
     *
     * 즉, unicast 는 구독자가 2명 이상인 경우에는 사용 할 수 없다.
     */
  }
}
