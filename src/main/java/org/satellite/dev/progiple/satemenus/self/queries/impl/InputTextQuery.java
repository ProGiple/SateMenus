package org.satellite.dev.progiple.satemenus.self.queries.impl;

import org.bukkit.entity.Player;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.queries.Query;

import java.util.UUID;
import java.util.function.BiConsumer;

public class InputTextQuery implements Query<String> {
    private final MenuSettingsBuilder builder;
    private final UUID uuid;
    private final BiConsumer<InputTextQuery, String> consumer;
    public InputTextQuery(Player target, MenuSettingsBuilder builder, BiConsumer<InputTextQuery, String> consumer) {
        this.builder = builder;
        this.uuid = target.getUniqueId();
        this.consumer = consumer;
    }

    @Override
    public void request(String string) {
        this.consumer.accept(this, string);
    }
}
