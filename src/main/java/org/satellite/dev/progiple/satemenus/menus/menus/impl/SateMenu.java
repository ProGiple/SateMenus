package org.satellite.dev.progiple.satemenus.menus.menus.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.IMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.menus.items.Decoration;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.updatable.UpdatableIMenu;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.Refreshable;
import org.satellite.dev.progiple.satemenus.menus.items.SMItem;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;
import org.satellite.dev.progiple.satemenus.menus.menus.ISateMenu;
import org.satellite.dev.progiple.satemenus.menus.menus.Recreatable;
import org.satellite.dev.progiple.satemenus.menus.params.MenuConfiguredItem;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;

import java.util.*;

@Getter
public class SateMenu implements ISateMenu, Refreshable, Recreatable, AnimatedMenu {
    private final List<Item> itemList;
    private final Player player;
    private final Inventory inventory;
    private final MenuSettings settings;
    private final Set<IAnimation> playingAnimations;
    protected Recreatable recreatable;
    public SateMenu(@NotNull Player player, MenuSettings settings, @Nullable Recreatable recreatable) {
        this.player = player;
        this.settings = settings;
        this.itemList = new LinkedList<>();
        this.recreatable = recreatable;
        this.playingAnimations = new HashSet<>();

        String strTitle = ColorManager.color(Utils.setPlaceholders(player, settings.title().replace("[player]", player.getName())));
        Component title = Component.text(strTitle).toBuilder().build();
        if (settings.type() == null || SateMenus.CHEST_TYPES.contains(settings.type())) {
            this.inventory = Bukkit.createInventory(player, settings.rows() * 9, title);
        } else {
            this.inventory = Bukkit.createInventory(player, settings.type(), title);
        }

        if (settings.decorations() != null) {
            Decoration decoration = new Decoration(settings.decorations(), inventory);
            decoration.getDecorationItems().forEach(i -> i.replaceLore(l -> Utils.setPlaceholders(player, l)));
            decoration.insert(inventory);
        }
    }

    @Override
    public void refresh() {
        SateMenu menu = new SateMenu(player, settings, recreatable);
        MenuManager.openInventory(menu);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        for (MenuConfiguredItem item : settings.items()) {
            if (settings.checkConditions(player, item.viewConditions())) {
                for (int slot : item.slots()) {
                    if (slot < 0) continue;
                    SMItem menuItem = new SMItem(item, slot);
                    this.addItems(false, menuItem);
                }
            }
        }

        this.insertAll();

        IAnimation animation = LunaMath.getRandom(settings.animations());
        if (animation != null) animation.play(this);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        this.itemClick(e);
    }

    @Override
    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();

        this.playingAnimations.forEach(a -> a.stop(this));
        this.playingAnimations.clear();

        if (this.settings.checkConditions(player, settings.closeAction().conditions()))
            settings.closeAction().process(player, null);
        else if (settings.closeAction().cancelEventIfError()) {
            Bukkit.getScheduler().runTaskLater(SateMenus.getInstance(), () -> {
                MenuManager.openInventory(this);
            }, 4L);
        }
    }

    @Override
    public boolean isCancelled(Cancellable cancellable, int i) {
        return true;
    }

    @Override
    public Collection<Item> findItems(ItemStack itemStack) {
        return this.itemList.stream().filter(i -> i.getItemStack().equals(itemStack)).toList();
    }

    @Override
    public Collection<Item> findItems(Material material) {
        return this.itemList.stream().filter(i -> i.getMaterial().equals(material)).toList();
    }

    @Override
    public Collection<Item> findItems(Class<?> aClass) {
        return this.itemList.stream().filter(i -> aClass.isAssignableFrom(i.getClass())).toList();
    }

    @Override
    public Collection<Item> findItems(String s) {
        return this.itemList.stream().filter(i -> i.getDisplayName().equals(s)).toList();
    }

    @Override
    public Item findFirstItem(ItemStack itemStack) {
        return Utils.find(itemList, i -> i.getItemStack().equals(itemStack)).orElse(null);
    }

    @Override
    public Item findFirstItem(Class<?> aClass) {
        return Utils.find(itemList, i -> aClass.isAssignableFrom(i.getClass())).orElse(null);
    }

    @Override
    public Item findFirstItem(String s) {
        return Utils.find(itemList, i -> i.getDisplayName().equals(s)).orElse(null);
    }

    @Override
    public Item findFirstItem(Material material) {
        return Utils.find(itemList, i -> i.getMaterial().equals(material)).orElse(null);
    }

    @Override
    public Item findFirstItem(int i) {
        return Utils.find(itemList, it -> it.getSlot() == i).orElse(null);
    }

    @Override
    public Item findFirstItem(ItemStack itemStack, int i) {
        return Utils.find(itemList, it -> it.getItemStack().equals(itemStack) && it.getSlot() == i).orElse(null);
    }

    @Override
    public boolean itemClick(@NotNull Material material, InventoryClickEvent e) {
        Item item = findFirstItem(material);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(@NotNull String s, InventoryClickEvent e) {
        Item item = findFirstItem(s);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(@NotNull Class<?> aClass, InventoryClickEvent e) {
        Item item = findFirstItem(aClass);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(@NotNull ItemStack itemStack, InventoryClickEvent e) {
        Item item = findFirstItem(itemStack);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(int i, InventoryClickEvent e) {
        Item item = findFirstItem(i);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(@NotNull ItemStack itemStack, int i, InventoryClickEvent e) {
        Item item = findFirstItem(itemStack, i);
        return item != null && item.onClick(e) != null;
    }

    @Override
    public boolean itemClick(@NotNull InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return false;

        return itemClick(item, e.getSlot(), e);
    }

    @Override
    public void addItems(boolean b, Item... items) {
        for (Item item : items) {
            itemList.add(item);
            if (b) item.insert(this);
        }
    }

    @Override
    public Collection<Item> addItems(Collection<Item> collection, boolean b) {
        for (Item item : collection) {
            itemList.add(item);
            if (b) item.insert(this);
        }

        return this.itemList;
    }

    @Override
    public Collection<Item> insertAll() {
        this.itemList.forEach(i -> i.insert(this));
        return this.itemList;
    }

    @Override
    public void clear() {
        this.itemList.forEach(i -> i.remove(this));
        this.itemList.clear();
    }

    public boolean isUpdatable() {
        return this instanceof UpdatableIMenu;
    }

    @Override
    public IMenu reCreate(Player player) {
        if (this.recreatable instanceof SateMenu sateMenu) {
            SateMenu menu = Menus.open(player, sateMenu.settings, false);
            if (menu == null) return null;

            menu.recreatable = sateMenu.recreatable;
            return menu;
        }
        else if (this.recreatable != null)
            return this.recreatable.reCreate(player);
        else
            return null;
    }
}
