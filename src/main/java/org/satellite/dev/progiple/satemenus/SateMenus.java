package org.satellite.dev.progiple.satemenus;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
import org.novasparkle.lunaspring.API.commands.CommandInitializer;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.LunaPlugin;
import org.satellite.dev.progiple.satemenus.lunaActions.OpenMenuAction;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationTask;
import org.satellite.dev.progiple.satemenus.menus.params.animations.Animations;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

public final class SateMenus extends LunaPlugin {
    @Getter
    private static SateMenus instance;
    public static final EnumSet<InventoryType> CHEST_TYPES = EnumSet.of(
            InventoryType.CHEST, InventoryType.ENDER_CHEST, InventoryType.BARREL,
            InventoryType.SHULKER_BOX, InventoryType.PLAYER, InventoryType.CREATIVE);

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        loadAnimationActions();

        if (!this.dirExists())
            this.loadFiles("animations/animation.yml", "menus/example.yml", "templates/template.yml");
        this.saveDefaultConfig();

        AnnounceUtils.registerAction(new OpenMenuAction());
        loadData();

        CommandInitializer.initialize(this, "#.subcommands");
        this.processListeners("#.handlers");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        TaskManager.stopAll(AnimationTask.class, null);
    }

    public static void loadData() {
        Animations.loadFromDir(new File(instance.getDataFolder(), "animations/"));
        Templates.loadFromDir(new File(instance.getDataFolder(), "templates/"));
        Menus.loadFromDir(new File(instance.getDataFolder(), "menus/"));
    }

    private void loadAnimationActions() {
        Set<ClassEntry<AnimationAction.Type>> entries = AnnotationScanner.findAnnotatedClasses(
                this,
                AnimationAction.Type.class,
                "#.menus.params.animations.actions.impl");
        for (ClassEntry<AnimationAction.Type> entry : entries) {
            Animations.register(entry.getAnnotation().value(), entry.getClazz());
        }
    }
}
