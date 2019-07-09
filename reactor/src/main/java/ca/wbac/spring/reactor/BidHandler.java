package ca.wbac.spring.reactor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class BidHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(
            webSocketSession.receive()
                .map(message -> "Hello, from Spring Reactor")
                .map(webSocketSession::textMessage)
        );
    }
}
