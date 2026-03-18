package org.satellite.dev.progiple.satemenus.menus.menus;

import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.satellite.dev.progiple.satemenus.menus.params.animations.IAnimation;

import java.util.Collection;

public interface AnimatedMenu extends ItemListMenu {
    IAnimation getPlayingAnimation();
    void setPlayingAnimation(IAnimation animation);
}
