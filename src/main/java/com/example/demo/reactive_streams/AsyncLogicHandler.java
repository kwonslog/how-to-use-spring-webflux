package com.example.demo.reactive_streams;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class AsyncLogicHandler implements Publisher<String> {

  @Override
  public void subscribe(Subscriber<? super String> s) {
    s.onSubscribe(
      new Subscription() {
        @Override
        public void request(long n) {
          String value = "홍길동";
          log.debug(
            "게시자 : 데이터 요청 시그널 request 받고, 데이터 : {} 보냄",
            value
          );

          s.onNext(value);
          s.onComplete();
        }

        @Override
        public void cancel() {
          log.debug("게시자 : 취소 시그널 cancel 받았음");
        }
      }
    );
  }

  public static void main(String[] args) {
    Subscriber<String> subscriber1 = new Subscriber<String>() {
      @Override
      public void onSubscribe(Subscription s) {
        log.debug("구독자1 : 데이터 1건을 요청 합니다.");
        s.request(1);
      }

      @Override
      public void onNext(String t) {
        log.debug("구독자1 : onNext 시그널을 받았음. 결과값 : {}", t);
      }

      @Override
      public void onError(Throwable t) {
        log.debug("구독자1 : onError 시그널을 받았음");
      }

      @Override
      public void onComplete() {
        log.debug("구독자1 : onComplete 시그널을 받았음");
      }
    };

    Subscriber<String> subscriber2 = new Subscriber<String>() {
      @Override
      public void onSubscribe(Subscription s) {
        log.debug("구독자2 : 데이터 1건을 요청 합니다.");
        s.request(1);
      }

      @Override
      public void onNext(String t) {
        log.debug("구독자2 : onNext 시그널을 받았음. 결과값 : {}", t);
      }

      @Override
      public void onError(Throwable t) {
        log.debug("구독자2 : onError 시그널을 받았음");
      }

      @Override
      public void onComplete() {
        log.debug("구독자2 : onComplete 시그널을 받았음");
      }
    };

    AsyncLogicHandler handler = new AsyncLogicHandler();
    handler.subscribe(subscriber1);
    handler.subscribe(subscriber2);
  }
}
