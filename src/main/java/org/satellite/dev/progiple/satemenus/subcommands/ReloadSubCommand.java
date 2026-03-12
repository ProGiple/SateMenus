package org.satellite.dev.progiple.satemenus.subcommands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.configs.Config;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = "reload")
@Permissions("#.reload")
public class ReloadSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        SateMenus.loadData();
        Config.reload();
        Config.sendMessage(sender, "reload");
    }
}
