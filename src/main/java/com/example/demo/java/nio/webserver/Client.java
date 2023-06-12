package com.example.demo.java.nio.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

  static final Charset charset = StandardCharsets.UTF_8;
  static final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

  /**
   * HTTP 프로토콜 GET 형식으로 Server 로 요청하고 응답값을 받는다.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.configureBlocking(false);

    // 서버에 비동기로 연결을 시도합니다.
    socketChannel.connect(new InetSocketAddress("localhost", 8080));

    Selector selector = Selector.open();
    // OP_CONNECT 이벤트를 등록하여 연결 준비 상태를 감지하도록 합니다.
    socketChannel.register(selector, SelectionKey.OP_CONNECT);

    while (socketChannel.isOpen()) {
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

      while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();

        if (key.isConnectable()) {
          SocketChannel client = (SocketChannel) key.channel();

          // 연결이 완료되었는지 확인합니다.
          if (client.isConnectionPending()) {
            client.finishConnect();
            System.out.println("Connection Established");
          }

          client.register(selector, SelectionKey.OP_WRITE);

          ByteBuffer buffer = ByteBuffer.allocate(256);

          String test =
            "GET /index.html HTTP/1.1\r\n" +
            "Host: www.example.com\r\n" +
            "User-Agent: curl/7.43.0\r\n" +
            "Accept: */*\r\n" +
            "\r\n";
          buffer.put(test.getBytes(charset));
          buffer.flip();
          client.write(buffer);

          //해당 채널이 감지하는 이벤트를 OP_READ 로 변경한다.
          key.interestOps(SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          SocketChannel client = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(1024);
          int bytesRead = client.read(buffer);

          if (bytesRead == -1) {
            client.close();
            key.cancel();
            return;
          }

          buffer.flip();
          byte[] requestBytes = new byte[bytesRead];
          buffer.get(requestBytes);
          String result = new String(requestBytes);
          log.debug("result: {}", result);

          client.close();
        }

        keyIterator.remove();
      }
    }
  }
}
