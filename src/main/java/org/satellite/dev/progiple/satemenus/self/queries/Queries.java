package org.satellite.dev.progiple.satemenus.self.queries;

import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.util.utilities.Cache;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class Queries {
    private final Cache<UUID, Query<?>> cache = new Cache<>(300, TimeUnit.SECONDS);

    public void register(UUID uuid, Query<?> query) {
        cache.put(uuid, query);
    }

    public void unregister(UUID uuid) {
        cache.remove(uuid);
    }

    public void unregister(Query<?> query) {
        List<UUID> keysForRemoval = cache.toMap().entrySet()
                .stream()
                .filter(e -> e.getValue().equals(query))
                .map(Map.Entry::getKey)
                .toList();
        keysForRemoval.forEach(cache::remove);
    }

    public <E extends Query<E>> E getQuery(Class<E> clazz, UUID uuid) {
        Query<?> query = cache.getIfPresent(uuid);
        if (query == null || !clazz.isAssignableFrom(query.getClass()))
            return null;
        return (E) query;
    }
}
