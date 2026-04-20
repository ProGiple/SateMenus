package org.satellite.dev.progiple.satemenus.self.menusCreator.converter;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;

import java.util.List;

public interface IConverter {
    String getId();
    NoArgCommand.AccessFlag executor();
    void convert(CommandSender sender, String[] args);
    List<String> tabComplete(CommandSender sender, List<String> args);
    default int getMinArgs() {
        Args args = this.getClass().getAnnotation(Args.class);
        return args == null ? 0 : args.min();
    }
}
