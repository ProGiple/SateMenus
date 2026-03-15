package org.satellite.dev.progiple.satemenus.menus.items;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.updatable.UpdatableIMenu;
import org.novasparkle.lunaspring.API.menus.updatable.UpdatableItem;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.Refreshable;
import org.satellite.dev.progiple.satemenus.menus.menus.impl.SateMenu;
import org.satellite.dev.progiple.satemenus.menus.params.MenuConfiguredAction;
import org.satellite.dev.progiple.satemenus.menus.params.MenuConfiguredItem;
import org.satellite.dev.progiple.satemenus.utils.SateCache;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class SMItem extends Item implements Refreshable, UpdatableItem {
    private final MenuConfiguredItem configuredItem;
    public SMItem(MenuConfiguredItem configuredItem, int slot) {
        super(configuredItem.section(), slot);
        this.configuredItem = configuredItem;
    }

    @Override
    public void refresh() {
        this.insert();
    }

    @Override
    public void tick(UpdatableIMenu menu) {
    }

    @Override
    public Item insert(@NotNull ItemListMenu itemListMenu, @Range(from = 0L, to = 53L) byte slot) {
        this.updateNative(itemListMenu.getPlayer());
        return super.insert(itemListMenu, slot);
    }

    @Override
    public SMItem onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        IMenu menu = this.getMenu();
        if (!(menu instanceof SateMenu sateMenu)) {
            return this;
        }

        UUID uuid = player.getUniqueId();

        SateCache cache = configuredItem.cache() == null ? sateMenu.getSettings().cooldownCache() : configuredItem.cache();
        if (cache != null && cache.contains(cache.buildStatement(uuid, this))) {
            return this;
        }

        if (!checkClick(player, configuredItem.anyClick())) {
            if (configuredItem.anyClick() != null && configuredItem.anyClick().cancelEventIfError())
                return this;
        }

        MenuConfiguredAction action = e.getClick().isLeftClick() ? configuredItem.leftClick() : configuredItem.rightClick();
        this.checkClick(player, action);

        return this;
    }

    protected boolean checkClick(Player player, MenuConfiguredAction action) {
        boolean b = action == null || action.conditions() == null || Conditions.checkConditions(player, action.conditions());
        if (b && action != null) action.process(player, this);
        return b;
    }

    protected void updateNative(Player target) {
        this.setLore(new ArrayList<>(this.defaultLore), "player-%-" + target.getName());
        this.replaceLore(l -> Utils.setPlaceholders(target, l));
    }
}
