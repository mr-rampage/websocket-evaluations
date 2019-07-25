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

  private Disposable socketCounter;

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

    Flowable<Long> interval = Flowable.interval(1, TimeUnit.SECONDS);

    socketCounter = authenticatedSocket$
      .flatMap(ws -> interval.map(count -> new AbstractMap.SimpleEntry<>(ws, count)))
      .filter(pair -> !pair.getKey().isClosed())
      .subscribe(pair -> pair.getKey().writeTextMessage(pair.getValue().toString()));

    return server.rxListen(8080).ignoreElement();
  }

  @Override
  public void stop() {
    socketCounter.dispose();
  }
}
