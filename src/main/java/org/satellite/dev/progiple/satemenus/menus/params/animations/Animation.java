package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Animation implements IAnimation {
    private final AnimationStage playStage;
    private final Map<String, ConfigurationSection> reservedItems;
    public Animation(IConfig config) {
        this.reservedItems = new HashMap<>();

        ConfigurationSection reservedItems = config.getSection("reservedItems");
        for (String key : reservedItems.getKeys(false)) {
            this.reservedItems.put(key, reservedItems.getConfigurationSection(key));
        }

        this.playStage = new AnimationStage(this, config.getSection("PLAY"));
    }

    @Override
    public void play(AnimatedMenu menu) {
        if (!menu.getPlayingAnimations().contains(this))
            this.playStage.play(menu);
    }

    @Override
    public void stop(AnimatedMenu menu) {
        menu.getPlayingAnimations().remove(this);
        TaskManager.stopAll(AnimationTask.class, r -> r.getMenu().equals(menu) && r.getStage().equals(playStage));
        menu.findItems(AnimationItem.class).forEach(i -> {
            playStage.backItem(menu, i, true);
        });
    }
}
