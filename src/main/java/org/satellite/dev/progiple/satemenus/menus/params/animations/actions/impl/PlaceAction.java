package org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AbstractAnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;

@AnimationAction.Type("place")
public class PlaceAction extends AbstractAnimationAction {
    public PlaceAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        super(animationStage, slot, settings);
    }

    @Override
    public void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack) {
        if (item != null) item.remove(menu);
        AnimationItem animationItem = new AnimationItem(settings, slot, item, itemStack);
        menu.addItems(true, animationItem);

        super.execute(menu, timeMillis, index, item, itemStack);
    }
}
