package ca.wbac.vertx.websocket;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer()
      .websocketHandler(ws -> ws.handler(event -> ws.writeTextMessage("Hello, from the server!")))
      .listen(8080);
  }
}
