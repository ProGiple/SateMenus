package org.satellite.dev.progiple.satemenus.menus.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.exceptions.NoItemMetaException;

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

    @Override
    public @NotNull ItemMeta getMeta() {
        try {
            return super.getMeta();
        }
        catch (NoItemMetaException e) {
            return Bukkit.getItemFactory().getItemMeta(Material.STONE);
        }
    }
}
