package org.satellite.dev.progiple.satemenus.subcommands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.service.managers.VanishManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

import java.util.List;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = "open")
@Permissions("#.open")
@Args(min = 3, max = Integer.MAX_VALUE)
public class OpenSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        MenuSettings menuSettings = Menus.getSettings(strings[2]);
        Menus.open(VanishManager.exact(sender, strings[1]), menuSettings);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.getPlayerNicks(list.get(0), sender) :
                list.size() == 2 ? Utils.tabCompleterFiltering(Menus.getMenuSettings().keySet(), list.get(1)) : null;
    }
}
