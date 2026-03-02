package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.items.AnimationItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;

import java.util.*;

@Getter
public class AnimationStage {
    private final Animation animation;
    private final Map<Integer, Map<Byte, ConfigurationSection>> map;
    public AnimationStage(Animation animation, ConfigurationSection section) {
        this.animation = animation;
        this.map = new HashMap<>();

        for (String key : section.getKeys(false)) {
            ConfigurationSection tickSection = section.getConfigurationSection(key);
            int tick = LunaMath.toInt(key);

            Map<Byte, ConfigurationSection> map = new HashMap<>();
            for (String tickSectionKey : tickSection.getKeys(false)) {
                ConfigurationSection itemSection = tickSection.getConfigurationSection(tickSectionKey);
                if (itemSection == null) {
                    String itemId = tickSection.getString(tickSectionKey);
                    itemSection = animation.getReservedItems().get(itemId);
                }

                String[] place = tickSectionKey.split("-");
                if (place.length == 1)
                    map.put(LunaMath.toByte(place[0]), itemSection);
                else
                    for (byte i = LunaMath.toByte(place[0]); i <= LunaMath.toByte(place[1]); i++) {
                        map.put(i, itemSection);
                    }
            }

            this.map.put(tick, map);
        }
    }

    public void play(AnimatedMenu menu) {
        menu.getPlayingAnimations().add(animation);
        AnimationTask task = new AnimationTask(this, menu);
        task.runTaskAsynchronously(SateMenus.getInstance());
    }

    public void processTick(AnimatedMenu menu, int tick) {
        Map<Byte, ConfigurationSection> map = this.map.get(tick);
        if (map == null) return;

        map.forEach((slot, section) -> {
            Item item = menu.getItemList()
                    .stream()
                    .filter(i -> i instanceof AnimationItem && i.getSlot() == slot)
                    .findFirst()
                    .orElse(menu.findFirstItem(slot));
            if (section == null) {
                if (item != null)
                    backItem(menu, item, true);
            }
            else {
                menu.getItemList().remove(item);
                item = new AnimationItem(section, slot, item);
                menu.addItems(true, item);
            }
        });
    }

    public void backItem(ItemListMenu menu, Item item, boolean firstIteration) {
        menu.getItemList().remove(item);
        if (item instanceof AnimationItem animationItem) {
            Item prevItem = animationItem.getPrevItem();
            if (prevItem == null)
                item.remove(menu);
            else
                backItem(menu, prevItem, false);
        }
        else {
            if (firstIteration)
                item.remove(menu);
            else {
                menu.addItems(true, item);
            }
        }
    }
}
