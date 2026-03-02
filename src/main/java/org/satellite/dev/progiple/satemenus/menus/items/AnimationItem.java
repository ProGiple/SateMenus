package org.satellite.dev.progiple.satemenus.menus.items;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.menus.items.Item;

@Getter
public class AnimationItem extends Item {
    private final Item prevItem;
    public AnimationItem(ConfigurationSection section, byte slot, Item prevItem) {
        super(section, slot);
        this.prevItem = prevItem;
    }
}
