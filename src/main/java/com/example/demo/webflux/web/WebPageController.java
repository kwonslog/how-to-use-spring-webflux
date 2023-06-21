package com.example.demo.webflux.web;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebPageController {

  @Bean
  public RouterFunction<ServerResponse> webPageIndex() {
    RouterFunction<ServerResponse> route = RouterFunctions
      .route()
      .GET(
        "/page/index",
        request ->
          ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index2", Map.of("message", "hello, world!"))
      )
      .build();

    return route;
  }
}
