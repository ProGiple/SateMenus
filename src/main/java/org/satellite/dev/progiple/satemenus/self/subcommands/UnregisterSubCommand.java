package org.satellite.dev.progiple.satemenus.self.subcommands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;

import java.util.Collection;
import java.util.List;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = "unregister")
@Permissions("#.unregister")
@Args(min = 3, max = Integer.MAX_VALUE)
public class UnregisterSubCommand implements LunaExecutor {
    // satemenus unregister template/animation/menu <id>

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        String id = strings[2];

        boolean success = false;
        if (strings[1].equalsIgnoreCase("template")) {
            Template template = Templates.getTemplate(id);
            if (template != null) {
                Templates.unregister(template);
                success = true;
            }
        }
        else if (strings[1].equalsIgnoreCase("animation")) {
            if (Animations.get(id) != null) {
                Animations.unregister(id);
                success = true;
            }
        }
        else if (strings[1].equalsIgnoreCase("menu")) {
            MenuSettings settings = Menus.getSettings(id);
            if (settings != null) {
                Menus.unregister(settings);
                success = true;
            }
        }

        String msgId = strings[1].substring(0, 1).toUpperCase() + strings[1].substring(1);
        msgId = "unregister" + msgId + (success ? "Success" : "Error");
        Config.sendMessage(sender, msgId, "id-%-" + id);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        if (list.size() == 1)
            return Utils.tabCompleterFiltering(List.of("template", "animation", "menu"), list.get(0));
        else if (list.size() == 2) {
            Collection<String> collection = switch (list.get(0)) {
                case "template" -> Templates.getIds();
                case "animation" -> Animations.keySet();
                case "menu" -> Menus.getMenuSettings().keySet();
                default -> null;
            };
            if (collection != null) return Utils.tabCompleterFiltering(collection, list.get(1));
        }

        return null;
    }
}
