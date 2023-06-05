package com.example.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestCompletableFuture {

  /*
   * 알고가자.
   *
   * 동기 : 하나의 작업이 끝날때까지 기다렸다가 다음 작업을 실행하는 것.
   *
   * 비동기 : 하나의 작업이 끝날때까지 기다리지 않고 다음 작업을 실행하는 것.
   *
   * 블록킹 : 하나의 작업이 완료 될때까지 대기하는 동작.
   *
   * 논블록킹 : 하나의 작업을 실행하고 또다른 작업을 실행 하는 동작.
   *
   */
  public static void main(String[] args)
    throws InterruptedException, ExecutionException {
    //CompletableFuture 비동기 처리를 위한 기능을 제공한다.

    print("\n case 1 ---");
    case1();
    print("\n case 2 ---");
    case2();
    print("\n case 3 ---");
    case3();
    print("\n case 4 ---");
    case4();
    print("\n case 5 ---");
    case5();
    print("\n case 6 ---");
    case6();
    print("\n case 7 ---");
    case7();
    print("\n case 8 ---");
    case8();
    print("\n case 9 ---");
    case9();
    print("\n case 10 ---");
    case10();
  }

  public static void case1() {
    //기본
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello world";
      }
    );

    String result = supplyAsync.join();
    print(result);
  }

  public static void case2() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에";
      }
    );

    //supplyAsync 결과값을 받아서 추가적인 처리.
    //String result = supplyAsync.thenApply(value -> value + " world").get();
    //예제코드와 같이 단순히 결과값이 필요한 경우 join 메소드를 사용하는 것이 더 편리함.
    String result = supplyAsync.thenApply(value -> value + " world").join();

    print(result);
  }

  public static void case3() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에 바로";
      }
    );

    //supplyAsync 결과값을 받아서 소모함.
    supplyAsync.thenAccept(value -> print(value + " world"));
  }

  public static void case4() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에";
      }
    );

    //supplyAsync 결과값을 받아서 추가적인 처리를 여러개 붙일수 있다.
    //체이닝된 thenApply 메소드는 동기적으로 호출 된다.
    print("main thread: " + Thread.currentThread().getName());
    String result = supplyAsync
      .thenApply(value -> {
        print("async thread1: " + Thread.currentThread().getName());
        return value + " world";
      })
      .thenApply(value -> {
        print("async thread2: " + Thread.currentThread().getName());
        return value + " 1";
      })
      .thenApply(value -> {
        print("async thread3: " + Thread.currentThread().getName());
        return value + " 2";
      })
      .thenApply(value -> {
        print("async thread4: " + Thread.currentThread().getName());
        return value + " 3";
      })
      .join();

    print(result);
  }

  public static void case5() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에";
      }
    );

    print("main thread: " + Thread.currentThread().getName());
    String result = supplyAsync
      .thenCompose(value ->
        CompletableFuture.supplyAsync(() -> {
          print("async thread1: " + Thread.currentThread().getName());
          return value + " world";
        })
      )
      .thenCompose(value ->
        CompletableFuture.supplyAsync(() -> {
          print("async thread2: " + Thread.currentThread().getName());
          return value + " 1";
        })
      )
      .thenCompose(value ->
        CompletableFuture.supplyAsync(() -> {
          print("async thread3: " + Thread.currentThread().getName());
          return value + " 2";
        })
      )
      .thenCompose(value ->
        CompletableFuture.supplyAsync(() -> {
          print("async thread4: " + Thread.currentThread().getName());
          return value + " 3";
        })
      )
      .join();

    print(result);
  }

  public static void case6() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에";
      }
    );

    //supplyAsync 결과값을 받아서 추가적인 처리를 여러개 붙일수 있다.
    //체이닝된 thenApplyAsync 메소드는 비동기적으로 호출 된다.

    print("main thread: " + Thread.currentThread().getName());
    String result = supplyAsync
      .thenApplyAsync(value -> {
        print("async thread1: " + Thread.currentThread().getName());
        return value + " world";
      })
      .thenApplyAsync(value -> {
        print("async thread2: " + Thread.currentThread().getName());
        return value + " 1";
      })
      .thenApplyAsync(value -> {
        print("async thread3: " + Thread.currentThread().getName());
        return value + " 2";
      })
      .thenApplyAsync(value -> {
        print("async thread4: " + Thread.currentThread().getName());
        return value + " 3";
      })
      .join();

    print(result);
  }

  public static void case7() {
    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        return "hello 뒤에";
      }
    );

    //thenApply 와 thenCompose 의 기능적인 차이가 없는것 처럼 보여서 비교해 본다.
    CompletableFuture<CompletableFuture<String>> thenApply = supplyAsync.thenApply(value -> {
        //return value + " world";
        return CompletableFuture.supplyAsync(() -> {
          return value + " world";
        });
      }
    );

    CompletableFuture<String> thenCompose = supplyAsync.thenCompose(value -> {
      return CompletableFuture.supplyAsync(() -> {
        return value + " world";
      });
    });

    //같은 기능을 하는 코드이지만 thenCompose 를 사용하는 것이 한꺼풀 덜어진다.(flatmap 처럼)
    //thenApply 는 결과값에 대한 가공처리 할때 사용하는 것이 좋을 것 같다.
    //theCompose 는 결과값을 가지고 API 요청과 같은 추가적인 프로세스를 처리해야 할때 좋을 것 같다.
    String result1 = thenApply.join().join();
    String result2 = thenCompose.join();

    print(result1);
    print(result2);
  }

  public static void case8() {
    print("main thread: " + Thread.currentThread().getName());

    CompletableFuture<String> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
        print("async1 thread : " + Thread.currentThread().getName());

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        return "hello 뒤에";
      }
    );

    CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
        print("async2 thread : " + Thread.currentThread().getName());

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        return " world";
      }
    );

    CompletableFuture<String> result = supplyAsync1.thenCombine(
      supplyAsync2,
      (v1, v2) -> v1 + v2
    );

    print("--요기 나오나?");

    print(result.join());
  }

  public static void case9() {
    print("main thread: " + Thread.currentThread().getName());

    CompletableFuture<String> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
        print("async1 thread : " + Thread.currentThread().getName());

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        return "hello 뒤에";
      }
    );

    CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> {
        print("async2 thread : " + Thread.currentThread().getName());

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        return " world";
      }
    );

    CompletableFuture<String> result = supplyAsync1.thenCombineAsync(
      supplyAsync2,
      (v1, v2) -> v1 + v2
    );

    print("--요기 나오나?");

    print(result.join());
  }

  public static void case10() {
    print("main thread: " + Thread.currentThread().getName());

    CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
        throw new RuntimeException("예외발생");
      }
    );

    String result = supplyAsync
      .exceptionally(ex -> {
        ex.printStackTrace();
        return "예외발생";
      })
      .join();

    print(result);
  }

  public static void print(Object o) {
    System.out.println(o);
  }
}
