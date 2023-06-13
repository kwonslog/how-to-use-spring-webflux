package com.example.demo.webflux.basic.router;

import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RouterFunctions.Visitor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/*
 * 라우터의 역할은 요청을 받아서 어떤 핸들러가 처리 할지 연결해 주는 것.
 */
@Slf4j
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

  @Bean
  public RouterFunction<ServerResponse> testRoute(
    WelcomeHandler welcomeHandler
  ) {
    return RouterFunctions.route(
      RequestPredicates.GET("/welcome3"),
      welcomeHandler::welcome3
    );
  }

  @Bean
  public RouterFunction<ServerResponse> testNest(
    WelcomeHandler welcomeHandler
  ) {
    /**
     * /test/welcome4
     * /test/welcome5
     *
     * 이렇게 앞에 공통으로 사용할 주소를 지정 가능.
     *
     */
    return RouterFunctions.nest(
      RequestPredicates.path("/test"),
      RouterFunctions
        .route()
        .GET("/welcome4", welcomeHandler::welcome4)
        .GET("/welcome5", welcomeHandler::welcome5)
        .build()
    );
  }

  @Bean
  public RouterFunction<ServerResponse> testResource() {
    /*
     * /static/ 경로로 시작되는 요청에 대한 정적 리소스를 제공한다.
     */
    return RouterFunctions.resources(
      "/static/**",
      new ClassPathResource("/static/")
    );
  }

  @Bean
  public RouterFunction<ServerResponse> testDirectHandler() {
    return RouterFunctions
      .route()
      .GET(
        "/welcome6",
        request ->
          ServerResponse
            .ok()
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue("welcome6!!")
      )
      .build();
  }

  @Bean
  public RouterFunction<ServerResponse> testRouteAccept() {
    WelcomeHandler wh = new WelcomeHandler();

    RouterFunction<ServerResponse> route = RouterFunctions
      .route()
      .GET("/welcome7", wh::welcome7)
      .build();

    /*
     * Visitor 패턴
     * Visitor 를 등록하여 RouterFunction 에서 호출 되는 메소드를 추적 할 수 있다.
     */
    route.accept(
      new Visitor() {
        @Override
        public void startNested(RequestPredicate predicate) {
          log.debug("Visitor : startNested");
        }

        @Override
        public void endNested(RequestPredicate predicate) {
          log.debug("Visitor : endNested");
        }

        @Override
        public void route(
          RequestPredicate predicate,
          HandlerFunction<?> handlerFunction
        ) {
          log.debug("Visitor : route");
          log.debug("Visitor : {}", predicate);
          log.debug("Visitor : {}", handlerFunction);

          WelcomeHandler wh = new WelcomeHandler();

          handlerFunction = wh::welcome2;

          log.debug("Visitor : {}", handlerFunction);
        }

        @Override
        public void resources(
          Function<ServerRequest, Mono<Resource>> lookupFunction
        ) {
          log.debug("Visitor : resources");
        }

        @Override
        public void attributes(Map<String, Object> attributes) {
          log.debug("Visitor : attributes");
        }

        @Override
        public void unknown(RouterFunction<?> routerFunction) {
          log.debug("Visitor : unknown");
        }
      }
    );

    return route;
  }

  @Bean
  public RouterFunction<ServerResponse> testRouteAnd() {
    RouterFunction<ServerResponse> router1 = RouterFunctions
      .route()
      .GET("/hello", request -> ServerResponse.ok().bodyValue("Hello, World!"))
      .build();
    RouterFunction<ServerResponse> router2 = RouterFunctions
      .route()
      .GET("/greet", request -> ServerResponse.ok().bodyValue("Greetings!"))
      .build();
    RouterFunction<ServerResponse> router3 = RouterFunctions
      .route()
      .GET("/ok", request -> ServerResponse.ok().bodyValue("OK!"))
      .build();

    /*
     * 개별적으로 router 를 만들고 and() 를 사용하여 조합한다.
     * /hello, /greet, /ok 를 각각 호출해 보면 Handler가 실행 된다.
     */
    return router1.and(router2).and(router3);
  }

  @Bean
  public RouterFunction<ServerResponse> testRouteAndNest() {
    RouterFunction<ServerResponse> helloRouter = RouterFunctions
      .route()
      .GET(
        "/hello2",
        request -> ServerResponse.ok().bodyValue("Hello, World! - 2")
      )
      .build();
    RouterFunction<ServerResponse> greetRouter = RouterFunctions
      .route()
      .GET(
        "/greet2",
        request -> ServerResponse.ok().bodyValue("Greetings! - 2")
      )
      .build();

    //호출 /api/hello2
    RouterFunction<ServerResponse> nestedRouter = RouterFunctions.nest(
      RequestPredicates.path("/api"),
      helloRouter
    );

    //호출 /greet2
    //호출 /sub/api/hello2
    RouterFunction<ServerResponse> combinedRouter = greetRouter.andNest(
      RequestPredicates.path("/sub"),
      nestedRouter
    );

    /*
     * RouterFunction.nest 메소드를 사용하여 계층구조를 만들수 있다.
     * andNext 메소드를 사용하면 계층구조를 동적으로 만들수 있다.
     */

    return combinedRouter;
  }

  @Bean
  public RouterFunction<ServerResponse> testRouteAndOther() {
    RouterFunction<ServerResponse> helloRouter30 = RouterFunctions
      .route()
      .GET(
        "/hello30",
        request -> ServerResponse.ok().bodyValue("Hello, World! - 30")
      )
      .build();

    RouterFunction<ServerResponse> helloRouter31 = RouterFunctions
      .route()
      .GET(
        "/hello30",
        request -> ServerResponse.ok().bodyValue("Hello, World! - 31")
      )
      .build();

    /*
     * and 메소드와 andOther 메소드의 기능은 유사해 보인다.
     *
     * 차이점은 and 메소드로 연결하려면 리턴 타입이 같아야 한다.
     * andOther 는 다른 리턴 타입을 사용 할 수 있다.
     *
     *
     */
    helloRouter31.andOther(helloRouter30);

    return helloRouter31;
  }

  @Bean
  public RouterFunction<ServerResponse> testRouteFilter() {
    log.debug("testRouteFilter -- start");
    RouterFunction<ServerResponse> route = RouterFunctions
      .route()
      .GET("/value1", request -> ServerResponse.ok().bodyValue("value1 ok"))
      .build();

    HandlerFilterFunction<ServerResponse, ServerResponse> hff = (
      request,
      next
    ) -> {
      Mono<ServerResponse> responseMono = null;

      //요청값에 name 파라메터가 있는 경우와 없는 경우를 구분해서 처리.
      if (request.queryParam("name").isPresent()) {
        responseMono = next.handle(request);
      } else {
        responseMono = Mono.from(ServerResponse.ok().bodyValue("no named"));
      }
      return responseMono;
    };

    log.debug("testRouteFilter -- end");

    return route.filter(hff);
  }

  @Bean
  public RouterFunction<ServerResponse> testAndRoute() {
    RouterFunction<ServerResponse> route = RouterFunctions
      .route()
      .GET(
        "/androute",
        request -> ServerResponse.ok().bodyValue("androute test!")
      )
      .build();

    /*
     * 호출 /androute 와 /androute2 인 경우를 구분하여 핸들러가 실행 된다.
     */
    return route.andRoute(
      request -> request.path().equals("/androute2"),
      request -> ServerResponse.ok().bodyValue("androute2 test!")
    );
  }

  @Bean
  public RouterFunction<ServerResponse> testWithAttribute() {
    RouterFunction<ServerResponse> route = RouterFunctions
      .route()
      .GET(
        "/attr",
        request ->
          ServerResponse
            .ok()
            //attr 을 사용
            .bodyValue("attr : " + request.attribute("attr").orElse("empty"))
      )
      //attr 을 추가
      .withAttribute("attr", "attr value !")
      .build();

    return route;
  }
}
