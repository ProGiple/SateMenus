package org.satellite.dev.progiple.satemenus.menus.menus;

import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;

import java.util.Collection;

public interface AnimatedMenu extends ItemListMenu {
    IAnimation getPlayingAnimation();
    void setPlayingAnimation(IAnimation animation);
    default Item findFirstItemAsync(int slot) {
        synchronized (getItemList()) {
            return this.findFirstItem(slot);
        }
    }
}
