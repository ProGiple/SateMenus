package org.satellite.dev.progiple.satemenus.menus.items;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.menus.items.Item;

@Getter
public class AnimationItem extends Item {
    private final Item prevItem;
    private final ItemStack prevItemStack;
    public Sound sound;
    public float volume;
    public AnimationItem(ConfigurationSection section,
                         byte slot,
                         Item prevItem,
                         ItemStack itemStack) {
        super(section, slot);
        this.prevItem = prevItem;
        this.prevItemStack = itemStack;
    }
}
