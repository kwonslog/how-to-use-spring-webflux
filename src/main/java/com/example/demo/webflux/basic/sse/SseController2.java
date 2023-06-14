package com.example.demo.webflux.basic.sse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
public class SseController2 {

  private final Map<String, Sinks.Many<String>> sinkMap = new ConcurrentHashMap<>();

  /*
   * 클라이언트는 이 주소로 연결 한다.
   */
  @GetMapping("/connect/{clientId}")
  public Flux<ServerSentEvent<String>> connectClient(@PathVariable String clientId) {
    log.debug("/connectClient start!");

    Sinks.Many<String> sink = createSink(clientId);
    return sink.asFlux().map(e -> ServerSentEvent.<String>builder().data(e).build());
  }

  private Sinks.Many<String> createSink(String clientId) {
    /*
     * X.
     * 아래 코드는 clientId 와 상관없이 모든 클라이언트에게 메세지가 전송 된다.
     * 없으면 생성하고 생성한 값을 리턴
     */
    //return sinkMap.computeIfAbsent(clientId, k -> Sinks.many().multicast().directBestEffort());

    /*
     * O.
     * unicast() 를 사용하여 clientId 에 해당하는 대상에게만 메세지가 전송 된다.
     */
    return sinkMap.computeIfAbsent(clientId, k -> Sinks.many().unicast().onBackpressureBuffer());
  }

  /*
   * 이 주소를 통해 클라이언트에게 메세지를 보낸다.
   */
  @GetMapping("/send/{clientId}/{msg}")
  public void sendMessageToClient(@PathVariable String clientId, @PathVariable String msg) {
    log.debug("/ss3 publish : {}/{}", clientId, msg);

    Sinks.Many<String> sink = getClientSink(clientId);
    if (sink != null) {
      sink.tryEmitNext(msg);
    }
  }

  private Sinks.Many<String> getClientSink(String clientId) {
    return sinkMap.get(clientId);
  }

  /*
   * sinkMap 의 상태 정보 확인용도.
   */
  @GetMapping("/info/{clientId}")
  public void printClientMapInfo(@PathVariable String clientId) {
    log.debug("sinkMap size : {}", sinkMap.size());
    sinkMap.keySet().stream().forEach(key -> log.debug("sinkMap key : {}", key));
  }

  //---
  public static void main(String[] args) {
    //값을 생성하고 그 값을 리턴
    test_computeIfAbsent();
    //값을 생성하고 null을 리턴
    test_putIfAbsent();
  }

  public static void test_computeIfAbsent() {
    Map<String, String> map = new HashMap<>();

    //k1 에 값이 없으면 v1 값을 넣어주고, v1을 리턴한다.
    String result = map.computeIfAbsent(
      "k1",
      t -> {
        log.debug("t : {}", t);
        return "v1";
      }
    );

    log.debug("result1: {}", result);

    result =
      map.computeIfAbsent(
        "k1",
        t -> {
          log.debug("t : {}", t);
          return "v1";
        }
      );

    log.debug("result2: {}", result);
  }

  public static void test_putIfAbsent() {
    Map<String, String> map = new HashMap<>();

    //k1 값이 없으면 v1 값을 넣어주고, null 을 리턴한다.
    String result = map.putIfAbsent("k1", "v1");
    log.debug("result1: {}", result);

    //k1 값이 있으면 그 값을 리턴한다.
    result = map.putIfAbsent("k1", "v1");
    log.debug("result2: {}", result);

    result = map.putIfAbsent("k2", "v2");
    log.debug("result3: {}", result);

    result = map.putIfAbsent("k2", "v2");
    log.debug("result4: {}", result);
  }
}
