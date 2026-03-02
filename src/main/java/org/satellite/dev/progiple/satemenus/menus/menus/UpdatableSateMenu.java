package org.satellite.dev.progiple.satemenus.menus.menus;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.updatable.UpdatableIMenu;
import org.novasparkle.lunaspring.API.menus.updatable.tasks.OptimizedUpdatableTask;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

@Getter
public class UpdatableSateMenu extends SateMenu implements UpdatableIMenu {
    private final OptimizedUpdatableTask runnable;
    public UpdatableSateMenu(@NotNull Player player, MenuSettings settings, @Nullable Recreatable recreatable) {
        super(player, settings, recreatable);
        this.runnable = new OptimizedUpdatableTask(this, settings.updatingTime(), true);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        super.onOpen(e);
        UpdatableIMenu.super.onOpen(e);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        super.onClose(e);
        this.runnable.cancel();
    }
}
