package com.example.demo.java.flow.api;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPubSub {

  static class Pubb implements Publisher<String> {

    @Override
    public void subscribe(Subscriber<? super String> subscriber) {
      subscriber.onSubscribe(
        new Subscription() {
          @Override
          public void request(long n) {
            for (int i = 0; i < n; i++) {
              subscriber.onNext("Data " + (i + 1));
            }
          }

          @Override
          public void cancel() {
            subscriber.onComplete();
          }
        }
      );
    }
  }

  static class Subb implements Subscriber<String> {

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
      this.subscription = subscription;
      subscription.request(5);
    }

    @Override
    public void onNext(String item) {
      log.debug("onNext : {}", item);
      //subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
      log.debug("onError : {}", throwable);
    }

    @Override
    public void onComplete() {
      log.debug("onComplete");
    }
  }

  public static void main(String[] args) {
    Pubb pubb = new Pubb();
    Subb subb = new Subb();

    pubb.subscribe(subb);
  }
}
