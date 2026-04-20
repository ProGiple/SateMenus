package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.novasparkle.lunaspring.API.util.utilities.Cache;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.menus.impl.SateMenu;
import org.satellite.dev.progiple.satemenus.menus.params.ITemplated;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@UtilityClass
public class Creator {
    public static Supplier<Collection<MenuSettingsBuilder>> COLLECTION_FACTORY = HashSet::new;
    private final Cache<UUID, Collection<MenuSettingsBuilder>> buildersCache = new Cache<>(30, TimeUnit.MINUTES, 25);

    public ITemplated build(MenuSettingsBuilder builder) {
        var config = builder.saveToConfig();
        ITemplated result = builder.generatorType().generate(config);

        builder.generatorType().save(result);
        buildersCache.toMap().forEach((u, c) -> c.remove(builder));
        return result;
    }

    public CompletableFuture<ITemplated> buildAsync(MenuSettingsBuilder builder) {
        return CompletableFuture.supplyAsync(() -> build(builder),
                runnable -> Bukkit.getScheduler().runTaskAsynchronously(SateMenus.getInstance(), runnable)
        );
    }

    public MenuSettingsBuilder startCreating(String id, GeneratorType generatorType) {
        ITemplated templated = Menus.getSettings(id);
        if (templated == null) templated = Templates.getTemplate(id);
        return templated != null ? new MenuSettingsBuilder(templated) : new MenuSettingsBuilder(id, generatorType);
    }

    public MenuSettingsBuilder startCreating(String id, GeneratorType type, UUID target) {
        MenuSettingsBuilder builder = null;

        var list = buildersCache.getIfPresent(target);
        if (list != null)
            builder = Utils.find(list, s -> s.id().equals(id) && s.generatorType() == type).orElse(null);

        if (builder == null) builder = startCreating(id, type);
        cache(target, builder);
        return builder;
    }

    public void cache(UUID uuid, MenuSettingsBuilder builder) {
        var list = buildersCache.getIfPresent(uuid);
        if (list == null) list = COLLECTION_FACTORY.get();

        list.add(builder);
        buildersCache.put(uuid, list);
    }
}
