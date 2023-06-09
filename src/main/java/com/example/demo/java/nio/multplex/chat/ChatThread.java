package com.example.demo.java.nio.multplex.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * 출처 : https://github.com/brewagebear/blog-example/tree/df8888d2849b2bb969cf08ab0f2802959e01a466/nio-example/src/main/java/simple_chatting_example/nio
 */

@Slf4j
public class ChatThread extends Thread {

  private static final Logger log = LogManager.getLogger(ChatThread.class);

  private SocketChannel sc = null;

  public ChatThread(SocketChannel sc) {
    this.sc = sc;
  }

  @Override
  public void run() {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    try {
      while (!Thread.currentThread().isInterrupted()) {
        buffer.clear();
        BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(System.in)
        );

        String message = bufferedReader.readLine();

        if (message.equals("quit") || message.equals("shutdown")) {
          sc.socket().close();
          sc.close();
          System.exit(0);
        }

        //쓰기 작업을 하고
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        //flip 을 호출해야만 입력한 값을 읽을 수 있음.
        buffer.flip();

        sc.write(buffer);
      }
    } catch (Exception e) {
      log.warn("run()", e);
    } finally {
      clearBuffer(buffer);
    }
  }

  private void clearBuffer(ByteBuffer buffer) {
    if (buffer != null) {
      buffer.clear();
      buffer = null;
    }
  }
}
