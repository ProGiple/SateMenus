package org.satellite.dev.progiple.satemenus.self.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.novasparkle.lunaspring.API.events.LunaHandler;
import org.satellite.dev.progiple.satemenus.self.queries.Queries;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

@LunaHandler
public class SendChatMessageHandler implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        var query = Queries.getQuery(InputTextQuery.class, e.getPlayer().getUniqueId());
        if (query != null && query.request(e.getMessage())) e.setCancelled(true);
    }
}
