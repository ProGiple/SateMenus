package org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.configs.Config;

import java.util.ArrayList;
import java.util.List;

public abstract class ScrollingButton<E> extends EditableButton implements LoreUpdatable {
    private final List<E> elements;
    protected int maximumLines;
    protected boolean reversingClicks;
    protected E selectedElement;
    public ScrollingButton(ConfigurationSection section,
                           MenuSettingsBuilder builder,
                           E selectedNow) {
        super(section, builder);
        this.selectedElement = selectedNow;
        this.maximumLines = section.getInt("maximumLines", 8);
        this.reversingClicks = section.getBoolean("reversingClicks", false);
        this.elements = registerElements();
        updateLore();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        E newElement;

        int currentIndex = elements.indexOf(selectedElement);
        if ((reversingClicks && e.isLeftClick()) || (!reversingClicks && e.isRightClick()))
            newElement = elements.get(currentIndex <= 0 ? elements.size() - 1 : currentIndex - 1);
        else
            newElement = elements.get((currentIndex + 1) % elements.size());

        this.change(e.getWhoClicked(), newElement);
        this.selectedElement = newElement;
        updateLore();

        return this.insert();
    }

    public abstract List<E> registerElements();

    public abstract void change(HumanEntity player, E newElement);

    public void updateLore() {
        ArrayList<String> lore = new ArrayList<>();
        for (String s : this.defaultLore) {
            if (s.contains("[list]") || s.contains("[elements]")) {
                String[] formats = Config.getScrollingFormats();

                int size = elements.size();
                int selectedIndex = selectedElement == null ? 0 : Math.max(elements.indexOf(selectedElement), 0);

                int maximumLines = Math.min(this.maximumLines, size);
                int offset = maximumLines / 2;
                int startIndex = (selectedIndex - offset) % size;

                if (startIndex < 0) startIndex += size;
                for (int i = 0; i < maximumLines; i++) {
                    int index = (startIndex + i) % size;
                    E value = elements.get(index);
                    String format = formats[value.equals(selectedElement) ? 1 : 0];
                    lore.add(format.replace("[element]", value.toString()));
                }
            }
            else
                lore.add(ColorManager.color(s));
        }

        this.setLore(lore);
    }
}
