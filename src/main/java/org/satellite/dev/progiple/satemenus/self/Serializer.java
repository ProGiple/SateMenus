package org.satellite.dev.progiple.satemenus.self;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Serializer {
    public ConfigurationSection preSerializeItem(ConfigurationSection section, ItemStack item) {
        section.set("material", item.getType().name());
        if (item.getAmount() > 1) section.set("amount", item.getAmount());
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            var component = meta.displayName();
            String displayName = component == null ? null : LegacyComponentSerializer
                    .legacySection()
                    .serialize(component);
            if (displayName != null) section.set("displayName", displayName.replace("§", "&"));
            if (meta.hasLore()) {
                List<Component> lore = meta.lore();
                if (lore != null) section.set("lore", lore
                        .stream()
                        .map(l -> LegacyComponentSerializer.legacySection().serialize(l))
                        .toList());
            }
            if (meta.hasEnchants()) section.set("enchanted", true);
            section.set("baseHead", NBTManager.getBase64FromHead(item));

            var itemFlags = meta.getItemFlags();
            if (!itemFlags.isEmpty()) section.set("itemflags", itemFlags
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
        if (intSlots == null || intSlots.isEmpty()) {
            return new ArrayList<>();
        }

        intSlots.sort(Comparator.comparingInt(i -> i));

        List<String> ranges = new ArrayList<>();
        List<Integer> singles = new ArrayList<>();

        int i = 0;
        int size = intSlots.size();
        while (i < size) {
            int start = intSlots.get(i);
            int end = start;

            while (i + 1 < size && intSlots.get(i + 1) == end + 1) {
                end = intSlots.get(i + 1);
                i++;
            }

            if (start == end) {
                singles.add(start);
            } else {
                ranges.add(start + "-" + end);
            }

            i++;
        }

        List<String> result = new ArrayList<>();
        if (!singles.isEmpty()) {
            result.add(singles.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ")));
        }

        result.addAll(ranges);

        return result;
    }

    public <I> void serializeItems(Supplier<ConfigurationSection> baseSup,
                                   Map<I, List<Integer>> items,
                                   Function<I, ItemStack> itemStackGetter,
                                   @Nullable Function<I, String> idGetter,
                                   @Nullable BiConsumer<I, ConfigurationSection> editConsumer) {
        if (items.isEmpty()) return;
        var base = baseSup.get();

        AtomicInteger atomic = new AtomicInteger(0);
        items.forEach((i, slots) -> {
            ItemStack item = itemStackGetter.apply(i);

            String sectionName = idGetter == null ? null : idGetter.apply(i);
            if (sectionName == null || sectionName.isEmpty() || sectionName.equals("AUTO")) {
                sectionName = String.valueOf(atomic.incrementAndGet());
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasDisplayName())
                        sectionName = ChatColor.stripColor(item.getItemMeta().getDisplayName()) + "-" + sectionName;
                }
            }

            ConfigurationSection section = base.createSection(sectionName);
            section = this.preSerializeItem(section, item);

            List<String> slotList = this.performanceSlots(slots);
            section.set("slots", slotList);

            if (editConsumer != null)
                editConsumer.accept(i, section);
        });
    }
}
