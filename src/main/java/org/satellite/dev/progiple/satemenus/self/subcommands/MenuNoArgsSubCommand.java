package org.satellite.dev.progiple.satemenus.self.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Flags;
import org.novasparkle.lunaspring.API.commands.annotations.ZeroArgCommand;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

@ZeroArgCommand("satemenus")
@Flags(NoArgCommand.AccessFlag.PLAYER_ONLY)
public class MenuNoArgsSubCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        MenuSettings settings = Menus.getMenuSettings().values()
                .stream()
                .filter(s -> s.commands().containsKey("menu"))
                .findFirst()
                .orElse(null);
        if (settings != null) {
            Player player = (Player) sender;
            Menus.open(player, settings);
        }
    }
}
