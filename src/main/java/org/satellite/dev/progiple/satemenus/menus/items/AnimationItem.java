package org.satellite.dev.progiple.satemenus.menus.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.exceptions.NoItemMetaException;

@Getter
public class AnimationItem extends Item {
    private final Item prevItem;
    private final ItemStack prevItemStack;
    public AnimationItem(ConfigurationSection section,
                         byte slot,
                         Item prevItem,
                         ItemStack itemStack) {
        super(section, slot);
        this.prevItem = prevItem;
        this.prevItemStack = itemStack;
    }

    public AnimationItem(NonMenuItem nonMenuItem, byte slot, Item prevItem, ItemStack itemStack) {
        super(nonMenuItem, slot);
        this.prevItem = prevItem;
        this.prevItemStack = itemStack;
    }

    public AnimationItem(byte slot, Item prevItem, ItemStack itemStack) {
        super(Material.AIR, slot);
        this.prevItem = prevItem;
        this.prevItemStack = itemStack;
    }

    public boolean isAir() {
        return material == null || material.isAir();
    }

    @Override
    public Item applyMenuNBT() {
        if (isAir() || this.amount <= 0) {
            return this;
        }

        Item.marker(this.getItemStack());
        return this;
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

    @Override
    public NonMenuItem setGlowing(boolean enchanted) {
        return isAir() ? null : super.setGlowing(enchanted);
    }
}
