package org.satellite.dev.progiple.satemenus.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.self.configuration.Message;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrableMenuCommand extends Command {
    private static final String FALLBACK_PREFIX = "SateMenus".toLowerCase(Locale.ROOT).trim();
    private static CommandMap commandMap = null;

    private MenuSettings menu;
    private boolean registered = false;
    private boolean unregistered = false;
    public RegistrableMenuCommand(String id, List<String> commands) {
        super(commands.isEmpty() ? id : commands.get(0));
        if (commands.size() > 1) {
            this.setAliases(commands.subList(1, commands.size()));
        }
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final @NotNull String commandLabel, final @NotNull String[] typedArgs) {
        if (this.unregistered) {
            throw new IllegalStateException(String.format("Command %s was unregistered!", getName()));
        }

        if (!(sender instanceof Player player)) {
            Message.INVALID_SENDER.send(sender, "sender-%-" + sender.getClass().getName());
            return true;
        }

        Menus.open(player, menu, player.hasPermission("satemenus.openBypass"));
        return true;
    }

    public void register(MenuSettings menu) {
        if (registered) {
            throw new IllegalStateException(String.format("Command %s was already registered!", getName()));
        }

        if (commandMap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getServer());
            } catch (final Exception e) {
                e.printStackTrace();
                return;
            }
        }

        this.menu = menu;
        commandMap.register(FALLBACK_PREFIX, this);
        registered = true;
    }

    public void unregister() {
        if (!registered) {
            throw new IllegalStateException("This command was not registered!");
        }

        if (unregistered) {
            throw new IllegalStateException("This command was already unregistered!");
        }

        unregistered = true;
        if (commandMap == null) {
            this.menu = null;
            return;
        }

        Field cMap;
        Field knownCommands;
        try {
            cMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cMap.setAccessible(true);
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);

            final Map<String, Command> knownCommandsMap = (Map<String, Command>) knownCommands.get(cMap.get(Bukkit.getServer()));

            knownCommandsMap.remove(this.getName());
            knownCommandsMap.remove(FALLBACK_PREFIX + ":" + this.getName());
            for (String alias : this.getAliases()) {
                knownCommandsMap.remove(alias);
                knownCommandsMap.remove(FALLBACK_PREFIX + ":" + alias);
            }

            this.unregister(commandMap);
        } catch (final @NotNull Exception exception) {
            exception.printStackTrace();
        }

        this.menu = null;
    }

    public boolean registered() {
        return registered;
    }
}