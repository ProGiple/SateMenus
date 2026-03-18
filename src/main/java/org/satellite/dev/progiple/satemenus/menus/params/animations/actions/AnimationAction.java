package org.satellite.dev.progiple.satemenus.menus.params.animations.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface AnimationAction extends Cloneable {
    byte getSlot();
    ConfigurationSection getSettings();
    AnimationStage getAnimationStage();
    void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack);
    AnimationAction clone(byte slot);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Type {
        String value();
    }
}
