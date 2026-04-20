package org.satellite.dev.progiple.satemenus.self.menusCreator.menus.main.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems.ScrollingButton;

import java.util.List;

public class ChangeInventoryTypeItem extends ScrollingButton<InventoryType> {
    public ChangeInventoryTypeItem(ConfigurationSection section, MenuSettingsBuilder builder) {
        super(section, builder, builder.type());
    }

    @Override
    public List<InventoryType> registerElements() {
        return List.of(
                InventoryType.CHEST,
                InventoryType.DISPENSER,
                InventoryType.DROPPER,
                InventoryType.FURNACE,
                InventoryType.WORKBENCH,
                InventoryType.ENCHANTING,
                InventoryType.BREWING,
                InventoryType.ANVIL,
                InventoryType.SMITHING,
                InventoryType.BEACON,
                InventoryType.HOPPER,
                InventoryType.BLAST_FURNACE,
                InventoryType.SMOKER,
                InventoryType.LOOM,
                InventoryType.CARTOGRAPHY,
                InventoryType.GRINDSTONE,
                InventoryType.STONECUTTER
        );
    }

    @Override
    public void change(HumanEntity player, InventoryType newElement) {
        builder.type(newElement);
    }
}
