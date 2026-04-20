package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.TripleFunction;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class AnimationStage {
    public static @NotNull Supplier<@NotNull Collection<AnimationAction>> ACTION_COLLECTION_FABRIC = ArrayList::new;
    public static @NotNull TripleFunction<AnimationStage, ConfigurationSection, Byte, AnimationAction> ACTION_FABRIC = (s, c, b) -> {
        String type = c.getString("type");
        Class<?> clazz = Animations.getAnimationAction(type);
        try {
            return (AnimationAction) clazz.getDeclaredConstructor(
                            AnimationStage.class,
                            byte.class,
                            ConfigurationSection.class)
                    .newInstance(s, b, c);
        } catch (Exception e) {
            return null;
        }
    };
    public static Function<String, Integer> KEY_TO_MILLIS_FABRIC = k -> {
        if (k.startsWith("~"))
            return LunaMath.toInt(k.substring(1));

        return LunaMath.toInt(k) * 50;
    };

    private final Animation animation;
    private final Map<Integer, Collection<AnimationAction>> map;
    public AnimationStage(Animation animation, ConfigurationSection section) {
        this.animation = animation;
        this.map = new HashMap<>();

        for (String key : section.getKeys(false)) {
            ConfigurationSection tickSection = section.getConfigurationSection(key);
            int timeMillis = KEY_TO_MILLIS_FABRIC.apply(key);

            Collection<AnimationAction> actions = ACTION_COLLECTION_FABRIC.get();
            for (String tickSectionKey : tickSection.getKeys(false)) {
                ConfigurationSection itemSection = tickSection.getConfigurationSection(tickSectionKey);
                if (itemSection == null) {
                    String id = tickSection.getString(tickSectionKey);
                    itemSection = animation.getReservedActions().get(id);
                    if (itemSection == null) continue;
                }

                String[] place = tickSectionKey.split("-");

                if (place.length == 1)
                    actions.add(ACTION_FABRIC.apply(this, itemSection, LunaMath.toByte(place[0])));
                else {
                    byte firstSlot = LunaMath.toByte(place[0]);

                    AnimationAction action = ACTION_FABRIC.apply(this, itemSection, firstSlot);
                    if (action == null) continue;

                    actions.add(action);
                    for (byte i = ++firstSlot; i <= LunaMath.toByte(place[1]); i++) {
                        actions.add(action.clone(i));
                    }
                }
            }

            this.map.put(timeMillis, actions);
        }
    }

    public void play(AnimatedMenu menu) {
        menu.setPlayingAnimation(this.animation);
        AnimationTask task = new AnimationTask(this, menu);
        task.handle();
    }

    public void processTick(AnimatedMenu menu, int timeMillis) {
        Collection<AnimationAction> actions = map.get(timeMillis);
        if (actions == null) return;

        short index = 0;
        int invSize = menu.getInventory().getSize();
        for (AnimationAction action : actions) {
            if (action == null) continue;

            byte slot = action.getSlot();
            if (slot >= invSize) continue;

            ItemStack itemStack = menu.getInventory().getItem(slot);
            Item item = menu.findFirstItemAsync(slot);

            action.execute(menu, timeMillis, index++, item, itemStack);
        }
    }
}
