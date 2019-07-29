package ca.wbac.vertx.rx.websocket;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.vertx.reactivex.core.http.ServerWebSocket;
import io.vertx.reactivex.core.AbstractVerticle;

import java.util.AbstractMap;
import java.util.concurrent.TimeUnit;

public class CounterVerticle extends AbstractVerticle {
  private final Flowable<ServerWebSocket> authenticatedSocket$;
  private Disposable socketCounter;

  CounterVerticle(final Flowable<ServerWebSocket> authenticatedSocket$) {
    this.authenticatedSocket$ = authenticatedSocket$;
  }

  @Override
  public Completable rxStart() {
    Flowable<Long> interval = Flowable.interval(1, TimeUnit.SECONDS);

    Flowable<AbstractMap.SimpleEntry<ServerWebSocket, Long>> intervalProducer = authenticatedSocket$
      .flatMap(ws -> interval.map(count -> new AbstractMap.SimpleEntry<>(ws, count)))
      .filter(pair -> !pair.getKey().isClosed());

    socketCounter = intervalProducer.subscribe(pair -> pair.getKey().writeTextMessage(pair.getValue().toString()));

    return Completable.complete();
  }

  @Override
  public void stop() {
    socketCounter.dispose();
  }
}
