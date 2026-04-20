package org.satellite.dev.progiple.satemenus.self.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.configuration.PluginConfig;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
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

    public String[] getScrollingFormats() {
        ConfigurationSection section = config.getSection("scrollingEditFormats");
        if (section == null) return new String[]{"- [element]", " -> [element]"};
        return new String[]{
                ColorManager.color(section.getString("notSelected")),
                ColorManager.color(section.getString("selected"))
        };
    }

    public String getString(String path) {
        return config.getString(path);
    }
}
