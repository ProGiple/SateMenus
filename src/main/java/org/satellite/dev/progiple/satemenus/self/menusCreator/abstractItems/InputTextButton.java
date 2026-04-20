package org.satellite.dev.progiple.satemenus.self.menusCreator.abstractItems;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.self.menusCreator.MenuSettingsBuilder;
import org.satellite.dev.progiple.satemenus.self.queries.impl.InputTextQuery;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public abstract class InputTextButton extends EditableButton implements LoreUpdatable {
    private final String nowValue;
    private final ConfigurationSection section;
    public InputTextButton(ConfigurationSection section,
                           MenuSettingsBuilder builder,
                           @Nullable String nowValue) {
        super(section, builder);
        this.section = section;
        this.nowValue = nowValue;
        updateLore();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);

        var inputTextQuery = new InputTextQuery(player, builder)
                .thenAccept(registerAccept())
                .thenCancel(registerCancel());
        inputTextQuery.register(player.getUniqueId());

        sendEditMessage(section, player);
        return this;
    }

    public void updateLore() {
        this.replaceLore(l -> Utils.applyReplacements(l, "value-%-" + ColorManager.color(nowValue)));
    }

    public abstract BiConsumer<InputTextQuery, String> registerAccept();
    public abstract Consumer<InputTextQuery> registerCancel();
}