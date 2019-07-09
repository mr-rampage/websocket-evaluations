package ca.wbac.vertx.websocket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class ManagementVerticle extends AbstractVerticle {
  @Override
  public void start() {
    EventBus eventBus = vertx.eventBus();

    eventBus.consumer("Data Synchronization", event -> eventBus.publish("Management Data", "Here's the data!"));
  }
}
