package org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;

import java.util.List;

@Getter
public abstract class EditableButton extends Item {
    protected final MenuSettingsBuilder builder;
    public EditableButton(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section);
        this.builder = builder;
    }

    protected static void sendEditMessage(ConfigurationSection section, CommandSender sender) {
        List<String> messages = section.getStringList("editMessages");
        if (messages.isEmpty()) {
            var msg = section.getString("editMessages");
            if (msg == null) return;

            messages.add(msg);
        }

        AnnounceUtils.sendMessage(sender, messages);
    }
}
