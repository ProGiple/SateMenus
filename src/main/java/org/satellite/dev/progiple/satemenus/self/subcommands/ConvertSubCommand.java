package org.satellite.dev.progiple.satemenus.self.subcommands;

import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.configuration.Message;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.converter.Converters;
import org.satellite.dev.progiple.satemenus.self.menusCreator.converter.IConverter;

import java.util.Arrays;
import java.util.List;

@SubCommand(appliedCommand = "satemenus", commandIdentifiers = "convert")
@Permissions("#.convert")
@Args(min = 2, max = Integer.MAX_VALUE)
public class ConvertSubCommand implements LunaExecutor {
    // menu convert chest

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        IConverter converter = Converters.find(strings[1]);
        if (converter == null) {
            Config.sendMessage(sender, "converterIsNull", "id-%-" + strings[1]);
            return;
        }

        if (converter.executor() != null && !converter.executor().check(sender)) {
            return;
        }

        if (strings.length - 2 < converter.getMinArgs()) {
            Message.TOO_LOW_ARGS.send(sender);
            return;
        }

        String[] args = Arrays.copyOfRange(strings, 2, strings.length);
        converter.convert(sender, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        int size = list.size();
        if (size == 1)
            return Utils.tabCompleterFiltering(Converters.getConverters()
                    .stream()
                    .map(IConverter::getId)
                    .toList(), list.get(0));
        if (size > 1) {
            IConverter converter = Converters.find(list.get(0));
            if (converter != null)
                return Utils.tabCompleterFiltering(
                        converter.tabComplete(sender, list.subList(1, size)),
                        list.get(size - 1));
        }
        return null;
    }
}
