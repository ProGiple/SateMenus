package org.satellite.dev.progiple.satemenus.self.queries;

import java.util.UUID;
import java.util.function.Consumer;

@FunctionalInterface
public interface Query<E> {
    boolean request(E object);

    default void register(UUID uuid) {
        Queries.register(uuid, this);
    }
}
