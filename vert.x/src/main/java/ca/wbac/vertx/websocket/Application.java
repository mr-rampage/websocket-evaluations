package ca.wbac.vertx.websocket;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Application {
  public static void main(String[] args) {
    System.out.println("Main is running");

    VertxOptions options = new VertxOptions();

    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();

        vertx.deployVerticle(new DataSynchronizationVerticle());
        vertx.deployVerticle(new ManagementVerticle());

        vertx.deployVerticle(new MainVerticle());
      } else {
        System.out.println("Failed: " + res.cause());
      }
    });
  }
}
