package org.satellite.dev.progiple.satemenus.self.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Flags;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.Creator;
import org.satellite.dev.progiple.satemenus.self.menusCreator.GeneratorType;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;

import java.util.List;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = {"edit"})
@Permissions("#.edit")
@Flags(NoArgCommand.AccessFlag.PLAYER_ONLY)
@Args(min = 3, max = Integer.MAX_VALUE)
public class EditSubCommand implements LunaExecutor {
    // menu edit template/menu <id>

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        Player player = (Player) sender;

        String id = strings[2];
        GeneratorType generatorType = Utils.getEnumValue(
                GeneratorType.class,
                strings[1].toUpperCase(),
                GeneratorType.MENU);
        if (generatorType == GeneratorType.MENU && Menus.getSettings(id) == null) {
            Config.sendMessage(player, "unregisterMenuError", "id-%-" + id);
            return;
        }
        else if (generatorType == GeneratorType.TEMPLATE && Templates.getTemplate(id) == null) {
            Config.sendMessage(player, "unregisterTemplateError", "id-%-" + id);
            return;
        }

        var builder = Creator.startCreating(id, generatorType, player.getUniqueId());
        new MainEditMenu(player, builder).open();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? List.of("template", "menu") :
                list.size() == 2 ? Utils.tabCompleterFiltering(
                        (list.get(0).equalsIgnoreCase("template") ?
                                Templates.getIds() :
                                Menus.getMenuSettings().keySet()), list.get(1)
                ) : null;
    }
}
