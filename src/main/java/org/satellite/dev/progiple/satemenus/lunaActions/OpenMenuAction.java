package org.satellite.dev.progiple.satemenus.lunaActions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.messageActions.MessageAction;
import org.novasparkle.lunaspring.API.messageActions.PlayerMessageAction;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

@MessageAction("OPEN_MENU")
public class OpenMenuAction extends PlayerMessageAction {
    // [OPEN_MENU] id

    @Override
    public void execute(Player player, String s) {
        MenuSettings settings = Menus.getSettings(s);
        if (settings != null) {
            Bukkit.getScheduler().runTaskAsynchronously(SateMenus.getInstance(), () -> {
                Menus.open(player, settings);
            });
        }
    }
}
