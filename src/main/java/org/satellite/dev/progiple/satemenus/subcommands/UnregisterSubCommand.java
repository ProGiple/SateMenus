package org.satellite.dev.progiple.satemenus.subcommands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

import java.util.List;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = "unregister")
@Permissions("#.unregister")
@Args(min = 2, max = Integer.MAX_VALUE)
public class UnregisterSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        MenuSettings settings = Menus.getSettings(strings[1]);
        Menus.unregister(settings);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.tabCompleterFiltering(Menus.getMenuSettings().keySet(), list.get(0)) : null;
    }
}
