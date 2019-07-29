package ca.wbac.vertx.rx.websocket;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Promise;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.ServerWebSocket;

public class MainVerticle extends AbstractVerticle {

  private CounterVerticle counterVerticle;
  private HttpServer server;

  @Override
  public Completable rxStart() {
    server = vertx.createHttpServer();
    Flowable<ServerWebSocket> authenticatedSocket$ = server
      .websocketStream()
      .toFlowable()
      .doOnNext(ws -> {
        Promise<Integer> promise = Promise.promise();
        ws.setHandshake(promise);
        promise.complete(101);
      });

    counterVerticle = new CounterVerticle(authenticatedSocket$);

    return vertx.rxDeployVerticle(counterVerticle)
      .flatMap(pid -> server.rxListen(8080))
      .ignoreElement();
  }

  @Override
  public void stop() {
    vertx.undeploy(counterVerticle.deploymentID());
    server.close();
  }
}
