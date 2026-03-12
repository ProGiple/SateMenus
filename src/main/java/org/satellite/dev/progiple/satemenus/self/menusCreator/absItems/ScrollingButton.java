package org.satellite.dev.progiple.satemenus.self.menusCreator.absItems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettingsBuilder;

import java.util.List;

public abstract class ScrollingButton<E> extends EditableButton {
    private final List<E> elements;
    protected E selectedElement;
    protected boolean reversingClicks = false;
    public ScrollingButton(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section, builder);
        this.elements = registerElements();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        E newElement;

        int currentIndex = elements.indexOf(selectedElement);
        if ((reversingClicks && e.isLeftClick()) || (!reversingClicks && e.isRightClick()))
            newElement = elements.get(currentIndex <= 0 ? elements.size() - 1 : currentIndex - 1);
        else
            newElement = elements.get((currentIndex + 1) % elements.size());

        this.change(e.getWhoClicked(), newElement);
        return this;
    }

    public abstract List<E> registerElements();

    public abstract void change(HumanEntity player, E newElement);
}
