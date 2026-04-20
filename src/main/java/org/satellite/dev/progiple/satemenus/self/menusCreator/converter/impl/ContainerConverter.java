package org.satellite.dev.progiple.satemenus.self.menusCreator.converter.impl;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.Creator;
import org.satellite.dev.progiple.satemenus.self.menusCreator.GeneratorType;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.converter.IConverter;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.MainEditMenu;

import java.util.List;

@Args(min = 2, max = Integer.MAX_VALUE)
public class ContainerConverter implements IConverter {
    @Override
    public String getId() {
        return "container";
    }

    @Override
    public NoArgCommand.AccessFlag executor() {
        return NoArgCommand.AccessFlag.PLAYER_ONLY;
    }

    @Override
    public void convert(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Block block = player.getTargetBlock(9);
        if (block == null || !(block.getState() instanceof Container container)) {
            Config.sendMessage(sender, "converterChestIsNull");
            return;
        }

        GeneratorType generatorType = Utils.getEnumValue(GeneratorType.class, args[0].toUpperCase(), GeneratorType.MENU);
        var inventory = container.getInventory();

        MenuSettingsBuilder builder = Creator.startCreating(args[1], generatorType, player.getUniqueId());
        builder.type(inventory.getType());
        builder.rows(inventory.getSize() / 9);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            builder.decorations().put(i, itemStack);
        }

        String title = container.getCustomName();
        if (title != null) builder.title(title);

        new MainEditMenu(player, builder).open();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> args) {
        return args.size() == 1 ? List.of("template", "menu") : args.size() == 2 ? List.of("<id>") : null;
    }
}
