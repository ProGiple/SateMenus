package org.satellite.dev.progiple.satemenus.utils;

import org.novasparkle.lunaspring.API.events.CooldownPrevent;
import org.satellite.dev.progiple.satemenus.menus.items.SMItem;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SateCache extends CooldownPrevent<String> {
    public SateCache(int ticks) {
        super(ticks * 50L, TimeUnit.MILLISECONDS);
    }

    public String buildStatement(UUID uuid, SMItem item) {
        return uuid + ";" + item.getConfiguredItem().id() + ";" + item.getSlot();
    }
}
