package com.example.demo.webflux.basic.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class EchoHandler implements WebSocketHandler {

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    //클라이언트가 보낸 값을 확인하고 결과값을 리턴.
    // return session.send(
    //   session.receive().map(msg -> handleTextMessage(msg, session))
    // );

    //클라이언트가 보낸 값만 확인.
    // return session
    //   .receive()
    //   .doOnNext(wsm -> log.debug("doOnNext : {}", wsm.getPayloadAsText()))
    //   .then();

    return session.send(
      session
        .receive()
        .doOnNext(wsm -> log.debug("doOnNext : {}", wsm.getPayloadAsText()))
        .map(wsm -> session.textMessage("Echo " + wsm.getPayloadAsText()))
    );
  }
  //   private WebSocketMessage handleTextMessage(
  //     WebSocketMessage message,
  //     WebSocketSession session
  //   ) {
  //     String payload = message.getPayloadAsText();
  //     String[] parts = payload.split(":");
  //     if (parts.length < 2) {
  //       throw new IllegalArgumentException("Invalid message format");
  //     }

  //     String messageType = parts[0];
  //     String messageContent = parts[1];

  //     switch (messageType) {
  //       case "greeting":
  //         return session.textMessage("Hello, " + messageContent);
  //       case "farewell":
  //         return session.textMessage("Goodbye, " + messageContent);
  //       default:
  //         throw new IllegalArgumentException(
  //           "Unknown message type: " + messageType
  //         );
  //     }
  //   }
}
