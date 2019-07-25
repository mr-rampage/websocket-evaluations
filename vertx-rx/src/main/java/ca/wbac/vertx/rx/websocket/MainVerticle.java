package ca.wbac.vertx.rx.websocket;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.vertx.reactivex.core.Promise;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.ServerWebSocket;

import java.util.AbstractMap;
import java.util.concurrent.TimeUnit;

public class MainVerticle extends AbstractVerticle {

  private String counterVerticleId;

  @Override
  public Completable rxStart() {
    HttpServer server = vertx.createHttpServer();
    Flowable<ServerWebSocket> authenticatedSocket$ = server
      .websocketStream()
      .toFlowable()
      .doOnNext(ws -> {
        Promise<Integer> promise = Promise.promise();
        ws.setHandshake(promise);
        promise.complete(101);
      });

    vertx.deployVerticle(new CounterVerticle(authenticatedSocket$), res -> counterVerticleId = res.result());

    return server.rxListen(8080).ignoreElement();
  }

  @Override
  public void stop() {
    vertx.undeploy(counterVerticleId);
  }
}
