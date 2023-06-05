package com.example.demo.java.flow.api;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublisherExample implements Publisher<String> {

  private Subscriber<? super String> subscriber;

  @Override
  public void subscribe(Subscriber<? super String> subscriber) {
    log.debug("1. subscribe - subscriber: " + subscriber);
    this.subscriber = subscriber;
  }

  public void publishData(String data) {
    subscriber.onNext(data);
  }

  public void complete() {
    subscriber.onComplete();
  }

  public static void main(String[] args) {
    Subscriber<String> subscriber = new Subscriber<String>() {
      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        log.debug("2. onSubscribe - subscription: " + subscription);
        /* 아래 request 메소드를 주석처리 하여도 데이터 생산과 소비는 실행된다.
         * > 왜?
         *   request 메소드를 호출하는 것은 backpressure 를 이용하여 데이터를 컨트롤 한다는 의미이다.
         *   즉, request 메소드를 호출하지 않더라도 데이터의 생산과 소비는 이루어진다.
         *   단, 이것을 컨트롤(구독자가 원하는 만큼의 데이터를) 하지 않는다는 것이다.
         */
        subscription.request(1);
      }

      @Override
      public void onNext(String item) {
        log.debug("4. onNext - receive: " + item);
        //subscription.request(1);
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onComplete() {
        log.debug("5. onComplete - complete");
      }
    };

    //publisher 는 데이터 스트림을 생성한다.
    PublisherExample publisherExample = new PublisherExample();

    //데이터 스트림을 구독한다.
    publisherExample.subscribe(subscriber);

    //onSubscribe 를 호출하여 구독을 시작한다.
    // subscriber.onSubscribe(
    //   new Subscription() {
    //     @Override
    //     public void request(long n) {
    //       log.debug("3. request - n: " + n);
    //     }

    //     @Override
    //     public void cancel() {}
    //   }
    // );

    publisherExample.publishData("hello");
    publisherExample.publishData("world");
    publisherExample.publishData("!");
    publisherExample.complete();
  }
}
