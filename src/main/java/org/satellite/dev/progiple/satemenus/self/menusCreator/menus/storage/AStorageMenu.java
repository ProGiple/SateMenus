package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.storage;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.IEditMenu;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;

@Getter
public abstract class AStorageMenu extends AMenu implements IEditMenu {
    protected final MenuSettingsBuilder builder;
    public AStorageMenu(@NotNull Player player, MenuSettingsBuilder builder) {
        super(player);
        this.builder = builder;

        String title = Config.getString("storageEdit.title");
        if (SateMenus.CHEST_TYPES.contains(builder.type())) {
            this.initialize(title, (byte) (9 * builder.rows()), null, false);
        }
        else {
            this.initialize(title, builder.type(), null, false);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(SateMenus.getInstance(), () -> {
            new MainEditMenu((Player) e.getPlayer(), builder).open();
        }, 3L);
    }

    @Override
    public boolean onCloseFirstUnregisterFlag() {
        return true;
    }
}
