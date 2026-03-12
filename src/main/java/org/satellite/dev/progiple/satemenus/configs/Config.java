package org.satellite.dev.progiple.satemenus.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.configuration.PluginConfig;
import org.satellite.dev.progiple.satemenus.SateMenus;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        config = new PluginConfig(SateMenus.getInstance());
    }

    public void reload() {
        config.reload();
    }

    public void sendMessage(CommandSender sender, String message, String... rpl) {
        config.sendMessage(sender, message, rpl);
    }
}
