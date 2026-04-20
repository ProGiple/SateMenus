package org.satellite.dev.progiple.satemenus.self.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.novasparkle.lunaspring.API.events.LunaHandler;
import org.satellite.dev.progiple.satemenus.menus.Menus;

@LunaHandler
public class SendCommandHandler implements Listener {
    @EventHandler
    private void onSend(PlayerCommandPreprocessEvent e) {
        String[] split = e.getMessage().split(" ");

        String command = split[0];
        if (command.startsWith("/")) command = command.substring(1);

        if (command.equals("menu") && split.length > 1) return;

        String finalCommand = command;
        Menus.getMenuSettings()
                .values()
                .stream()
                .filter(s -> {
                    Boolean b = s.commands().get(finalCommand);
                    return b != null && !b;
                })
                .findAny()
                .ifPresent(settings -> {
                    var menu = Menus.open(e.getPlayer(), settings, false);
                    if (menu != null) e.setCancelled(true);
                });
    }
}
