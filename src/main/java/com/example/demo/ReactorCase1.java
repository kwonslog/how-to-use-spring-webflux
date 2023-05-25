package com.example.demo;

import reactor.core.publisher.Flux;

public class ReactorCase1 {
    public static void main(String[] args) {
        case1();
    }

    public static void case1() {
        Flux
            .just("hello", "world")
            .subscribe(System.out::println);
    }

}
