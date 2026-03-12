package org.satellite.dev.progiple.satemenus.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.service.managers.VanishManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.configs.Config;
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
        if (menuSettings == null) {
            Config.sendMessage(sender, "menuIsNotExists", "id-%-" + strings[2]);
            return;
        }

        Player player = VanishManager.exact(sender, strings[1]);
        if (player == null) {
            Config.sendMessage(sender, "playerIsOffline", "player-%-" + strings[1]);
            return;
        }

        Menus.open(player, menuSettings);
        Config.sendMessage(sender, "openMenuSuccess",
                "player-%-" + player.getName(),
                "id-%-" + menuSettings.id());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.getPlayerNicks(list.get(0), sender) :
                list.size() == 2 ? Utils.tabCompleterFiltering(Menus.getMenuSettings().keySet(), list.get(1)) : null;
    }
}
