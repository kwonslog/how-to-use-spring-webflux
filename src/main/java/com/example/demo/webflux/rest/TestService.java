package com.example.demo.webflux.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

  public String getData() {
    return "OK";
  }
}
