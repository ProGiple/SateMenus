package org.satellite.dev.progiple.satemenus.self;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Serializer {
    public ConfigurationSection preSerializeItem(ConfigurationSection section, ItemStack item) {
        section.set("material", item.getType().name());
        section.set("amount", item.getAmount());
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            section.set("displayName", meta.getDisplayName());
            if (meta.hasLore()) section.set("lore", meta.getLore());
            if (meta.hasEnchants()) section.set("enchanted", true);
            section.set("baseHead", NBTManager.getBase64FromHead(item));
            section.set("itemflags", meta.getItemFlags()
                    .stream()
                    .map(Enum::name)
                    .toList());

            Color color;
            if (meta instanceof LeatherArmorMeta colorMeta) {
                color = colorMeta.getColor();
            } else if (meta instanceof PotionMeta colorMeta) {
                color = colorMeta.getColor();
            } else if (meta instanceof MapMeta colorMeta) {
                color = colorMeta.getColor();
            } else
                color = null;

            if (color != null) section.set("color", String.format("%d, %d, %d",
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue()));

            if (item.getDurability() != 0) {
                section.set("durability.amount", item.getType().getMaxDurability() - item.getDurability());
                section.set("durability.spent", true);
            }

            if (meta.isUnbreakable()) section.set("unbreakable", true);
            if (meta.hasCustomModelData()) section.set("modeldata", meta.getCustomModelData());

            if (meta instanceof PotionMeta potionMeta) {
                List<String> effects = potionMeta.getCustomEffects()
                        .stream()
                        .map(e -> {
                            String type = e.getType().getName();
                            int duration = e.getDuration();
                            int amplifier = e.getAmplifier();
                            return type + " <S> " + duration + " <S> " + amplifier;
                        })
                        .toList();
                section.set("potion_effects", effects);
            }
        }

        Utils.Items.getEnchantments(item).forEach((e, lvl) -> {
            section.set("enchants." + e.getKey().getKey(), lvl);
        });

        return section;
    }

    public List<String> performanceSlots(List<Integer> intSlots) {
        intSlots.sort(Comparator.comparingInt(i -> i));
        System.out.println("Отсортировано: " + intSlots);

        List<String> slots = new ArrayList<>();
        StringBuilder structuredSlots = new StringBuilder();

        int i = 0;
        int size = intSlots.size();

        while (i < size) {
            int minSlot = intSlots.get(i);
            int maxSlot = minSlot;

            while (i + 1 < size && intSlots.get(i + 1) - maxSlot <= 1) {
                maxSlot = intSlots.get(i + 1);
                i++;
            }

            if (minSlot == maxSlot) {
                structuredSlots.append(minSlot).append(", ");
            } else {
                slots.add(minSlot + "-" + maxSlot);
            }

            i++;
        }

        if (!structuredSlots.isEmpty()) {
            structuredSlots.delete(structuredSlots.length() - 2, structuredSlots.length());
        }

        slots.add(0, structuredSlots.toString());
        return slots;
    }
}
