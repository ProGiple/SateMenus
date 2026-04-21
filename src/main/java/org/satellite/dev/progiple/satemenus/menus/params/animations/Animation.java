package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl.RemoveAction;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Animation implements IAnimation {
    private final IConfig config;
    private final AnimationStage playStage;
    private final Map<String, ConfigurationSection> reservedActions;
    public Animation(IConfig config) {
        this.config = config;
        this.reservedActions = new HashMap<>();

        ConfigurationSection reservedItems = config.getSection("reservedActions");
        for (String key : reservedItems.getKeys(false)) {
            this.reservedActions.put(key, reservedItems.getConfigurationSection(key));
        }

        this.playStage = new AnimationStage(this, config.getSection("PLAY"));
    }

    @Override
    public void play(AnimatedMenu menu) {
        if (menu.getPlayingAnimation() == null)
            this.playStage.play(menu);
    }

    @Override
    public void stop(AnimatedMenu menu) {
        menu.setPlayingAnimation(null);
        TaskManager.stopAll(AnimationTask.class, r -> r.getMenu().equals(menu) && r.getStage().equals(playStage));
        menu.findItems(AnimationItem.class).forEach(i -> {
            RemoveAction.backItem(menu, i, i.getItemStack(), i.getSlot(), true);
        });
    }
}
