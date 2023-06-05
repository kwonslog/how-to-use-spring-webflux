package com.example.demo.java.flow.api;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSubmissionPublisher {

  static class Subb implements Subscriber<String> {

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
      this.subscription = subscription;
      subscription.request(1);
    }

    @Override
    public void onNext(String item) {
      log.debug("onNext : {}", item);
      subscription.request(1);
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
    SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
    Subb subb = new Subb();
    Subb subb2 = new Subb();

    publisher.subscribe(subb);
    publisher.subscribe(subb2);

    log.debug("구독자수 : {} ", publisher.getNumberOfSubscribers());

    publisher.submit("1");
    int drops = publisher.offer("너만봐", 1, TimeUnit.SECONDS, null);

    log.debug("drops : {} ", drops);
    publisher.close();

    try {
      Thread.sleep(1000);

      Thread t = Thread.currentThread(), o;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
