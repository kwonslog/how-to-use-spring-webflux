package com.example.demo.java.flow.api;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriberExample implements Subscriber<String> {

  @Override
  public void onSubscribe(Subscription subscription) {
    subscription.request(10);
  }

  @Override
  public void onNext(String item) {
    log.debug("received: {}", item);
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {
    log.debug("complete");
  }

  public static void main(String[] args) {
    // 데이터를 발행하는 Publisher 생성
    Publisher<String> publisher = new Publisher<String>() {
      @Override
      public void subscribe(Subscriber<? super String> subscriber) {
        subscriber.onSubscribe(
          new Subscription() {
            @Override
            public void request(long n) {
              // 데이터를 발행
              for (int i = 0; i < n; i++) {
                subscriber.onNext("Data " + (i + 1));
              }
              // 데이터 발행 완료
              subscriber.onComplete();
            }

            @Override
            public void cancel() {
              // 구독 취소
            }
          }
        );
      }
    };

    // Subscriber 생성 및 Publisher에 구독
    Subscriber<String> subscriber = new SubscriberExample();
    publisher.subscribe(subscriber);
  }
}
