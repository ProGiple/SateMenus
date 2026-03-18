package org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AbstractAnimationAction;

@AnimationAction.Type("remove")
public class RemoveAction extends AbstractAnimationAction {
    public RemoveAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        super(animationStage, slot, settings);
    }

    @Override
    public void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack) {
        backItem(menu, item, itemStack, slot, true);
        super.execute(menu, timeMillis, index, item, itemStack);
    }

    public static void backItem(ItemListMenu menu, Item item, ItemStack itemStack, int slot, boolean isFirstIteration) {
        if (item instanceof AnimationItem animationItem) {
            menu.getItemList().remove(item);
            backItem(menu, animationItem.getPrevItem(), animationItem.getPrevItemStack(), slot, false);
        }
        else if (item != null) {
            if (isFirstIteration)
                item.remove(menu);
            else
                menu.addItems(true, item);
        }
        else {
            if (isFirstIteration)
                menu.getInventory().setItem(slot, null);
            else
                menu.getInventory().setItem(slot, itemStack);
        }
    }
}
