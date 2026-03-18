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

@AnimationAction.Type("change_slot")
public class ChangeSlotAction extends AbstractAnimationAction {
    private final byte targetSlot;
    public ChangeSlotAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        super(animationStage, slot, settings);
        this.targetSlot = (byte) settings.getInt("target");
    }

    @Override
    public void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack) {
        super.execute(menu, timeMillis, index, item, itemStack);
        if (item != null)
            item.remove(menu);
        else if (itemStack != null)
            menu.getInventory().setItem(slot, null);
        else
            return;

        AnimationItem animationItem1 = new AnimationItem(slot, item, itemStack);

        item = menu.findFirstItem(targetSlot);
        itemStack = menu.getInventory().getItem(targetSlot);
        AnimationItem animationItem2 = new AnimationItem(item, targetSlot, item, itemStack);
        menu.addItems(true, animationItem1, animationItem2);
    }
}
