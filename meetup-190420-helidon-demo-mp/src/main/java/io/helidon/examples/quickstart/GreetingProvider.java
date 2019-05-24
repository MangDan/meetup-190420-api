package io.helidon.examples.quickstart;

import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GreetingProvider {
    private final AtomicReference<String> message = new AtomicReference<>();

    @Inject
    public GreetingProvider(@ConfigProperty(name = "app.greeting") String message) {
        this.message.set(message);
    }

    String getMessage() {
        return message.get();
    }

    void setMessage(String message) {
        this.message.set(message);
    }
}
