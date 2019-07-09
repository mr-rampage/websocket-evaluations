package ca.wbac.vertx.websocket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class DataSynchronizationVerticle extends AbstractVerticle {
  @Override
  public void start() {
    EventBus eventBus = vertx.eventBus();

    vertx.setPeriodic(1000, event -> eventBus.publish("Data Synchronization", "Give me data!"));
    eventBus.consumer("Management Data", event -> System.out.println("Data Synchronization got Management Data"));
  }
}
