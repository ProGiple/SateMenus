package org.satellite.dev.progiple.satemenus.menus.params.animations;

import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;

public interface IAnimation {
    IConfig getConfig();
    void play(AnimatedMenu menu);
    void stop(AnimatedMenu menu);
}
