package org.satellite.dev.progiple.satemenus.menus.params.animations.actions;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;

@Getter
public abstract class AbstractAnimationAction implements AnimationAction {
    protected final AnimationStage animationStage;
    protected byte slot;
    protected final ConfigurationSection settings;

    protected final SoundParams soundParams;

    public AbstractAnimationAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        this.animationStage = animationStage;
        this.slot = slot;
        this.settings = settings;

        ConfigurationSection soundSection = settings.getConfigurationSection("sound");
        if (soundSection != null) {
            this.soundParams = new SoundParams(
                    Utils.getEnumValue(Sound.class, soundSection.getString("type")),
                    (float) soundSection.getDouble("volume", 1.0),
                    soundSection.getBoolean("single", true));
        }
        else {
            this.soundParams = null;
        }
    }

    @Override @MustBeInvokedByOverriders
    public void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack) {
        Player player = menu.getPlayer();
        if (this.soundParams != null) {
            if (soundParams.isSingle && index > 0) return;
            AnnounceUtils.sound(player, soundParams.sound, soundParams.volume);
        }
    }

    @Override @SneakyThrows
    public AnimationAction clone(byte slot) {
        AbstractAnimationAction action = (AbstractAnimationAction) this.clone();
        action.slot = slot;
        return action;
    }

    public record SoundParams(Sound sound, float volume, boolean isSingle) {}
}
