package org.satellite.dev.progiple.satemenus.self.queries.impl;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.queries.Queries;
import org.satellite.dev.progiple.satemenus.self.queries.Query;

import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public class InputTextQuery implements Query<String> {
    public static final Set<String> CANCEL_TRIGGERS = Set.of("cancel", "exit", "выход", "отмена");

    private final MenuSettingsBuilder builder;
    private final UUID uuid;
    private BiConsumer<InputTextQuery, String> accept;
    private Consumer<InputTextQuery> cancel;
    private boolean async = false;
    public InputTextQuery(Player target, MenuSettingsBuilder builder) {
        this.builder = builder;
        this.uuid = target.getUniqueId();
    }

    @Override
    public boolean request(String string) {
        if (async) process(string);
        else Bukkit.getScheduler().runTask(SateMenus.getInstance(), () -> process(string));

        Queries.unregister(uuid);
        return true;
    }

    protected void process(String string) {
        if (this.accept != null && string != null && !CANCEL_TRIGGERS.contains(string.toLowerCase())) {
            this.accept.accept(this, string);
        }
        else if (this.cancel != null) {
            this.cancel.accept(this);
        }
    }

    public InputTextQuery thenAccept(BiConsumer<InputTextQuery, String> accept) {
        this.accept = accept;
        return this;
    }

    public InputTextQuery thenCancel(Consumer<InputTextQuery> cancel) {
        this.cancel = cancel;
        return this;
    }

    public InputTextQuery async(boolean async) {
        this.async = async;
        return this;
    }
}
