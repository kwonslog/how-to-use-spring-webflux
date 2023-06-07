package com.example.demo.hotcold.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

@Slf4j
public class SequenceExample {

  public static void main(String[] args) throws InterruptedException {
    // cold();
    // hot();
    //hot2();
    // hot3();
    // hot4();
    // hot5();
    hot6();
  }

  public static void cold() {
    Flux<Integer> source = Flux
      .range(1, 3)
      .doOnSubscribe(s -> log.debug("subscribed to source"));

    source.subscribe(result -> log.debug("result1: {}", result));
    source.subscribe(result -> log.debug("result2: {}", result));
    // 11:33:40.750 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 11:33:40.751 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 11:33:40.753 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 11:33:40.753 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 11:33:40.755 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 11:33:40.755 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 11:33:40.755 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 11:33:40.756 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
  }

  /*
   * cache() 를 사용하여 cold -> hot 으로 변경.
   */
  public static void hot() {
    Flux<Integer> source = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      //cache 개수 지정 가능.
      .cache();

    source.subscribe(result -> log.debug("result1: {}", result));
    source.subscribe(result -> log.debug("result2: {}", result));
    // 11:35:00.855 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 11:35:00.856 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 11:35:00.859 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 11:35:00.860 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 11:35:00.860 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 4
    // 11:35:00.861 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 5
    // 11:35:00.864 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 11:35:00.864 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 11:35:00.864 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
    // 11:35:00.865 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 4
    // 11:35:00.865 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 5
  }

  /*
   * replay(), connect() 를 사용하여 cold -> hot 으로 변경.
   */
  public static void hot2() {
    ConnectableFlux<Integer> replay = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      .replay();

    replay.subscribe(result -> log.debug("result1: {}", result));
    replay.subscribe(result -> log.debug("result2: {}", result));
    //구독 된 상태라도 connect() 호출을 해야 데이터 방출이 시작된다.
    replay.connect();
    // 11:33:12.903 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 11:33:12.904 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 11:33:12.906 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 11:33:12.906 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 11:33:12.907 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 11:33:12.907 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 11:33:12.907 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
    // 11:33:12.908 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 4
    // 11:33:12.908 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 4
    // 11:33:12.909 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 5
    // 11:33:12.909 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 5
  }

  /*
   * replay(), autoConnect() 를 사용하여 cold -> hot 으로 변경.
   */
  public static void hot3() {
    ConnectableFlux<Integer> replay = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      .replay();

    //구독이 발생하면 자동으로 데이터 방출을 시작한다.
    Flux<Integer> source = replay.autoConnect();

    source.subscribe(result -> log.debug("result1: {}", result));
    source.subscribe(result -> log.debug("result2: {}", result));
    // 11:44:00.336 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 11:44:00.336 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 11:44:00.339 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 11:44:00.339 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 11:44:00.339 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 4
    // 11:44:00.340 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 5
    // 11:44:00.341 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 11:44:00.342 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 11:44:00.342 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
    // 11:44:00.343 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 4
    // 11:44:00.343 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 5
  }

  /*
   * publish(), connect() 를 사용하여 cold -> hot 으로 변경.
   */
  public static void hot4() {
    ConnectableFlux<Integer> publish = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      .publish();
    publish.subscribe(result -> log.debug("result1: {}", result));
    publish.subscribe(result -> log.debug("result2: {}", result));
    publish.connect();
    // 16:40:36.627 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 16:40:36.632 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 16:40:36.634 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 16:40:36.635 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 16:40:36.635 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 16:40:36.635 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 16:40:36.636 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
    // 16:40:36.636 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 4
    // 16:40:36.637 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 4
    // 16:40:36.638 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 5
    // 16:40:36.638 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 5

    //publish.connect() 와 동일한 결과를 출력한다.
    // Flux<Integer> source = publish.autoConnect(2);
    // source.subscribe(result -> log.debug("result1: {}", result));
    // source.subscribe(result -> log.debug("result2: {}", result));
  }

  /**
   * publish(), refCount(2) 를 사용하여 cold -> hot 으로 변경.
   */
  public static void hot5() {
    Flux<Integer> source = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      .publish()
      //최소 구독자 개수에 따라 출력되는 로그가 달라 진다.
      .refCount(2);
    //.refCount();

    source.subscribe(result -> log.debug("result1: {}", result));
    source.subscribe(result -> log.debug("result2: {}", result));
    // 16:41:06.552 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - subscribed to source
    // 16:41:06.558 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 1
    // 16:41:06.561 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 1
    // 16:41:06.561 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 2
    // 16:41:06.562 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 2
    // 16:41:06.562 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 3
    // 16:41:06.562 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 3
    // 16:41:06.563 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 4
    // 16:41:06.569 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 4
    // 16:41:06.570 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result1: 5
    // 16:41:06.571 [main] DEBUG com.example.demo.hotcold.sequence.SequenceExample - result2: 5
  }

  public static void hot6() {
    Flux<Integer> source = Flux
      .range(1, 5)
      .doOnSubscribe(s -> log.debug("subscribed to source"))
      .share();
    source.subscribe(result -> log.debug("result1: {}", result));
    source.subscribe(result -> log.debug("result2: {}", result));
    //share() 의 동작은 publish().refCount() 와 같다.
    // Flux<Integer> source2 = Flux
    //   .range(1, 5)
    //   .doOnSubscribe(s -> log.debug("subscribed to source"))
    //   .publish()
    //   .refCount();

    // source2.subscribe(result -> log.debug("result1: {}", result));
    // source2.subscribe(result -> log.debug("result2: {}", result));
  }
}
