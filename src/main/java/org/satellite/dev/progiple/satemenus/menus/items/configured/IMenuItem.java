package org.satellite.dev.progiple.satemenus.menus.items.configured;

import org.satellite.dev.progiple.satemenus.menus.params.actions.IMenuAction;

public interface IMenuItem {
    String id();
    IMenuAction anyClick();
    IMenuAction rightClick();
    IMenuAction leftClick();
    long clickCooldownCache();
    boolean removal();
}
