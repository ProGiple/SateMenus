package org.satellite.dev.progiple.satemenus.menus.menus;

import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;

public interface ISateMenu extends ItemListMenu {
    MenuSettings getSettings();
}
