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
import java.util.Iterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Draft {

  public static void main(String[] args) {
    Server server = new Server();

    try {
      server.init();
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

@Slf4j
class Server {

  private Selector selector;
  private ServerSocketChannel serverSocketChannel;

  private final Charset charset = StandardCharsets.UTF_8;
  private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

  public void init() throws IOException {
    this.serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
    serverSocketChannel.configureBlocking(false);

    this.selector = Selector.open();
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void start() throws IOException {
    while (serverSocketChannel.isOpen()) {
      this.selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();

        if (!key.isValid()) {
          continue;
        }

        if (key.isAcceptable()) {
          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          SocketChannel client = server.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          SocketChannel client = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(1024);
          int numRead = client.read(buffer);
          log.debug("numRead: {}", numRead);

          if (numRead == -1) {
            client.close();
            key.cancel();
            return;
          }

          String response = "HTTP/1.1 200 OK\r\n\r\nHello World!";
          buffer.clear();
          buffer.put(response.getBytes(charset));
          buffer.flip();

          key.attach(buffer);
          key.interestOps(SelectionKey.OP_WRITE);
        } else if (key.isWritable()) {
          SocketChannel client = (SocketChannel) key.channel();
          log.debug("client: {}", client);

          ByteBuffer buffer = (ByteBuffer) key.attachment();

          log.debug("buffer: {}", buffer);
          client.write(buffer);
          if (!buffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
          }
          buffer.compact();
        }
      }
    }
  }
}
