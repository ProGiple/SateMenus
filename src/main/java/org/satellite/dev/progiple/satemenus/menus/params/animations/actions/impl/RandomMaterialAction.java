package org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AbstractAnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;

import java.util.Arrays;
import java.util.List;

@AnimationAction.Type("random_material")
public class RandomMaterialAction extends AbstractAnimationAction {
    public static List<Material> ITEM_MATERIALS = Arrays.stream(Material.values())
            .filter(m -> m.isItem() && !m.isAir() && !m.isEmpty())
            .toList();

    public RandomMaterialAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        super(animationStage, slot, settings);
    }

    @Override
    public void execute(AnimatedMenu menu, int timeMillis, short index, @Nullable Item item, @Nullable ItemStack itemStack) {
        if (item != null) item.remove(menu);
        AnimationItem animationItem = new AnimationItem(settings, slot, item, itemStack);
        animationItem.setMaterial(LunaMath.getRandomIfPresent(ITEM_MATERIALS, animationItem::getMaterial));
        menu.addItems(true, animationItem);

        super.execute(menu, timeMillis, index, item, itemStack);
    }
}
