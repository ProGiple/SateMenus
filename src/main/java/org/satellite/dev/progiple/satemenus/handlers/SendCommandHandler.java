package org.satellite.dev.progiple.satemenus.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.novasparkle.lunaspring.API.events.LunaHandler;
import org.satellite.dev.progiple.satemenus.menus.Menus;

@LunaHandler
public class SendCommandHandler implements Listener {
    @EventHandler
    private void onSend(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0];
        if (command.startsWith("/")) command = command.substring(1);

        String finalCommand = command;
        Menus.getMenuSettings()
                .values()
                .stream()
                .filter(s -> {
                    Boolean b = s.commands().get(finalCommand);
                    return b != null && b;
                })
                .findAny().ifPresent(settings ->
                        Menus.open(e.getPlayer(), settings, false));
    }
}
