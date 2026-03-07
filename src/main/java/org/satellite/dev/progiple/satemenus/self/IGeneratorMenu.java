package org.satellite.dev.progiple.satemenus.self;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.menus.IMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface IGeneratorMenu extends IMenu {
    Configuration getConfig();

    default void saveToConfig(Inventory inventory) {
        Configuration config = getConfig();
        Map<ItemStack, List<Integer>> map = new HashMap<>();

        config.set("decorations", null);
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                List<Integer> slots = map.get(itemStack);
                if (slots == null) slots = new ArrayList<>();

                slots.add(i);
                map.put(itemStack, slots);
            }
        }

        Serializer serializer = new Serializer();
        AtomicInteger i = new AtomicInteger(0);
        map.forEach((item, slots) -> {
            String sectionName = String.valueOf(i.incrementAndGet());
            ConfigurationSection section = config.createSection("decorations", sectionName);

            section = serializer.preSerializeItem(section, item);

            List<String> slotList = serializer.performanceSlots(slots);
            if (slotList.size() == 1) section.set("slot", slotList.get(0));
            else section.set("slots", slotList);

            section = sectionFormating(section);
        });

        config.save();
    }

    default ConfigurationSection sectionFormating(ConfigurationSection section) {
        return section;
    }
}
