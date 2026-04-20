package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.params.ITemplated;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.utils.RegistrableMenuCommand;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter @Setter @Accessors(fluent = true)
public class MenuSettingsBuilder {
    public static Supplier<ConfigSaver> CONFIG_SAVER_FACTORY = ConfigSaver::new;

    private final GeneratorType generatorType;
    private final String id;
    private final Map<String, Boolean> commands;
    private final List<String> animations;
    private final List<MenuBuilderItem> items;
    private Template template;
    private String title = "ExampleTitle";
    private InventoryType type = InventoryType.CHEST;
    private Integer rows = 3;
    private MenuBuilderAction openAction;
    private MenuBuilderAction closeAction;
    private Map<Integer, ItemStack> decorations;
    private Integer cooldownCachedTicks = 0;
    private Integer updatingTime = 0;
    public MenuSettingsBuilder(ITemplated templated) {
        this.id = templated.id();
        this.animations = new ArrayList<>(templated.getAnimationIds());
        this.items = templated.items().stream().flatMap(i -> {
            var list = new ArrayList<MenuBuilderItem>();
            i.slots().forEach(j -> list.add(new MenuBuilderItem(i, j)));
            return list.stream();
        }).collect(Collectors.toList());

        if (templated instanceof MenuSettings ms) {
            this.generatorType = GeneratorType.MENU;
            this.commands = new HashMap<>(ms.commands());
        }
        else {
            this.generatorType = GeneratorType.TEMPLATE;
            this.commands = new HashMap<>();
        }

        this.title = templated.title() == null ? "" : templated.title();
        this.type = templated.type() != null ? templated.type() : InventoryType.CHEST;
        this.rows = templated.rows() == null ? 0 : templated.rows();
        this.openAction = templated.openAction() != null ?
                new MenuBuilderAction(templated.openAction()) :
                new MenuBuilderAction();
        this.closeAction = templated.closeAction() != null ?
                new MenuBuilderAction(templated.closeAction()) :
                new MenuBuilderAction();
        this.decorations = convertDecorations(templated.decorations());
        this.cooldownCachedTicks = templated.clickCooldownTicks() == null ? 0 : templated.clickCooldownTicks();
        this.updatingTime = templated.updatingTime() == null ? 0 : templated.updatingTime();
    }

    public MenuSettingsBuilder(String id, GeneratorType generatorType) {
        this.id = id;
        this.generatorType = generatorType;
        this.commands = new HashMap<>();
        this.animations = new ArrayList<>();
        this.items = new ArrayList<>();
        this.decorations = new HashMap<>();
        this.openAction = new MenuBuilderAction();
        this.closeAction = new MenuBuilderAction();
    }

    public ITemplated build() {
        return Creator.build(this);
    }

    public CompletableFuture<ITemplated> buildAsync() {
        return Creator.buildAsync(this);
    }

    public Configuration getMenuConfig() {
        return new Configuration(SateMenus.getInstance().getDataFolder(), generatorType.path + id);
    }

    public IConfig saveToConfig() {
        var config = getMenuConfig();

        ConfigSaver saver = CONFIG_SAVER_FACTORY.get();
        saver.save(this, config);

        return config;
    }

    public static RegistrableMenuCommand registrableMenuCommandCreate(String id, Map<String, Boolean> commands) {
        List<String> commandList = commands.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .filter(k -> !k.equals("menu"))
                .toList();
        return new RegistrableMenuCommand(id, commandList);
    }

    public static Map<Integer, ItemStack> convertDecorations(ConfigurationSection section) {
        var map = new HashMap<Integer, ItemStack>();
        if (section != null) for (String key : section.getKeys(false)) {
            ConfigurationSection config = section.getConfigurationSection(key);
            ItemStack itemStack = new NonMenuItem(config).getItemStack();

            var slots = Utils.getSlotList(config.getStringList("slots"));
            for (int slot : slots) {
                map.put(slot, itemStack.clone());
            }
        }

        return map;
    }
}
