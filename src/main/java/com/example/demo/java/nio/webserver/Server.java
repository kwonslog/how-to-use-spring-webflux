package com.example.demo.java.nio.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

  private Selector selector;
  private ServerSocketChannel serverSocketChannel;

  private final Charset charset = StandardCharsets.UTF_8;
  private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

  /*
   * 브라우저를 통해 localhost:8080 으로 접속하면 Hello World 를 리턴한다.
   */
  public static void main(String[] args) {
    Server server = new Server();

    try {
      server.init();
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void init() throws IOException {
    this.serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

    //논블록킹 처리 하도록 설정
    serverSocketChannel.configureBlocking(false);

    this.selector = Selector.open();

    /* 클라이언트 연결에 대한 이벤트 감지 설정
     *  selector 를 serverSocketChannel 에 등록한다.
     *  이때 selector 가 감지해야 하는 이벤트(OP_ACCEPT)를 지정 한다.
     */
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void start() throws IOException {
    while (serverSocketChannel.isOpen()) {
      //블록킹
      int selectCount = this.selector.select();
      //int selectCount = this.selector.select(TimeUnit.SECONDS.toMillis(1));

      //논블록킹
      //int selectCount = this.selector.selectNow();

      log.debug("selectCount: {}", selectCount);

      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();

        if (key.isAcceptable()) {
          log.debug("isAcceptable ---");
          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          SocketChannel client = server.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          log.debug("isReadable ---");
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
          String httpRequest = new String(requestBytes);

          // split into lines
          String[] lines = httpRequest.split("\r\n");

          // request line
          String requestLine = lines[0];
          String[] requestParts = requestLine.split(" ");
          String method = requestParts[0]; // GET, POST, etc.
          String uri = requestParts[1]; // the requested URI
          String version = requestParts[2]; // HTTP version

          log.debug("method: {}", method);
          log.debug("uri: {}", uri);
          log.debug("version: {}", version);

          // headers
          Map<String, String> headers = new HashMap<>();
          for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty()) {
              break; // end of headers
            }
            int colonPos = line.indexOf(':');
            String headerName = line.substring(0, colonPos).trim();
            String headerValue = line.substring(colonPos + 1).trim();
            headers.put(headerName, headerValue);
            log.debug("{} : {}", headerName, headerValue);
          }
          // TODO: handle the HTTP request

          key.interestOps(SelectionKey.OP_WRITE);
        } else if (key.isWritable()) {
          log.debug("isWritable ---");
          SocketChannel client = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(256);

          buffer.put("HTTP/1.1 200 OK\r\n\r\nHello, World!".getBytes(charset));
          buffer.flip();
          client.write(buffer);
          client.close();
        }
      }
    }
  }
}
