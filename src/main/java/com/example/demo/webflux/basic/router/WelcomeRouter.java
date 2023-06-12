package com.example.demo.webflux.basic.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/*
 * 라우터의 역할은 요청을 받아서 어떤 핸들러가 처리 할지 연결해 주는 것.
 */
@Configuration
public class WelcomeRouter {

  @Bean
  public RouterFunction<ServerResponse> welcomeRoute(
    WelcomeHandler welcomeHandler
  ) {
    // return RouterFunctions.route(
    //   RequestPredicates.GET("/welcome"),
    //   welcomeHandler::welcome
    // );
    return RouterFunctions
      .route()
      .GET("/welcome", welcomeHandler::welcome)
      .GET("/welcome2", request -> welcomeHandler.welcome2(request))
      .build();
  }

  public RouterFunction<ServerResponse> testRoute() {
    //RouterFunctions.resources(null, null)
    //RouterFunctions.nest(null, null)
    //RouterFunctions.

    return null;
  }
}
