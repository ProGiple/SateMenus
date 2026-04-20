package org.satellite.dev.progiple.satemenus.self.menusCreator.converter.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;
import org.novasparkle.lunaspring.API.conditions.Conditions;
import org.novasparkle.lunaspring.API.conditions.abs.Condition;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.self.configs.Config;
import org.satellite.dev.progiple.satemenus.self.menusCreator.Creator;
import org.satellite.dev.progiple.satemenus.self.menusCreator.GeneratorType;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.converter.IConverter;
import org.satellite.dev.progiple.satemenus.self.menusCreator.menus.conditions.ManageConditionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Args(min = 1, max = Integer.MAX_VALUE)
public class DeluxeMenusConverter implements IConverter {
    @Override
    public String getId() {
        return "deluxemenus";
    }

    @Override
    public NoArgCommand.AccessFlag executor() {
        return null;
    }

    @Override
    public void convert(CommandSender sender, String[] args) {
        String path = args[0];

        File file = new File(SateMenus.getInstance().getDataFolder(), "../DeluxeMenus/menus/" + path);
        if (!file.exists()) {
            Config.sendMessage(sender, "deluxeMenusFileIsNull");
            return;
        }

        String strGenType = args.length > 1 ? args[1] : "";
        GeneratorType generatorType = Utils.getEnumValue(GeneratorType.class, strGenType, GeneratorType.MENU);

        String targetId = file.getName().replace(".yml", "");

        MenuSettingsBuilder builder;
        if (sender instanceof Player player) {
            builder = Creator.startCreating(targetId, generatorType, player.getUniqueId());
        }
        else {
            builder = Creator.startCreating(targetId, generatorType);
        }

        IConfig config = new IConfig(file);

        String title = config.getString("menu_title");
        if (title != null) builder.title(title);

        List<String> commands = config.getStringList("open_command");
        if (commands.isEmpty()) commands.add(config.getString("open_command"));

        boolean registerCommands = config.getBoolean("register_command");
        builder.commands().clear();
        commands.forEach(c -> builder.commands().put(c, registerCommands));


    }

    protected Conditions.Operation getOperation(ConfigurationSection section) {
        int value = section.getInt("minimum_requirements");
        return value == 1 ? Conditions.Operation.OR : Conditions.Operation.AND;
    }

//    protected List<ManageConditionsMenu.ConditionStruct> convertRequirements(ConfigurationSection section) {
//        List<ManageConditionsMenu.ConditionStruct> list = new ArrayList<>();
//
//        for (String key : section.getKeys(false)) {
//            ConfigurationSection subSection = section.getConfigurationSection(key);
//
//            String conditionId = subSection.getString("type");
//            Condition<?> condition = Conditions.getCondition(conditionId);
//            if (condition == null) continue;
//
//            var struct = new ManageConditionsMenu.ConditionStruct();
//            struct.
//        }
//    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> args) {
        return args.size() == 1 ? getDeluxeMenusFiles()
                .stream()
                .map(this::getFilePath)
                .toList() : args.size() == 2 ? List.of("menu", "template") : null;
    }

    protected List<File> getDeluxeMenusFiles() {
        List<File> files = new ArrayList<>();

        File dir = new File(SateMenus.getInstance().getDataFolder(), "../DeluxeMenus/");
        if (!dir.exists() || !dir.isDirectory()) return files;

        addFiles(files, dir);
        return files;
    }

    protected void addFiles(List<File> files, File dir) {
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                files.add(file);
            }
            else if (file.isDirectory()) {
                addFiles(files, file);
            }
        }
    }

    protected String getFilePath(File file) {
        return file.getAbsolutePath().replaceFirst("^.*/menus/", "");
    }
}
