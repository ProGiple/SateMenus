package org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.satellite.dev.progiple.satemenus.menus.params.animations.AnimationStage;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AbstractAnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;

@AnimationAction.Type("none")
public class NoneAction extends AbstractAnimationAction {
    public NoneAction(AnimationStage animationStage, byte slot, ConfigurationSection settings) {
        super(animationStage, slot, settings);
    }
}
